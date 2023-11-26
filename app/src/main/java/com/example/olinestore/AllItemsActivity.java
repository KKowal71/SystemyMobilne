package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AllItemsActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private ArrayList<ListItem> itemList;
    ItemsAdapter adapter;

    private ListView itemsListView;
    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        itemsListView = findViewById(R.id.itemsListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        firestore = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        getDataFromFirestore();

        adapter = new ItemsAdapter(this, itemList);
        itemsListView.setAdapter(adapter);
    }



    private void getDataFromFirestore() {
        firestore.collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                String name = (String) document.getData().get("name");
                                String brand = (String) document.getData().get("brand");
                                String colors = (String) document.getData().get("colors");
                                float price = Float.parseFloat((String) document.getData().get("prices.amountMin"));
                                String categories = (String) document.getData().get("categories");
                                String currency = (String) document.getData().get("prices.currency");
                                ListItem item = new ListItem(name, brand, categories, colors, price, currency);
                                System.out.println(item.toString());
                                itemList.add(item);
                            } catch (NumberFormatException e) {
                                System.out.println(e);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        totalAmountTextView.setText(String.valueOf(itemList.size()) + " products");
                    } else {
                        System.out.println("error");
                    }
                });
    }
}