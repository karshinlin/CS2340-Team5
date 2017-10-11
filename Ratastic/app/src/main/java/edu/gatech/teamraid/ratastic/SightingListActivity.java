package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import edu.gatech.teamraid.ratastic.Model.RatSighting;


/**
 * Created by maxengle on 10/10/17.
 */

public class SightingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightingview);

        Button backBtn = (Button) findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent goBack = new Intent(SightingListActivity.this, MainActivity.class);
                startActivity(goBack);
            }
        });

        ArrayList<String> sightingList = new ArrayList<>();
        RatSighting thisSighting = MainActivity.getCurrentSighting();
        sightingList.add("Unique Key: " + thisSighting.getUID());
        sightingList.add("Created Date: " + thisSighting.getCreatedDate());
        sightingList.add("Location Type: " + thisSighting.getLocationType());
        sightingList.add("Incident Zip: " + thisSighting.getIncidentZip());
        sightingList.add("Incident Address: " + thisSighting.getIncidentAddress());
        sightingList.add("City: " + thisSighting.getCity());
        sightingList.add("Borough: " + thisSighting.getBorough());
        sightingList.add("Latitude: " + thisSighting.getLat());
        sightingList.add("Longitude: " + thisSighting.getLng());

        ListView sightingListView = (ListView)findViewById(R.id.sightingListView);
        final ArrayAdapter<String> sightingAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.listTextView, sightingList);
        sightingListView.setAdapter(sightingAdapter);
    }
}