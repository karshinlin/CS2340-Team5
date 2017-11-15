package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import edu.gatech.teamraid.ratastic.Model.RatSighting;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final double NEW_YORK_LAT = 40.7128;
    private static final double NEW_YORK_LONG = 74.0060;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mAuth.signOut();
                startActivity(intent);
            }
        });

        Button reportSighting = findViewById(R.id.addRatSighting);
        reportSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this, ReportRatSightingActivity.class);
                MapsActivity.this.startActivity(i);
            }
        });

        Button listBtn = findViewById(R.id.listButton);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchMap = new Intent(MapsActivity.this, MainActivity.class);
                MapsActivity.this.startActivity(switchMap);
            }
        });

        Button graphBtn = findViewById(R.id.graphButton);
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToGraph = new Intent(MapsActivity.this, GraphActivity.class);
                MapsActivity.this.startActivity(switchToGraph);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LatLng ny = new LatLng(NEW_YORK_LAT, NEW_YORK_LONG);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        for(RatSighting rs : RatSighting.ratSightingArray) {
            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting the position for the marker
            markerOptions.position(new LatLng(rs.getLocation().getLat(),
                    rs.getLocation().getLng()));

            // This will be displayed on taping the marker
            markerOptions.title(rs.toString() + " #" + rs.getUID());

            // Adding marker to map
            Marker aMarker = googleMap.addMarker(markerOptions);
            aMarker.setTag(rs);

            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory
                    .newLatLng(new LatLng(rs.getLocation().getLat(), rs.getLocation().getLng())));

            // Go to details on click
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, SightingListActivity.class);
                    intent.putExtra("RatSighting", (RatSighting) marker.getTag());
                    startActivity(intent);
                }
            });
        }
    }
}
