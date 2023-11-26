package com.example.olinestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if(firebaseAuth.getUid() != null) {
            firestore.collection("registeredUsers").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            welcomeTextView.setText("Hi, " + document.getData().get("name").toString());

                        }
                    }
                }
            });
        } else {
            welcomeTextView.setText("eShopXpress");
        }
        accountInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(firebaseAuth.getCurrentUser());
                if(firebaseAuth.getUid() == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else{
                    startActivity(new Intent(getApplicationContext(), UserPanelActivity.class));
                }
//                FirebaseAuth
//                if(firebaseAuth.getCurrentUser() == null) {
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                } else {
//                    startActivity(new Intent(getApplicationContext(), UserFinancesFragment.class));
//                }
//
//                firebaseAuth.auth().onAuthStateChanged(function(user) {
//                    if (user) {
//                        // User is signed in.
//                    } else {
//                        // No user is signed in.
//                    }
//                });
            }
        });
        ArrayList<ListItem> itemList = new ArrayList<>();
        itemList.add(new ListItem("Item 1"));
        itemList.add(new ListItem("Item 2"));
        itemList.add(new ListItem("Item 3"));

        // Create an ArrayAdapter to bind the data to the ListView
        ArrayAdapter<ListItem> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item,
                R.id.textView,
                itemList
        );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.add(new ListItem("nowy itemek"));
                adapter.notifyDataSetChanged();
            }
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
                if(s.length() >= 5){
                    System.out.println(s);
                }
            }
        });
    }


    private void init() {
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        welcomeTextView = findViewById(R.id.welcomeTextView);
        accountInfoImage = findViewById(R.id.accountInfoImage);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button6);
        searchText = findViewById(R.id.editTextText);
    }

    private TextView welcomeTextView;
    private EditText searchText;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ImageView accountInfoImage;
    private ListView listView;
    private Button button;


}