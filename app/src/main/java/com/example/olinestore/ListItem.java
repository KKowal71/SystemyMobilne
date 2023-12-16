package com.example.olinestore;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ListItem implements Serializable {
    private String brand;
    private String categories;
    private String colors;
    private String name;
    private String currency;
    private float price;
    private String[] sizes;
    private String size;

    public int getAmount() {
        return amount;
    }

    private int amount;

    public String getSize() {
        return size;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String[] getSizes() {
        return sizes;
    }

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategories() {
        return categories;
    }

    public String getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public float getPrice() {
        return price;
    }

    public ListItem(String name, String brand, String colors, float price, String currency, String imagePath, String[] sizes) {
        this.name = name;
        this.brand = brand;
        this.colors = colors;
        this.currency = currency;
        this.price = price;
        if (!imagePath.isEmpty()) {
            this.imagePath = "images/" + imagePath;
        } else{
            this.imagePath = "images/buty.png";
        }
        this.sizes = sizes;
        this.amount = 1;
    }
}
