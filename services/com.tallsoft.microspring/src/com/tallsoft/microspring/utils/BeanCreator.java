/*
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.microspring.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import com.tallsoft.microspring.BeanDefinition;
import com.tallsoft.microspring.BeanProperty;
import com.tallsoft.microspring.BeansException;
import com.tallsoft.microspring.NoSuchBeanDefinitionException;
import com.tallsoft.microspring.PropertyValue;
import com.tallsoft.microspring.BeanProperty.PropertyType;
import com.tallsoft.microspring.context.ApplicationContextAware;
import com.tallsoft.microspring.xmlconfig.FileSystemXmlApplicationContext;

/**
 * <p>
 * Given a list of all the beans, create all dependancies, or reuse singletons
 * etc.
 * </p>
 * 
 * @author J.Gibbons
 */
public class BeanCreator {
    private HashMap<String, Object> instances = new HashMap<String, Object>();
    private FileSystemXmlApplicationContext factory;

    public BeanCreator(FileSystemXmlApplicationContext factory) {
        this.factory = factory;
    }

    // @todo and need to do circular object graph guarding

    /**
     * If this bean makes use of a parent template then combine the two.
     * 
     * @param defn
     *            The child definition
     * @return The combined definition
     * @exception BeansException
     *                if the parent ref cannot be found, or some other error
     *                occured.
     */
    private BeanDefinition getCombinedDefinition(BeanDefinition defn) throws BeansException {
        if (defn.getParentBeanId() == null)
            return defn;

        BeanDefinition parent = factory.getBeanDefn(defn.getParentBeanId());
        FileSystemXmlApplicationContext parentFactory = (FileSystemXmlApplicationContext) factory.getParent();
        while (parent == null && parentFactory != null) {
            parent = parentFactory.getBeanDefn(defn.getParentBeanId());
            parentFactory = (FileSystemXmlApplicationContext) factory.getParent();
        }
        if (parent == null) {
            throw new NoSuchBeanDefinitionException("The bean [" + defn.getId() + "], with class ["
                    + defn.getClassName() + "] contains a reference to parent bean [" + defn.getParentBeanId()
                    + "] which cannot be located.");
        }

        BeanDefinition combined = new BeanDefinition();
        combined.setClassName(parent.getClassName());
        combined.setDependsOn(parent.getDependsOn());
        combined.setId(parent.getId());
        combined.setInitMethod(parent.getInitMethod());
        combined.setDestroyMethod(parent.getDestroyMethod());

        combined.setClassName(defn.getClassName());
        combined.setDependsOn(defn.getDependsOn());
        combined.setId(defn.getId());
        combined.setInitMethod(defn.getInitMethod());
        combined.setDestroyMethod(parent.getDestroyMethod());
        combined.setSingleton(defn.isSingleton());

        HashMap<String, BeanProperty> parentProps = parent.getProperties();
        HashMap<String, BeanProperty> childProps = defn.getProperties();

        for (String key : parentProps.keySet()) {
            if (!childProps.containsKey(key)) {
                combined.addProperty(key, parentProps.get(key));
            }
        }
        for (String key : childProps.keySet()) {
            combined.addProperty(key, childProps.get(key));
        }
        return combined;
    }

    /**
     * Create the bean.
     * 
     * @param defn
     * @return The created object
     * @throws BeansException
     */
    public Object createGraph(BeanDefinition defn) throws BeansException {
        // First off, if this is an aggregate bean, we want to derive the defn
        // from a parent.
        if (defn.hasNoClass()) {
            throw new BeansException("Illegal attempt to create an instance of a bean [" + defn.getId()
                    + "] with no class - probably a template bean.");            
        }
        if (defn.isAbstract()) {
            throw new BeansException("Illegal attempt to create an instance of an abstract bean [" + defn.getId()
                    + "] with class [" + defn.getClassName() + "]");
        }
        if (defn.getParentBeanId() != null) {
            defn = getCombinedDefinition(defn);
        }

        String name = defn.getDependsOn();
        if (name != null && name.trim().length() > 0) {
            ensureBean(name);
        }

        // Now for all the properties which refer to other beans which must be
        // created, or
        // which refer to singletons.
        ArrayList<String> beans = defn.getAllBeanRefProperties();
        for (String idref : beans) {
            // OK, tis a bean reference
            ensureBean(idref);
        }

        // Finally, no dependants, so create me!
        try {
            Object bean = Class.forName(defn.getClassName()).newInstance();

            injectDependencies(bean, defn);
            // If there is an awareness of the context, then set it
            if (bean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) bean).setApplicationContext(factory);
            }

