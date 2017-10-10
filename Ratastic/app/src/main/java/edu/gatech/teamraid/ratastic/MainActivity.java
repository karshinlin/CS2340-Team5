package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.gatech.teamraid.ratastic.Model.RatSighting;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mAuth.signOut();
                startActivity(intent);
            }
        });
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.ratsightings)));
            String []nextLine;
            ArrayList<RatSighting> sightings = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {
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
                sightings.add(new RatSighting(UID, createdDate, locationType, incidentZip, incidentAddress, city, borough, lat, lng));
            }
        } catch (IOException e) {
            //Couldn't load data
            e.printStackTrace();
        }
    }
}
