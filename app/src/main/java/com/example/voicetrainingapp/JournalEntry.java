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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntry extends AppCompatActivity {
    //https://www.geeksforgeeks.org/how-to-build-a-simple-notes-app-in-android/
    //https://www.youtube.com/watch?v=td1jX_zDi5s
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;
    FirebaseUser user;
    int entryID = 0;
    String date, email, id, idp1, idp2;
    JournalDetails details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalentry);

        // UI components
        Button submitBtn = findViewById(R.id.submitBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        EditText ratingValue = findViewById(R.id.rating);
        EditText entryValue = findViewById(R.id.editText);

        // Initialize journal details object
        details = new JournalDetails();
        //Get current date in dd-mm-yyyy format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(new Date());

        // Making sure there is a user logged in so we can get the as a reference
        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                dbRef = db.getReference().child(user.getEmail().replace('.', ','));
            } else {
                // User is not logged in, redirect to Login screen
                Toast.makeText(this, "User not logged in. Redirecting to login screen.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, Login.class));
                finish();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize database reference: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Optionally handle other initialization steps or close the activity
        }

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    entryID = (int) snapshot.getChildrenCount();//check for the number of existing entries there are for the user so i can assign the correct id for the next entry
                    System.out.println("currently "+entryID+" exists for "+user.getEmail().toString());
                }
                else if(!snapshot.exists()){
                    entryID = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Could not connect to firebase :c");
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
                            //The child would look something like test@test.com0
                            id = idp1 + idp2;
                            details.setId(id);

                            // if the entry is over 250 characters it is invalid
                            if(details.getEntryText().length() > 250) {
                                Toast.makeText(JournalEntry.this, "Entrys cannot be greater than 250 character!", Toast.LENGTH_SHORT).show();
                            }
                            // if the entry has no text it is not valid
                            else if (details.getEntryText().length() == 0){
                                Toast.makeText(JournalEntry.this, "Entrys must have text!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dbRef.child(String.valueOf(entryID + 1) + "Journal").setValue(details);
                                Toast.makeText(JournalEntry.this, "Journal Entry Updated!", Toast.LENGTH_SHORT).show();
                                Intent journalPage = new Intent(getApplicationContext(), Journal.class);
                                startActivity(journalPage);
                                finish();
                            }
                        }
                        else { //If the rating is not a number between 1 and 5 it is not valid
                            Toast.makeText(JournalEntry.this, "Rating must be a number between 1 - 5!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else{//If the rating is not a number between 1 and 5 it is not valid
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

        //Cancel button for if a user changes there mind, goes back to the main journal screen
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