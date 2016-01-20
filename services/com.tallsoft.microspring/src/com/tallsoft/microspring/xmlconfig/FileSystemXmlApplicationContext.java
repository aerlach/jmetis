/*
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.microspring.xmlconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.tallsoft.microspring.BeanDefinition;
import com.tallsoft.microspring.BeanFactory;
import com.tallsoft.microspring.BeanNotOfRequiredTypeException;
import com.tallsoft.microspring.BeansException;
import com.tallsoft.microspring.NoSuchBeanDefinitionException;
import com.tallsoft.microspring.context.ApplicationContext;
import com.tallsoft.microspring.context.ApplicationContextException;
import com.tallsoft.microspring.context.ApplicationEvent;
import com.tallsoft.microspring.context.ApplicationListener;
import com.tallsoft.microspring.context.ConfigurableApplicationContext;
import com.tallsoft.microspring.context.ContextClosedEvent;
import com.tallsoft.microspring.context.ContextRefreshedEvent;
import com.tallsoft.microspring.utils.BeanCreator;
import com.tallsoft.microspring.utils.TSParser;

/**
 * <p>
 * All the MicroSpring lives here (pretty much). All singletons created that are
 * application listeners will have onApplicationEvent called when I am closed.,
 * or refreshed. And you may set me up under a parent context, which I will
 * refer to should I fail to locate a bean for myself. And thats it. It is not a
 * complete implementation of spring. After all you have spring for that!
 * </p>
 * 
 * @author J.Gibbons
 */
public class FileSystemXmlApplicationContext implements ConfigurableApplicationContext, BeanFactory, TSParser {

    private HashMap<String, BeanDefinition> beans = new HashMap<String, BeanDefinition>();
    private HashMap<String, Object> singletons = new HashMap<String, Object>();
    private long startDate;
    private String configFileName;

    private ApplicationContext parentContext;
    private ArrayList<ConfigurableApplicationContext> kids = new ArrayList<ConfigurableApplicationContext>();

    private Stack<String> recursionGuard = new Stack<String>();
    
    
    /**
     * Create a new XmlBeanFactory with the given file name
     * 
     * @param filename
     *            The filename
     * @throws IOException
     *             if couldnt read the file
     * @throws SAXException
     *             if couldnt parse the file
     */
    public FileSystemXmlApplicationContext(String filename) throws BeansException {
        this.configFileName = filename;
//            readConfigFromFile(filename);
            
        refresh();
//            startUpAllSingletons();
    }

    /**
     * Read the config file
     * 
     * @param filename
     * @throws IOException
     * @throws SAXException
     */
    private void readConfigFromFile(String filename) throws IOException, SAXException {
        this.configFileName = filename;
        File f = new File(filename);
        if (f.exists()) {
            FileInputStream fis = new FileInputStream(f);
            readDefinitions(fis);
        } else {
            throw new IOException("Could not read file [" + filename + "] as it doesn't exist.");
        }

    }

    /**
     * Util to read the definitions
     * 
     * @param inputStream
     * @throws SAXException
     * @throws IOException
     */
    private void readDefinitions(InputStream inputStream) throws SAXException, IOException {
        XMLReader xr = XMLReaderFactory.createXMLReader();

        // Set the ContentHandler of the XMLReader
        XmlMicroSpringParser theContentHander = new XmlMicroSpringParser();
        theContentHander.setParser(xr, this, "beans", null);

        xr.parse(new InputSource(inputStream));
        beans = theContentHander.getBeans();
    }

    /**
     * Just because of the interface. No impl needed
     */
    public void setParser(XMLReader reader, TSParser parent, String el_name, Attributes attribs) {
    }

    /**
     * Just because of the interface. No impl needed
     */
    public void childFinishedParsing(TSParser child, String el_name) {
    } // end of childFinishedParsing

    /**
     * Does this bean factory contain a bean definition with the given name?
     * 
     * @param name
     * @return
     */
    public boolean containsBean(String name) {
        return beans.containsKey(name);
    }

    /**
     * Returns the bean definition, from this context, or parent contexts.
     * 
     * @param name
     * @return The bean definition
     * @throws NoSuchBeanDefinitionException
     */
    public BeanDefinition getBeanDefn(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition defn = beans.get(name);
        if (defn == null) {
            if (parentContext != null) {
                defn = ((FileSystemXmlApplicationContext) parentContext).getBeanDefn(name);
            } else {
                throw new NoSuchBeanDefinitionException("No bean definition for name [" + name + "]");
            }
        }
        return defn;
    }

