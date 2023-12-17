package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.olinestore.Fragments.AllItemsFragment;

import java.util.ArrayList;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Intent intent = getIntent();
        ArrayList<ListItem> filteredItems = (ArrayList<ListItem>) intent.getSerializableExtra("filteredItems");
        ArrayList<String> categories = intent.getStringArrayListExtra("categories");
        int numberOfItems = intent.getIntExtra("itemsOnPage", 0);

        minPrizeTextView = findViewById(R.id.minPrizeTextView);
        maxPrizeTextView = findViewById(R.id.maxPrizeTextView);
        minPrizeSeekBar = findViewById(R.id.minPrizeSeekBar);
        maxPrizeSeekBar = findViewById(R.id.maxPrizeSeekBar);
        itemsOnPageNumber = findViewById(R.id.itemsOnPageET);
        confirmFiltersButton = findViewById(R.id.confirmFiltersButton);
        categoriesSpinner = findViewById(R.id.categorySinner);
        colorSpinner = findViewById(R.id.colorSpinner);

        itemsOnPageNumber.setText(String.valueOf(numberOfItems));

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String colors[] = {"black", "blue", "green", "pink", "black", "orange", "yellow", "brown", "silver", "gold"};
        ArrayAdapter<CharSequence> colorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        colorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorsAdapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmFiltersButton.setOnClickListener(v->{
            confirmFilters();
            finish();
        });
    }

    public void confirmFilters(){
        Integer number = Integer.parseInt(itemsOnPageNumber.getText().toString());
        AllItemsFragment.itemsOnPageListener.setValue(number);
        System.out.println(category);
        AllItemsFragment.categoryListener.setValue(category);
    }
    EditText itemsOnPageNumber;
    TextView minPrizeTextView;
    TextView maxPrizeTextView;
    Spinner colorSpinner;
    Spinner categoriesSpinner;
    SeekBar maxPrizeSeekBar;
    SeekBar minPrizeSeekBar;
    String category;

    Button confirmFiltersButton;
}