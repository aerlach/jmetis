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

import java.util.ArrayList;

/**
 * <p>
 * Silly class for testing
 * </p>
 * 
 * @author J.Gibbons
 */
public class RegionInfo {
    private CityScape cityFinder;
    private ArrayList regions;

    /**
     * @return Returns the cityFinder.
     */
    public CityScape getCityFinder() {
        return cityFinder;
    }

    /**
     * @param cityFinder
     *            The cityFinder to set.
     */
    public void setCityFinder(CityScape cityFinder) {
        this.cityFinder = cityFinder;
    }

    /**
     * @return Returns the regions.
     */
    public ArrayList getRegions() {
        return regions;
    }

    /**
     * @param regions
     *            The regions to set.
     */
    public void setRegions(ArrayList regions) {
        this.regions = regions;
    }
}
