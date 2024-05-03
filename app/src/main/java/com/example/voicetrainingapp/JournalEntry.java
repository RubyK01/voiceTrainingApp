package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntry extends AppCompatActivity {
    //https://www.geeksforgeeks.org/how-to-build-a-simple-notes-app-in-android/
    //https://www.youtube.com/watch?v=td1jX_zDi5s
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef, ref2;
    FirebaseUser user;
    int entryID = 0;
    String date, email, rating, id, idp1, idp2;
    JournalDetails details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalentry);

        // Initialize UI components
        Button submitBtn = findViewById(R.id.submitBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        EditText ratingValue = findViewById(R.id.rating);
        EditText entryValue = findViewById(R.id.editText);

        // Initialize journal details
        details = new JournalDetails();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(new Date());

        // Ensure the Firebase user is not null and initialize database reference
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

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    entryID = (int) snapshot.getChildrenCount();
                    System.out.println("currently "+entryID+" exists for "+user.getEmail().toString());
                }
                else if(!snapshot.exists()){
                    entryID = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){ //Checking if the user is signed in, user should only have been able to get here if they are logged in so double checking to be safe
                    // https://stackoverflow.com/questions/2841550/what-does-d-mean-in-a-regular-expression
                    // https://www.javatpoint.com/java-regex
                    // Here I used the matches method from regex to see if the ratingValue contains a number since in the app it says it has to be a number between 1 and 5
                    if (ratingValue.getText().toString().matches("\\d+")) {
                        if(ratingValue.getText().toString().contains("1") || ratingValue.getText().toString().contains("2") || ratingValue.getText().toString().contains("3") || ratingValue.getText().toString().contains("4") || ratingValue.getText().toString().contains("5")) {
                            email = user.getEmail().toString();
                            details.setEmail(email.toString());
                            details.setDate(date.toString());
                            details.setRating(ratingValue.getText().toString());
                            details.setEntryText(entryValue.getText().toString());
                            idp1 = user.getEmail().toString();
                            idp2 = String.valueOf(entryID);
                            id = idp1 + idp2;
                            details.setId(id);

                            dbRef.child(String.valueOf(entryID + 1)).setValue(details);

                            Intent journalPage = new Intent(getApplicationContext(), Journal.class);
                            startActivity(journalPage);
                            finish();
                        }
                        else {
                            Toast.makeText(JournalEntry.this, "Rating must be a number between 1 - 5!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else{
                        Toast.makeText(JournalEntry.this, "Rating must be a number between 1 - 5!", Toast.LENGTH_SHORT).show();
                        return;
                    }
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
    }
}