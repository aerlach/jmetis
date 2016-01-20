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

/**
 * <p>
 * Silly class for testing
 * </p>
 * 
 * @author J.Gibbons
 */
public class BaseGeography {
    private String planetName;
    private String system;

    /**
     * @return Returns the planetName.
     */
    public String getPlanetName() {
        return planetName;
    }

    /**
     * @param planetName
     *            The planetName to set.
     */
    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    /**
     * @return Returns the system.
     */
    public String getSystem() {
        return system;
    }

    /**
     * @param system
     *            The system to set.
     */
    public void setSystem(String system) {
        this.system = system;
    }
}