    /**
     * Return the aliases for the given bean name, if defined. MicroSpring does
     * not support aliases
     * 
     * @param name
     * @return
     */
    public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return new String[0];
    }

    /**
     * Return an instance, which may be shared or independent, of the given bean
     * name.
     * 
     * @param name
     * @return
     */
    public Object getBean(String name) throws BeansException {
        if (recursionGuard.contains(name)) {
            String msg = "Circular dependency detected in bean creation!\n";
            String pad="";
            for (String id : recursionGuard) {
                if (pad.length()>0) msg+="-->";
                msg+=" "+id+"\n";
                pad+=" ";
            }
            throw new BeansException(msg);
        }
        recursionGuard.push(name);
        try {            
            Object ret = null;
    
            BeanDefinition defn = beans.get(name);
            if (defn == null) {
                if (parentContext != null) {
                    return ((FileSystemXmlApplicationContext) parentContext).getBean(name);
                } else {
                    throw new NoSuchBeanDefinitionException("Bean definition for [" + name + "] cannot be found.");                
                }
            }
    
            if (defn.isSingleton()) {
                return threadSafeGetSingleton(name, defn);
            }
    
            // instantiate all dependancies
            BeanCreator creator = new BeanCreator(this);
            ret = creator.createGraph(defn);
            return ret;
        } finally {
            recursionGuard.pop();
        }
    }

    /**
     * If you have insane bean dependancies you could get dead lock here. I
     * leave it to your design not to be multithreaded and mad.
     * 
     * @param name
     * @param defn
     * @return
     */
    private synchronized Object threadSafeGetSingleton(String name, BeanDefinition defn) throws BeansException {
        if (singletons.get(name) != null) {
            return singletons.get(name);
        }

        // instantiate all dependancies
        BeanCreator creator = new BeanCreator(this);
        Object ret = creator.createGraph(defn);

        singletons.put(name, ret);
        return ret;
    }

    /**
     * Return an instance (possibly shared or independent) of the given bean
     * name.
     * 
     * @param name
     * @param requiredType
     * @return
     */
    public Object getBean(String name, Class requiredType) throws BeansException {
        Object o = getBean(name);

        if (!o.getClass().isInstance(requiredType)) {
            throw new BeanNotOfRequiredTypeException("Bean called [" + name + "] is not of type ["
                    + requiredType.getName() + "] but is instead of type [" + o.getClass().getName() + "]");
        }
        return o;
    }

    /**
     * Determine the type of the bean with the given name.
     * 
     * @param name
     * @return
     */
    public Class getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition defn = getBeanDefn(name);
        try {
            return Class.forName(defn.getClassName());
        } catch (ClassNotFoundException ex) {
            // impossible because of validation...
            throw new NoSuchBeanDefinitionException("Bean definition for [" + name + "] has class name of ["
                    + defn.getClassName() + "] which threw a ClassNotFoundExcepion.");
        }
    }

    /**
     * Is this bean a singleton?
     * 
     * @param name
     * @return
     */
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition defn = getBeanDefn(name);
        return defn.isSingleton();
    }

    /**
     * Return a friendly name for this context.
     * 
     * @return
     */
    public String getDisplayName() {
        return "MicroSpring's XmlBeanFactory";
    }

    /**
     * Return the parent context, or null if there is no parent, and this is the
     * root of the context hierarchy.
     * 
     * @return
     */
    public ApplicationContext getParent() {
        return parentContext;
    }

    /**
     * Return the timestamp when this context was first loaded.
     * 
     * @return
     */
    public long getStartupDate() {
        return startDate;
    }

    /**
     * Close this application context, releasing all resources and locks that
     * the implementation might hold.
     */
    public void close() throws ApplicationContextException {
        if (parentContext instanceof FileSystemXmlApplicationContext) {
            ((FileSystemXmlApplicationContext) parentContext).removeKid(this);
        }

        ContextClosedEvent ev = new ContextClosedEvent(this);
        publishEvent(ev);

        for (String beanId : singletons.keySet()) {
            try {
                killTheBean(beanId, beans);
            } catch (BeansException ex) {
                throw new ApplicationContextException(ex.getMessage());
            }
        }
        singletons.clear();

        for (ConfigurableApplicationContext kid : kids) {
            kid.close();
        }
        kids.clear();
    }

    /**
     * publishes to the singletons, the event that we know about.
     * 
     * @param ev
     */
    public void publishEvent(ApplicationEvent ev) {
        for (Object o : singletons.values()) {
            if (o instanceof ApplicationListener) {
                ApplicationListener appList = (ApplicationListener) o;
                appList.onApplicationEvent(ev);
            }
        }
    }

    /**
     * Return the internal bean factory of this application context.
     * 
     * @return
     */
    // Note in real spring it's ConfigurableListableBeanFactory
    // getBeanFactory();
    public BeanFactory getBeanFactory() {
        return this;
    }

    /**
     * refresh the persistent representation of the configuration. NOTE: This
     * calls refresh on child contexts and sends a refresh event to all
     * singleton beans. NOTE: NEVER EVER change the destroy method to a non
     * existant method on the old bean being replaced when you refresh with a
     * new config file. If you do you will corrupt the runtime.
     */
    public void refresh() throws BeansException {
        // preserve the old definitions, use for the old destroy method
        HashMap<String, BeanDefinition> oldDefns = new HashMap<String, BeanDefinition>();
        oldDefns.putAll(beans);
        
        try {
            readConfigFromFile(configFileName);
        } catch (Exception ex) {
            throw new BeansException(
                    "Refresh failed due to internal exception when rereading the config file.  This application context is now corrupt."
                            + "  Base exception: " + ex.getClass().getName() + ":" + ex.getMessage());
        }

        // it would be possible at this point to reinject properties into
        // existing
        // singletons....hmm. Should I? i.e. I could get the singletons that
        // exist.
        // and do 1 pass to compare class names. If different I could UNLOAD the
        // class.
        // Then do another pass, and for ALL singletons inject the properties
        // again.
        ArrayList<String> resetList = new ArrayList<String>();
        synchronized (this) {
            ArrayList<String> killList = new ArrayList<String>();
            ContextClosedEvent closeEvent = new ContextClosedEvent(this);
            for (String beanId : singletons.keySet()) {
                Object bean = singletons.get(beanId);
                BeanDefinition defn = getBeanDefn(beanId);
                String beanClassName = bean.getClass().getName();
                if (!defn.getClassName().equals(beanClassName)) {
                    killList.add(beanId);
                } else {
                    resetList.add(beanId);
                }
            }
            for (String beanId : killList) {
                Object bean = singletons.get(beanId);
                killTheBean(beanId, oldDefns);
                singletons.remove(beanId);
            }
        }
        
        // And finally, call all the property setters again + setApplCon+init
        // method.
        BeanCreator creator = new BeanCreator(this);
        for (String beanId : resetList) {
            Object bean = singletons.get(beanId);
            BeanDefinition defn = getBeanDefn(beanId);
            creator.injectDependencies(bean, defn);
        }
        startUpAllSingletons();

        // Now tell them they have been refreshed
        ContextRefreshedEvent ev = new ContextRefreshedEvent(this);
        publishEvent(ev);

        // and get any child contexts to refresh
        for (ConfigurableApplicationContext kid : kids) {
            kid.refresh();
        }
    }

    /**
     * called after init or on refresh
     * It will not do a thing if the singleton already exists.
     */
    private void startUpAllSingletons() throws BeansException {
        // Create all singletons if they do not already exist.
        BeanCreator creator = new BeanCreator(this);
        for (String beanId : beans.keySet()) {
            BeanDefinition defn = getBeanDefn(beanId);
            Object bean = singletons.get(beanId);
            if (bean == null && !defn.isAbstract() && 
                    !defn.hasNoClass() && !defn.isLazyInit()) {
                try {
                    creator.createGraph(defn);
                } catch (BeansException ex) {
                    if (defn.getParentBeanId()!=null && parentContext==null) {
                        // chance that the defn is in the parent, so call this
                        // again on set parent.
                    } else {
                        throw ex;
                    }
                }
            }
        }
    }
    
    
    /**
     * Kills a bean by calling its destroy method
     * 
     * @param bean
     * @throws BeansException
     */
    private void killTheBean(String beanId, HashMap<String, BeanDefinition> defns) throws BeansException {
        // Kill this bean!
        Object bean = singletons.get(beanId);
        if (bean != null) {
            BeanDefinition defn = defns.get(beanId);
            if (defn==null) {
                // gets from parent contexts as well.
                defn = getBeanDefn(beanId);
            }
            if (defn.getDestroyMethod() != null) {
                try {
                    Method m = bean.getClass().getMethod(defn.getDestroyMethod());
                    if (m == null) {
                        throw new BeansException("Could not call the specified microspring destroy method ["
                                + defn.getDestroyMethod() + "] on bean [" + defn.getId() + " with class name ["
                                + defn.getClassName() + "] as no method of that name.");
                    }
                    m.invoke(bean);
                } catch (Exception ex) {
                    throw new BeansException("Could not call the specified microspring destroy method ["
                            + defn.getDestroyMethod() + "] on bean [" + defn.getId() + " with class name ["
                            + defn.getClassName() + "], due to " + ex.getClass().getName() + ":" + ex.getMessage());
                }
            }
        }
    }

    /**
     * Set the parent of this application context.
     * If there are any singletons which had parent defns which were only
     * realised in the parent context then this boy will create them now.
     * 
     * @param parent
     */
    public void setParent(ApplicationContext parent) throws BeansException {
        parentContext = parent;
        if (parent instanceof FileSystemXmlApplicationContext) {
            ((FileSystemXmlApplicationContext) parent).addKid(this);
        }
        startUpAllSingletons();
    }

    void addKid(ConfigurableApplicationContext kid) {
        kids.add(kid);
    }

    void removeKid(ConfigurableApplicationContext kid) {
        kids.remove(kid);
    }
}
