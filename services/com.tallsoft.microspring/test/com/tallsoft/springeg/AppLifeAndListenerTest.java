/*
 * AppListenerTest.java
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
import com.tallsoft.springeg.testobjects.TestEvents;

/**
 * <p>
 * Test lifecycle and event listening
 * </p>
 * 
 * @author J.Gibbons
 */
public class AppLifeAndListenerTest extends TestCase {
    private BeanFactory factory;

    public void setUp() throws Exception {
        URL fileUrl = getClass().getResource("example2.xml");
        factory = new FileSystemXmlApplicationContext(fileUrl.getFile());
    }

    public void testCloseEvent() throws Exception {
        TestEvents obj = (TestEvents) factory.getBean("lifeCycleTester");

        assertEquals("init count", 1, obj.getInitCalledCount());
        assertEquals("context count", 1, obj.getSetAppConCount());

        // Refresh the objects and the definitions. BUT note that existing
        // singletons are not reloaded.
        ((ConfigurableApplicationContext) factory).refresh();
        assertEquals("refresh count", 1, obj.getRefreshCount());

        ((ConfigurableApplicationContext) factory).close();

        assertEquals("close count", 1, obj.getCloseCount());

        // Now the context is closed, then the obj will NOT get more events
        ((ConfigurableApplicationContext) factory).close();

        assertEquals("close count still 1", 1, obj.getCloseCount());

        // same with refresh
        ((ConfigurableApplicationContext) factory).refresh();
        assertEquals("refresh count", 1, obj.getRefreshCount());

    }
}
