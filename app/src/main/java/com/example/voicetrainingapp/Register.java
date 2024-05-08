package com.example.voicetrainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    // https://www.youtube.com/watch?v=QAKq8UBv4GI
    // This register and login classes are based on the above video
    // I have altered the register class to validate the inputs as well as
    // create a field in firebase to see if the user is currently waiting to talk to a speech therapist
    TextInputEditText editTextEmail, editTextPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    DatabaseReference dbRef, dbRefDetails;
    ProgressBar progressBar;
    TextView text;

    public void onStart() {
        super.onStart();
        ////https://firebase.google.com/docs/auth/android/password-auth
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent mainScreen = new Intent(getApplicationContext(), FirstFragment.class);
            startActivity(mainScreen);
            finish();
        }
    }
    private Boolean validDetails (String password, String email){
        // I used a pattern to check if it the password contains a captial and lower case letter as well as a number
        // https://stackoverflow.com/a/49515564
        Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
        Boolean validDetails = null;
        if (email.isEmpty()) {
            Toast.makeText(Register.this, "Email required.", Toast.LENGTH_SHORT).show();
            validDetails = false;
        }
        else if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(Register.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            validDetails = false;
        }
        else if (password.isEmpty()) {
            Toast.makeText(Register.this, "Password required.", Toast.LENGTH_SHORT).show();
            validDetails = false;
        }
        else if (password.isEmpty() && email.isEmpty()) {
            Toast.makeText(Register.this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            validDetails = false;
        }
        else if (textPattern.matcher(password).matches() && password.length() >= 8) {
            validDetails = true;
        }
        else {
            validDetails = false;
            Toast.makeText(Register.this, "Password not valid.", Toast.LENGTH_SHORT).show();
        }
        return validDetails;
    }

    private void addToWaitList(String email){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());

        WaitList waitList = new WaitList(date, false);
        dbRef.child(email.replace(".",",")).setValue(waitList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("WaitList");
        //https://www.youtube.com/watch?v=QAKq8UBv4GI
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        text = findViewById(R.id.loginNow);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(getApplicationContext(), Login.class);
                startActivity(loginPage);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(validDetails(password, email)) {
                    progressBar.setVisibility(View.VISIBLE);
                    //https://firebase.google.com/docs/auth/android/password-auth
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Account Created!",
                                        Toast.LENGTH_SHORT).show();
                                addToWaitList(email);
                                Intent loginPage = new Intent(getApplicationContext(), Login.class);
                                startActivity(loginPage);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}