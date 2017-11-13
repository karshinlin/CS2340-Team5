package edu.gatech.teamraid.ratastic;

import org.junit.Test;

import java.util.ArrayList;

import edu.gatech.teamraid.ratastic.Model.Location;
import edu.gatech.teamraid.ratastic.Model.RatSighting;

import static org.junit.Assert.assertEquals;

/**
 * Created by colby on 11/13/17.
 */

public class DateFilterUnitTest {

    @Test
    public void testFilter() throws Exception {
        RatSighting.ratSightingArray.clear();

        RatSighting r1 = new RatSighting("", "11/13/2017", new Location("", "", "", "", "", 0, 0));
        RatSighting r2 = new RatSighting("", "11/14/2017", new Location("", "", "", "", "", 0, 0));
        RatSighting r3 = new RatSighting("", "11/13/2017", new Location("", "", "", "", "", 0, 0));
        RatSighting r4 = new RatSighting("", "11/14/2017", new Location("", "", "", "", "", 0, 0));
        RatSighting r5 = new RatSighting("", "11/16/2017", new Location("", "", "", "", "", 0, 0));

        RatSighting.ratSightingArray.add(r1);
        RatSighting.ratSightingArray.add(r2);
        RatSighting.ratSightingArray.add(r3);
        RatSighting.ratSightingArray.add(r4);
        RatSighting.ratSightingArray.add(r5);

        ArrayList<RatSighting> filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "11/13/2017", "11/15/2017");
        assertEquals(filtered.size(), 4);

        filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "11/14/2017", "11/16/2017");
        assertEquals(filtered.size(), 3);

        filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "11/10/2017", "11/12/2017");
        assertEquals(filtered.size(), 0);

    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadArguments() throws Exception {
        RatSighting.ratSightingArray.clear();

        RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray,
                "9/25/2017", "9/24/2017");

    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyArguments() throws Exception {
        RatSighting.ratSightingArray.clear();

        RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "", "");
    }

}
