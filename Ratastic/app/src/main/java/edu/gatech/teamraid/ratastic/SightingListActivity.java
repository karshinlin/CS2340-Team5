package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;


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
    }
}
