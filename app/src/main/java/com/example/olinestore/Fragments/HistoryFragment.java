package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.olinestore.HistoryItemAdapter;
import com.example.olinestore.ItemsAdapter;
import com.example.olinestore.ListItem;
import com.example.olinestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getShoppingHistory();
    }

    private void init(@NonNull View view) {
        itemsListView = view.findViewById(R.id.historyListView);
        historyItemList = new ArrayList<>();
        adapter = new HistoryItemAdapter(getActivity(), historyItemList);
        itemsListView.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    private void getShoppingHistory() {
        String uid = firebaseAuth.getUid();
        firestore
                .collection("History")
                .get()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        for (DocumentSnapshot doc: documents) {
                            if (doc.getString("uID").equals(uid)) {
                                String date = doc.getString("date");
                                String nrOfItems = doc.getString("nrOfItems");
                                String orderPrice = doc.getString("orderPrice");
                                String[] tab = {date, nrOfItems, orderPrice};
                                historyItemList.add(tab);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String[]> historyItemList;
    HistoryItemAdapter adapter;
    private ListView itemsListView;
}