package com.example.olinestore.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.olinestore.R;


public class filtersFragment extends Fragment {



    public filtersFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        minPrizeTextView = view.findViewById(R.id.minPrizeTextView);
        maxPrizeTextView = view.findViewById(R.id.maxPrizeTextView);
        minPrizeSeekBar = view.findViewById(R.id.minPrizeSeekBar);
        maxPrizeSeekBar = view.findViewById(R.id.maxPrizeSeekBar);
        confirmFilters = view.findViewById(R.id.confirmFiltersButton);
        categoriesSpinner = view.findViewById(R.id.categorySinner);

        confirmFilters.setOnClickListener(v->{
            ((AllItemsFragment) getParentFragment()).visibilityListener.setValue(false);

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            minPrizeSeekBar.setMin(10);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            maxPrizeSeekBar.setMin(10);
        }
        minPrizeSeekBar.setMax(3000);
        maxPrizeSeekBar.setMax(3000);
        minPrizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPrizeTextView.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        maxPrizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPrizeTextView.setText(String.valueOf(seekBar.getProgress() * 1.4));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
            handleSpinnerActions();
    }
    private void handleSpinnerActions() {
        categoriesSpinner.setAdapter(((AllItemsFragment) getParentFragment()).categoriesAdapter);

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((AllItemsFragment) getParentFragment()).category = parent.getItemAtPosition(position).toString();
//                Toast.makeText(AllItemsActivity.this,  category, Toast.LENGTH_SHORT).show();
                ((AllItemsFragment) getParentFragment()).getDataFromFirestore();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    TextView minPrizeTextView;
    TextView maxPrizeTextView;
    Spinner colorSpinner;
    Spinner categoriesSpinner;
    SeekBar maxPrizeSeekBar;
    SeekBar minPrizeSeekBar;

    Button confirmFilters;
}