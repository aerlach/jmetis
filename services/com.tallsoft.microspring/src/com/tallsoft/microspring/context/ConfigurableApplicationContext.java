/*
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.microspring.context;

import com.tallsoft.microspring.BeanFactory;
import com.tallsoft.microspring.BeansException;

/**
 * <p>
 * SPI interface to be implemented by most if not all application contexts.
 * Provides means to configure an application context in addition to the
 * application context client methods in the ApplicationContext interface.
 * 
 * Configuration and lifecycle methods are encapsulated here to avoid making
 * them obvious to ApplicationContext client code.
 * </p>
 * 
 * @author J.Gibbons
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * Add a new BeanFactoryPostProcessor that will get applied to the internal
     * bean factory of this application context on refresh, before any of the
     * bean definitions get evaluated.
     * 
     * @param beanFactoryPostProcessor
     */
    // Not in MicroSpring void
    // addBeanFactoryPostProcessor(BeanFactoryPostProcessor
    // beanFactoryPostProcessor)

    /**
     * Close this application context, releasing all resources and locks that
     * the implementation might hold.
     */
    void close() throws ApplicationContextException;

    /**
     * Return the internal bean factory of this application context.
     * 
     * @return
     */
    // Note in real spring it's ConfigurableListableBeanFactory
    // getBeanFactory();
    BeanFactory getBeanFactory() throws IllegalStateException;

    /**
     * refresh the persistent representation of the configuration, which might
     * an XML file, properties file, or relational database schema.
     */
    void refresh() throws BeansException, IllegalStateException;

    /**
     * Set the parent of this application context.
     * 
     * @param parent
     */
    void setParent(ApplicationContext parent) throws BeansException;

}
