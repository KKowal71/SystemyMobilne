package com.example.olinestore;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getUid() != null) {
                setupHiTextForUser(firebaseAuth.getUid());
            } else {
                welcomeTextView.setText("eShopXpress");
            }
        });

        bagButton.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, BagActivity.class));
        });

        accountInfoImage.setOnClickListener(v -> {
            if (firebaseAuth.getUid() == null) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), UserPanelActivity.class));
            }
        });
        ArrayList<ListItem> itemList = new ArrayList<>();

        // Create an ArrayAdapter to bind the data to the ListView
        ArrayAdapter<ListItem> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item,
                R.id.textView,
                itemList
        );
        button.setOnClickListener(v -> {
//            itemList.add(new ListItem("nowy itemek"));
            adapter.notifyDataSetChanged();
        });

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 5) {
                    System.out.println(s);
                }
            }
        });
        button.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AllItemsActivity.class));
        });
    }


    private void init() {
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        welcomeTextView = findViewById(R.id.welcomeTextView);
        accountInfoImage = findViewById(R.id.accountInfoImage);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.allItemsButton);
        searchText = findViewById(R.id.editTextText);
        bagButton = findViewById(R.id.cartButton);
    }

    private void setupHiTextForUser(String Uid) {
        firestore.collection("registeredUsers").document(Uid).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Object name = document.getData().get("name");
                            if (name != null) {
                                welcomeTextView.setText(
                                        "Hi, " + name);
                            }
                        }
                    }
                });
    }

    private TextView welcomeTextView;
    private EditText searchText;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ImageView accountInfoImage;
    private ListView listView;
    private Button button;
    private FloatingActionButton bagButton;


}