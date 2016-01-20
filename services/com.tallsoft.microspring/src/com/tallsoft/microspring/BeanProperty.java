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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * No OO here, to cut down on lines of code. i.e. write fast.
 * Anyway, it does a property which contains a value or lists, maps, props and sets.
 * </p>
 * 
 * @author  J.Gibbons
 */
public class BeanProperty {
    public enum PropertyType {SET,VALUE,MAP,LIST,PROP};
    
    private String name;
    private PropertyType type=PropertyType.VALUE;

    private PropertyValue value;    
    private HashMap<String, PropertyValue> mapValues=new HashMap<String, PropertyValue>();
    private ArrayList<PropertyValue> listValues = new ArrayList<PropertyValue>();
    private Properties propertyValues = new Properties();
    private Set<PropertyValue> setValues = new TreeSet<PropertyValue>();




    /**
     * @return Returns the listValues.
     */
    public ArrayList<PropertyValue> getListValues() {
        return listValues;
    }
    /**
     * @param listValues The listValues to set.
     */
    public void setListValues(ArrayList<PropertyValue> listValues) {
        this.listValues = listValues;
    }
    /**
     * @return Returns the mapValues.
     */
    public HashMap<String, PropertyValue> getMapValues() {
        return mapValues;
    }
    /**
     * @param mapValues The mapValues to set.
     */
    public void setMapValues(HashMap<String, PropertyValue> mapValues) {
        this.mapValues = mapValues;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the propertyValues.
     */
    public Properties getPropertyValues() {
        return propertyValues;
    }
    /**
     * @param propertyValues The propertyValues to set.
     */
    public void setPropertyValues(Properties propertyValues) {
        this.propertyValues = propertyValues;
    }
    /**
     * @return Returns the setValues.
     */
    public Set<PropertyValue> getSetValues() {
        return setValues;
    }
    /**
     * @param setValues The setValues to set.
     */
    public void setSetValues(Set<PropertyValue> setValues) {
        this.setValues = setValues;
    }
    /**
     * @return Returns the type.
     */
    public PropertyType getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(PropertyType type) {
        this.type = type;
    }
    /**
     * @return Returns the value.
     */
    public PropertyValue getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(PropertyValue value) {
        this.value = value;
    }

    public void addValue(String key, PropertyValue val) {
        if (type==PropertyType.LIST) {
            this.listValues.add(val);
        }
        if (type==PropertyType.MAP) {
            this.mapValues.put(key,val);
        }
        if (type==PropertyType.PROP) {
            this.propertyValues.setProperty(key, val.getValue());
        }
        if (type==PropertyType.SET) {
            this.setValues.add(val);            
        }
        if (type==PropertyType.VALUE) {
            this.value = val;
        }
    }

}
