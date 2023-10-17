package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore fireStore = null;
    Map<String, Object> user = new HashMap<>();

    Button connectButton;
    EditText nameField;
    EditText surnameField;
    Button addUserButton;
    Button userPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireStore = FirebaseFirestore.getInstance();
                connectButton.setEnabled(false);
                Toast.makeText(getApplicationContext(),
                               "connected successfully", Toast.LENGTH_LONG)
                        .show();
            }
        });

        userPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(MainActivity.this, UserPanelActivity.class));

            }
        });

    }

    private void init() {
        setContentView(R.layout.activity_main);
        connectButton = (Button) findViewById(R.id.connectButton);
        nameField = (EditText) findViewById(R.id.nameField);
        surnameField = (EditText) findViewById(R.id.surnameField);
        addUserButton = (Button) findViewById(R.id.addUserButton);
        userPanel = (Button) findViewById(R.id.userPanelButton);
    }
}