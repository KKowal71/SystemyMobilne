package com.example.olinestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class HistoryItemAdapter extends BaseAdapter {

    public HistoryItemAdapter(Context context, ArrayList<String[]> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.history_list_item, null);
        }
        TextView dateTV = view.findViewById(R.id.Datetext);
        TextView itemCountTV = view.findViewById(R.id.ItemCount);
        TextView pricePayedTV = view.findViewById(R.id.PricePayed);
        dateTV.setText(dataList.get(i)[0]);
        itemCountTV.setText(dataList.get(i)[1]);
        pricePayedTV.setText(dataList.get(i)[2]);

        return view;
    }

    private ArrayList<String[]> dataList;
    private Context context;
}