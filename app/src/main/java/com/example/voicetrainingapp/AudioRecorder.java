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
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private static final int SAMPLE_RATE = 44100; // Sample rate in Hz
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    private File outputFile;

    @SuppressLint("MissingPermission")//I do a permission check already in the SecondFragment class
    public void startRecording() {
        prepareFile();
        if (audioRecord == null) {
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

    private void prepareFile() {
        File outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audioOutput");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            Log.e("AudioRecorder", "Failed to create directory");
            return;
        }
        outputFile = new File(outputDir, System.currentTimeMillis() + ".wav");
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
