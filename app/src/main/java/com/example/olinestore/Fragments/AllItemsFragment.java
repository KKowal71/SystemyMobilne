package com.example.olinestore.Fragments;

import com.example.olinestore.ListItem;
import com.example.olinestore.ItemsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.olinestore.R;
import com.example.olinestore.ItemDetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olinestore.Fragments.SearchFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllItemsFragment extends Fragment {
    private FirebaseFirestore firestore;
    private ArrayList<ListItem> itemList;
    private ArrayList<ListItem> filteredItemList;
    private ArrayList<ListItem> displayedItemList;
    ItemsAdapter itemsAdapter;

    public ArrayAdapter<String> categoriesAdapter;
    private ArrayList<String> categories;
//    public String category = "Shoes";

    private Map<String, String[]> categoriesSizes;
    public ListView itemsListView;
    private TextView totalAmountTextView;
    private Button nextPageButton;
    private Button previousPageButton;
    private int numberOfItems = 10;
    private int pageNumber = 0;
    private EditText searchItemEditText;
    private Button filterButton;
    private FragmentContainerView filtersFragment;
    public Boolean isFilterFragmentShown = false;
    public MutableLiveData<Boolean> visibilityListener =
            new MutableLiveData<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filtersFragment = view.findViewById(R.id.filtersFragment);
        filterButton = view.findViewById(R.id.filtersButton);


        visibilityListener.setValue(isFilterFragmentShown);
        visibilityListener.observe(AllItemsFragment.this,
                                   new Observer<Boolean>() {
                                       @Override
                                       public void onChanged(Boolean aBoolean) {
                                           Toast.makeText(getContext(),
                                                          String.valueOf(
                                                                  isFilterFragmentShown),
                                                          Toast.LENGTH_SHORT)
                                                   .show();
                                           if (!visibilityListener.getValue()) {
                                               itemsListView.setVisibility(
                                                       View.VISIBLE);
                                               filterButton.setVisibility(
                                                       View.VISIBLE);
                                               filtersFragment.setVisibility(
                                                       View.INVISIBLE);
                                           } else {
                                               itemsListView.setVisibility(
                                                       View.INVISIBLE);
                                               filterButton.setVisibility(
                                                       View.INVISIBLE);
                                               filtersFragment.setVisibility(
                                                       View.VISIBLE);
                                           }
                                       }
                                   });
        filterButton.setOnClickListener(v -> {
            isFilterFragmentShown = true;
            visibilityListener.setValue(isFilterFragmentShown);
        });
        firestore = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        categoriesSizes = new HashMap<>();
        getAllItemsAndCategoriesFromStorage();


        itemsListView = view.findViewById(R.id.itemsListView);
        nextPageButton = view.findViewById(R.id.nextPageButton);
        previousPageButton = view.findViewById(R.id.previusPageButton);
        searchItemEditText =
                ((SearchFragment) getParentFragment()).getSearchText();

        totalAmountTextView = view.findViewById(R.id.pageNumberTextView);
        itemList = new ArrayList<>();
        filteredItemList = new ArrayList<>();

        displayedItemList = new ArrayList<>();
//        getDataFromFirestore();
        itemsAdapter = new ItemsAdapter(getContext(), displayedItemList);
        itemsListView.setAdapter(itemsAdapter);

        nextPageButton.setOnClickListener(v -> {
            handleDisplayedItem("next");
            showSpecifiedNumberOfItems();
        });
        previousPageButton.setOnClickListener(v -> {
            handleDisplayedItem("previous");
            showSpecifiedNumberOfItems();
        });

        itemsListView.setOnItemClickListener((parent, view2, position, id) -> {
            // Handle item click
            ;
            String chosenItem = displayedItemList.get(position).getName();
            Toast.makeText(getContext(), chosenItem, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), ItemDetailsActivity.class);
            intent.putExtra("item", itemList.get(position));
            startActivity(intent);
        });

        searchItemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                filteredItemList.clear();
                if (s.length() > 0) {
                    for (ListItem item : itemList) {
                        if (item.getName().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            filteredItemList.add(item);
                        } else if (item.getBrand().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            filteredItemList.add(item);
                        }
                    }
                } else {
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

    ;

    private void showSpecifiedNumberOfItems() {
        displayedItemList.clear();
        for (int i = pageNumber * numberOfItems;
             i < (pageNumber + 1) * numberOfItems &&
                     i < filteredItemList.size(); i++) {
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

//    public void getDataFromFirestore() {
//        itemList.clear();
//        firestore.collection(category)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            addItemFromDocument(document);
//                        }
//                        pageNumber = 0;
//                        filteredItemList.addAll(itemList);
//                        showSpecifiedNumberOfItems();
//                        itemsAdapter.notifyDataSetChanged();
//                    }
//                });
//    }

    private void addItemFromDocument(QueryDocumentSnapshot document, String category) {
        String name = (String) document.getString("name");
        String brand = (String) document.getString("brand");
        if (brand == null) {
            System.out.println("AHA");

        }
        System.out.println(name);
        System.out.println(brand);
        String colors = (String) document.getString("colors");
        float price = Float.parseFloat(document.getString("price"));
        String currency = (String) document.getString("currency");
        String path = document.getString("img");
        ListItem item = new ListItem(name, brand, colors, price, currency, path,
                                     category, categoriesSizes.get(category));
        itemList.add(item);
    }

    public void getAllItemsAndCategoriesFromStorage() {
        firestore.collection("categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        String categoryName =
                                (String) document.getData().get("name");
                        String sizes =
                                "size," + document.getData().get("sizes");
                        categories.add(categoryName);
                        categoriesSizes.put(categoryName, sizes.split(","));
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                }
                getAllItemsFromStorage();
                categoriesAdapter = new ArrayAdapter<>(getContext(),
                                                       android.R.layout.simple_spinner_item,
                                                       categories);
                categoriesAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
            }
        });
    }

    public void getAllItemsFromStorage() {
        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (String cat : categories) {
            Task<QuerySnapshot> getCategory = firestore.collection(cat).get();
            tasks.add(getCategory);

            getCategory.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        addItemFromDocument(document, cat);
                    }
                }
            });
        }

        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            pageNumber = 0;
            filteredItemList.addAll(itemList);
            showSpecifiedNumberOfItems();
            itemsAdapter.notifyDataSetChanged();
        });


    }

    public ArrayList<ListItem> getItemList() {
        return itemList;
    }


}