            // And then call any init-method that applies
            if (defn.getInitMethod() != null && defn.getInitMethod().length() > 0) {
                Method m = bean.getClass().getMethod(defn.getInitMethod());
                if (m == null) {
                    throw new NoSuchMethodException();
                }
                m.invoke(bean);
            }
            return bean;
        } catch (NoSuchMethodException ex) {
            throw new BeansException("Could not instantiate class bean name [" + defn.getId() + "] with class name ["
                    + defn.getClassName() + "] as the object does not have a method called [" + defn.getInitMethod()
                    + "] which is the specified initMethod");
        } catch (BeansException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BeansException("Could not instantiate class bean name [" + defn.getId() + "] with class name ["
                    + defn.getClassName() + "] as got " + ex.getClass().getName() + " : " + ex.getMessage());
        }
    }

    private void ensureBean(String name) throws BeansException {
        if (!instances.containsKey(name)) {
            Object o = factory.getBean(name);
            instances.put(name, o);
        }
        return;
    }

    public void injectDependencies(Object o, BeanDefinition defn) throws BeansException {
        for (BeanProperty prop : defn.getProperties().values()) {
            callSetter(prop.getName(), prop, o);
        }
    }

    private void callSetter(String name, BeanProperty prop, Object o) throws BeansException {
        String funcName = "set" + name;

        try {
            Method[] methods = o.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase(funcName)) {
                    Class[] argClasses = method.getParameterTypes();
                    Object[] args = createArgs(argClasses, prop);
                    if (args != null) {
                        method.invoke(o, args);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new BeansException("Failed to call setter for property [" + prop.getName() + "] on bean [" + name
                    + "].  Got exception " + ex.getClass().getName() + ":" + ex.getMessage());
        }
    }

    /**
     * Converts the types if it can
     * 
     * @param argTypes
     *            The types - if not 1 entry then returns null
     * @param prop
     *            The property to be set
     * @return The array of objects!
     */
    private Object[] createArgs(Class[] argClasses, BeanProperty prop) throws BeansException {
        if (argClasses.length != 1)
            return null;

        Object ret[] = new Object[1];
        ret[0] = getArgInstance(argClasses[0], prop);
        return ret;
    }

    /**
     * Looks it up as a bean ref or as a string. That simple (sadly)
     * 
     * @param val
     * @return The bean ref or the string
     */
    private Object getListObj(PropertyValue val) throws BeansException {
        if (val.getBeanRef() != null)
            return instances.get(val.getBeanRef());
        if (val.isNull())
            throw new BeansException("There is a <null/> item in a list, which is illegal.");
        return val.getValue();
    }

    private Object getArgInstance(Class argClass, BeanProperty prop) throws BeansException {
        try {
            if (prop.getType() == PropertyType.VALUE) {
                if (prop.getValue().isNull()) {
                    // errr. dunno
                    return null;
                } else {
                    Object val = getArgInstance(argClass, prop.getValue());
                    return val;
                }
            }
            if (prop.getType() == PropertyType.MAP) {
                if (argClass.getClass().isInstance(Map.class)) {
                    Map ret = (Map) argClass.newInstance();
                    HashMap<String, PropertyValue> map = prop.getMapValues();
                    for (String key : map.keySet()) {
                        ret.put(key, getListObj(map.get(key)));
                    }
                    return ret;
                }
            }

            if (prop.getType() == PropertyType.SET) {
                if (argClass.getClass().isInstance(Set.class)) {
                    Set ret = (Set) argClass.newInstance();
                    Set<PropertyValue> set = prop.getSetValues();
                    for (PropertyValue val : set) {
                        ret.add(getListObj(val));
                    }
                    return ret;
                }
            }
            if (prop.getType() == PropertyType.LIST) {
                if (argClass.getClass().isInstance(List.class)) {
                    List ret = (List) argClass.newInstance();
                    List<PropertyValue> list = prop.getListValues();
                    for (PropertyValue val : list) {
                        ret.add(getListObj(val));
                    }
                    return ret;
                }
            }
            if (prop.getType() == PropertyType.PROP) {
                if (argClass.getClass().isInstance(Properties.class)) {
                    Properties props = new Properties();
                    Properties src = prop.getPropertyValues();
                    Iterator iter = src.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        props.put(key, getListObj((PropertyValue) src.get(key)));
                    }
                    return props;
                }
            }
            System.out.println("Failed to match type [" + argClass + "]");
            return null;
        } catch (Exception ex) {
            throw new BeansException("Failed creating an argument instance for property [" + prop.getName()
                    + "] with type [" + prop.getType() + "].  Exception=" + ex.getClass().getName() + ":"
                    + ex.getMessage());
        }
    }

    private Object getArgInstance(Class argClass, PropertyValue val) {
        if (val.getBeanRef() != null)
            return instances.get(val.getBeanRef());

        if (argClass == String.class) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return "";
            }
            return val.getValue();
        }
        if (argClass == java.lang.Boolean.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Boolean(false);
            }
            return Boolean.parseBoolean(val.getValue());
        }
        if (argClass == java.lang.Long.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Long(0);
            }
            return new Long(val.getValue());
        }
        if (argClass == java.lang.Integer.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Integer(0);
            }
            return new Integer(val.getValue());
        }
        if (argClass == java.lang.Short.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Short((short) 0);
            }
            return new Short(val.getValue());
        }
        if (argClass == java.lang.Byte.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Byte((byte) 0);
            }
            return Byte.parseByte(val.getValue());
        }
        if (argClass == java.lang.Character.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Character('\0');
            }
            return new Character(val.getValue().charAt(0));
        }
        if (argClass == java.lang.Float.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Float(0);
            }
            return new Float(val.getValue());
        }
        if (argClass == java.lang.Double.TYPE) {
            if (val.getValue() == null || val.getValue().length() == 0) {
                return new Double(0);
            }
            return new Double(val.getValue());
        }

        System.out.println("Failed to match type [" + argClass + "]");
        return null;
    }
}
