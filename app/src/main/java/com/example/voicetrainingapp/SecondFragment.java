package com.example.voicetrainingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voicetrainingapp.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private AudioRecorder audioRecorder;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123;
    private boolean isRecording = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioRecorder = new AudioRecorder();

        Button recordButton = view.findViewById(R.id.recordButton);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://stackoverflow.com/questions/37027857/creating-a-button-to-start-audio-record-then-when-you-want-to-stop-the-audio-re
                //Modifed stop start button that will check if permission is granted
                if (checkRecordAudioPermission()) {//calls method to check if audio persmission is granted
                    if (!isRecording) {// if start recording is tapped
                        audioRecorder.startRecording();// start recording
                        isRecording = true; // set is recording to true
                        recordButton.setText("Stop Recording"); // change button text
                    } else {
                        // Stop recording
                        audioRecorder.stopRecording();
                        isRecording = false;
                        recordButton.setText("Start Recording");
                    }
                } else {
                    // Permission not granted, request it
                    requestRecordAudioPermission();
                }
            }
        });

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //https://www.tabnine.com/code/java/methods/androidx.core.content.ContextCompat/checkSelfPermission
    //https://stackoverflow.com/questions/42832847/what-is-the-difference-between-contextcompat-checkselfpermission-and-activityc
    //methods for checking permissions
    // Check if the RECORD_AUDIO permission is granted
    private boolean checkRecordAudioPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Request the RECORD_AUDIO permission
    private void requestRecordAudioPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_AUDIO_PERMISSION_REQUEST_CODE);
    }
}
