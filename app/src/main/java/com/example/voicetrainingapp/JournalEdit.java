package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class JournalEdit extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;
    FirebaseUser user;
    String date, email, id, entryText, rating;

    private EditText editText;  // EditText for entry text
    private EditText ratingInput;  // EditText for rating

    // https://stackoverflow.com/questions/47366591/how-to-update-only-specific-field-on-firebase-database-on-android
    // https://stackoverflow.com/questions/44224083/how-to-update-child-with-new-fields-in-firebase-realtime-database
    // https://stackoverflow.com/questions/47366591/how-to-update-only-specific-field-on-firebase-database-on-android

    //From the above stackoverflow threads I was able to construct away to update existing journal entrys which update in the database and thus on the journal page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_edit);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID"); // This should exactly match the Firebase node key
        entryText = intent.getStringExtra("entryText");
        date = intent.getStringExtra("date");
        rating = intent.getStringExtra("rating");

        editText = findViewById(R.id.editText);
        editText.setText(entryText);
        ratingInput = findViewById(R.id.rating);
        ratingInput.setText(rating);

        initializeFirebase();

        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> finish());

        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(v -> {
            if (!ratingInput.getText().toString().trim().matches("[1-5]")) {
                Toast.makeText(JournalEdit.this, "Rating must be a number between 1 - 5!", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference entryRef = dbRef.child(id);  // Directly use id from intent
            updateJournalEntry(entryRef, editText.getText().toString().trim(), ratingInput.getText().toString().trim());
        });
    }

    private void initializeFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            String safeEmail = user.getEmail().replace('.', ',');
            dbRef = db.getReference(safeEmail);  // Only go up to the user level here
        } else {
            Toast.makeText(this, "User not logged in or email unavailable.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void updateJournalEntry(DatabaseReference journalRef, String newEntryText, String newRating) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("date", date);
        updateMap.put("details", "Journal ID: " + id + " - Date: " + date + " - Rating: " + newRating);
        updateMap.put("email", user.getEmail());
        updateMap.put("entryText", newEntryText);
        updateMap.put("rating", newRating);

        journalRef.updateChildren(updateMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(JournalEdit.this, "Journal entry updated successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Journal.class));
            finish();
        })
        .addOnFailureListener(e -> {
            Toast.makeText(JournalEdit.this, "Failed to update journal entry: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}
