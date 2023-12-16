package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class BagActivity extends AppCompatActivity {
    ItemsAdapter adapter;
    ListView bagListView;

    TextView totalAmount;
    Button goToSummaryButton;
    TextView noItemsInfo;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Bag bag = Bag.getInstance();
        noItemsInfo = findViewById(R.id.noItemsInfo);
        noItemsInfo.setVisibility(bag.getItemsCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        goToSummaryButton = findViewById(R.id.goToSummaryButton);
        goToSummaryButton.setOnClickListener(v -> {
            if(!isUserSignedIn()){
                Toast.makeText(BagActivity.this, "Please sign in to buy Your products", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BagActivity.this, LoginActivity.class));
            } else if (bag.getItemsCount() != 0) {
                Intent intent = new Intent(BagActivity.this, SummaryActivity.class);
                intent.putExtra("totalAmount", bag.getTotalAmount());
                intent.putExtra("itemsCount", bag.getItemsCount());
                startActivity(intent);
            } else {
                Toast.makeText(BagActivity.this, "The bag is empty!!", Toast.LENGTH_SHORT).show();
            }
        });
        totalAmount = findViewById(R.id.totalAmountTextView);
        adapter = new ItemsAdapter(this, bag.getItems(), true, totalAmount);
        bagListView = findViewById(R.id.bagListView);
        bagListView.setAdapter(adapter);
        // Format the float value using DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("####.####");
        String formattedValue = decimalFormat.format(bag.getTotalAmount());
        totalAmount.setText(formattedValue + " USD");
    }

    private boolean isUserSignedIn() {
        if(firebaseAuth.getCurrentUser() != null) return true;
        else return false;
    }
}