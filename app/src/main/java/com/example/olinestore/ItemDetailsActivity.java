package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;
import java.util.Map;

public class ItemDetailsActivity extends AppCompatActivity {
    private Button addToBagButton;
    private TextView nameTextView;
    private TextView brandTextView;
    private TextView colorsTextView;
    private TextView priceTextView;
    private TextView categoriesTextView;
    private ImageView itemImageView;
    private Bag bag;
    ListItem item;
    private LinearLayout colorsView;
    private Map<String, Integer> colors =  Map.of("green", Color.parseColor("#008000"), "yellow", Color.parseColor("#FFFF00"), "black", Color.parseColor("#000000"), "white", Color.parseColor("#FFFFFF"),"grey", Color.parseColor("#808080"), "blue", Color.parseColor("#0000FF"), "brown", Color.parseColor("#593611"), "orange", Color.parseColor("#e38a0e"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        bag = Bag.getInstance();
        Intent intent = getIntent();
        item = (ListItem) intent.getSerializableExtra("item");
        nameTextView = findViewById(R.id.itemDetailsName);
        brandTextView = findViewById(R.id.brandDetailsTextView);
        colorsTextView = findViewById(R.id.colorsDetailsTextView);
        colorsView = findViewById(R.id.colorsView);
        priceTextView = findViewById(R.id.itemDetailsPrice);
        categoriesTextView = findViewById(R.id.categoriesDetailsTextView);
        itemImageView = findViewById(R.id.itemDetailsImage);
        nameTextView.setText(item.getName());
        appendText(brandTextView, item.getBrand());
        appendText(categoriesTextView, item.getCategories());
//        appendText(colorsTextView, item.getColors());
        addColorImageView();

        priceTextView.setText(item.getPrice() + " " + item.getCurrency());
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(item.getImagePath());
        Task<byte[]> image = imageRef.getBytes(1024 * 1024);
        image.addOnSuccessListener(byteArray -> {
            if (byteArray == null || byteArray.length == 0) {
                Toast.makeText(ItemDetailsActivity.this, "Image can not be set", Toast.LENGTH_SHORT).show();
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            itemImageView.setImageBitmap(bitmap);
        });

        addToBagButton = findViewById(R.id.addToBagButton);
        addToBagButton.setOnClickListener(v-> {
            bag.addToBag(item);
            Intent toBagIntent = new Intent(ItemDetailsActivity.this, BagActivity.class);
            startActivity(toBagIntent);
        });
    }
    private void appendText(TextView textView, String textToAppend) {
        String text = textView.getText().toString() + ("\n" + textToAppend);
        textView.setText(text);
    }

    private void addColorImageView() {
        String [] itemColors = item.getColors().split(",");
        for(String element :itemColors){
            if(colors.containsKey(element.toLowerCase())){
                ImageView color = new ImageView(this);
                color.setMinimumWidth(100);
                color.setMinimumHeight(100);
                color.setBackgroundColor(colors.get(element.toLowerCase()));
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params1.setMargins(0, 0, 8, 0); // Set right margin for space
                color.setLayoutParams(params1);
                colorsView.addView(color);
            }
        }
//        colors.forEach((key, value) -> {
//            if(Arrays.asList(itemColors).contains(key)){
//                ImageView color = new ImageView(this);
//                color.setBackgroundColor(value);
//                colorsView.addView(color);
//            }
//        });
    }

}