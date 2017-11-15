package edu.gatech.teamraid.ratastic;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.gatech.teamraid.ratastic.Model.RatSighting;


public class SightingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightingview);

        Button backBtn = (Button) findViewById(R.id.logoutButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SightingListActivity.this.finish();
            }
        });

        ArrayList<String> sightingList = new ArrayList<>();

        RatSighting thisSighting = (RatSighting) getIntent().getSerializableExtra("RatSighting");
        sightingList.add("Unique Key: " + thisSighting.getUID());
        sightingList.add("Created Date: " + thisSighting.getCreatedDate());
        sightingList.add("Location Type: " + thisSighting.getLocation().getLocationType());
        sightingList.add("Incident Zip: " + thisSighting.getLocation().getIncidentZip());
        sightingList.add("Incident Address: " + thisSighting.getLocation().getIncidentAddress());
        sightingList.add("City: " + thisSighting.getLocation().getCity());
        sightingList.add("Borough: " + thisSighting.getLocation().getBorough());
        sightingList.add("Latitude: " + thisSighting.getLocation().getLat() + " N");
        sightingList.add("Longitude: " + Math.abs(thisSighting.getLocation().getLng()) + " W");

        ListView sightingListView = (ListView)findViewById(R.id.sightingListView);
        final ListAdapter sightingAdapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, R.id.listTextView, sightingList);
        sightingListView.setAdapter(sightingAdapter);
    }
}
