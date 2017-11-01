package edu.gatech.teamraid.ratastic;

import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.gatech.teamraid.ratastic.Model.DataLogger;
import edu.gatech.teamraid.ratastic.Model.Location;
import edu.gatech.teamraid.ratastic.Model.RatSighting;

public class ReportRatSightingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_rat_sighting);

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportRatSightingActivity.this.finish();
            }
        });

        final EditText streetAddress = (EditText) findViewById(R.id.address);
        final EditText zip = (EditText) findViewById(R.id.zip);
        final EditText city = (EditText) findViewById(R.id.city);
        final EditText locationType = (EditText) findViewById(R.id.locationType);
        final EditText borough = (EditText) findViewById(R.id.borough);
        final EditText latitude = (EditText) findViewById(R.id.latitude);
        final EditText longitude = (EditText) findViewById(R.id.longitude);
        Button report = (Button) findViewById(R.id.reportSighting);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Date time = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String timeString = df.format(time);
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                    try {
                        timeString = df.format(time);
                    } catch (ParseException ex) {
                        //Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, ex);
                        timeString = null;
                    }
                    String UID = "";
                    for (int i = 0; i < 8; i++) {
                        Random random = new Random();
                        UID = UID + random.nextInt(10);
                    }
                    float lat = Float.parseFloat(latitude.getText().toString());
                    float lng = Float.parseFloat(longitude.getText().toString());
                    Location ratLocation = new Location(locationType.getText().toString(),
                            zip.getText().toString(), streetAddress.getText().toString(), city.getText().toString(),
                            borough.getText().toString(), lat, lng);
                    RatSighting newSighting = new RatSighting(UID, time.toString(), ratLocation);

                    if (!RatSighting.ratSightingHashMap.containsKey(UID)) {
                        RatSighting.ratSightingHashMap.put(UID, newSighting);
                        RatSighting.ratSightingArray.add(0, newSighting);
                        DataLogger dbHelper = DataLogger.getHelper(ReportRatSightingActivity.this);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        dbHelper.writeNormal(db, UID, timeString, locationType.getText().toString(),zip.getText().toString(),
                                streetAddress.getText().toString(), city.getText().toString(), borough.getText().toString(),
                                latitude.getText().toString(), longitude.getText().toString());
                        db.close();
                    }
                    ReportRatSightingActivity.this.finish();
                } catch (NumberFormatException e) {
                    TextView errorMsg = (TextView) findViewById(R.id.latlongerror);
                    errorMsg.setVisibility(view.VISIBLE);
                }


            }
        });


    }
}
