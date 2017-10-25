package edu.gatech.teamraid.ratastic.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by colby on 10/4/17.
 */

public class RatSighting implements Serializable{

    private String UID;
    private String createdDate;

    private Location location;

    public static ArrayList<RatSighting> ratSightingArray = new ArrayList<>();

    public RatSighting(String UID, String createdDate, Location location) {
        this.UID = UID;
        this.createdDate = createdDate;
        this.location = location;

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() { return location; }

    public String toString() {
        return "Sighting in " + location.getCity();
    }
}
