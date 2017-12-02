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
import java.util.List;

import edu.gatech.teamraid.ratastic.Model.RatSighting;

/**
 * Class that creates and shows the graphs based on logged Rat reports
 */
public class Graph2Activity extends AppCompatActivity {
    private static final float BAR_WIDTH = 0.9f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);

        Button toMapBtn = (Button) findViewById(R.id.toMapButton2);
        toMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToMap = new Intent(Graph2Activity.this, MapsActivity.class);
                Graph2Activity.this.startActivity(switchToMap);
            }
        });

        Button toGraph1Btn = (Button) findViewById(R.id.graph1Button);
        toGraph1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToGraph2 = new Intent(Graph2Activity.this, GraphActivity.class);
                Graph2Activity.this.startActivity(switchToGraph2);
            }
        });
        CreateBarChart();
    }

    //Creates the bar chart for number of sightings by date
    private void CreateBarChart() {
        BarChart barChart = (BarChart) findViewById(R.id.barChart2);

        barChart.setDrawGridBackground(true);

        ArrayList<String> locationArray = new ArrayList<>();
        ArrayList<Integer> countArray = new ArrayList<>();
        for (RatSighting ratSight : RatSighting.ratSightingArray) {
            String thisLocation = ratSight.getLocation().getBorough();
            if (thisLocation.length() < 3) {
                thisLocation = "OTHER";
            }
            if (locationArray.contains(thisLocation)) {
                int index = locationArray.indexOf(thisLocation);
                int curr = countArray.get(index);
                curr++;
                countArray.set(index, curr);
            } else {
                locationArray.add(thisLocation);
                countArray.add(1);
            }
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < countArray.size(); i++) {
            barEntries.add(new BarEntry(i, countArray.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Number of sightings for the specified" +
                " location type");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(BAR_WIDTH);

        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(locationArray));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
    }

    private final class MyXAxisValueFormatter implements IAxisValueFormatter {
        private final List<String> dateValues;
        private MyXAxisValueFormatter(List<String> values) {
            this.dateValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return dateValues.get((int) value);
        }
    }
}
