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
    //
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    private File outputFile;

    @SuppressLint("MissingPermission")//I do a permission check already in the SecondFragment class so I have suppressed the need for a check here
    public void startRecording() { // https://stackoverflow.com/questions/34860767/android-audiorecord-failing-when-calling-the-startrecording-method
        prepareFile();
        if (audioRecord == null) {// If
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        }
        audioRecord.startRecording();
        isRecording = true;
        new Thread(new AudioRecordRunnable()).start();
    }

    public void stopRecording() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    private void prepareFile() { // method to set the output directory,
        // https://stackoverflow.com/a/36695883
        // https://stackoverflow.com/a/46657146
        // From the above I was able to get the downloads folder and learn how to create a folder within the downloads folder
        // and write a file to that folder
        File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audioOutput");
        if (!outputDir.exists()) { // Here I check if directory I have set exists "downloads/audioOutput" and if it doesnt create it
            outputDir.mkdirs();
            Log.e("AudioRecorder prepareFile", "Failed to create directory");
            return;
        }
        outputFile = new File(outputDir, System.currentTimeMillis() + ".wav"); // I set the file name to the current
    }

    public String getAudioFilePath() {
        if (outputFile != null) {
            return outputFile.getAbsolutePath();
        }
        return null;
    }

    private class AudioRecordRunnable implements Runnable {
        @Override
        public void run() {
            byte[] audioData = new byte[BUFFER_SIZE];
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                while (isRecording) {
                    int read = audioRecord.read(audioData, 0, audioData.length);
                    if (read > 0) {
                        fos.write(audioData, 0, read);
                    }
                }
                fos.flush();
            } catch (IOException e) {
                Log.e("AudioRecorder", "Error writing to file", e);
            }
        }
    }
}
