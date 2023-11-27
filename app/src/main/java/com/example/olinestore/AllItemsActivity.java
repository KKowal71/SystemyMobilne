package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
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
    ItemsAdapter adapter;

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
        storage = FirebaseStorage.getInstance();

        itemsListView = findViewById(R.id.itemsListView);
        nextPageButton = findViewById(R.id.nextPageButton);
        previousPageButton = findViewById(R.id.previusPageButton);

        totalAmountTextView = findViewById(R.id.pageNumberTextView);
        firestore = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        displayedItemList = new ArrayList<>();
        getDataFromFirestore();

        adapter = new ItemsAdapter(this, displayedItemList);
        itemsListView.setAdapter(adapter);

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
            Toast.makeText(AllItemsActivity.this,xd, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AllItemsActivity.this, ItemDetailsActivity.class);
//            intent.putExtra("name", displayedItemList.get(position).getName());
//            intent.putExtra("brand", displayedItemList.get(position).getBrand());
//            intent.putExtra("colors", displayedItemList.get(position).getColors());
//            intent.putExtra("price", String.valueOf(displayedItemList.get(position).getPrice()) + " " + displayedItemList.get(position).getCurrency());
//            intent.putExtra("categories", displayedItemList.get(position).getCategories());
//            intent.putExtra("imagePath", displayedItemList.get(position).getImagePath());
            intent.putExtra("item", itemList.get(position));
            startActivity(intent);
        });
}

    private void showSpecifiedNumberOfItems() {
        displayedItemList.clear();
        for (int i = pageNumber * numberOfItems; i < (pageNumber + 1) * numberOfItems; i++) {
            displayedItemList.add(itemList.get(i));
        }
        adapter.notifyDataSetChanged();
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
                                ListItem item = new ListItem(name, brand, categories, colors, price, currency, "images/buty.png");
                                itemList.add(item);
                            } catch (NumberFormatException e) {
                                System.out.println(e);
                            }
                        }
                        showSpecifiedNumberOfItems();
                        adapter.notifyDataSetChanged();
//                        totalAmountTextView.setText(String.valueOf(itemList.size()) + " products");
                    } else {
                        System.out.println("error");
                    }
                });
    }


}



