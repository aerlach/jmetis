/*
 * TestEvents.java
 * 
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.springeg.testobjects;

import com.tallsoft.microspring.BeansException;
import com.tallsoft.microspring.context.ApplicationContext;
import com.tallsoft.microspring.context.ApplicationContextAware;
import com.tallsoft.microspring.context.ApplicationEvent;
import com.tallsoft.microspring.context.ApplicationListener;
import com.tallsoft.microspring.context.ContextClosedEvent;
import com.tallsoft.microspring.context.ContextRefreshedEvent;

/**
 * <p>
 * Test the callbacks
 * </p>
 * 
 * @author J.Gibbons
 */
public class TestEvents implements ApplicationContextAware, ApplicationListener {
    private int initCalledCount = 0;
    private int setAppConCount = 0;
    private int closeCount = 0;
    private int refreshCount = 0;

    /**
     * Invoked after population of normal bean properties but before a custom
     * init-method.
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (initCalledCount > 0)
            throw new BeansException("BAD CALL ORDER");
        setAppConCount++;
    }

    public void customInit() throws BeansException {
        if (initCalledCount > 0)
            throw new BeansException("Init more than once!");
        if (setAppConCount != 1)
            throw new BeansException("Appcontext was called " + setAppConCount + " prior to custom init.");
        initCalledCount++;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            closeCount++;
        }
        if (event instanceof ContextRefreshedEvent) {
            refreshCount++;
        }
    }

    /**
     * @return Returns the closeCount.
     */
    public int getCloseCount() {
        return closeCount;
    }

    /**
     * @return Returns the initCalledCount.
     */
    public int getInitCalledCount() {
        return initCalledCount;
    }

    /**
     * @return Returns the refreshCount.
     */
    public int getRefreshCount() {
        return refreshCount;
    }

    /**
     * @return Returns the setAppConCount.
     */
    public int getSetAppConCount() {
        return setAppConCount;
    }
}
