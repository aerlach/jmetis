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
 * Only implemented in microspring so we can get the refresh and close events. 
 * ie implement this and you will have onApplicationEvent called with 
 * ContextClosedEvent or ContextRefreshedEvent as a param. But only if you 
 * are a singleton!
 * </p>
 * 
 * @author J.Gibbons
 */
public interface ApplicationListener {
    /**
     * Handle an application event.
     * 
     * @param event
     */
    void onApplicationEvent(ApplicationEvent event);
}
