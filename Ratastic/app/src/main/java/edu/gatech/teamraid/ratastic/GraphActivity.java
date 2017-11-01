package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by maxengle on 10/31/17.
 */

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Button toMapBtn = (Button) findViewById(R.id.toMapButton);
        toMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToMap = new Intent(GraphActivity.this, MapsActivity.class);
                GraphActivity.this.startActivity(switchToMap);
            }
        });
    }
}
