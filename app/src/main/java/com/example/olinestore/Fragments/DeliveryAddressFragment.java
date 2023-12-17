package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.olinestore.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.StringJoiner;


public class DeliveryAddressFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_address, container,
                                false);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setCancellButtonAction();
        setApplyButtonFunction();

    }

    private void init(@NonNull View view) {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        cancellButton = view.findViewById(R.id.CancelButton);
        applyButton = view.findViewById(R.id.ApplyButton);
        streetInput = view.findViewById(R.id.StreetAndNumber);
        postalCodeInput = view.findViewById(R.id.PostalCode);
        cityInput = view.findViewById(R.id.City);

    }

    private void setCancellButtonAction() {
        cancellButton.setOnClickListener(l -> {

            clearInputs();
            ((UserPanelFragment) getParentFragment()).switchDeliveryAddresForm();
        });
    }

    private void clearInputs() {
        streetInput.getEditText().setText("");
        postalCodeInput.getEditText().setText("");
        cityInput.getEditText().setText("");
    }

    private void setApplyButtonFunction() {
        applyButton.setOnClickListener(l -> {
            setUserAddress(firebaseAuth.getUid());
        });
    }

    private void setUserAddress(String Uid) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(streetInput.getEditText().getText())
                .add(postalCodeInput.getEditText().getText())
                .add(cityInput.getEditText().getText());
        String newAddress = joiner.toString();
        firestore
                .collection("registeredUsers")
                .document(Uid)
                .update("address", newAddress)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),
                                               "Delivery address successfully updated",
                                               Toast.LENGTH_SHORT)
                                        .show();
                                clearInputs();
                                ((UserPanelFragment) getParentFragment()).switchDeliveryAddresForm();
                            }

                        });
    }


    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    private Button cancellButton;

    private Button applyButton;
    private TextInputLayout streetInput;
    private TextInputLayout postalCodeInput;
    private TextInputLayout cityInput;
}