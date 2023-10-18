package com.example.olinestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.olinestore.MainActivity;
import com.example.olinestore.R;
import com.example.olinestore.UserPanelActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {


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
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (areAuthStringsCorrect(email, password)) {
                    authenticate(email, password);
                }
            }
        });
    }

    private void init(@NonNull View view) {
        nameField = view.findViewById(R.id.nameField);
        surnameField = view.findViewById(R.id.surnameField);
        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);
        confirmPasswordField = view.findViewById(R.id.confirmPasswordField);
        dateOfBirthField = view.findViewById(R.id.birthdayField);
        registerButton = view.findViewById(R.id.registerButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void authenticate(String email, String password) {
        firebaseAuth.signOut();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(),
                                       new AuthOnCompleteListener());

    }

    private boolean areAuthStringsCorrect(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter email",
                           Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter password",
                           Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private class AuthOnCompleteListener
            implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                addUserDataToFirestore();
//                GO TO MAIN ACTIVITY;
                startActivity(
                        new Intent(getActivity(), MainActivity.class));
                Toast.makeText(getActivity(), "REIGSTER SUCCESSFUL",
                               Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "REIGSTER NOT SUCCESSFUL",
                               Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addUserDataToFirestore() {
        Map<String, Object> user = getUserInputMap();
        String userID = firebaseAuth.getUid();
        firestore.collection(userCollectionName).document(userID).set(user)
                .addOnCompleteListener(
                        task -> Toast.makeText(getContext(), "USER CREATED",
                                       Toast.LENGTH_LONG).show());
    }

    private Map<String, Object> getUserInputMap() {
        Map<String, Object> user = new HashMap<>();

        String email = emailField.getText().toString();
        user.put("email", email);

        String name = nameField.getText().toString();
        user.put("name", name);

        String surname = surnameField.getText().toString();
        user.put("surname", surname);

        String birthday = dateOfBirthField.getText().toString();
        user.put("birthday", birthday);
        return user;
    }

    private String userCollectionName = "registeredUsers";

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private EditText emailField;
    private EditText nameField;
    private EditText surnameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText dateOfBirthField;
    private Button registerButton;
}