package com.example.voicetrainingapp;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder {
    // In this class I make use of both media recorder and audio recorder
    // I have good reason for this, I can use media recorder to get access to the devices microphone and audio recorder to capture raw sound
    // Having the raw sound means I can do a clean analysis of the file without having to worry about compression messing up the results
    // https://www.geeksforgeeks.org/audio-recorder-in-android-with-example/
    //
    private AudioRecord audioRecord; // creating AudioRecord object from the AudioRecord class in the media package
    private boolean isRecording = false; // Boolean used to check if the the recording has stared or not

    //Here I have the sample rate set to 44.1 kHz which is usually the sample rate for music
    //due to the high frequencies of music so I thought this sample rate would be best suited here
    //Source: https://www.voicescloud.com/news/uncategorized/what-is-the-minimum-sampling-rate-for-voice/
    private static final int SAMPLE_RATE = 44100;
    // For short recordings it is typically done in mono
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    // 16 bit wav file
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    private File outputFile;
    private File testFile;

    @SuppressLint("MissingPermission")//I do a permission check already in the SecondFragment class so I have suppressed the need for a check here
    // start recording
    public void startRecording() {
        prepareFile(); // get the filepath ready to write to
        if (audioRecord == null) {// If there no instance of audio record create one with the above settings
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        }
        audioRecord.startRecording(); // start recording
        isRecording = true; // set boolean to true so we can end the recording
        new Thread(new AudioRecordRunnable()).start(); // new thread so theres no issue when taking in data and writing it.
    }

    //end recording
    public void stopRecording() {
        if (audioRecord != null) { // if theres a currently a instance kill it
            isRecording = false; //
            audioRecord.stop(); // to stop recording
            audioRecord.release(); // release the resources taken up by recording e.g memory
            audioRecord = null; // kill the instance
        }
    }

    private void prepareFile() { // method to set the output directory,
        // https://stackoverflow.com/a/36695883
        // https://stackoverflow.com/a/46657146
        // From the above I was able to get the downloads folder and learn how to create a folder within the downloads folder
        // and write a file to that folder
        File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audioOutput");
        File testFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audioOutput");
        if (!outputDir.exists()) { // Here I check if directory I have set exists "downloads/audioOutput" and if it doesnt create it
            outputDir.mkdirs();
            Log.e("AudioRecorder prepareFile", "Failed to create directory");
            return;
        }
        outputFile = new File(outputDir, System.currentTimeMillis() + ".wav"); // I set the file name to the current
        testFile = new File(testFilePath, "50hztest.wav");
    }

    public String getAudioFilePath() {// Return the file path of the audio so that I can pass the file through the frequencyAnalyzer class
        if (outputFile != null) {  // if they file hasnt been created yet return null
            return outputFile.getAbsolutePath();
        }
        return null;
    }

    public String getTestAudioFilePath() {// Return the file path of the audio so that I can pass the file through the frequencyAnalyzer class
        if (testFile != null) {
            return testFile.getAbsolutePath();
        }
        return null;
    }

    //Separate thread for writing the audio file
    private class AudioRecordRunnable implements Runnable {

        // https://www.tabnine.com/code/java/methods/java.io.ByteArrayOutputStream/flush
        @Override
        public void run() {
            byte[] audioData = new byte[BUFFER_SIZE];// byte buffer to hold audio data from the mic
            try (FileOutputStream fos = new FileOutputStream(outputFile)) { //Writing raw audio to the outputFile
                while (isRecording) { // While recording read data from the audioRecord instance and write to the buffer
                    int read = audioRecord.read(audioData, 0, audioData.length);
                    if (read > 0) { // To make sure i do capture audio i check if the bytes coming in are greater than 0
                        fos.write(audioData, 0, read); // only the bytes greater than 0 get read so theres no unneeded data being saved
                    }
                }
                fos.flush(); // flush to make sure data in the buffer is written
            } catch (IOException e) {// catch any errors and print it
                System.out.println("AudioRecorder error: "+e);
            }
        }
    }
}
