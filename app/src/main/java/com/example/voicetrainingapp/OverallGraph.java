package com.example.voicetrainingapp;

import androidx.appcompat.app.AppCompatActivity;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OverallGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_graph);
        //https://www.geeksforgeeks.org/how-to-add-a-pie-chart-into-an-android-application/
        // https://www.youtube.com/watch?v=2YlFfNt5wCI

        PieChart HzChart = findViewById(R.id.pie_chart);

        //Chart segments
        Segment masculine = new Segment("Masculine", 0);
        Segment feminine = new Segment("Feminine", 0);
        Segment androgynous = new Segment("Androgynous", 0);

        //Chart formatters
        SegmentFormatter mascFormatter = new SegmentFormatter(Color.parseColor("3F51B5"),Color.BLUE);
        SegmentFormatter femFormatter = new SegmentFormatter(Color.parseColor("3F51B5"),Color.MAGENTA);
        SegmentFormatter androFormatter = new SegmentFormatter(Color.parseColor("3F51B5"),Color.GREEN);

        HzChart.addSeries(masculine, mascFormatter);
        HzChart.addSeries(feminine, femFormatter);
        HzChart.addSeries(androgynous, androFormatter);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}