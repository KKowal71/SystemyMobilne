package com.example.olinestore;

public class ListItem {
    private String text;

    public ListItem(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
    @Override
    public String toString() {
        return text;
    }
}
