package com.example.olinestore;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;


import com.example.olinestore.Fragments.HistoryFragment;
import com.example.olinestore.Fragments.SettingsFragment;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
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



        settingsImage.setOnClickListener(v -> {
            if (firebaseAuth.getUid() != null) {
                SettingsFragment fragment = new SettingsFragment();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.homeFragment, fragment);
                transaction.commitNow();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        });

        historyImage.setOnClickListener(v -> {
            if (firebaseAuth.getUid() != null) {
                HistoryFragment fragment = new HistoryFragment();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.homeFragment, fragment);
                transaction.commitNow();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        });

    }


    private void init() {
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        welcomeTextView = findViewById(R.id.welcomeTextView);
        accountInfoImage = findViewById(R.id.accountInfoImage);

        searchText = findViewById(R.id.editTextText);
        bagButton = findViewById(R.id.cartButton);
        settingsImage = findViewById(R.id.settingsImage);
        historyImage = findViewById(R.id.historyImage);
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
    private ImageView settingsImage;
    private ImageView historyImage;

    private FloatingActionButton bagButton;

    private FragmentManager fm = getSupportFragmentManager();


}