package com.example.olinestore.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.olinestore.MainActivity;
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


        String[] langs = {"English", "Polish", "Espanol"};
        ArrayAdapter<CharSequence> langSpinnerAdapt = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, langs);
        langSpinner.setAdapter(langSpinnerAdapt);
        textSize.setOnCheckedChangeListener(sizeListener);

        textSize.setChecked(wasSizeChecked);

    }


    private void updateThemeConfiguration(float newTextScaleX) {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = newTextScaleX; // set textScaleX to fontScale

        getActivity().getTheme().applyStyle(R.style.NormalTheme, true);

        // Apply the updated configuration to the resources
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

    }


    private class SizeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton,
        boolean isChecked) {

            float newTextScale = 1.0f;
            if (isChecked) {
                newTextScale = 1.4f;
                textSize.setText("Shrink text");
                wasSizeChecked = true;
            } else {
                textSize.setText("Enlarge text");
                wasSizeChecked = false;
            }
            updateThemeConfiguration(newTextScale);
        }
    }
    private SizeListener sizeListener = new SizeListener();

    private Switch textSize ;

    private Spinner langSpinner;

    private static boolean wasSizeChecked = false;
}