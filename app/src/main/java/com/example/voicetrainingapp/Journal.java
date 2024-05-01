package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;

public class Journal extends AppCompatActivity {
    // https://www.geeksforgeeks.org/how-to-build-a-simple-notes-app-in-android/
    // https://www.youtube.com/watch?v=E9drbKeVG7Y
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;

        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        ListView listView = findViewById(R.id.listView);
        Button backBtn = findViewById(R.id.backButton);
        Button addBtn = findViewById(R.id.addBtn);
        TextView noEntry = findViewById(R.id.noEntryText);
        noEntry.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

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


        if (set == null) {
            listView.setVisibility(View.GONE);
            noEntry.setVisibility(View.VISIBLE);
        } else {
            notes = new ArrayList(set);
        }

        // Using custom listView Provided by Android Studio
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Going from MainActivity to NotesEditorActivity
                Intent intent = new Intent(getApplicationContext(), JournalEntry.class);
                intent.putExtra("noteId", i);
                startActivity(intent);

            }
        });
    }
}