package com.example.olinestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olinestore.AllItemsActivity;
import com.example.olinestore.ItemDetailsActivity;
import com.example.olinestore.ItemsAdapter;
import com.example.olinestore.ListItem;
import com.example.olinestore.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class allItemsFragment extends Fragment {
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


    public allItemsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storage = FirebaseStorage.getInstance();

        itemsListView = view.findViewById(R.id.itemsListView);
        nextPageButton = view.findViewById(R.id.nextPageButton);
        previousPageButton = view.findViewById(R.id.previusPageButton);

        totalAmountTextView = view.findViewById(R.id.pageNumberTextView);
        firestore = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();
        displayedItemList = new ArrayList<>();
        getDataFromFirestore();

        adapter = new ItemsAdapter(getActivity(), displayedItemList);
        itemsListView.setAdapter(adapter);

        nextPageButton.setOnClickListener(v -> {
            handleDisplayedItem("next");
            showSpecifiedNumberOfItems();
        });
        previousPageButton.setOnClickListener(v -> {
            handleDisplayedItem("previous");
            showSpecifiedNumberOfItems();
        });

        itemsListView.setOnItemClickListener((parent, view1, position, id) -> {
            // Handle item click
            String xd = displayedItemList.get(position).getName();
//            Toast.makeText(AllItemsActivity.this,xd, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
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
                                float price = Float.parseFloat((String) document.getData().get("price"));
                                String categories = (String) document.getData().get("categories");
                                String currency = (String) document.getData().get("prices");
//                                ListItem item = new ListItem(name, brand, colors, price, currency, "images/buty.png");
//                                itemList.add(item);
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