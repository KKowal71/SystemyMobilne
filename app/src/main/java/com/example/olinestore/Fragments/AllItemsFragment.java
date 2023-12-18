package com.example.olinestore.Fragments;

import com.example.olinestore.FiltersActivity;
import com.example.olinestore.ListItem;
import com.example.olinestore.ItemsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.olinestore.R;
import com.example.olinestore.ItemDetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllItemsFragment extends Fragment {
    private FirebaseFirestore firestore;
    private ArrayList<ListItem> itemList;
    public  ArrayList<ListItem> filteredItemList;
    private ArrayList<ListItem> displayedItemList;
    ItemsAdapter itemsAdapter;

    private ArrayList<String> categories;
    public String category = "";
    private Integer minPrice = 1;
    private Integer maxPrice = 2500;

    private Map<String, String[]> categoriesSizes;
    public ListView itemsListView;
    private TextView totalAmountTextView;
    private Button nextPageButton;
    private Button previousPageButton;
    public  Integer numberOfItems = 10;
    private int pageNumber = 0;
    private EditText searchItemEditText;
    private Button filterButton;

    public static MutableLiveData<Integer> itemsOnPageListener =
            new MutableLiveData<>();
    public static MutableLiveData<String> categoryListener =
            new MutableLiveData<>();
    public static MutableLiveData<Integer> minPriceListener =
            new MutableLiveData<>();
    public static MutableLiveData<Integer> maxPriceListener =
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
        filterButton = view.findViewById(R.id.filtersButton);

        firestore = FirebaseFirestore.getInstance();
        categories = new ArrayList<>();
        categories.add("-");
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

        itemsAdapter = new ItemsAdapter(getContext(), displayedItemList);
        itemsListView.setAdapter(itemsAdapter);

        filterButton.setOnClickListener(v -> {
            System.out.println(categories.get(0));
            Intent intent = new Intent(getActivity(), FiltersActivity.class);
            intent.putStringArrayListExtra("categories", categories);
            intent.putExtra("itemsOnPage", numberOfItems);
            intent.putExtra("filteredItems", filteredItemList);
            startActivity(intent);
        });

        nextPageButton.setOnClickListener(v -> {
            handleDisplayedItem("next");
            showSpecifiedNumberOfItems();
        });
        previousPageButton.setOnClickListener(v -> {
            handleDisplayedItem("previous");
            showSpecifiedNumberOfItems();
        });

        itemsListView.setOnItemClickListener((parent, view2, position, id) -> {

            String chosenItem = displayedItemList.get(position).getName();
            Toast.makeText(getContext(), chosenItem, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), ItemDetailsActivity.class);
            intent.putExtra("item", displayedItemList.get(position));
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
                if (s.length() > 0) {
                    applyFilters(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        itemsOnPageListener.setValue(numberOfItems);
        itemsOnPageListener.observe(AllItemsFragment.this,
                aInteger -> {
                    numberOfItems = itemsOnPageListener.getValue();
                    showSpecifiedNumberOfItems();
                });

        categoryListener.setValue(category);
        categoryListener.observe(AllItemsFragment.this,
                aString -> {
                    category = categoryListener.getValue();

                    System.out.println("change");
                    applyFilters(searchItemEditText.getEditableText().toString());
                });

        minPriceListener.setValue(minPrice);
        minPriceListener.observe(AllItemsFragment.this,
                aInteger -> {
                    minPrice = minPriceListener.getValue();
                    applyFilters(searchItemEditText.getEditableText().toString());
                });

        maxPriceListener.setValue(maxPrice);
        maxPriceListener.observe(AllItemsFragment.this,
                aInteger -> {
                    maxPrice = maxPriceListener.getValue();
                    applyFilters(searchItemEditText.getEditableText().toString());
                });
    }

    ;
    private void applyFilters(String s){
        pageNumber = 0;
        filteredItemList.clear();
        for (ListItem item : itemList) {
            boolean condition1 = category.isEmpty() || category.equals("-") || item.getCategories().equals(category);
            boolean condition2 = item.getName().toLowerCase()
                    .contains(s.toLowerCase()) || item.getBrand().toLowerCase()
                    .contains(s.toLowerCase());
            boolean condition3 = (minPrice.floatValue() == 1.0 && maxPrice.floatValue() == 1.0) || minPrice.floatValue() <= item.getPrice() && item.getPrice() <= maxPrice.floatValue() ;
            if (condition1 && condition2 && condition3) {
                filteredItemList.add(item);
            }
        }
        showSpecifiedNumberOfItems();
    }

    private void showSpecifiedNumberOfItems() {
        displayedItemList.clear();
        for (int i = pageNumber * numberOfItems;
             i < (pageNumber + 1) * numberOfItems &&
                     i < filteredItemList.size(); i++) {
            displayedItemList.add(filteredItemList.get(i));
        }
        itemsAdapter.notifyDataSetChanged();
        totalAmountTextView.setText("Page " + (pageNumber + 1) + "\n(max. " + numberOfItems + " items on page)");
    }

    private void handleDisplayedItem(String mode) {
        if (mode.equals("next") && pageNumber < filteredItemList.size() / numberOfItems) {
            pageNumber += 1;
        } else if (mode.equals("previous") && pageNumber > 0) {
            pageNumber -= 1;
        }
    }



    private void addItemFromDocument(QueryDocumentSnapshot document, String category) {
        String name = document.getString("name");
        String brand = document.getString("brand");
        if (brand == null) {
            System.out.println("AHA");

        }
        System.out.println(name);
        System.out.println(brand);
        String colors = document.getString("colors");
        float price = Float.parseFloat(document.getString("price"));
        String currency = document.getString("currency");
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



