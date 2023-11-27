package com.example.olinestore;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ListItem {
    private String text;
    private String brand;
    private String categories;
    private String colors;
    private String name;
    private String currency;
    private float price;



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

    public ListItem(String name, String brand, String categories, String colors, float price, String currency, String imagePath) {
        this.name = name;
        this.brand = brand;
        this.categories = categories;
        this.colors = colors;
        this.currency = currency;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getText() {
        return this.text;
    }
    @Override
    public String toString() {
        return name + brand + price;
    }
}
