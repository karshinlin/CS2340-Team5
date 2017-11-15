package edu.gatech.teamraid.ratastic;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.teamraid.ratastic.Model.RatSighting;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Checks if a rat sighting was added.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * DATE     | DEV    | DESCRIPTION
 * 11/12/17:  angelseay   Created.
 *
 */
@RunWith(AndroidJUnit4.class)
public class ReportRatSightingActivityTest {
    @Rule
    public ActivityTestRule<ReportRatSightingActivity> mActivityRule = new ActivityTestRule<>(ReportRatSightingActivity.class);

    /**
     * Method to sample add a sighting
     * @throws Exception if sample sighting cannot be added
     */
    @Test
    public void checkAddSighting() throws Exception {
        String streetAddress = "2919 SAMPSON AVE";
        String zip = "10465";
        String city = "NEW YORK CITY";
        String locationType = "ADDRESS";
        String borough = "BRONX";
        String latitude = "40.820953";
        String longitude = "-73.817473";

        float lat = Float.parseFloat(latitude);
        float lng = Float.parseFloat(longitude);

        int numSightings = RatSighting.ratSightingArray.size();

        onView(withId(R.id.address)).perform(typeText(streetAddress), closeSoftKeyboard());
        onView(withId(R.id.zip)).perform(typeText(zip), closeSoftKeyboard());
        onView(withId(R.id.city)).perform(typeText(city), closeSoftKeyboard());
        onView(withId(R.id.locationType)).perform(typeText(locationType), closeSoftKeyboard());
        onView(withId(R.id.borough)).perform(typeText(borough), closeSoftKeyboard());
        onView(withId(R.id.latitude)).perform(typeText(latitude), closeSoftKeyboard());
        onView(withId(R.id.longitude)).perform(typeText(longitude), closeSoftKeyboard());
        onView(withId(R.id.reportSighting)).perform(click());

        // If array size wasn't updated, rat sighting wasn't added
        Assert.assertEquals("Did not add rat sighting", numSightings + 1, RatSighting.ratSightingArray.size());

        // Check different fields
        Assert.assertEquals("Added rat sighting, streetAddress incorrect", streetAddress, RatSighting.ratSightingArray.get(numSightings).getLocation().getIncidentAddress());
        Assert.assertEquals("Added rat sighting, zip incorrect", zip, RatSighting.ratSightingArray.get(numSightings).getLocation().getIncidentZip());
        Assert.assertEquals("Added rat sighting, city incorrect", city, RatSighting.ratSightingArray.get(numSightings).getLocation().getCity());
        Assert.assertEquals("Added rat sighting, locationType incorrect", locationType, RatSighting.ratSightingArray.get(numSightings).getLocation().getLocationType());
        Assert.assertEquals("Added rat sighting, borough incorrect", borough, RatSighting.ratSightingArray.get(numSightings).getLocation().getBorough());
        Assert.assertEquals("Added rat sighting, latitude incorrect", lat, RatSighting.ratSightingArray.get(numSightings).getLocation().getLat());
        Assert.assertEquals("Added rat sighting, longitude incorrect", lng, RatSighting.ratSightingArray.get(numSightings).getLocation().getLng());


    }
}
