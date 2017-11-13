package edu.gatech.teamraid.ratastic.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by colby on 10/4/17.
 */

public class RatSighting implements Serializable{

    private String UID;
    private String createdDate;

    private Location location;

    public static ArrayList<RatSighting> ratSightingArray = new ArrayList<>();
    public static HashMap<String, RatSighting> ratSightingHashMap = new HashMap<>();

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

    public static ArrayList<RatSighting> getRatSightingArrayBetweenDates(ArrayList<RatSighting> ratSightings,
                                                                         String start, String end) {

        if (ratSightings == null) {
            throw new IllegalArgumentException("Cannot pass null data into method");
        }
        if (start.equals("") || end.equals("") || start.compareTo(end) > 0) {
            throw new IllegalArgumentException("The start date must be after the end date.");
        }
        ArrayList<RatSighting> newSightings = new ArrayList<>();
        for (RatSighting sighting : ratSightings) {
            if (sighting.createdDate.compareTo(start) >= 0 && sighting.createdDate.compareTo(end) <= 0) {
                newSightings.add(sighting);
            }
        }

        return newSightings;
    }
}
