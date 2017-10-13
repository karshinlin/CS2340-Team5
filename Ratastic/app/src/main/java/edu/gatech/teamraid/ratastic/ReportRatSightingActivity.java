package edu.gatech.teamraid.ratastic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

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

        final EditText address = (EditText) findViewById(R.id.address);
        final EditText zip = (EditText) findViewById(R.id.zip);
        final EditText city = (EditText) findViewById(R.id.city);
        final EditText locationType = (EditText) findViewById(R.id.locationType);
        final EditText borough = (EditText) findViewById(R.id.borough);

        Button report = (Button) findViewById(R.id.reportSighting);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date time = Calendar.getInstance().getTime();
                RatSighting newSighting = new RatSighting("", time.toString(), locationType.getText().toString(),
                        zip.getText().toString(), address.getText().toString(), city.getText().toString(),
                        borough.getText().toString(), 0, 0);
                RatSighting.ratSightingArray.add(newSighting);
                ReportRatSightingActivity.this.finish();
            }
        });


    }
}
