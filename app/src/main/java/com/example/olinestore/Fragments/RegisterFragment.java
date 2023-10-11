package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.olinestore.R;


public class RegisterFragment extends Fragment {
    private EditText emailField;
    private EditText nameField;
    private EditText surnameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText dateOfBirthField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameField = view.findViewById(R.id.nameField);
        surnameField = view.findViewById(R.id.surnameField);
        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);
        confirmPasswordField = view.findViewById(R.id.confirmPasswordField);
        dateOfBirthField = view.findViewById(R.id.birthdayField);


    }
}