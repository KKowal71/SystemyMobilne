package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.olinestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SearchFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcomeTextView = view.findViewById(R.id.welcomeTextView);
        searchText = view.findViewById(R.id.editTextText);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        fm = getChildFragmentManager();
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getUid() != null) {
                setupHiTextForUser(firebaseAuth.getUid());
            } else {
                welcomeTextView.setText("eShopXpress");
            }
        });
    }

    private void setupHiTextForUser(String Uid) {
        firestore.collection("registeredUsers").document(Uid).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Object name = document.getData().get("name");
                            if (name != null) {
                                welcomeTextView.setText(
                                        "Hi, " + name);
                            }
                        }
                    }
                });
    }

    public void changeFragment() {
        try {
            HomeFragment possibleFragment =
                    (HomeFragment) fm.findFragmentById(
                            R.id.SearchFragmentContainer);

            AllItemsFragment fragment = new AllItemsFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.SearchFragmentContainer, fragment);
            transaction.commitNow();
        } catch (ClassCastException e) {
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.SearchFragmentContainer, fragment);
            transaction.commitNow();
        }
    }

    public EditText getSearchText() {
        return searchText;
    }
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private TextView welcomeTextView;
    private EditText searchText;

    private FragmentManager fm;
}