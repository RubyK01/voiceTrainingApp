package com.example.voicetrainingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Arrays;
@SuppressWarnings({"deprecation", "unchecked"}) //To integrate Firebase I had to change SDK versions and some of of the code comes from deprciated version of
//android plot so I have to surpress the errors that causes for it to work
public class SesionGraph extends AppCompatActivity {
    //Variable to count how many times the view button is pressed in order to change how many results are visible
    private int viewCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_graph);
        //https://www.geeksforgeeks.org/how-to-get-extra-data-from-intent-in-android/
        //Above link is where i learned how to get data from other activities.
        //grabbing the Hz data from second fragment
        ArrayList<Double> firstSoundResults = (ArrayList<Double>) getIntent().getSerializableExtra("firstSoundResults");
        ArrayList<Double> secondSoundResults = (ArrayList<Double>) getIntent().getSerializableExtra("secondSoundResults");
        ArrayList<Double> thirdSoundResults = (ArrayList<Double>) getIntent().getSerializableExtra("thirdSoundResults");
        //A list to hold time data, since the .wav files are always 15 seconds the list will always be 0-15
        ArrayList<Double> seconds = new ArrayList<>(Arrays.asList(0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0,13.0,14.0,15.0));

        //http://halfhp.github.io/androidplot/docs/xyplot.html
        //Using AndroidPlots XY class
        XYPlot plot = findViewById(R.id.sessionGraph);
        XYSeries firstResults = new SimpleXYSeries(
                seconds,
                firstSoundResults,
                "1st"
        );

        XYSeries secondResults = new SimpleXYSeries(
                seconds,
                secondSoundResults,
                "2nd"
        );

        XYSeries thirdResults = new SimpleXYSeries(
                seconds,
                thirdSoundResults,
                "3rd"
        );

        LineAndPointFormatter firstResultFormatter = new LineAndPointFormatter(Color.RED, null, Color.argb(50, 255, 0, 0), null);
        firstResultFormatter.getVertexPaint().setColor(Color.RED); // Hide the vertex markers
        firstResultFormatter.setFillPaint(new Paint());
        firstResultFormatter.getFillPaint().setColor(Color.argb(50, 255, 0, 0)); // Semi-transparent fill

        LineAndPointFormatter secondResultFormatter = new LineAndPointFormatter(Color.BLUE, null, Color.argb(50, 0, 255, 0), null);
        secondResultFormatter.getVertexPaint().setColor(Color.BLUE);
        secondResultFormatter.setFillPaint(new Paint());
        secondResultFormatter.getFillPaint().setColor(Color.argb(50, 0, 255, 0));

        LineAndPointFormatter thirdResultFormatter = new LineAndPointFormatter(Color.GREEN, null, Color.argb(50, 0, 0, 255), null);
        thirdResultFormatter.getVertexPaint().setColor(Color.GREEN);
        thirdResultFormatter.setFillPaint(new Paint());
        thirdResultFormatter.getFillPaint().setColor(Color.argb(50, 0, 0, 255));

        //Adding plots to graph as default view
        plot.addSeries(firstResults,firstResultFormatter);
        plot.addSeries(secondResults,secondResultFormatter);
        plot.addSeries(thirdResults,thirdResultFormatter);
        //setting the title of the graph
        plot.setTitle("Pitch Over Time");
        //Labeling the x axis
        plot.setDomainLabel("Time (seconds)");
        //Labeling the y axis
        plot.setRangeLabel("Pitch (Hz)");
        //https://www.tabnine.com/code/java/methods/com.androidplot.xy.XYPlot/setDomainBoundaries
        //Using Domain/Range Boundaries I can have the x & y axis go from 0 to 15 in seconds
        //and 0 from 255 in hz
        plot.setDomainBoundaries(0, 15, BoundaryMode.FIXED);
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 3);
        plot.setRangeBoundaries(85, 255, BoundaryMode.FIXED);
        plot.setRangeStep(StepMode.INCREMENT_BY_VAL,25);
        plot.redraw();

        Button viewButton = findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Every time the view button is tapped viewCount variable has a 1 added
                //The resulting number decides on which plot is visible
                viewCount = viewCount + 1;
                plot.clear();
                if(viewCount == 1){//Tap once for just the first result
                    plot.addSeries(firstResults,firstResultFormatter);
                }
                else if(viewCount == 2){//Tap twice for just the second result
                    plot.addSeries(secondResults,secondResultFormatter);
                }
                else if(viewCount == 3){//Tap three times for just the third result
                    plot.addSeries(thirdResults,thirdResultFormatter);
                }
                else{//Tapping a fourth time will reset the view to showing all three plots at once
                    plot.addSeries(firstResults,firstResultFormatter);
                    plot.addSeries(secondResults,secondResultFormatter);
                    plot.addSeries(thirdResults,thirdResultFormatter);
                    viewCount = 0;
                }
                plot.redraw();
            }
        });
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