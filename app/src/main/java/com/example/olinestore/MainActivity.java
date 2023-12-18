package com.example.olinestore;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;


import com.example.olinestore.Fragments.BagFragment;
import com.example.olinestore.Fragments.HistoryFragment;
import com.example.olinestore.Fragments.SearchFragment;
import com.example.olinestore.Fragments.SettingsFragment;
import com.example.olinestore.Fragments.UserPanelFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        bagButton.setOnClickListener(v->{
            BagFragment fragment = new BagFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.ShownFragment, fragment);
            transaction.commitNow();
        });

        accountInfoImage.setOnClickListener(v -> {
            if (firebaseAuth.getUid() != null) {
                UserPanelFragment fragment = new UserPanelFragment();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.ShownFragment, fragment);
                transaction.commitNow();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



        settingsImage.setOnClickListener(v -> {
            SettingsFragment fragment = new SettingsFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.ShownFragment, fragment);
            transaction.commitNow();
        });

        historyImage.setOnClickListener(v -> {
            if (firebaseAuth.getUid() != null) {
                HistoryFragment fragment = new HistoryFragment();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.ShownFragment, fragment);
                transaction.commitNow();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        homeImage.setOnClickListener(v ->  goToHome());
    }


    private void init() {
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        accountInfoImage = findViewById(R.id.accountInfoImage);

        bagButton = findViewById(R.id.cartButton);
        settingsImage = findViewById(R.id.settingsImage);
        historyImage = findViewById(R.id.historyImage);
        homeImage = findViewById(R.id.homeImage);
    }

    public void goToHome() {
        try {
            SearchFragment possibleFragment =
                    (SearchFragment) fm.findFragmentById(
                            R.id.ShownFragment);
            possibleFragment.setHomeFragment();
        } catch (ClassCastException e) {
            SearchFragment fragment = new SearchFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.ShownFragment, fragment);
            transaction.commitNow();
        } catch (Exception e) {

        }
    }

    private FirebaseAuth firebaseAuth;

    private ImageView accountInfoImage;
    private ImageView settingsImage;
    private ImageView historyImage;

    private ImageView homeImage;

    private FloatingActionButton bagButton;

    private FragmentManager fm = getSupportFragmentManager();


}