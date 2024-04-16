package com.example.voicetrainingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voicetrainingapp.databinding.FragmentSecondBinding;

import java.io.File;
import java.io.IOException;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private AudioRecorder audioRecorder;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123;
    private boolean isRecording = false;
    private FrequencyAnalyzer frequencyAnalyzer;
    private int currentRecordingCount = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        frequencyAnalyzer = new FrequencyAnalyzer(); // Initialize AudioFrequencyAnalyzer
        audioRecorder = new AudioRecorder();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button recordButton = view.findViewById(R.id.recordButton);
        TextView hzText = view.findViewById(R.id.hzText);
        TextView dBText = view.findViewById(R.id.dBText);
        TextView soundStage = view.findViewById(R.id.soundStage);

        recordButton.setOnClickListener(v -> {
            if (checkRecordAudioPermission()) {
                if (!isRecording) {
                    new CountDownTimer(16000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            recordButton.setText("Recording: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            recordButton.setText("Start Recording");
                            // Stop recording and process as necessary
                            if (isRecording) {
                                audioRecorder.stopRecording();
                                isRecording = false;
                                File audioFile = new File(audioRecorder.getAudioFilePath());
                                double frequency = frequencyAnalyzer.calculateFrequency(audioFile);
                                String formattedFrequency = String.format("%.2f", frequency);
                                hzText.setText("Hz: " + formattedFrequency);
                                Double dB = 20 * Math.log10(frequency);
                                String formattedDB = String.format("%.2f", dB);
                                dBText.setText("dB: " + formattedDB);
                            }
                        }
                    }.start();
                    isRecording = true;
                    audioRecorder.startRecording();
                } else {
                    audioRecorder.stopRecording();
                    isRecording = false;
                    recordButton.setText("Start Recording");
                    // Optionally stop the countdown here if you keep a reference to the CountDownTimer
                }
            } else {
                requestRecordAudioPermission();
            }
        });


        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        Button buttonThird = view.findViewById(R.id.button_third); // Ensure this button is in your fragment_second layout
        buttonThird.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SesionGraph.class);
            startActivity(intent);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean checkRecordAudioPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestRecordAudioPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_AUDIO_PERMISSION_REQUEST_CODE);
    }
}

