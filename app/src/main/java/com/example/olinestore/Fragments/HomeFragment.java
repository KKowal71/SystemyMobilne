package com.example.olinestore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.example.olinestore.ListItem;
import com.example.olinestore.R;
import com.google.android.gms.actions.ItemListIntents;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;


public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


        getDataFromFirestore("Shoes");
    }

    private void init(@NonNull View view) {
        initPopular(view);
        initVisited(view);
        firestore = FirebaseFirestore.getInstance();
        popularIl = new ArrayList<>();
        visitedIl = new ArrayList<>();
        il = new ArrayList<>();
    }

    private void initPopular(@NonNull View view) {
        popularFragments = new FragmentContainerView[5];
        popularFragments[0] = view.findViewById(R.id.popularItemFragment1);
        popularFragments[1] = view.findViewById(R.id.popularItemFragment2);
        popularFragments[2] = view.findViewById(R.id.popularItemFragment3);
        popularFragments[3] = view.findViewById(R.id.popularItemFragment4);
        popularFragments[4] = view.findViewById(R.id.popularItemFragment5);
    }

    private void initVisited(@NonNull View view) {
        lastVisitedFragments = new FragmentContainerView[5];
        lastVisitedFragments[0] = view.findViewById(R.id.visitedItemFragment1);
        lastVisitedFragments[1] = view.findViewById(R.id.visitedItemFragment2);
        lastVisitedFragments[2] = view.findViewById(R.id.visitedItemFragment3);
        lastVisitedFragments[3] = view.findViewById(R.id.visitedItemFragment4);
        lastVisitedFragments[4] = view.findViewById(R.id.visitedItemFragment5);
    }

    private void getDataFromFirestore(String categoryName) {
        firestore.collection(categoryName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                String name =
                                        (String) document.getData().get("name");
                                String path =
                                        (String) document.getData().get("img");
                                path = "images/" + path;
                                String[] tab = {name, path};
                                il.add(tab);
                            } catch (NumberFormatException e) {
                            }
                        }
                        Random rand = new Random();
                        for (int i = 0; i < popularFragments.length +
                                lastVisitedFragments.length; i++) {
                            int randomNumber = rand.nextInt(il.size());

                            if (i < popularFragments.length) {
                                popularIl.add(il.get(randomNumber));
                            } else {
                                visitedIl.add(il.get(randomNumber));
                            }
                        }
                        showItems();

                    }
                });

    }


    public void getallDataFromStorage() {
        firestore.collection("categories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        String categoryName =
                                (String) document.getData().get("name");
                        if (!categoryName.isEmpty()) {
                            getDataFromFirestore(categoryName);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }

                }

            }
        });
    }

    private void showItems() {
        showPopular();
        showVisited();
    }

    private void showPopular() {
        try {
            for (int i = 0; i < popularIl.size(); i++) {
                ((ItemFragment) popularFragments[i].getFragment()).setName(
                        popularIl.get(i)[0]);
                StorageReference ref = FirebaseStorage.getInstance()
                        .getReference(popularIl.get(i)[1]);
                ((ItemFragment) popularFragments[i].getFragment()).setImg(ref);
            }
        } catch (Exception e) {

        }
    }

    private void showVisited() {
        try {
            for (int i = 0; i < visitedIl.size(); i++) {
                ((ItemFragment) lastVisitedFragments[i].getFragment()).setName(
                        visitedIl.get(i)[0]);
                StorageReference ref = FirebaseStorage.getInstance()
                        .getReference(visitedIl.get(i)[1]);
                ((ItemFragment) lastVisitedFragments[i].getFragment()).setImg(
                        ref);
            }
        } catch (Exception e) {

        }


    }

    ArrayList<String[]> popularIl;
    ArrayList<String[]> visitedIl;
    ArrayList<String[]> il;
    private FirebaseFirestore firestore;
    private FragmentContainerView[] popularFragments;
    private FragmentContainerView[] lastVisitedFragments;

}

