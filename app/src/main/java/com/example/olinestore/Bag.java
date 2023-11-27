package com.example.olinestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Bag {
    private static Bag instance;


    private Bag() {}
    private ArrayList<ListItem> items = new ArrayList<>();
    private float totalAmount;
    private String text = "japierdole";

    public ArrayList<ListItem> getItems() {
        return items;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void addToBag(ListItem listItem) {
        items.add(listItem);
        updateTotalAmount();
    }

    public void removeFromBag(ListItem listItem) {
        items.remove(listItem);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        for (ListItem item : items) {
            totalAmount += item.getPrice();
        }
    }

    public String getText() {
        return text;
    }

    public static Bag getInstance() {
        if (instance == null) {
            instance = new Bag();
        }
        return instance;
    }
}