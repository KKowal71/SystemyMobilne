package com.example.olinestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

        closedEyeImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataVisible(passwordField, isPasswordShown1, closedEyeImage1);
                isPasswordShown1 = !isPasswordShown1;
            }
        });

        closedEyeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataVisible(confirmPasswordField, isPasswordShown2, closedEyeImage2);
                isPasswordShown2 = !isPasswordShown2;
            }
        });
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
        closedEyeImage1 = view.findViewById(R.id.closedEyeImage1);
        closedEyeImage2 = view.findViewById(R.id.closedEyeImage2);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        isPasswordShown1 = false;
        isPasswordShown2 = false;
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

    private void isDataVisible(EditText field, boolean check, ImageView icon) {
        if (check) {
            field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            field.setSelection(passwordField.getText().length());
            icon.setImageResource(R.drawable.closed_eye);
        } else {
            field.setInputType(InputType.TYPE_CLASS_TEXT);
            field.setSelection(passwordField.getText().length());
            icon.setImageResource(R.drawable.baseline_open_eye_24);
        }
    }

    private class AuthOnCompleteListener
            implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                addUserDataToFirestore();

                getActivity().onBackPressed();
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
    private ImageView closedEyeImage1;
    private ImageView closedEyeImage2;
    private boolean isPasswordShown1;
    private boolean isPasswordShown2;
}