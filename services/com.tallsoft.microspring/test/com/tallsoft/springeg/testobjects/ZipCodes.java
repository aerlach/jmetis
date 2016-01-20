/*
 * ZipCodes.java
 * 
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
public class ZipCodes {
    private HashMap<String, String> lookupTable;

    /**
     * @return Returns the lookupTable.
     */
    public HashMap<String, String> getLookupTable() {
        return lookupTable;
    }

    /**
     * @param lookupTable
     *            The lookupTable to set.
     */
    public void setLookupTable(HashMap<String, String> lookupTable) {
        this.lookupTable = lookupTable;
    }
}
