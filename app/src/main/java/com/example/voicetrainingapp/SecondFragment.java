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
import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private AudioRecorder audioRecorder;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123;
    private boolean isRecording = false;
    private FrequencyAnalyzer frequencyAnalyzer;
    ArrayList<Double> frequencyResults = new ArrayList<Double>();
    ArrayList<Double> decibalResults = new ArrayList<Double>();
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
                                //Grabbing the file path of the .wav that gets created.
                                File audioFile = new File(audioRecorder.getAudioFilePath());
                                //https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
                                //Sending the filepath into the frequency calculation method
                                double frequency = frequencyAnalyzer.calculateFrequency(audioFile);
                                //Rounding the result to two decimal places
                                Double roundedFrequency = Math.round(frequency * 100.0) / 100.0;
                                hzText.setText("Hz: " + roundedFrequency);
                                Double dB = 20 * Math.log10(frequency);
                                Double roundedDB = Math.round(dB * 100.0) / 100.0;
                                dBText.setText("dB: " + roundedDB);

                                frequencyResults.add(roundedFrequency);
                                decibalResults.add(roundedDB);
                            }
                        }
                    }.start();
                    isRecording = true;
                    audioRecorder.startRecording();
                    int currentRecordingCount = 0;
                    currentRecordingCount++;
                } else {
                    audioRecorder.stopRecording();
                    isRecording = false;
                    recordButton.setText("Start Recording");
                }
            } else {
                requestRecordAudioPermission();
            }
        });


        binding.buttonSecond.setOnClickListener(new View.OnClickListener() { // Button to go back to the home screen.
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        Button buttonThird = view.findViewById(R.id.button_third); // Making the results button direct user to the graph screen
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

