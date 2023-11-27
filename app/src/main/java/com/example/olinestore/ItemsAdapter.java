package com.example.olinestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
//        TextView colorsTextView = convertView.findViewById(R.id.colorsTextView);
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
//        TextView categoriesTextView = convertView.findViewById(R.id.categoryTextView);
        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        nameTextView.setText(dataList.get(position).getName());
        brandTextView.setText(dataList.get(position).getBrand());
//        colorsTextView.setText(dataList.get(position).getColors());
//        categoriesTextView.setText(dataList.get(position).getCategories());
        priceTextView.setText(String.valueOf(dataList.get(position).getPrice()) + " " + dataList.get(position).getCurrency());


        StorageReference imageRef = FirebaseStorage.getInstance().getReference(dataList.get(position).getImagePath());
        Task<byte[]> image = imageRef.getBytes(1024 * 1024);
        image.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] byteArray) {
                // Convert byte array to Bitmap
                if (byteArray == null || byteArray.length == 0) {

                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                itemImageView.setImageBitmap(bitmap);

                // Use the Bitmap as needed (e.g., display in ImageView)
                // For example, assuming you have an ImageView named imageView:
                // imageView.setImageBitmap(bitmap);
            }
        });






        return convertView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
        }
    }
}
