/*
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.microspring;

/**
 * <p>
 * Copy of Spring exception
 * </p>
 * 
 * @author J.Gibbons
 */
public class BeanNotOfRequiredTypeException extends BeansException {
    public BeanNotOfRequiredTypeException(String msg) {
        super(msg);
    }
}
