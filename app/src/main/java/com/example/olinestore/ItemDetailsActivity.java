package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemDetailsActivity extends AppCompatActivity {
    Button addToBagButton;
    TextView nameTextView;
    TextView brandTextView;
    TextView colorsTextView;
    TextView priceTextView;
    TextView categoriesTextView;
    ImageView itemImageView;
    Bag bag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        bag = Bag.getInstance();
        System.out.println(bag.getText());
        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        String brand = intent.getStringExtra("brand");
//        String price = intent.getStringExtra("price");
//        String categories = intent.getStringExtra("categories");
//        String colors = intent.getStringExtra("colors");
//        String imagePath = intent.getStringExtra("imagePath");
        ListItem item = (ListItem) intent.getSerializableExtra("item");
        nameTextView = findViewById(R.id.itemDetailsName);
        brandTextView = findViewById(R.id.brandDetailsTextView);
        colorsTextView = findViewById(R.id.colorsDetailsTextView);
        priceTextView = findViewById(R.id.itemDetailsPrice);
        categoriesTextView = findViewById(R.id.categoriesDetailsTextView);
        itemImageView = findViewById(R.id.itemDetailsImage);
        nameTextView.setText(item.getName());
        appendText(brandTextView, item.getBrand());
        appendText(categoriesTextView, item.getCategories());
        appendText(colorsTextView, item.getColors());
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

}