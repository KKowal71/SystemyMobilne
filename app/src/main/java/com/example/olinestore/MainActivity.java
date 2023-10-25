package com.example.olinestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.olinestore.Fragments.UserFinancesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
    }

    private void init() {
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        welcomeTextView = findViewById(R.id.welcomeTextView);
        accountInfoImage = findViewById(R.id.accountInfoImage);
    }

    private TextView welcomeTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ImageView accountInfoImage;


}