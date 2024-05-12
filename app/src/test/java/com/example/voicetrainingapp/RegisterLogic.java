package com.example.voicetrainingapp;

import java.util.regex.Pattern;

public class RegisterLogic {

    public String validDetails(String email, String password) {
        Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

        if (email.isEmpty()) {
            return "Email required.";
        }
        else if (!email.contains("@") || !email.contains(".")) {
            return "Invalid email format.";
        }
        else if (password.isEmpty()) {
            return "Password required.";
        }
        else if (!textPattern.matcher(password).matches() || password.length() < 8) {
            return "Password not valid.";
        }
        else {
            return "Valid";

        }
    }
}
