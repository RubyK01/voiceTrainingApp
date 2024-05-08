package com.example.voicetrainingapp;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voicetrainingapp.databinding.FragmentFirstBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // https://stackoverflow.com/a/68049144
        // How I learned to use image buttons
        // https://stackoverflow.com/a/15117536
        // How I got the images to scale to the button size
        Button btnLogout = view.findViewById(R.id.logout);
        ImageButton btnVoiceTraining = (ImageButton) view.findViewById(R.id.button_first);
        ImageButton btnJournal = (ImageButton) view.findViewById(R.id.button_second);
        ImageButton btnProgress = (ImageButton) view.findViewById(R.id.button_third);

        btnVoiceTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        // Button to take the user to the overall progress pie chart
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent progressPage = new Intent(getContext(), OverallGraph.class); //Name the intent set the destination
                startActivity(progressPage); //Start the next activity
                getActivity().finish(); //End the current activity
            }
        });

        // Had an issue where when I would go back to the main screen the buttons other than the voice training button wouldn't work
        // Solved it by using a method from a stack overflow post that mentioned using context
        // https://stackoverflow.com/a/55459563
        btnLogout.setOnClickListener(new View.OnClickListener() { // Button for when the user wants to signout
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginPage = new Intent(getContext(), Login.class); //Name the intent set the destination
                startActivity(loginPage); //Start the next activity
                getActivity().finish(); //End the current activity
            }
        });

        // Button to take the user to the Journal screen
        btnJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent journalPage = new Intent(getContext(), Journal.class); //Name the intent set the destination
                startActivity(journalPage); //Start the next activity
                getActivity().finish(); //End the current activity
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}