package com.example.voicetrainingapp;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

//https://www.tabnine.com/code/java/classes/android.media.MediaRecorder
//https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder
public class AudioRecorder {

    private MediaRecorder mediaRecorder;  // variable for MediaRecorder to handle audio recording
    private String filePath;  // variable to store the file path where the recorded audio will be saved
    private Context context;  // variable to hold the context of the application


    // Method to start the audio recording process
    public void startRecording() {
        mediaRecorder = new MediaRecorder();  // Create a new instance of MediaRecorder

        // Configure the MediaRecorder for audio recording
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// telling mediaRecorder to use the deveices mircrophone
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// setting the outputted audio file to the .mp3 format
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // setting the audio compression which I have set to Advanced Audio Coding

        // Set the output file path
        filePath = createOutputFilePath();
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();  // Prepare the MediaRecorder
            mediaRecorder.start();  // Start the audio recording
            Log.d("AudioRecorder", "Start recording...");  // Log a debug message
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AudioRecorder", "Error preparing or starting MediaRecorder: " + e.getMessage());  // Log an error message if an exception occurs
        }
    }

    // Method to stop the audio recording process
    // https://stackoverflow.com/questions/21659217/android-media-recorder-keeps-crashing-why
    //modified code to stop the recorder as I had issue it would crash the app when I tap stop recording, in my case I made it into a try catch
    public void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();  // Stop the audio recording
                Log.d("AudioRecorder", "Recording stopped successfully.");  // Log a debug message
            } catch (IllegalStateException e) {
                // Handle the exception appropriately (e.g., log it)
                Log.e("AudioRecorder", "Error stopping MediaRecorder: " + e.getMessage());  // Log an error message if an exception occurs
            } finally {
                mediaRecorder.release();  // Release resources
                mediaRecorder = null;  // Set the MediaRecorder instance to null
            }
        }
    }

    // Method to get the file path where the recorded audio is stored
    public String getFilePath() {
        return filePath;
    }

    // Method to create the output file path for the recorded audio
    private String createOutputFilePath() {
        // Use the Downloads directory to save the file
        File outputDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audioOutput");

        // Check if the directory does not exist and attempt to create it
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            handleDirectoryCreationError();  // Call the method to handle directory creation errors
        }

        // Return the complete file path for the recorded audio
        return outputDirectory.getAbsolutePath() + File.separator + "recorded_audio.mp3";
    }

    // Method to handle errors during the creation of the output directory
    private void handleDirectoryCreationError() {
        // Log an error message indicating the failure to create the directory
        Log.e("AudioRecorder", "Failed to create directory");
        // You can add more error handling here if needed
    }
}
