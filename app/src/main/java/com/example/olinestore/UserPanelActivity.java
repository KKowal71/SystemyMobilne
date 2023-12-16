package com.example.olinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        setUpBackButtonOnClick();
        setUpLogOutButtonOnClickInfo();

        changeAddressButton.setOnClickListener(l -> {
            switchDeliveryAddresForm();
        });
        updateUserAddress();
    }

    private void initialize() {
        setContentView(R.layout.activity_user_panel);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        logOutButton = (ImageView) findViewById(R.id.logOutButton);
        backButton = findViewById(R.id.backButton);
        addressInfoTV = findViewById(R.id.textInfo);
        currentAddressTV = findViewById(R.id.CurrentDeliveryAddress);
        changeAddressButton = findViewById(R.id.changeButton);
        deliveryFragment = findViewById(R.id.fragmentContainerView3);

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

    public void switchDeliveryAddresForm() {
        if (!isFormShowed) {

            currentAddressTV.setVisibility(View.INVISIBLE);
            changeAddressButton.setVisibility(View.INVISIBLE);
            addressInfoTV.setVisibility(View.INVISIBLE);
            deliveryFragment.setVisibility(View.VISIBLE);
            isFormShowed = true;
        } else {
            updateUserAddress();
            currentAddressTV.setVisibility(View.VISIBLE);
            changeAddressButton.setVisibility(View.VISIBLE);
            addressInfoTV.setVisibility(View.VISIBLE);
            deliveryFragment.setVisibility(View.INVISIBLE);
            isFormShowed = false;
        }
    }

    private void updateUserAddress() {
        firestore
                .collection("registeredUsers")
                .document(firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(
                        task ->
                        {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String address =
                                            document.getString("address");
                                    if (address == null || address.isEmpty()) {
                                        currentAddressTV.setText("Please add delivery address");
                                    } else {
                                        currentAddressTV.setText(address);
                                    }
                                }
                            }
                        });

    }

    private FirebaseFirestore firestore;
    private boolean isFormShowed = false;

    private ImageView logOutButton;
    private FirebaseAuth firebaseAuth;
    private ImageButton backButton;

    private View deliveryFragment;

    private Button changeAddressButton;
    private TextView addressInfoTV;
    private TextView currentAddressTV;
}