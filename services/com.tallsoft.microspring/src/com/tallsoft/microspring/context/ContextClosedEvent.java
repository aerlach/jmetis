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
 * All ApplicationListener beans will be called with this when the app context
 * is closed.
 * </p>
 * 
 * @author J.Gibbons
 */
public class ContextClosedEvent extends ApplicationEvent {

    /**
     * Creates a new ContextClosedEvent.
     * 
     * @param source
     */
    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }

    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) source;
    }
}
