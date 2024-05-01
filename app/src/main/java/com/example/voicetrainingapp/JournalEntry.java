package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntry extends AppCompatActivity {
    //https://www.geeksforgeeks.org/how-to-build-a-simple-notes-app-in-android/
    //https://www.youtube.com/watch?v=td1jX_zDi5s
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;
    FirebaseUser user;
    int noteId = 0;
    String date, email, entryText, rating;
    JournalDetails details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalentry);
        Button submitBtn = findViewById(R.id.submitBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        EditText ratingValue = findViewById(R.id.rating);
        EditText entryValue = findViewById(R.id.editText);
        details = new JournalDetails();
        dbRef = db.getReference().child("journalEntry");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(new Date());
        details.setDate(date);
        rating = ratingValue.getText().toString();
        user = FirebaseAuth.getInstance().getCurrentUser();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    noteId = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    email = user.getEmail().toString();
                    details.setEmail(email.toString());
                    details.setDate(date.toString());
                    details.setRating(rating);
                    details.setEntryText(entryValue.getText().toString());

                    dbRef.child(String.valueOf(noteId+1)).setValue(details);

                    Intent journalPage = new Intent(getApplicationContext(), Journal.class);
                    startActivity(journalPage);
                    finish();
                }
                else{//If there is no user instance detected redirect to login page to avoid crashing
                    Intent loginPage = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginPage);
                    finish();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent journalPage = new Intent(getApplicationContext(), Journal.class);
                startActivity(journalPage);
                finish();
            }
        });
        // Fetch data that is passed from Journal
        Intent intent = getIntent();

        // Accessing the data using key and value
        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            entryValue.setText(Journal.notes.get(noteId));
        } else {

            Journal.notes.add("");
            noteId = Journal.notes.size() - 1;
            Journal.arrayAdapter.notifyDataSetChanged();

        }
    }
}