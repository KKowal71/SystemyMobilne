package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UserPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        setUpBackButtonOnClick();
        setUpLogOutButtonOnClickInfo();
    }

    private void initialize() {
        setContentView(R.layout.activity_user_panel);
        logOutButton = (ImageView) findViewById(R.id.logOutButton);
        backButton = findViewById(R.id.backButton);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setUpBackButtonOnClick() {
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setUpLogOutButtonOnClickInfo() {
        logOutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(getApplicationContext(),
                           "Logged out successfully", Toast.LENGTH_SHORT)
                    .show();
            finish();
        });
    }

    private ImageView logOutButton;
    private FirebaseAuth firebaseAuth;
    private ImageButton backButton;
}