package com.example.voicetrainingapp;

import org.jtransforms.fft.DoubleFFT_1D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class FrequencyAnalyzer {
    // https://stackoverflow.com/questions/75409414/getting-frequencies-and-magnitudes-from-audio-sample-using-fft
    public List<Double> calculateFrequency(File audioFile) { // Calculation takes in the new audio file that was saved
        List<Double> frequencies = new ArrayList<>(); // array list to hold the frequencies results
        final int sampleRate = 44100;  // Sample rate in Hz
        final int bytesPerSample = 2;  // 16-bit audio
        final int channelCount = 1;    // Mono audio
        final int bytesPerSecond = sampleRate * bytesPerSample * channelCount;

        // Fast Fourier Transforms

        try (FileInputStream fis = new FileInputStream(audioFile)) { // try getting the file to calculate the frequencies off
            byte[] buffer = new byte[bytesPerSecond]; // Byte buffer to read the file
            // https://stackoverflow.com/a/29570220

            // I order the bytes from WAV file by little endian as WAV files themselves use this method of storing btyes.
            // Little Endian stores bytes by going smallest to largest

            // https://vi-control.net/community/threads/need-technical-advice-with-riff-wave.137584/
            // "All WAV files are RIFF files, and all RIFF files are little-endian by default.
            // Little-endian means the least significant byte is the first byte. (Big-endian means the most significant byte is first
            // - but you probably guessed that)
            // A WAV file consists of two data "chunks", the header and the data.
            while (fis.read(buffer) != -1) { // read the buffer till theres nothing else
                double[] audioAsDouble = new double[buffer.length / 2]; 
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < audioAsDouble.length; i++) {
                    audioAsDouble[i] = byteBuffer.getShort();
                }

                // https://stackoverflow.com/a/11868383
                // Creating the Fast Fourier Transforms Object from jTransforms that takes in arrayList holding bytes
                DoubleFFT_1D fft = new DoubleFFT_1D(audioAsDouble.length);

                // https://stackoverflow.com/a/12209744
                // Since I am using real audio I am using realFoward function
                fft.realForward(audioAsDouble);

                // https://stackoverflow.com/a/7675171
                // From the above I used as a basis on how to get the result of multiple frequencies
                // The post covers one frequency when I want one for every second of the recording
                double maxMagnitude = 0;
                int maxIndex = 1;  // start at 1 to ignore DC component
                for (int i = 1; i < audioAsDouble.length / 2; i++) {
                    double re = audioAsDouble[2 * i]; // work real number
                    double im = audioAsDouble[2 * i + 1]; // work out imaginary number
                    double magnitude = Math.sqrt(re * re + im * im); // workout the magnitude
                    if (magnitude > maxMagnitude) {
                        maxMagnitude = magnitude;
                        maxIndex = i;
                    }
                }

                double frequency = maxIndex * (sampleRate / (double) audioAsDouble.length);
                frequencies.add(frequency);
            }
        } catch (IOException e) {
            System.err.println("Error processing audio file: " + e.getMessage());
            e.printStackTrace();
        }
        return frequencies; // return the arraylist
    }
}