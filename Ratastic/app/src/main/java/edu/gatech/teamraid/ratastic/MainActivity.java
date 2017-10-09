package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        if (User.currentUser != null) text.setText("Hello " + User.currentUser.getUserType());
        try {
            CSVReader reader = new CSVReader(new FileReader(new File("/raw/ratsightings.csv")));
            String []nextLine;
            ArrayList<RatSighting> sightings = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals("Unique Key")) {
                    continue;
                }
                String UID = nextLine[0];
                float lat = Float.parseFloat(nextLine[nextLine.length - 3]);
                float lng = Float.parseFloat(nextLine[nextLine.length - 2]);
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
