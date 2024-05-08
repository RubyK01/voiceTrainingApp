package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.androidplot.pie.*;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OverallGraph extends AppCompatActivity {
    TextView mascText, femText, androText;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_graph);
        // https://www.youtube.com/watch?v=2YlFfNt5wCI How I learned to create a pie chart with android plot

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = db.getReference().child(user.getEmail().replace('.', ',')+"frequency");
        System.out.println("Looking for | "+user.getEmail().replace('.', ',')+"frequency | in the database");
        Query query = dbRef.orderByChild("email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int mascCount = 0;
                    int femCount = 0;
                    int androCount = 0;
                    ArrayList<Long> frequencies = new ArrayList<>();
                    // https://stackoverflow.com/a/40251242
                    // I learned how to retrieve arrayLists from firebase from the above
                    GenericTypeIndicator<ArrayList<Long>> x = new GenericTypeIndicator<ArrayList<Long>>() {
                    };
                    if (user != null) { //Check if a user somehow got here without logging in
                        // https://stackoverflow.com/a/40367515
                        // From the above I learned how to loop through each child in the base
                        for (DataSnapshot freqSnapshot : snapshot.getChildren()) {
                            ArrayList<Long> firstSoundResults = freqSnapshot.child("firstSoundResults").getValue(x);
                            System.out.println("First Array: "+firstSoundResults);
                            ArrayList<Long> secondSoundResults = freqSnapshot.child("secondSoundResults").getValue(x);
                            System.out.println("Second Array: "+secondSoundResults);
                            ArrayList<Long> thirdSoundResults = freqSnapshot.child("thirdSoundResults").getValue(x);
                            System.out.println("Third Array: "+thirdSoundResults);

                            if (firstSoundResults != null) frequencies.addAll(firstSoundResults);
                            if (secondSoundResults != null) frequencies.addAll(secondSoundResults);
                            if (thirdSoundResults != null) frequencies.addAll(thirdSoundResults);
                            System.out.println("Combined Array: "+frequencies);
                        }

                        //Loop to sort the overall frequencies result to see if the voice is most masc, fem or andro.
                        for (int i = 0; i < frequencies.size(); i++) {
                            if (frequencies.get(i) >= 85 && frequencies.get(i) <= 175) {
                                mascCount++;
                            } else if (frequencies.get(i) >= 147 && frequencies.get(i) <= 294) {
                                femCount++;
                            } else{
                                androCount++;
                            }
                        }
                        updateChart(mascCount, femCount, androCount, frequencies.size());
                    } else {// If a user is not logged in they get sent to the login screen.
                        Intent loginPage = new Intent(getApplicationContext(), Login.class);
                        startActivity(loginPage);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Could not connect to firebase :c");
            }
        });

        TextView chartTitle = findViewById(R.id.chart_title);
        chartTitle.setText("Overall Progress");
        mascText = findViewById(R.id.mascText);
        femText = findViewById(R.id.femText);
        androText = findViewById(R.id.androText);

        PieChart HzChart = findViewById(R.id.pie_chart);

        //Default values I set
        int mascValue = 50;
        int femValue = 50;
        int androValue = 50;

        //Chart segments
        //I have the titles left blank as the text within the segments really small
        //There are labels at the bottom of the screen for the user to read
        Segment masculine = new Segment("", mascValue);
        Segment feminine = new Segment("", femValue);
        Segment androgynous = new Segment("", androValue);

        //Chart formatters
        SegmentFormatter mascFormatter = new SegmentFormatter(Color.parseColor("#3F51B5"),Color.BLACK);
        SegmentFormatter femFormatter = new SegmentFormatter(Color.parseColor("#FF4081"),Color.BLACK);
        SegmentFormatter androFormatter = new SegmentFormatter(Color.parseColor("#4CAF50"),Color.BLACK);

        HzChart.addSegment(masculine, mascFormatter);
        HzChart.addSegment(feminine, femFormatter);
        HzChart.addSegment(androgynous, androFormatter);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainScreen);
                finish();
            }
        });
    }
    private void updateChart(int mascCount, int femCount, int androCount, int total){
        PieChart HzChart = findViewById(R.id.pie_chart);
        HzChart.clear();


        if(total>0){
            int mascPercentage = 100 * mascCount / total;
            int femPercentage = 100 * femCount / total;
            int androPercentage = 100 * androCount / total;
            //Chart segments
            //I have the titles left blank as the text within the segments really small
            //There are labels at the bottom of the screen for the user to read
            Segment masculine = new Segment("", mascPercentage);
            Segment feminine = new Segment("", femPercentage);
            Segment androgynous = new Segment("", androPercentage);

            //Chart formatters
            SegmentFormatter mascFormatter = new SegmentFormatter(Color.parseColor("#3F51B5"),Color.BLACK);
            SegmentFormatter femFormatter = new SegmentFormatter(Color.parseColor("#FF4081"),Color.BLACK);
            SegmentFormatter androFormatter = new SegmentFormatter(Color.parseColor("#4CAF50"),Color.BLACK);

            HzChart.addSegment(masculine, mascFormatter);
            HzChart.addSegment(feminine, femFormatter);
            HzChart.addSegment(androgynous, androFormatter);

            HzChart.redraw();
            TextView chartTitle = findViewById(R.id.chart_title);
            chartTitle.setText("Overall Progress");
            mascText.setText("Masculine: "+mascPercentage+"%");
            femText.setText("Feminine: "+femPercentage+"%");
            androText.setText("Androgynous: "+androPercentage+"%");
        }
        else{
            TextView chartTitle = findViewById(R.id.chart_title);
            chartTitle.setText("Overall Progress");
            mascText.setText("Masculine: 0%");
            femText.setText("Feminine: 0%");
            androText.setText("Androgynous: 0%");
        }
    }
}