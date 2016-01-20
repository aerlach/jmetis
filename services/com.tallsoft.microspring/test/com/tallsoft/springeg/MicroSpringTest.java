/*
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
import com.tallsoft.microspring.BeansException;
import com.tallsoft.microspring.NoSuchBeanDefinitionException;
import com.tallsoft.microspring.xmlconfig.FileSystemXmlApplicationContext;
import com.tallsoft.springeg.testobjects.CityScape;
import com.tallsoft.springeg.testobjects.PlanetInfo;
import com.tallsoft.springeg.testobjects.RegionInfo;

/**
 * <p>
 * A single test to do the job at hand...
 * </p>
 * 
 * @author J.Gibbons
 */
public class MicroSpringTest extends TestCase {
    private BeanFactory factory;

    public void setUp() throws Exception {
        URL fileUrl = getClass().getResource("example.xml");
        factory = new FileSystemXmlApplicationContext(fileUrl.getFile());
    }

    public void testType() throws Exception {
        assertEquals("test type", CityScape.class, factory.getType("cityFinder"));
    }

    /**
     * Tests map
     * 
     * @throws Exception
     */
    public void testSimpleBean() throws Exception {
        CityScape cityFinder = (CityScape) factory.getBean("cityFinder");
        assertEquals("trying to find code", "London", cityFinder.lookupByCode("LDN"));
        assertEquals("trying to find code", "Frankfurt", cityFinder.lookupByCode("FFT"));
        assertNull("trying to find code", cityFinder.lookupByCode("NY"));
    }

    /**
     * Test ref id
     * 
     * @throws Exception
     */
    public void testRefBean() throws Exception {
        RegionInfo r = (RegionInfo) factory.getBean("region");
        CityScape cityFinder = (CityScape) factory.getBean("cityFinder");
        assertEquals("Singleton test", cityFinder, r.getCityFinder());
    }

    /**
     * Test list
     * 
     * @throws Exception
     */
    public void testList() throws Exception {
        RegionInfo r = (RegionInfo) factory.getBean("region");
        assertEquals("Number of regions", 2, r.getRegions().size());
        assertTrue("Find Europe", r.getRegions().contains("Europe"));
        assertTrue("Find America", r.getRegions().contains("America"));
    }

    /**
     * Test templating
     */
    public void testTemplate() throws Exception {
        PlanetInfo earth = (PlanetInfo) factory.getBean("earth");
        PlanetInfo mars = (PlanetInfo) factory.getBean("mars");

        assertNotNull(earth);
        assertNotNull(mars);

        assertEquals("sol", earth.getSystem());
        assertEquals("sol", mars.getSystem());
        assertEquals("Earth", earth.getPlanetName());
        assertEquals("Mars", mars.getPlanetName());
    }

    /**
     * Test that base class cant be opened
     * 
     */
    public void testCantCreateAnAbstract() {
        try {
            factory.getBean("templateGeography");
        } catch (BeansException ex) {
            ex.printStackTrace(System.out);
            return;
        }
        fail("Needed an exception");
    }

    public void testExceptionIfBadName() {
        try {
            factory.getBean("someRandom");
        } catch (NoSuchBeanDefinitionException ex) {
            ex.printStackTrace(System.out);
            return;
        } catch (Exception ex) {
            fail("Wrong exception :"+ex.getClass().getName()+":"+ex.getMessage());
        }
        fail("Needed an exception");
    }

}
