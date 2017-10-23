package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import edu.gatech.teamraid.ratastic.Model.Location;
import edu.gatech.teamraid.ratastic.Model.RatSighting;


/**
 * Created by angelseay on 10/23/17.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static RatSighting currentSighting;
    public static RatSighting getCurrentSighting() {
        return currentSighting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.ratsightings)));
            String []nextLine;
            int count = 0;
            while ((nextLine = reader.readNext()) != null && count < 26) {
                if (nextLine[0].equals("Unique Key") || nextLine[0].equals("")) {
                    continue;
                }
                String UID = nextLine[0];
                if (nextLine[49].equals("") || nextLine[50].equals("")) {
                    continue;
                }
                float lat = Float.parseFloat(nextLine[49]);
                float lng = Float.parseFloat(nextLine[50]);
                String createdDate = nextLine[1];
                String locationType = nextLine[7];
                String incidentZip = nextLine[8];
                String incidentAddress = nextLine[9];
                String city = nextLine[16];
                String borough = nextLine[23];
                Location ratLocation = new Location(locationType, incidentZip,
                        incidentAddress, city, borough, lat, lng);
                RatSighting.ratSightingArray.add(new RatSighting(UID, createdDate, ratLocation));
                count++;
            }
        } catch (IOException e) {
            //Couldn't load data
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ny = new LatLng(40.7128, 74.0060);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        for(RatSighting rs : RatSighting.ratSightingArray) {
            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting the position for the marker
            markerOptions.position(new LatLng(rs.getLocation().getLat(), rs.getLocation().getLng()));

            // This will be displayed on taping the marker
            markerOptions.title(rs.toString() + " #" + rs.getUID());

            // Adding marker to map
            Marker amarker = mMap.addMarker(markerOptions);
            amarker.setTag(rs);

            // Animating to the touched position
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(rs.getLocation().getLat(), rs.getLocation().getLng())));

            // Go to details on click
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, SightingListActivity.class);
                    currentSighting = (RatSighting) marker.getTag();
                    startActivity(intent);
                }
            });
        }
    }
}
