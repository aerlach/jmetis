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

import com.tallsoft.microspring.BeanProperty;
import com.tallsoft.microspring.PropertyValue;
import com.tallsoft.microspring.BeanProperty.PropertyType;
import com.tallsoft.microspring.utils.TSParser;

/**
 * <p>
 * A rather poor sax parser, but very quick to write which suits me.
 * From this you will end up with a BeanProperty and it's composite
 * PropertyValues.
 * </p>
 * 
 * @author J.Gibbons
 */
public class XmlProperty extends DefaultHandler implements TSParser {

    public static String EL_NAME = "property";
    public static String ATTR_NAME = "name";

    public static String EL_MAP = "map";
    public static String EL_LIST = "list";
    public static String EL_VALUE = "value";
    public static String EL_SET = "set";
    public static String EL_PROP = "prop";

    public static String EL_ENTRY = "entry";
    public static String ATTR_KEY = "key";

    public static String EL_REF = "ref";
    public static String ATTR_BEAN = "bean";

    public static String EL_NULL = "null";

    private XMLReader theReader;
    private TSParser parentParser;
    private Attributes myAttribs;
    private String endElement;
    private StringBuffer cdata = new StringBuffer();
    private String elementName = null;

    private BeanProperty currProperty;
    private String currKey;
    private PropertyValue currValue;

    /**
     * Return the property populated from xml
     * 
     * @return
     */
    public BeanProperty getProperty() {
        return currProperty;
    }

    public void setParser(XMLReader reader, TSParser parent, String el_name, Attributes attribs) {
        theReader = reader;
        parentParser = parent;
        endElement = el_name;
        myAttribs = attribs;

        createTheProperty();

        reader.setContentHandler(this);
    }

    /**
     * No children because I did it quickly
     */
    public void childFinishedParsing(TSParser child, String el_name) {
        // if (el_name.equals(XmlProperty.EL_NAME)) {
        // Do SOMIT HERE
        // }
        // reader.setContentHandler(this);
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
        cdata = new StringBuffer();

        // v lazy, no subclassing.
        if (elName.equals(EL_ENTRY)) {
            currKey = attributes.getValue(ATTR_KEY);
        }
        if (EL_VALUE.equals(elName)) {
            currValue = new PropertyValue();
        }
        if (EL_REF.equals(elName)) {
            currValue = new PropertyValue();
            currValue.setBeanRef(attributes.getValue(ATTR_BEAN));
        }

        if (EL_MAP.equals(elName)) {
            currProperty.setType(PropertyType.MAP);
        }
        if (EL_LIST.equals(elName)) {
            currProperty.setType(PropertyType.LIST);
        }
        if (EL_SET.equals(elName)) {
            currProperty.setType(PropertyType.SET);
        }
        if (EL_PROP.equals(elName)) {
            currProperty.setType(PropertyType.PROP);
        }
        if (EL_NULL.equals(elName) && currValue != null) {
            currValue.setNull(true);
        }
    }

    /**
     * Receive notification of the end of an element.
     */
    public void endElement(String uri, String localName, String qName) {
        String elName = localName;
        if (elName == null)
            elName = qName;

        String contents = cdata.toString().trim();

        if (elName.equals(EL_NAME)) {
            if (parentParser != null) {
                parentParser.childFinishedParsing(this, endElement);
            }
        }

        if (EL_VALUE.equals(elName)) {
            currValue.setValue(cdata.toString());

            currProperty.addValue(currKey, currValue);
            currValue = null;
            currKey = null;
        }

        // idref can either be <idref local="jhhjh"/> or <idref>jhhjh</idref>
        if (EL_REF.equals(elName)) {
            if (contents.length() > 0) {
                currValue.setBeanRef(contents);
            }
            currProperty.addValue(currKey, currValue);
            currValue = null;
            currKey = null;
        }

        cdata = new StringBuffer();
    }

    /**
     * Creates the current property
     */
    private void createTheProperty() {
        currProperty = new BeanProperty();

        currProperty.setName(myAttribs.getValue(ATTR_NAME));
    }

}
