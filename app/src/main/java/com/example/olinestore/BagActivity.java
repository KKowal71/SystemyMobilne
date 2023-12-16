package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class BagActivity extends AppCompatActivity {
    ItemsAdapter adapter;
    ListView bagListView;

    TextView totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        Bag bag = Bag.getInstance();
        totalAmount = findViewById(R.id.totalAmountTextView);
        adapter = new ItemsAdapter(this, bag.getItems(), true, totalAmount);
        bagListView = findViewById(R.id.bagListView);
        bagListView.setAdapter(adapter);
        // Format the float value using DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("####.####");
        String formattedValue = decimalFormat.format(bag.getTotalAmount());
        totalAmount.setText(formattedValue + " USD");
    }
}