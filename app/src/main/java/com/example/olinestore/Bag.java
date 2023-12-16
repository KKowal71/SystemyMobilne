package com.example.olinestore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        return (float) (Math.round(totalAmount * 100.0) / 100.0);
    }

    public void addToBag(ListItem listItem) {
        items.add(listItem);
        totalAmount += listItem.getPrice();
    }

    public void removeFromBag(ListItem listItem) {
        items.remove(listItem);
        totalAmount -= listItem.getPrice();
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