package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Journal extends AppCompatActivity {
    // https://www.geeksforgeeks.org/how-to-build-a-simple-notes-app-in-android/ - where I got the journal layout from and learned about arrayAdapters
    // https://www.youtube.com/watch?v=E9drbKeVG7Y - Part of where I learned to construct a query to firebase
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    FirebaseDatabase db = FirebaseDatabase.getInstance(); // connects instance to firebase
    DatabaseReference dbRef; // database reference
    FirebaseUser user; // variable containing user details e.g email
    String entryText, id, idp1, idp2, rating, date, expectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        ListView listView = findViewById(R.id.listView); // Intializing the listview where the entrys will be displayed
        Button backBtn = findViewById(R.id.backButton);
        Button addBtn = findViewById(R.id.addBtn);
        TextView noEntry = findViewById(R.id.noEntryText);
        noEntry.setVisibility(View.GONE);
        ArrayList<String>entryList = new ArrayList<>();
        ArrayList<String>dateList = new ArrayList<>();
        ArrayList<String>ratingList = new ArrayList<>();
        ArrayList<String>idList = new ArrayList<>();
        ArrayList<String>combinedList = new ArrayList<>();

        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                dbRef = db.getReference().child(user.getEmail().replace('.', ','));
            } else {
                // User is not logged in, redirect to Login screen
                Toast.makeText(this, "User not logged in. Redirecting to login screen.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, Login.class));
                finish();
                return; // Important to stop further execution in this case
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize database reference: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Optionally handle other initialization steps or close the activity
        }

        // Getting all the journal entry's from firebase that belong to the logged in user by checking if the entry belongs to their email
        Query query = dbRef.orderByChild("email").equalTo(user.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String entryId = child.getKey(); // Get the actual key for each journal entry
                        String entryText = child.child("entryText").getValue(String.class);
                        String date = child.child("date").getValue(String.class);
                        String rating = child.child("rating").getValue(String.class);

                        entryList.add(entryText);
                        dateList.add(date);
                        ratingList.add(rating);
                        idList.add(entryId);

                        String combinedEntry = entryText + " - Date: " + date + " - Rating: " + rating;
                        combinedList.add(combinedEntry);
                    }
                    if (!combinedList.isEmpty()) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Journal.this, android.R.layout.simple_list_item_1, combinedList);
                        listView.setAdapter(arrayAdapter);
                    }
                } else {
                    noEntry.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Database error: " + databaseError.getMessage());
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainMenu);
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entryPage = new Intent(getApplicationContext(), JournalEntry.class);
                startActivity(entryPage);
                finish();
            }
        });

        // Using custom listView Provided by Android Studio
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), JournalEdit.class);
                intent.putExtra("ID", idList.get(i));
                intent.putExtra("entryText", entryList.get(i));
                intent.putExtra("date", dateList.get(i));
                intent.putExtra("rating", ratingList.get(i));
                startActivity(intent);
            }
        });
    }
}