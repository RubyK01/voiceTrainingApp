package com.example.voicetrainingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class FrequencyResultDialogFragment extends DialogFragment {
    private double hz;

    public FrequencyResultDialogFragment(double hz) {
        this.hz = hz;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Frequency Result")
                .setMessage("Resulting Frequency: " + hz)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
