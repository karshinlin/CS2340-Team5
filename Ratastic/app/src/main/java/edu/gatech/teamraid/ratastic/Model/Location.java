package edu.gatech.teamraid.ratastic.Model;

import java.io.Serializable;

public class Location implements Serializable{

    private String locationType;
    private String incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private float lat;
    private float lng;

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

    public String getLocationType() {
        return locationType;
    }

    public String getIncidentZip() {
        return incidentZip;
    }

    public String getIncidentAddress() {
        return incidentAddress;
    }

    public String getCity() {
        return city;
    }

    public String getBorough() {
        return borough;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }
}
