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
 * Again no OO to save on typing time. ie bundle the local idref and value all
 * into here
 * </p>
 * 
 * @author J.Gibbons
 */
public class PropertyValue {
    enum PropertyType {
        SIMPLE_VALUE, BEAN_REF
    };

    private String value;
    private boolean isNull;
    private String beanRef;

    /**
     * @return Returns the idRef.
     */
    public String getBeanRef() {
        return beanRef;
    }

    /**
     * @param idRef
     *            The idRef to set.
     */
    public void setBeanRef(String idRef) {
        this.beanRef = idRef;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return Returns the isNull.
     */
    public boolean isNull() {
        return isNull;
    }

    /**
     * @param isNull
     *            The isNull to set.
     */
    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }
}
