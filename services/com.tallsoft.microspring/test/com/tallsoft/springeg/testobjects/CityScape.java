/*
 * Copyright (c) 2005  Tall Software Ltd
 *
 * DISCLAIMER OF WARRANTY. Software is provided "AS IS," without a
 * warranty of any kind. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, CONDITIONS OR OTHER TERMS, INCLUDING ANY IMPLIED TERM
 * OF SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
package com.tallsoft.springeg.testobjects;

import java.util.HashMap;

/**
 * <p>
 * Silly class for testing
 * </p>
 * 
 * @author J.Gibbons
 */
public class CityScape {
    private HashMap cityLookup;

    public void setCityMap(HashMap cityLookup) {
        this.cityLookup = cityLookup;
    }

    public String lookupByCode(String code) {
        return (String) cityLookup.get(code);
    }
}
