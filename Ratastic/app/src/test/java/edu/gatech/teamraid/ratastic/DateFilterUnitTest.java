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

        RatSighting r1 = new RatSighting("", "2017/11/13", new Location("", "", "", "", "", 0, 0));
        RatSighting r2 = new RatSighting("", "2017/11/14", new Location("", "", "", "", "", 0, 0));
        RatSighting r3 = new RatSighting("", "2017/11/13", new Location("", "", "", "", "", 0, 0));
        RatSighting r4 = new RatSighting("", "2017/11/14", new Location("", "", "", "", "", 0, 0));
        RatSighting r5 = new RatSighting("", "2017/11/16", new Location("", "", "", "", "", 0, 0));

        RatSighting.ratSightingArray.add(r1);
        RatSighting.ratSightingArray.add(r2);
        RatSighting.ratSightingArray.add(r3);
        RatSighting.ratSightingArray.add(r4);
        RatSighting.ratSightingArray.add(r5);

        ArrayList<RatSighting> filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "2017/11/13", "2017/11/15");
        assertEquals(4, filtered.size());

        filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "2017/11/14", "2017/11/16");
        assertEquals(3, filtered.size());

        filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "2017/11/10", "2017/11/12");
        assertEquals(0, filtered.size());

        filtered = RatSighting.getRatSightingArrayBetweenDates(RatSighting.ratSightingArray, "2017/11/10", "2018/11/12");
        assertEquals(5, filtered.size());

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
