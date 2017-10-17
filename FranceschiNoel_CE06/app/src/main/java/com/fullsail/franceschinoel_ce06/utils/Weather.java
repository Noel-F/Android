package com.fullsail.franceschinoel_ce06.utils;

import java.io.Serializable;

// Noel Franceschi
// MDF3 1610
// Weather.java

public class Weather implements Serializable {

    public String city;
    public String description;

    public double temp;
    public double lastUpdated;
    public double minTemp;
    public double maxTemp;

    public Weather(String city, String description, double temp, double lastUpdated, double minTemp, double maxTemp) {

        this.city = city;
        this.description = description;
        this.temp = temp;
        this.lastUpdated = lastUpdated;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;

    }

}
