package com.example.voicetrainingapp;

import org.jtransforms.fft.DoubleFFT_1D;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FrequencyAnalyzer {
    public double calculateFrequency(File audioFile) {
        double frequency = 0.0;
        try (FileInputStream fis = new FileInputStream(audioFile)) {
            byte[] audioData = new byte[(int) audioFile.length()];
            fis.read(audioData);

            double[] audioAsDouble = new double[audioData.length / 2];
            ByteBuffer buffer = ByteBuffer.wrap(audioData);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < audioAsDouble.length; i++) {
                audioAsDouble[i] = buffer.getShort();
                // Optionally print all samples or the first few to check data integrity
                if (i < 10) System.out.println("Sample " + i + ": " + audioAsDouble[i]);
            }

            DoubleFFT_1D fft = new DoubleFFT_1D(audioAsDouble.length);
            fft.realForward(audioAsDouble);

            double maxMagnitude = 0;
            int maxIndex = 0;  // start at 1 to ignore DC component if not looking for DC offset
            for (int i = 1; i < audioAsDouble.length / 2; i++) {
                double re = audioAsDouble[2 * i];
                double im = audioAsDouble[2 * i + 1];
                double magnitude = Math.sqrt(re * re + im * im);
                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;
                    maxIndex = i;
                }
                // Log the magnitude values to check for errors
                if (i < 20) System.out.println("Frequency Index " + i + " Magnitude: " + magnitude);
            }

            if (maxIndex > 0) {
                int sampleRate = 44100;
                frequency = maxIndex * (sampleRate / (double) audioAsDouble.length);
            }
            System.out.println("Max magnitude: " + maxMagnitude + " at frequency: " + frequency + " (Index: " + maxIndex + ")");
        } catch (Exception e) {
            System.err.println("Error processing audio file: " + e.getMessage());
            e.printStackTrace();
        }
        return frequency;
    }
}
