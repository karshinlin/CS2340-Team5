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
public class GraphActivity extends AppCompatActivity {
    private static final float BAR_WIDTH = 0.9f;

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
        CreateBarChart();
    }

    //Creates the bar chart for number of sightings by date
    private void CreateBarChart() {
        BarChart barChart = (BarChart) findViewById(R.id.barChart);

        barChart.setDrawGridBackground(true);

        ArrayList<String> dateArray = new ArrayList<>();
        ArrayList<Integer> countArray = new ArrayList<>();
        for (RatSighting ratSight : RatSighting.ratSightingArray) {
            String thisDate = ratSight.getCreatedDate();
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
        data.setBarWidth(BAR_WIDTH);

        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(dateArray));
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
