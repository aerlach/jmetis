/*
 * AppContextTest.java
 * 
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.springeg;

import java.net.URL;

import junit.framework.TestCase;

import com.tallsoft.microspring.BeanFactory;
import com.tallsoft.microspring.context.ConfigurableApplicationContext;
import com.tallsoft.microspring.xmlconfig.FileSystemXmlApplicationContext;
import com.tallsoft.springeg.testobjects.CityScape;
import com.tallsoft.springeg.testobjects.PlanetInfo;
import com.tallsoft.springeg.testobjects.ZipCodes;

/**
 * <p>
 * Test the parent thing.
 * </p>
 * 
 * @author J.Gibbons
 */
public class AppContextTest extends TestCase {
    private BeanFactory factory;

    public void setUp() throws Exception {
        // Vital parents are created first if thats where parent defns exist
        URL fileUrl = getClass().getResource("example.xml");
        ConfigurableApplicationContext parentFactory = new FileSystemXmlApplicationContext(fileUrl.getFile());

        fileUrl = getClass().getResource("example1.xml");
        factory = new FileSystemXmlApplicationContext(fileUrl.getFile());

        ((ConfigurableApplicationContext) factory).setParent(parentFactory);
    }

    public void testType() throws Exception {
        // look up in the parent
        assertEquals("test type", CityScape.class, factory.getType("cityFinder"));
    }

    public void testSimpleBeanInParent() throws Exception {
        CityScape cityFinder = (CityScape) factory.getBean("cityFinder");
        assertEquals("trying to find code", "London", cityFinder.lookupByCode("LDN"));
        assertEquals("trying to find code", "Frankfurt", cityFinder.lookupByCode("FFT"));
        assertNull("trying to find code", cityFinder.lookupByCode("NY"));
    }

    public void testSimpleBeanInMe() throws Exception {
        ZipCodes zipFinder = (ZipCodes) factory.getBean("zipLookup");
    }

    public void testTemplateBeanFromParent() throws Exception {
        PlanetInfo saturn = (PlanetInfo) factory.getBean("saturn");

        assertNotNull(saturn);

        assertEquals("sol", saturn.getSystem());
        assertEquals("Saturn", saturn.getPlanetName());
    }

}
