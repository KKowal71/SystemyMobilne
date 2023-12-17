package com.example.olinestore.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olinestore.Bag;
import com.example.olinestore.ItemsAdapter;
import com.example.olinestore.LoginActivity;
import com.example.olinestore.R;
import com.example.olinestore.SummaryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class BagFragment extends Fragment {
    ItemsAdapter adapter;
    ListView bagListView;

    TextView totalAmount;
    Button goToSummaryButton;
    TextView noItemsInfo;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Bag bag = Bag.getInstance();
        noItemsInfo = view.findViewById(R.id.noItemsInfo);
        noItemsInfo.setVisibility(bag.getItemsCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        goToSummaryButton = view.findViewById(R.id.goToSummaryButton);

        goToSummaryButton.setOnClickListener(v -> {
            if(!isUserSignedIn()){
                Toast.makeText(view.getContext(), "Please sign in to buy Your products", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(view.getContext(), LoginActivity.class));
            } else if (bag.getItemsCount() != 0) {
                Intent intent = new Intent(view.getContext(), SummaryActivity.class);
                intent.putExtra("totalAmount", bag.getTotalAmount());
                intent.putExtra("itemsCount", bag.getItemsCount());
                startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "The bag is empty!!", Toast.LENGTH_SHORT).show();
            }
        });
        totalAmount = view.findViewById(R.id.totalAmountTextView);
        adapter = new ItemsAdapter(view.getContext(), bag.getItems(), true, totalAmount);
        bagListView = view.findViewById(R.id.bagListView);
        bagListView.setAdapter(adapter);
        // Format the float value using DecimalFormat
        DecimalFormat decimalFormat = new DecimalFormat("####.####");
        String formattedValue = decimalFormat.format(bag.getTotalAmount());
        totalAmount.setText(formattedValue + " USD");
    }



    private boolean isUserSignedIn() {
        if(firebaseAuth.getCurrentUser() != null) return true;
        else return false;
    }
}