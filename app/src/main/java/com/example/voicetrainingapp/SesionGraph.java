package com.example.voicetrainingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.support.annotation.NonNull;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;

public class SesionGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_graph);

        XYPlot plot = findViewById(R.id.sessionGraph);

        // Assuming you have set up your series data here

        // Customize domain (bottom) and range (side) labels
        plot.setDomainLabel("Time (s)");
        plot.setRangeLabel("Frequency (Hz)");

        // Get domain and range label widgets and their corresponding TextPaint
        TextPaint domainLabelPaint = plot.getDomainLabelWidget().getLabelPaint();
        TextPaint rangeLabelPaint = plot.getRangeLabelWidget().getLabelPaint();

        // Set text size for domain and range labels
        float textSizeInPixels = PixelUtils.spToPix(16); // Example to set text size to 16sp
        domainLabelPaint.setTextSize(textSizeInPixels);
        rangeLabelPaint.setTextSize(textSizeInPixels);

        // Customize how the labels are displayed
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
                // Format the domain labels as you want them to appear
                Number num = (Number) obj;
                toAppendTo.append(String.format("%.1f", num.floatValue()));
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;
            }
        });

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                if (obj instanceof Number) {
                    Number num = (Number) obj;
                    toAppendTo.append(String.format("%.2f", num.doubleValue())); // Format to 2 decimal places
                }
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null; // No parsing needed for this example
            }
        });

        // Update the plot with the new settings
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