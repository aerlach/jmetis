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
 * Used as base class for refresh and close events
 * </p>
 * 
 * @author J.Gibbons
 */
public class ApplicationEvent {
    protected Object source;
    private long timestamp;

    /**
     * Create a new ApplicationEvent.
     * 
     * @param source
     */
    public ApplicationEvent(Object source) {
        this.source = source;
        timestamp = System.currentTimeMillis();
    }

    /**
     * Return the system time in milliseconds when the event happened.
     * 
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

}
