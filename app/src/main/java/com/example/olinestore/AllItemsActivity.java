package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AllItemsActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private ArrayList<ListItem> itemList;
    private ArrayList<ListItem> displayedItemList;
    ItemsAdapter itemsAdapter;
    ArrayAdapter<String> categoriesAdapter;
    private Spinner categoriesSpinner;

    private ArrayList<String> categories;
    private String category = "Shoes";
    private ListView itemsListView;
    private TextView totalAmountTextView;
    private Button nextPageButton;
    private Button previousPageButton;
    private int numberOfItems = 10;
    private int pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        categoriesSpinner = findViewById(R.id.categoriesSpinner);
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        getCategoriesFromStorage();
        String[] options = {"Opcja 1", "Opcja 2", "Opcja 3"};







        itemsListView = findViewById(R.id.itemsListView);
        nextPageButton = findViewById(R.id.nextPageButton);
        previousPageButton = findViewById(R.id.previusPageButton);

        totalAmountTextView = findViewById(R.id.pageNumberTextView);

        itemList = new ArrayList<>();
        displayedItemList = new ArrayList<>();
        getDataFromFirestore();

        itemsAdapter = new ItemsAdapter(this, displayedItemList);
        itemsListView.setAdapter(itemsAdapter);

        nextPageButton.setOnClickListener(v -> {
            handleDisplayedItem("next");
            showSpecifiedNumberOfItems();
        });
        previousPageButton.setOnClickListener(v -> {
            handleDisplayedItem("previous");
            showSpecifiedNumberOfItems();
        });

        itemsListView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click
            String xd = displayedItemList.get(position).getName();
            Toast.makeText(AllItemsActivity.this, xd, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AllItemsActivity.this, ItemDetailsActivity.class);
            intent.putExtra("item", itemList.get(position));
            startActivity(intent);
        });
    }

    private void showSpecifiedNumberOfItems() {
        displayedItemList.clear();
        for (int i = pageNumber * numberOfItems; i < (pageNumber + 1) * numberOfItems && i < itemList.size(); i++) {
            displayedItemList.add(itemList.get(i));
        }
        itemsAdapter.notifyDataSetChanged();
        totalAmountTextView.setText("Page " + String.valueOf(pageNumber + 1));
    }

    private void handleDisplayedItem(String mode) {
        if (mode.equals("next") && pageNumber < itemList.size() / 10 + 1) {
            pageNumber += 1;
        } else if (mode.equals("previous") && pageNumber > 0) {
            pageNumber -= 1;
        }
    }

    private void getDataFromFirestore() {
        itemList.clear();
        firestore.collection(category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                String name = (String) document.getData().get("name");
                                String brand = (String) document.getData().get("brand");
                                String colors = (String) document.getData().get("colors");
                                float price = Float.parseFloat((String) document.getData().get("price"));
                                String categories = (String) document.getData().get("categories");
                                String currency = (String) document.getData().get("currency");
                                String path = (String) document.getData().get("img");
                                ListItem item = new ListItem(name, brand, categories, colors, price, currency, path);
                                itemList.add(item);
                            } catch (NumberFormatException e) {
                                System.out.println(e);
                            }
                        }
                        showSpecifiedNumberOfItems();
                        itemsAdapter.notifyDataSetChanged();
//                        totalAmountTextView.setText(String.valueOf(itemList.size()) + " products");
                    } else {
                        System.out.println("error");
                    }
                });
    }

    private void getCategoriesFromStorage() {
        firestore.collection("categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        categories.add((String) document.getData().get("name"));
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                }
                categoriesAdapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, categories
                );
                handleSpinnerActions();
            }
        });
    }

    private void handleSpinnerActions() {
        categoriesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                Toast.makeText(AllItemsActivity.this,  category, Toast.LENGTH_SHORT).show();
                getDataFromFirestore();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
    }

}



