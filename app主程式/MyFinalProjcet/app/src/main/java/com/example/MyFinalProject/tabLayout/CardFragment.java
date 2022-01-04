package com.example.MyFinalProject.tabLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyFinalProject.R;
import com.example.MyFinalProject.firebase.Firebase;

import java.util.ArrayList;

public class CardFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private ArrayList<ArrayList> productModelArrayList_all;

    private String[] item = {"new_product","preorder_product","chosen_product"};

    public CardFragment(Integer counter) {
        // Required empty public constructor
        //this.productModelArrayList_all = productModelArrayList_all;
        this.counter = counter;
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);


    }
    public static void newInstance(Integer counter, ArrayList<ArrayList> productModelArrayList_all) {
/*
        CardFragment fragment = new CardFragment(productModelArrayList_all);
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);

        fragment.setArguments(args);
        return fragment;
        */

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
        //TextView textViewCounter = view.findViewById(R.id.tv_counter);
        //textViewCounter.setText("Fragment No " + (counter+1));

        Firebase firebase = new Firebase();
        firebase.fetchProduct(item[counter], new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {

                RecyclerView recyclerView  = view.findViewById(R.id.tab_recyclerview);
                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));
                tabProductAdapter tabProductAdapter = new tabProductAdapter(arrayList);
                recyclerView.setAdapter(tabProductAdapter);
            }
        });

    }
}