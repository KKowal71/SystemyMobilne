package com.example.olinestore.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.olinestore.MainActivity;
import com.example.olinestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserPanelFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_panel, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);


        setUpLogOutButtonOnClickInfo();

        changeAddressButton.setOnClickListener(l -> {
            switchDeliveryAddresForm();
        });
        updateUserAddress();
    };


    private void initialize(@NonNull View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        logOutButton = (ImageView) view.findViewById(R.id.logOutButton);
        addressInfoTV = view.findViewById(R.id.textInfo);
        currentAddressTV = view.findViewById(R.id.CurrentDeliveryAddress);
        changeAddressButton = view.findViewById(R.id.changeButton);
        deliveryFragment = view.findViewById(R.id.fragmentContainerView3);

    }



    private void setUpLogOutButtonOnClickInfo() {
        logOutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(getContext(),
                           "Logged out successfully", Toast.LENGTH_SHORT)
                    .show();
            ((MainActivity) getActivity()).goToHome();
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
    private View deliveryFragment;

    private Button changeAddressButton;
    private TextView addressInfoTV;
    private TextView currentAddressTV;
}