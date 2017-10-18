package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;

import edu.gatech.teamraid.ratastic.Model.Location;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ListView mainList;
    private ArrayAdapter<RatSighting> mainAdapter;

    public static RatSighting currentSighting;

    public static RatSighting getCurrentSighting() {
        return currentSighting;
    }

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
        Button reportSighting = (Button) findViewById(R.id.report);
        reportSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ReportRatSightingActivity.class);
                startActivity(i);
            }
        });
        TextView text = (TextView) findViewById(R.id.userType);
        if (User.currentUser != null && User.currentUser.getUserType() != null) text.setText("Hello " + User.currentUser.getUserType().toString());
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

        //creates the main ListView shown upon login

        mainList = (ListView)findViewById(R.id.mainListView);
        mainAdapter = new ArrayAdapter<RatSighting>(this, R.layout.activity_listview, R.id.listTextView, RatSighting.ratSightingArray);
        mainList.setAdapter(mainAdapter);

        mainList.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent cityClick = new Intent(MainActivity.this, SightingListActivity.class);
        currentSighting = (RatSighting) mainAdapter.getItem(i);
        startActivity(cityClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter.notifyDataSetChanged();

    }
}
