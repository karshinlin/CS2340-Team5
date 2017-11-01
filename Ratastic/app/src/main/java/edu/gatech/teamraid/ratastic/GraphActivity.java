package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import edu.gatech.teamraid.ratastic.Model.RatSighting;

/**
 * Created by maxengle on 10/31/17.
 */

public class GraphActivity extends AppCompatActivity {

    BarChart barChart;

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

        barChart = (BarChart) findViewById(R.id.barChart);

        barChart.setDrawGridBackground(true);

        String thisDate = "";
        ArrayList<String> dateArray = new ArrayList<String>();
        ArrayList<Integer> countArray = new ArrayList<Integer>();
        for (RatSighting ratSight : RatSighting.ratSightingArray) {
            thisDate = ratSight.getCreatedDate();
            thisDate = thisDate.substring(0, 7);
            if (dateArray.contains(thisDate)) {
                int index = dateArray.indexOf(thisDate);
                int curr = countArray.get(index);
                curr++;
                countArray.set(index, curr);
            } else {
                dateArray.add(thisDate);
                countArray.add(1);
            }
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < countArray.size(); i++) {
            barEntries.add(new BarEntry(i, countArray.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Number of sightings for the specified" +
                " date (Year-Month)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(dateArray));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        private ArrayList<String> dateValues;
        private MyXAxisValueFormatter(ArrayList<String> values) {
            this.dateValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return dateValues.get((int) value);
        }
    }
}
