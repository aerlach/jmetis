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

import com.tallsoft.microspring.BeansException;

/**
 * <p>
 * Copy of Spring API
 * </p>
 * <p>
 * After all setters are called and before the
 * </p>
 * 
 * @author J.Gibbons
 */
public interface ApplicationContextAware {
    /**
     * Set the ApplicationContext that this object runs in. Normally this call
     * will be used to initialize the object. Invoked after population of normal
     * bean properties but before a custom init-method.
     * 
     * @param applicationContext
     * @throws BeansException
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
