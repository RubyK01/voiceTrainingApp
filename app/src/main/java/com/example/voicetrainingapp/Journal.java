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

import java.util.ArrayList;
import java.util.HashSet;

public class Journal extends AppCompatActivity {
    //https://www.geeksforgeeks.org/how-to-build-a-simple-notes-app-in-android/
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_journal_entry_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {

            // Going from Jounral page to the entry page
            Intent intent = new Intent(getApplicationContext(), JournalEntry.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
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