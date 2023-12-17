package com.example.olinestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olinestore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// CustomAdapter.java
public class ItemsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> dataList;
    private ArrayList<ListItem> filteredDataList;
    private boolean isBagView;
    private TextView totalAmount;


    public ItemsAdapter(Context context, ArrayList<ListItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public ItemsAdapter(Context context, ArrayList<ListItem> dataList, boolean isBagView, TextView totalAmount) {
        this.context = context;
        this.dataList = dataList;
        this.isBagView = isBagView;
        this.totalAmount = totalAmount;
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
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        TextView sizeTextView = convertView.findViewById(R.id.sizeTextView);
        TextView amountTextNumber = convertView.findViewById(R.id.amountTextNumber);
        ImageView addAmountButton = convertView.findViewById(R.id.addAmountButton);

        amountTextNumber.setText(String.valueOf(dataList.get(position).getAmount()));
        nameTextView.setText(dataList.get(position).getName());
        brandTextView.setText(dataList.get(position).getBrand());
        priceTextView.setText(String.valueOf(dataList.get(position).getPrice()) + " " + dataList.get(position).getCurrency());
        Bag bag = Bag.getInstance();
        ImageView removeItemButton = convertView.findViewById(R.id.removeItemButton);
        removeItemButton.setOnClickListener(v->{
            bag.removeFromBag(dataList.get(position));
            updateBag(bag);
            this.notifyDataSetChanged();
        });
        addAmountButton.setOnClickListener(l -> {
            int val = dataList.get(position).getAmount();
            val += 1;
            dataList.get(position).setAmount(val);
            amountTextNumber.setText(String.valueOf(val));
            updateBag(bag);
        });
        ImageView removeAmountButton = convertView.findViewById(R.id.removeAmountButton);
        removeAmountButton.setOnClickListener(l -> {
            int val = dataList.get(position).getAmount();
            if (val > 1) {
                val -= 1;
                dataList.get(position).setAmount(val);
                amountTextNumber.setText(String.valueOf(val));
                updateBag(bag);
            }
        });
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(dataList.get(position).getImagePath());
        Task<byte[]> image = imageRef.getBytes(1024 * 1024 * 5);
        image.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] byteArray) {
                // Convert byte array to Bitmap
                if (byteArray == null || byteArray.length == 0) {

                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                itemImageView.setImageBitmap(bitmap);
            }
        });
        if(isBagView) {
            sizeTextView.setVisibility(sizeTextView.VISIBLE);
            amountTextNumber.setVisibility(sizeTextView.VISIBLE);
            addAmountButton.setVisibility(sizeTextView.VISIBLE);
            removeItemButton.setVisibility(View.VISIBLE);
            removeAmountButton.setVisibility(sizeTextView.VISIBLE);
            sizeTextView.setText("Size: " + dataList.get(position).getSize());
        }

        return convertView;
    }
    private void updateBag(Bag bag) {
        bag.calculateTotalAmount();
        DecimalFormat decimalFormat = new DecimalFormat("####.####");
        String formattedValue = decimalFormat.format(bag.getTotalAmount());
        totalAmount.setText(formattedValue + " USD");
    }




}
