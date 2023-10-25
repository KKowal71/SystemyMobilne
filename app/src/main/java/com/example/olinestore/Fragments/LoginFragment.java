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


public class LoginFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

//        continueAsGuestButton.setOnClickListener(view1 -> {
////                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainActivity);
//            startActivity(new Intent(getActivity(), MainActivity.class));
//        });

        closedEyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataVisible(passwordField, isPasswordShown, closedEyeImageView);
                isPasswordShown = !isPasswordShown;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if (areAuthStringsCorrect(email, password)) {
                    authenticate(email, password);
                }
            }
        });
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

    private void authenticate(String email, String password) {
//        firebaseAuth.signOut();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(),
                                       new AuthOnCompleteListener());
    }

    private class AuthOnCompleteListener
            implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                GO TO MAIN ACTIVITY;
                startActivity(
                        new Intent(getActivity(), MainActivity.class).putExtra("isGuest", false));
                Toast.makeText(getActivity(), "LOGIN SUCCESSFUL",
                               Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "LOGIN UNSUCCESSFUL",
                               Toast.LENGTH_LONG).show();
            }
        }
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

    private void init(@NonNull View view) {
        firebaseAuth = FirebaseAuth.getInstance();
//        continueAsGuestButton = view.findViewById(R.id.continueAsGuestButton);
        usernameField = view.findViewById(R.id.usernameField);
        passwordField = view.findViewById(R.id.passwordField);
        loginButton = view.findViewById(R.id.loginButton);
        closedEyeImageView = view.findViewById(R.id.closedEyeImage);
        isPasswordShown = false;
    }

    private FirebaseAuth firebaseAuth;
    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
//    private Button continueAsGuestButton;
    private ImageView closedEyeImageView;
    private boolean isPasswordShown;

}