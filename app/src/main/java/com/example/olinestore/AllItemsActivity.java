package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olinestore.Fragments.LoginFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllItemsActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private ArrayList<ListItem> itemList;
    private ArrayList<ListItem> filteredItemList;
    private ArrayList<ListItem> displayedItemList;
    ItemsAdapter itemsAdapter;

    ArrayAdapter<String> categoriesAdapter;
    private Spinner categoriesSpinner;
    private ArrayList<String> categories;
    private String category = "Shoes";

    private Map<String, String[]> categoriesSizes;
    private ListView itemsListView;
    private TextView totalAmountTextView;
    private Button nextPageButton;
    private Button previousPageButton;
    private int numberOfItems = 10;
    private int pageNumber = 0;
    private EditText searchItemEditText;
    private Button filterButton;
    private FragmentContainerView filtersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
//        categoriesSpinner = findViewById(R.id.categoriesSpinner);
        filtersFragment = findViewById(R.id.filtersFragment);
        filterButton = findViewById(R.id.filtersButton);
        filterButton.setOnClickListener(v->{
            if(itemsListView.getVisibility() == View.INVISIBLE){
                itemsListView.setVisibility(View.VISIBLE);
                filtersFragment.setVisibility(View.INVISIBLE);
            }
            else {
                itemsListView.setVisibility(View.INVISIBLE);
                filtersFragment.setVisibility(View.VISIBLE);
            }
        });
        firestore = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        categoriesSizes = new HashMap<>();
        getCategoriesFromStorage();

        Button searchButton = findViewById(R.id.searchButton);


        itemsListView = findViewById(R.id.itemsListView);
        nextPageButton = findViewById(R.id.nextPageButton);
        previousPageButton = findViewById(R.id.previusPageButton);
        searchItemEditText = findViewById(R.id.searchItemEditText);

        totalAmountTextView = findViewById(R.id.pageNumberTextView);
        itemList = new ArrayList<>();
        filteredItemList = new ArrayList<>();

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
            ;
            String chosenItem = displayedItemList.get(position).getName();
            Toast.makeText(AllItemsActivity.this, chosenItem, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AllItemsActivity.this, ItemDetailsActivity.class);
            intent.putExtra("item", itemList.get(position));
            startActivity(intent);
        });

        searchItemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredItemList.clear();
                if(s.length() > 0){
                    for(ListItem item:itemList){
                        if(item.getName().toLowerCase().contains(s.toString().toLowerCase())){
                            filteredItemList.add(item);
                        }
                    }
                }
                else {
                    filteredItemList.addAll(itemList);
                    System.out.println(itemList.get(0).getName());
                }
                showSpecifiedNumberOfItems();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showSpecifiedNumberOfItems() {
        displayedItemList.clear();
        for (int i = pageNumber * numberOfItems; i < (pageNumber + 1) * numberOfItems && i < filteredItemList.size(); i++) {
            displayedItemList.add(filteredItemList.get(i));
        }
        itemsAdapter.notifyDataSetChanged();
        totalAmountTextView.setText("Page " + String.valueOf(pageNumber + 1));
    }

    private void handleDisplayedItem(String mode) {
        if (mode.equals("next") && pageNumber < filteredItemList.size() / 10) {
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
                                String currency = (String) document.getData().get("currency");
                                String path = (String) document.getData().get("img");
                                ListItem item = new ListItem(name, brand, colors, price, currency, path, categoriesSizes.get(category));
                                itemList.add(item);
                            } catch (NumberFormatException e) {
                                System.out.println(e);
                            }
                        }
                        pageNumber = 0;
                        filteredItemList.addAll(itemList);
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
                        String categoryName = (String) document.getData().get("name");
                        String sizes = "size," + document.getData().get("sizes");
                        categories.add(categoryName);
                        categoriesSizes.put(categoryName, sizes.split(","));
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                }
                categoriesAdapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, categories
                );
//                handleSpinnerActions();
            }
        });
    }

//    private void handleSpinnerActions() {
//        categoriesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                category = parent.getItemAtPosition(position).toString();
//                Toast.makeText(AllItemsActivity.this,  category, Toast.LENGTH_SHORT).show();
//                getDataFromFirestore();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categoriesSpinner.setAdapter(categoriesAdapter);
//    }

}



