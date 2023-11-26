package com.example.olinestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.olinestore.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// CustomAdapter.java
public class ItemsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> dataList;

    public ItemsAdapter(Context context, ArrayList<ListItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView brandTextView = convertView.findViewById(R.id.brandTextView);
        TextView colorsTextView = convertView.findViewById(R.id.colorsTextView);
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
        TextView categoriesTextView = convertView.findViewById(R.id.categoryTextView);
        nameTextView.setText(dataList.get(position).getName());
        brandTextView.setText(dataList.get(position).getBrand());
        colorsTextView.setText(dataList.get(position).getColors());
        categoriesTextView.setText(dataList.get(position).getCategories());
        priceTextView.setText(String.valueOf(dataList.get(position).getPrice()) + " " + dataList.get(position).getCurrency());

        return convertView;
    }
}
