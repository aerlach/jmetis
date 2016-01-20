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

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.tallsoft.microspring.BeanDefinition;
import com.tallsoft.microspring.utils.TSParser;

/**
 * <p>
 * Is part of the SAX read of the config files.  Generates beans and hands off
 * to the XmlProperty class to read in property specs.
 * </p>
 * 
 * @author J.Gibbons
 */
public class XmlBean extends DefaultHandler implements TSParser {

    public static String EL_NAME = "bean";
    public static String ATTR_ID = "id";
    public static String ATTR_CLASS = "class";
    public static String ATTR_PARENT = "parent";
    public static String ATTR_DEPENDS_ON = "depends-on";
    public static String ATTR_SINGLETON = "singleton";
    public static String ATTR_ABSTRACT = "abstract";
    public static String ATTR_LAZY_INIT = "lazy-init";
    public static String ATTR_INIT_METHOD = "init-method";
    public static String ATTR_DESTROY_METHOD = "destroy-method";

    private XMLReader theReader;
    private TSParser parentParser;
    private Attributes myAttribs;
    private String endElement;
    private StringBuffer cdata = new StringBuffer();
    private String elementName = null;

    private BeanDefinition currBean;

    /**
     * Return the bean!
     * 
     * @return
     */
    public BeanDefinition getBean() {
        return currBean;
    }

    public void setParser(XMLReader reader, TSParser parent, String el_name, Attributes attribs) {
        theReader = reader;
        parentParser = parent;
        endElement = el_name;
        myAttribs = attribs;

        createTheBean();

        reader.setContentHandler(this);
    }

    public void childFinishedParsing(TSParser child, String el_name) {
        if (el_name.equals(XmlProperty.EL_NAME)) {
            XmlProperty prop = (XmlProperty) child;
            currBean.addProperty(prop.getProperty().getName(), prop.getProperty());
        }
        theReader.setContentHandler(this);
    } // end of childFinishedParsing

    // ********************************************************************
    // Standard SAX i/f
    // ********************************************************************

    /**
     * Receive notification of character data inside an element.
     */
    public void characters(char[] ch, int start, int length) {
        cdata.append(ch, start, length);
    }

    /**
     * Receive notification of the start of an element.
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String elName = localName;
        if (elName == null)
            elName = qName;

        if (elName.equals(XmlProperty.EL_NAME)) {
            XmlProperty prop = new XmlProperty();
            prop.setParser(theReader, this, elName, attributes);
        }
    }

    /**
     * Receive notification of the end of an element.
     */
    public void endElement(String uri, String localName, String qName) {
        String elName = localName;
        if (elName == null)
            elName = qName;

        if (elName.equals(EL_NAME)) {
            if (parentParser != null) {
                parentParser.childFinishedParsing(this, endElement);
            }
        }

    }

    /**
     * Creates the current bean and populated from the attributes
     */
    private void createTheBean() {
        String isAbstractStr = myAttribs.getValue(ATTR_ABSTRACT);
        boolean isAbstract = false;
        if (isAbstractStr != null && isAbstractStr.equals("true")) {
            isAbstract = true;
        }
        String isSingletonStr = myAttribs.getValue(ATTR_SINGLETON);
        boolean isSingleton = true;
        if (isSingletonStr != null && isSingletonStr.equals("false")) {
            isSingleton = false;
        }
        String isLazyStr = myAttribs.getValue(ATTR_LAZY_INIT);
        boolean isLazy = false;
        if (isLazyStr != null && isLazyStr.equals("true")) {
            isLazy = true;
        }
        currBean = new BeanDefinition();
        currBean.setId(myAttribs.getValue(ATTR_ID));
        currBean.setClassName(myAttribs.getValue(ATTR_CLASS));
        currBean.setParentBeanId(myAttribs.getValue(ATTR_PARENT));
        currBean.setAbstract(isAbstract);
        currBean.setSingleton(isSingleton);
        currBean.setLazyInit(isLazy);
        currBean.setInitMethod(myAttribs.getValue(ATTR_INIT_METHOD));
        currBean.setDestroyMethod(myAttribs.getValue(ATTR_DESTROY_METHOD));
        currBean.setDependsOn(myAttribs.getValue(ATTR_DEPENDS_ON));
    }

}
