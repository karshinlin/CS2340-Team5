package edu.gatech.teamraid.ratastic.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class RatSighting implements Serializable{

    private String UID;
    private String createdDate;

    private Location location;

    public static final ArrayList<RatSighting> ratSightingArray = new ArrayList<>();
    public static final HashMap<String, RatSighting> ratSightingHashMap = new HashMap<>();

    public RatSighting(String UID, String createdDate, Location location) {
        this.UID = UID;
        this.createdDate = createdDate;
        this.location = location;

    }

    /**
     * Gets uid of sighting
     * @return uid
     */
    public String getUID() {
        return UID;
    }

    /**
     * Returns rat sighting date
     * @return date
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * returns location object
     * @return location
     */
    public Location getLocation() { return location; }

    /**
     * returns city
     * @return city string
     */
    public String toString() {
        return "Sighting in " + location.getCity();
    }

    /**
     * Filters singleton Rat Sighting array between dates
     * @param ratSightings Array List
     * @param start start date
     * @param end end date
     * @return list of rat sightings
     */
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
