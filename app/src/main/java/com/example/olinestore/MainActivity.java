package com.example.olinestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        setContentView(R.layout.activity_main);
        connectButton = (Button) findViewById(R.id.connectButton);
        nameField = (EditText) findViewById(R.id.nameField);
        surnameField = (EditText) findViewById(R.id.surnameField);
        addUserButton = (Button) findViewById(R.id.addUserButton);
        userPanel = (Button) findViewById(R.id.userPanelButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireStore = FirebaseFirestore.getInstance();
                if (fireStore != null) {
                    connectButton.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "connected successfully", Toast.LENGTH_LONG).show();
                    fireStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<String> list = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list.add(document.getData().toString());
                                }
                                System.out.println(list.toString());
                            } else {
                                System.out.println("error");
                            }
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "connected error", Toast.LENGTH_LONG).show();
                }
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameField.getText().toString();
                String surname = surnameField.getText().toString();
                user.put("name", name);
                user.put("surname", surname);

                fireStore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "user added", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                });
                nameField.setText("");
                surnameField.setText("");
            }
        });
        userPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserPanelActivity.class));

            }
        });

    }
}