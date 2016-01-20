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

import org.xml.sax.*;
import org.xml.sax.XMLReader;

/**
 * <p>
 * Any parser in a heirarchy can implement this and hand off parsing to the
 * required classes Just uncomment the lines and make your class public class
 * foo extends DefaultHandler implements TSParser
 * </p>
 * 
 * @author J.Gibbons
 */
public interface TSParser {
    // private XMLReader theReader_;
    // private TSParser parentParser_;
    // private Attributes myAttribs_;
    // private String endElement_;
    // private StringBuffer cdata_ = new StringBuffer();
    // private String elementName_=null;

    public void setParser(XMLReader reader, TSParser parent, String el_name, Attributes attribs);

    // {
    // theReader_ = reader;
    // parentParser_ = parent;
    // endElement_ = el_name;
    // myAttribs_ = attribs;
    //    
    // reader.setContentHandler(this);
    // } // end of setParser

    public void childFinishedParsing(TSParser child, String el_name);
    // {
    // if (el_name.equals(SOMIT)) {
    // Do SOMIT HERE
    // }
    // reader.setContentHandler(this);
    // } // end of childFinishedParsing

} // end of TSParser

// ***************
// End of file
// ***************</pre>
