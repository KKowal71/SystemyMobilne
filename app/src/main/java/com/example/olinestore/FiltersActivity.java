package com.example.olinestore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olinestore.Fragments.AllItemsFragment;

import java.util.ArrayList;

public class FiltersActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        minPriceSeekBar = findViewById(R.id.minPrizeSeekBar);
        maxPriceSeekBar = findViewById(R.id.maxPrizeSeekBar);
        itemsOnPageNumber = findViewById(R.id.itemsOnPageET);
        confirmFiltersButton = findViewById(R.id.confirmFiltersButton);
        categoriesSpinner = findViewById(R.id.categorySinner);
        colorSpinner = findViewById(R.id.colorSpinner);

        itemsOnPageNumber.setText(String.valueOf(numberOfItems));

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        if(!AllItemsFragment.categoryListener.getValue().equals("")){
            categoriesSpinner.setSelection(categories.indexOf((String) AllItemsFragment.categoryListener.getValue()));
        }
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String colors[] = {"-", "black", "blue", "green", "pink", "black", "orange", "yellow", "brown", "silver", "gold"};
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
            if(confirmFilters()) {
                finish();
            }
        });

        minPriceSeekBar.setMin(1);
        minPriceSeekBar.setMax(2500);
        minPriceSeekBar.setProgress(10);
        minPriceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPrizeTextView.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxPriceSeekBar.setMin(1);
        maxPriceSeekBar.setMax(2500);
        maxPriceSeekBar.setProgress(10);
        maxPriceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPrizeTextView.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public boolean confirmFilters(){
        Integer number = Integer.parseInt(itemsOnPageNumber.getText().toString());
        if(number <= 0) {
            Toast.makeText(FiltersActivity.this, "Page number must be greater than 0!!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(minPriceSeekBar.getProgress() > maxPriceSeekBar.getProgress()){
            Toast.makeText(FiltersActivity.this, "Max prize must be greater than min prize!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            AllItemsFragment.itemsOnPageListener.setValue(number);
            System.out.println(category);
            AllItemsFragment.categoryListener.setValue(category);
            System.out.println(maxPriceSeekBar.getProgress());
            AllItemsFragment.maxPriceListener.setValue(maxPriceSeekBar.getProgress());
            AllItemsFragment.minPriceListener.setValue(minPriceSeekBar.getProgress());
            return true;
        }
    }
    EditText itemsOnPageNumber;
    TextView minPrizeTextView;
    TextView maxPrizeTextView;
    Spinner colorSpinner;
    Spinner categoriesSpinner;
    SeekBar maxPriceSeekBar;
    SeekBar minPriceSeekBar;
    String category;

    Button confirmFiltersButton;
}