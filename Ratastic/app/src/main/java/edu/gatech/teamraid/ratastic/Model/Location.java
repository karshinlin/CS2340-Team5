package edu.gatech.teamraid.ratastic.Model;

import java.io.Serializable;

/**
 * Location class with all relevant fields
 */
public class Location implements Serializable{

    private final String locationType;
    private final String incidentZip;
    private final String incidentAddress;
    private final String city;
    private final String borough;
    private final float lat;
    private final float lng;

    /**
     * Constructor to create a custom Location object
     * @param locationType type
     * @param incidentZip zip
     * @param incidentAddress address
     * @param city city
     * @param borough borough
     * @param lat latitude
     * @param lng longitude
     */
    public Location(String locationType, String incidentZip,
                       String incidentAddress, String city, String borough, float lat, float lng) {
        this.locationType = locationType;
        this.incidentZip = incidentZip;
        this.incidentAddress = incidentAddress;
        this.city = city;
        this.borough = borough;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Returns the type
     * @return string of the type
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * returns incident zip
     * @return zip
     */
    public String getIncidentZip() {
        return incidentZip;
    }

    /**
     * returns incident address
     * @return address
     */
    public String getIncidentAddress() {
        return incidentAddress;
    }

    /**
     * Returns city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * returns borough
     * @return borough
     */
    public String getBorough() {
        return borough;
    }

    /**
     * returns latitude
     * @return returns latitude
     */
    public float getLat() {
        return lat;
    }

    /**
     * returns longitude
     * @return longitude
     */
    public float getLng() {
        return lng;
    }
}
