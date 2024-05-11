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

import com.example.voicetrainingapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class Login extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword; //variables to hold inputted user credentials.
    Button btnLogin; //login button variable
    FirebaseAuth mAuth; //Firebase authenicifation
    FirebaseUser user;
    ProgressBar progressBar;//Variable used to hide and show the loading screen
    TextView text;//Variable used to make the Login Now text a link
//    @Test
//    public void testLogin(Task<AuthResult> testTask){
//        String email, password;
//        email = "example@email.com";
//        password = "Password1";
//        mAuth.signInWithEmailAndPassword(email, password);
//
//        if (testTask.isSuccessful()){
//            user.getEmail().toString();
//        }
//    }
    @Override
    public void onStart() {
        super.onStart();
        //https://firebase.google.com/docs/auth/android/password-auth
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // https://www.youtube.com/watch?v=QAKq8UBv4GI
        editTextEmail = findViewById(R.id.email); //To grab inputted email from textfield
        editTextPassword = findViewById(R.id.password);//To grab inputted password from textfield
        btnLogin = findViewById(R.id.btnLogin); //login button
        progressBar = findViewById(R.id.progressBar); //variable used to hide and show the loading screen
        text = findViewById(R.id.loginNow); //

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(getApplicationContext(), Register.class);
                startActivity(registerPage);
                finish();
            }
        });

        // Login button logic
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); // show the loading circle animation
                String email, password; // variables to inputted
                email = String.valueOf(editTextEmail.getText()); //getting the email
                password = String.valueOf(editTextPassword.getText()); // getting password
                System.out.println("Email: "+email);
                System.out.println("Password: "+password);
                if (email.isEmpty()){ //Fields cant be empty
                    Toast.makeText(Login.this, "Email required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Password required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(),"Logged in!", Toast.LENGTH_SHORT).show();
                                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mainScreen);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Invalid details.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}