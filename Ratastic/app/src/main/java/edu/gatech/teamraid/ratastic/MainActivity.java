package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.gatech.teamraid.ratastic.Model.RatSighting;
import edu.gatech.teamraid.ratastic.Model.User;

/**
 * Login Page for Application. Linked to activity_login.xml

 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 * 10/1/17:  KLIN     Created.
 * 10/9/17:  KLIN     Configured Firebase database usage to capture userType
 *
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        TextView text = (TextView) findViewById(R.id.userType);
        if (User.currentUser != null && User.currentUser.getUserType() != null) text.setText("Hello " + User.currentUser.getUserType().toString());
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
