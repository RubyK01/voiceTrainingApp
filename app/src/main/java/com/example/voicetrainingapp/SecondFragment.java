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
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private AudioRecorder audioRecorder;// Instance of AudioRecoder
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123;
    private boolean isRecording = false;//Used to check if the microphone is recording
    FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();//Instance of frequencyAnalyzer
    ArrayList<Double> frequencyResults = new ArrayList<Double>(); //ArrayList to hold the over all average as its the average of the three averages from the below arraylists.
    ArrayList<Double> firstSoundResults = new ArrayList<Double>(); //holds the frequencies for the first sound.
    ArrayList<Double> secondSoundResults = new ArrayList<Double>(); //holds the frequencies for the second sound.
    ArrayList<Double> thirdSoundResults = new ArrayList<Double>();//holds the frequencies for the third sound.
    ArrayList<Double> decibalResults = new ArrayList<Double>();//Holds the average dB results
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        frequencyAnalyzer = new FrequencyAnalyzer(); // Initialize FrequencyAnalyzer class
        audioRecorder = new AudioRecorder();// Initialize AudioRecorder class
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button recordButton = view.findViewById(R.id.recordButton);
        TextView hzText = view.findViewById(R.id.hzText);
        TextView dBText = view.findViewById(R.id.dBText);
        TextView hzAverageText = view.findViewById(R.id.hzText2);
        TextView dBAverageText = view.findViewById(R.id.dBText2);
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
                                List<Double> frequencies = frequencyAnalyzer.calculateFrequency(audioFile);
                                int currentRecordingCount = 0;
                                currentRecordingCount++;
                                //Getting the results of all the frequencies from each sound and saving it to an arraylist to send to the graph
                                if (soundStage.getText().equals("Sound: ")){
                                    for (int i =0; i < frequencies.size(); i++){
                                        firstSoundResults.add(frequencies.get(i));
                                    }
                                    soundStage.setText("Sound 1: Completed");
                                } else if (currentRecordingCount == 2) {
                                    for (int i =0; i < frequencies.size(); i++){
                                        secondSoundResults.add(frequencies.get(i));
                                    }
                                    soundStage.setText("Sound 2: Completed");
                                } else if (currentRecordingCount == 3) {
                                    for (int i =0; i < frequencies.size(); i++){
                                        thirdSoundResults.add(frequencies.get(i));
                                    }
                                    soundStage.setText("Sound 3: Completed");
                                }
                                else{
                                    // note for future ruby go figure how to reload the page.
                                }

                                double sum = 0;
                                if (frequencies.isEmpty()) {
                                    System.out.println("No frequencies found. Check if the audio file is correct and not silent.");
                                } else {
                                    sum = 0;
                                    for (double num : frequencies) {
                                        sum += num;
                                    }
                                    double averageFrequency = sum / frequencies.size();
                                    System.out.println("Average Frequency: " + averageFrequency);
                                }
                                double averageFrequency = sum / frequencies.size();
                                //Rounding the result to two decimal places
                                Double roundedFrequency = Math.round(averageFrequency * 100.0) / 100.0;
                                hzText.setText("Hz: " + roundedFrequency);
                                Double dB = 20 * Math.log10(averageFrequency);
                                Double roundedDB = Math.round(dB * 100.0) / 100.0;
                                dBText.setText("dB: " + roundedDB);

                                frequencyResults.add(roundedFrequency);
                                decibalResults.add(roundedDB);
                                if (currentRecordingCount ==3){
                                    double sumOfFrequencyResults = frequencyResults.get(0)+frequencyResults.get(1)+frequencyResults.get(2);
                                    double averageFrequencyResult = sumOfFrequencyResults / frequencyResults.size();
                                    hzAverageText.setText("Average hZ: "+averageFrequencyResult);

                                    double sumOfDBResults = decibalResults.get(0)+decibalResults.get(1)+decibalResults.get(2);
                                    double averageDBResults = sumOfDBResults / decibalResults.size();
                                    dBAverageText.setText("Average hZ: "+averageDBResults);

                                    soundStage.setText("Sound 3: Completed");
                                }
                            }
                        }
                    }.start();
                    isRecording = true;
                    audioRecorder.startRecording();
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

