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
    public List<Double> calculateFrequency(File audioFile) {
        List<Double> frequencies = new ArrayList<>();
        final int sampleRate = 44100;  // Sample rate in Hz
        final int bytesPerSample = 2;  // 16-bit audio
        final int channelCount = 1;    // Mono audio
        final int bytesPerSecond = sampleRate * bytesPerSample * channelCount;

        try (FileInputStream fis = new FileInputStream(audioFile)) {
            byte[] buffer = new byte[bytesPerSecond];

            while (fis.read(buffer) != -1) {
                double[] audioAsDouble = new double[buffer.length / 2];
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < audioAsDouble.length; i++) {
                    audioAsDouble[i] = byteBuffer.getShort();
                }

                DoubleFFT_1D fft = new DoubleFFT_1D(audioAsDouble.length);
                fft.realForward(audioAsDouble);

                double maxMagnitude = 0;
                int maxIndex = 1;  // start at 1 to ignore DC component
                for (int i = 1; i < audioAsDouble.length / 2; i++) {
                    double re = audioAsDouble[2 * i];
                    double im = audioAsDouble[2 * i + 1];
                    double magnitude = Math.sqrt(re * re + im * im);
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

        return frequencies;
    }
}