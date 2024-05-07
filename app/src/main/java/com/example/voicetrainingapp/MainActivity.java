package com.example.voicetrainingapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.voicetrainingapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"deprecation", "unchecked"})
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth auth;
    Button btnLogout, btnJournal, btnProgress;
    FirebaseUser user;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.logout);
        btnJournal = findViewById(R.id.button_second);
        btnProgress = findViewById(R.id.button_third);
        user = auth.getCurrentUser();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("WaitList");
        String email = user.getEmail().replace(".",",");

        if (text != null) {
            binding.userDetails.setText("test");
        } else {
            Log.e("MainActivity", "TextView is null");
        }
        if(user == null){
            Intent loginPage = new Intent(getApplicationContext(), Login.class);
            startActivity(loginPage);
            finish();
        }
        else{
            binding.userDetails.setText(user.getEmail().toString());
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginPage = new Intent(getApplicationContext(), Login.class);
                startActivity(loginPage);
                finish();
            }
        });

        btnJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent journalPage = new Intent(getApplicationContext(), Journal.class);
                startActivity(journalPage);
                finish();
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent progressPage = new Intent(getApplicationContext(), OverallGraph.class);
                startActivity(progressPage);
                finish();
            }
        });

        // I took the existing fab that came by default with the fragment template I started with
        // and
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.child(email).addValueEventListener(new ValueEventListener() {
                    boolean clickedBefore = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean onList = snapshot.child("onList").getValue(Boolean.class);
                        if (snapshot.exists()){
                            if(!clickedBefore) {
                                if (onList != true) {
                                    Map<String, Object> updateMap = new HashMap<>();
                                    updateMap.put("date", date);
                                    updateMap.put("onList", true);
                                    dbRef.child(email.replace('.', ',')).setValue(updateMap);
                                    Snackbar.make(view, "You have been added to the wait list to see a Speech Therapist.", Snackbar.LENGTH_LONG)
                                            .setAnchorView(R.id.fab)
                                            .setAction("Action", null).show();
                                    clickedBefore = true;
                                } else {
                                    Snackbar.make(view, "You are already on the list to see a Speech Therapist.", Snackbar.LENGTH_LONG)
                                            .setAnchorView(R.id.fab)
                                            .setAction("Action", null).show();
                                }
                            }
                        }
                        else {
                            // If the snapshot does not exist, assume the user is not on the list and add them
                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("date", date);
                            updateMap.put("onList", true);
                            dbRef.child(email.replace('.', ',')).setValue(updateMap);

                            Snackbar.make(view, "You have been added to the wait list to see a Speech Therapist.", Snackbar.LENGTH_LONG)
                                    .setAnchorView(R.id.fab)
                                    .setAction("Action", null).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(view, "Could not check wait list.", Snackbar.LENGTH_LONG)
                                .setAnchorView(R.id.fab)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
    }
}
