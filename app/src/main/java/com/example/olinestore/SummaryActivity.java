package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olinestore.Fragments.UserPanelFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private TextView userInfoTV;
    private TextView userAddressInfoTV;
    private TextView itemsCountTV;
    private TextView balanceAfterPurchaseTV;
    private TextView totalAmountTV;
    private Button goToUserPanelButton;
    private Button confirmPaymentButton;
    private float totalAmount;
    private int itemsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Intent intent = getIntent();
        totalAmount = intent.getFloatExtra("totalAmount", 0);
        itemsCount = intent.getIntExtra("itemsCount", 0);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userInfoTV = findViewById(R.id.userInfoTextView);
        userAddressInfoTV = findViewById(R.id.userAdressInfoTextView);
        itemsCountTV = findViewById(R.id.itemsCountTextView);
        balanceAfterPurchaseTV = findViewById(R.id.balanceAfterPurchaseTV);
        goToUserPanelButton = findViewById(R.id.goToUserPanelButton);
        totalAmountTV = findViewById(R.id.totalAmountTV);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        goToUserPanelButton.setOnClickListener(v -> {
            startActivity(new Intent(SummaryActivity.this, UserPanelFragment.class));
        });

        updateUserAddress();
        confirmPaymentButton.setOnClickListener(v -> {
            updateUserBalance(firebaseAuth.getUid());
            updateHistory();
        });

    }


    private void updateHistory() {
        Map<String, Object> data = new HashMap<>();
        data.put("uID", firebaseAuth.getUid());
        data.put("date", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        data.put("nrOfItems", itemsCount);
        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        String formattedValue = decimalFormat.format(totalAmount);

        data.put("orderPrice", formattedValue);
        firestore
                .collection("History")
                .add(data);
    }
    private void updateUserAddress() {
        firestore
                .collection("registeredUsers")
                .document(firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(
                        task ->
                        {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    float userBalance = Float.parseFloat(document.getString("balance"));
                                    userInfoTV.setText(document.getString("name") + " " + document.getString("surname"));
                                    itemsCountTV.setText("Number of item: " + itemsCount);
                                    DecimalFormat decimalFormat = new DecimalFormat("####.##");
                                    String formattedValue = decimalFormat.format(userBalance - totalAmount);
                                    balanceAfterPurchaseTV.setText("Balance after purchase: " + formattedValue);
                                    formattedValue = decimalFormat.format(totalAmount);
                                    totalAmountTV.setText("To pay: " + formattedValue + " USD");
                                    String address =
                                            document.getString("address");
                                    if (address == null || address.isEmpty()) {
                                        goToUserPanelButton.setVisibility(View.VISIBLE);
                                        userAddressInfoTV.setText("Please add delivery address");
                                    } else {
                                        userAddressInfoTV.setText(address);
                                        goToUserPanelButton.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        });

    }

    private void updateUserBalance(String Uid) {
        firestore
                .collection("registeredUsers")
                .document(Uid)
                .get()
                .addOnCompleteListener(
                        task ->
                        {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userBalance =
                                            document.getString("balance");
                                    if (userBalance != null) {
                                        Double newBalance = Double.parseDouble(userBalance);
                                        newBalance -= totalAmount;
                                        setUserBalance(Uid, newBalance);
                                    }
                                }
                            }
                        });
    }

    private void setUserBalance(String Uid, Double newBalance) {
        DecimalFormat decimalFormat = new DecimalFormat("####.##");
        String formattedValue = decimalFormat.format(newBalance);
        firestore
                .collection("registeredUsers")
                .document(Uid)
                .update("balance", formattedValue)
                .addOnCompleteListener(
                        task -> {
                            Bag bag = Bag.getInstance();
                            bag.clearBag();
                            Toast.makeText(SummaryActivity.this, "Purchase successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SummaryActivity.this, MainActivity.class));
                            finish();
                        });
    }
}