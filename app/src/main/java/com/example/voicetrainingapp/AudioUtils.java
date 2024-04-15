package com.example.voicetrainingapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

public class AudioUtils {
    public static short[] readAudioFile(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int fileSize = fileInputStream.available();
            byte[] audioData = new byte[fileSize];

            // Read the entire audio file into the byte array
            fileInputStream.read(audioData);
            fileInputStream.close();
            Log.d("AudioUtils", "Audio data size: " + audioData.length);
            System.out.println("soemthing");
            // Convert byte array to short array
            short[] shortAudioData = new short[fileSize / 2];
            for (int i = 0; i < fileSize; i += 2) {
                shortAudioData[i / 2] = (short) ((audioData[i] & 0xFF) | (audioData[i + 1] << 8));
            }

            return shortAudioData;

        } catch (IOException e) {
            e.printStackTrace();
            return new short[0];  // Return an empty array in case of an error
        }
    }
}

