package com.example.voicetrainingapp;

import static android.content.Intent.getIntent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voicetrainingapp.databinding.FragmentSecondBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    FirebaseDatabase db = FirebaseDatabase.getInstance(); // connects instance to firebase
    DatabaseReference dbRef; // database reference
    FirebaseUser user; // variable containing user details e.g email
    private AudioRecorder audioRecorder;// Instance of AudioRecoder
    MediaPlayer player;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123;
    private boolean isRecording = false;//Used to check if the microphone is recording
    FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();//Instance of frequencyAnalyzer
    FrequencyAnalyzer testFrequencyAnalyzer = new FrequencyAnalyzer();//Instance of frequencyAnalyzer
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
        testFrequencyAnalyzer = new FrequencyAnalyzer();
        dbRef = FirebaseDatabase.getInstance().getReference();
        return binding.getRoot();
    }

    public void saveResults(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String email = user.getEmail();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = dateFormat.format(new Date());

            FrequencyResults savedResults = new FrequencyResults();
            savedResults.setFirstSoundResults(firstSoundResults);
            savedResults.setSecondSoundResults(secondSoundResults);
            savedResults.setThirdSoundResults(thirdSoundResults);
            savedResults.setDate(date);
            savedResults.setEmail(email);

            dbRef.push().setValue(savedResults);
            Toast.makeText(getContext(), "Results saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "Not Logged in.", Toast.LENGTH_SHORT).show();
            Intent loginPage = new Intent(getContext(), Login.class);
            startActivity(loginPage);
            getActivity().finish();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button recordButton = view.findViewById(R.id.recordButton);
        Button explainButton = view.findViewById(R.id.explanationButton);

        try {
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                dbRef = db.getReference().child(user.getEmail().replace('.', ',')+"frequency");
            } else {
                // User is not logged in, redirect to Login screen
                Toast.makeText(getContext(), "Not Logged in.", Toast.LENGTH_SHORT).show();
                Intent loginPage = new Intent(getContext(), Login.class);
                startActivity(loginPage);
                getActivity().finish();
                return; // Important to stop further execution in this case
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to initialize database reference: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Optionally handle other initialization steps or close the activity
        }

        // https://www.youtube.com/watch?v=0SJAK_pKUoE
        // https://stackoverflow.com/a/34052313
        // To solve the issue of context since i cant say SecondFragment.this since this is not a fragment
        explainButton.setOnClickListener(new View.OnClickListener() { // plays explanation of how to use the voice training function
            @Override
            public void onClick(View v) {
                if(player == null){
                    player = MediaPlayer.create(getContext(),R.raw.explain);
                    player.start();
                }
                else{
                    player.release();
                    player = null;
                }
            }
        });


        // variables for the on screen text to be updated
        TextView hzText = view.findViewById(R.id.hzText);
        TextView dBText = view.findViewById(R.id.dBText);
        TextView soundStage = view.findViewById(R.id.soundStage);

        // https://stackoverflow.com/questions/37027857/creating-a-button-to-start-audio-record-then-when-you-want-to-stop-the-audio-re
        // record button logic
        recordButton.setOnClickListener(v -> {
            if (checkRecordAudioPermission()) {
                if (!isRecording) {
                    new CountDownTimer(16000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            recordButton.setText("Recording: " + millisUntilFinished / 1000);
                            recordButton.setEnabled(false);
                        }

                        public void onFinish() {
                            recordButton.setText("Start Recording");
                            recordButton.setEnabled(true);
                            // Stop recording and process as necessary
                            if (isRecording) {
                                audioRecorder.stopRecording();
                                isRecording = false;
                                //Grabbing the file path of the .wav that gets created.
                                File audioFile = new File(audioRecorder.getAudioFilePath());
                                //grabbing the test file
                                File testFile = new File(audioRecorder.getTestAudioFilePath());
                                //https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
                                //Sending the filepath into the frequency calculation method
                                List<Double> frequencies = frequencyAnalyzer.calculateFrequency(audioFile);
                                System.out.println(audioFile);

                                List<Double> testFrequencies = testFrequencyAnalyzer.calculateFrequency(testFile);
                                System.out.println(testFile);
                                System.out.println("Test frequencies: "+testFrequencies);

                                // testing note from Ruby
                                // For the purpose of testing the frequency analzyer class
                                // replace where frequencies with testFrequencies is below so that the calulation is with the test file
                                // files are called 150hztest.wav & 50hztest.wav
                                // Links to where i got the files
                                // https://www.youtube.com/watch?v=bslHKEh7oZk -50hz
                                // https://www.youtube.com/watch?v=cZaJzjMexfM -150hz
                                // on your android device please put them in the audioOutput folder that the audioRecorder class had created
                                // I suggest emailing the files to yourself and then moving the files into the folder
                                // in AudioRecorder line 68 change the file name to the one you want to test
                                // also in this class at line

                                //Getting the results of all the frequencies from each sound and saving it to an arraylist to send to the graph

                                if (soundStage.getText().equals("Sound: ")){
                                    for (int i =0; i < frequencies.size(); i++){
                                        firstSoundResults.add(frequencies.get(i));
                                    }
                                    soundStage.setText("Sound 1: Completed");
                                } else if (soundStage.getText().equals("Sound 1: Completed")) {
                                    for (int i =0; i < frequencies.size(); i++){
                                        secondSoundResults.add(frequencies.get(i));
                                    }
                                    soundStage.setText("Sound 2: Completed");
                                } else if (soundStage.getText().equals("Sound 2: Completed")) {
                                    for (int i =0; i < frequencies.size(); i++){
                                        thirdSoundResults.add(frequencies.get(i));
                                    }
                                    soundStage.setText("Sound 3: Completed");
                                }
                                System.out.println("First results: "+firstSoundResults);
                                System.out.println("Second results: "+secondSoundResults);
                                System.out.println("Third results: "+thirdSoundResults);

                                double sum = 0;
                                if (frequencies.isEmpty()) {
                                    System.out.println("Error no frequencies");
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
                                hzText.setText("Average hZ: \n" + roundedFrequency);
                                Double dB = 20 * Math.log10(averageFrequency);
                                Double roundedDB = Math.round(dB * 100.0) / 100.0;
                                dBText.setText("Average dB: \n" + roundedDB);

                                frequencyResults.add(roundedFrequency);
                                decibalResults.add(roundedDB);
                            }
                        }
                    }
                    .start();
                    isRecording = true;
                    audioRecorder.startRecording();
                }
                else {
                    audioRecorder.stopRecording();
                    isRecording = false;
                    recordButton.setText("Start Recording");
                }
            }
            else {
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
            if (firstSoundResults.isEmpty() || secondSoundResults.isEmpty() || thirdSoundResults.isEmpty()){
                Toast.makeText(getActivity(), "Please make three attempts before viewing results.", Toast.LENGTH_SHORT).show();
            }
            else{
                saveResults();
                Intent intent = new Intent(getActivity(), SesionGraph.class);
                //https://www.geeksforgeeks.org/how-to-use-putextra-and-getextra-for-string-data-in-android/
                //From the above I was able to figure out how to send the arrayLists containing the Hz data to the graphsession class
                intent.putExtra("firstSoundResults",firstSoundResults);
                intent.putExtra("secondSoundResults",secondSoundResults);
                intent.putExtra("thirdSoundResults",thirdSoundResults);
                startActivity(intent);
            }
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