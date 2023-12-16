package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.olinestore.R;


public class SettingsFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        langSpinner = view.findViewById(R.id.LanguageSpinner);
        textSize = view.findViewById(R.id.TextSize);
        accMode = view.findViewById(R.id.AccessibilityMode);
        String[] langs = {"English", "Polish", "Espanol"};
        ArrayAdapter<CharSequence> langSpinnerAdapt = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, langs);
        langSpinner.setAdapter(langSpinnerAdapt);
        textSize.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textSize.setText("Shrink text");
            } else {
                textSize.setText("Enlarge text");
            }
        });

        accMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                accMode.setText("Turn off");
            } else {
                accMode.setText("Turn on");
            }
        });
    }

    private Switch textSize;

    private Switch accMode;
    private Spinner langSpinner;
}