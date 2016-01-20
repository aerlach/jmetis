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

/**
 * <p>
 * Copy of Spring API
 * </p>
 * 
 * @author J.Gibbons
 */
public interface ApplicationContext {

    /**
     * Return a friendly name for this context.
     * 
     * @return
     */
    String getDisplayName();

    /**
     * Return the parent context, or null if there is no parent, and this is the
     * root of the context hierarchy.
     * 
     * @return
     */
    ApplicationContext getParent();

    /**
     * Return the timestamp when this context was first loaded.
     * 
     * @return
     */
    long getStartupDate();

    /**
     * Notify all listeners registered with this application of an application
     * event. ie those beans which are singletons and which implement
     * ApplicationListener
     * 
     * @param event
     */
    void publishEvent(ApplicationEvent event);

}
