package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.olinestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFinancesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_finances, container,
                                false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setDisplayedUserInfo(firebaseAuth.getUid());
        setDepositButtonAction(firebaseAuth.getUid());
    }


    private void setDisplayedUserInfo(String Uid) {

        fireStore.collection("registeredUsers")
                .document(Uid)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Object userName =
                                            document.getData().get("name");
                                    if (userName != null) {
                                        name.setText(userName.toString());
                                    }
                                    Object userSurname =
                                            document.getData().get("surname");
                                    if (userSurname != null) {
                                        surname.setText(userSurname.toString());
                                    }
                                    Double userBalance =
                                            document.getDouble("balance");
                                    if (userBalance != null) {
                                        balance.setText(userBalance + " USD");
                                    }
                                }
                            }
                        });

    }

    private void setDepositButtonAction(String Uid) {
        depositButton.setOnClickListener(l -> {

            updateUserBalanceBy100(Uid);
        });
    }

    private void updateUserBalanceBy100(String Uid) {
        fireStore
                .collection("registeredUsers")
                .document(Uid)
                .get()
                .addOnCompleteListener(
                        task ->
                        {
                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Double userBalance =
                                            document.getDouble("balance");
                                    if (userBalance != null) {
                                        Double newBalance = userBalance;
                                        newBalance += 100;
                                        setUserBalance(Uid, newBalance);
                                        System.out.println(newBalance);
                                    }
                                }
                            }
                        });
    }

    private void setUserBalance(String Uid, Double newBalance) {
        fireStore
                .collection("registeredUsers")
                .document(Uid)
                .update("balance", newBalance)
                .addOnCompleteListener(
                        task ->
                                balance.setText(
                                        newBalance.toString() + " USD"));
    }

    private void init(@NonNull View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        name = view.findViewById(R.id.userNameTV);
        surname = view.findViewById(R.id.userSurnameTV);
        balance = view.findViewById(R.id.balanceTV);
        depositButton = view.findViewById(R.id.depositButton);
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private TextView name;
    private TextView surname;

    private TextView balance;

    private Button depositButton;


}