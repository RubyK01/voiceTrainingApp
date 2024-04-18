package com.example.voicetrainingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.androidplot.xy.FillDirection;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import java.util.Arrays;

public class SesionGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_graph);
        //Average minimum and maximum for masculine and feminine vocal frequencies
        int masculineMin = 85;
        int masculineMax = 180;
        int feminineMin = 165;
        int feminineMax = 255;
        XYPlot plot = findViewById(R.id.sessionGraph);
        // arrays to hold the min and max ranges
        Number[] masculineRange = {masculineMin, masculineMin};
        Number[] feminineRange = {feminineMin, feminineMin};

        XYSeries masculineSeries = new SimpleXYSeries(
                Arrays.asList(masculineRange),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "Masc Range");

        XYSeries feminineSeries = new SimpleXYSeries(
                Arrays.asList(feminineRange),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "Fem Range");

// Create formatters for the ranges that use transparent shading
        LineAndPointFormatter masculineFormatter = new LineAndPointFormatter();
        masculineFormatter.getLinePaint().setColor(Color.TRANSPARENT); // Hide the line
        masculineFormatter.getFillPaint().setAlpha(50); // Semi-transparent shading
        masculineFormatter.setFillDirection(FillDirection.BOTTOM);

        LineAndPointFormatter feminineFormatter = new LineAndPointFormatter();
        feminineFormatter.getLinePaint().setColor(Color.TRANSPARENT); // Hide the line
        feminineFormatter.getFillPaint().setAlpha(50); // Semi-transparent shading
        feminineFormatter.setFillDirection(FillDirection.BOTTOM);

// Add the series to the plot with their formatters
        plot.addSeries(masculineSeries, masculineFormatter);
        plot.addSeries(feminineSeries, feminineFormatter);
        plot.setTitle("Pitch Over Time");
        plot.setDomainLabel("Duration (15 seconds)");
        plot.setRangeLabel("Pitch (Hz)");
        plot.redraw();


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This method call happens immediately when the button is pressed
                finish();
            }
        });
    }
}