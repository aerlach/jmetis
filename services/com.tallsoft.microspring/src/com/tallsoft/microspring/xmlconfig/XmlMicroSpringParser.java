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

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.tallsoft.microspring.BeanDefinition;
import com.tallsoft.microspring.utils.TSParser;

/**
 * <p>
 * A default Handler in the SAX spec.  It reads the bean element and
 * creates an XmlBean for each one as it streams in.
 * </p>
 * 
 * @author  J.Gibbons
 */
public class XmlMicroSpringParser extends DefaultHandler implements TSParser {
    private XMLReader theReader;
    private TSParser parentParser;
    private Attributes myAttribs;
    private String endElement;
    private StringBuffer cdata = new StringBuffer();
    private String elementName=null;

    private HashMap<String, BeanDefinition> beans = new HashMap<String, BeanDefinition>();
    

    /**
     * returns all the beans
     * @return
     */
    public HashMap<String, BeanDefinition>  getBeans() {
        return beans;
    }
    
    public void setParser(XMLReader reader, TSParser parent, String el_name, Attributes attribs) {
        theReader = reader;
        parentParser = parent;
        endElement = el_name;
        myAttribs = attribs;
        
        reader.setContentHandler(this);
    }

    public void childFinishedParsing(TSParser child, String el_name)
    {
        if (el_name.equals(XmlBean.EL_NAME)) {
            XmlBean xb = (XmlBean)child;
            beans.put(xb.getBean().getId(), xb.getBean());
        }
        theReader.setContentHandler(this);
    } // end of childFinishedParsing

    //********************************************************************
    // Standard SAX i/f
    //********************************************************************    
    
    /**
     * Receive notification of character data inside an element.
     */ 
    public void characters(char[] ch, int start, int length) {
        cdata.append(ch,start,length);
    }
    
    /**
     * Receive notification of the start of an element. 
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String elName = localName;
        if (elName==null) elName = qName;
        
        if (elName.equals(XmlBean.EL_NAME)) {
            XmlBean beanReader = new XmlBean();
            beanReader.setParser(theReader, this, elName, attributes);
        }
    }
    
    /**
     * Receive notification of the end of an element. 
     */
    public void endElement(String uri, String localName, String qName) {
        String elName = localName;
        if (elName==null) elName = qName;
        
        if (elName.equals(endElement)) {
            if (parentParser!=null) {
                parentParser.childFinishedParsing(this, endElement);
            }
        }
        
    }
     
}
