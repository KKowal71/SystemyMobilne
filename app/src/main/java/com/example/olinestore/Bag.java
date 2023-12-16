package com.example.olinestore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Bag {
    private static Bag instance;


    private Bag() {}
    private ArrayList<ListItem> items = new ArrayList<>();
    private float totalAmount = 0;



    public ArrayList<ListItem> getItems() {
        return items;
    }

    public void calculateTotalAmount() {
        totalAmount = 0;
        for(ListItem item:items){
            totalAmount += item.getPrice() * item.getAmount();
        }
    }

    public float getTotalAmount() {
        return (float) (Math.round(totalAmount * 100.0) / 100.0);
    }

    public void addToBag(ListItem listItem) {
        items.add(listItem);
        calculateTotalAmount();
    }

    public void removeFromBag(ListItem listItem) {
        items.remove(listItem);
        calculateTotalAmount();
    }



    public static Bag getInstance() {
        if (instance == null) {
            instance = new Bag();
        }
        return instance;
    }
}