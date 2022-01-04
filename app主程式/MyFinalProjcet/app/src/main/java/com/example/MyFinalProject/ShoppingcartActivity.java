package com.example.MyFinalProject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.MyFinalProject.firebase.Firebase;
import com.example.MyFinalProject.suggest_product.productAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShoppingcartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);
        initSuggestproduct();



    }



    @Override
    protected void onStart() {
        super.onStart();
    }
    private void initSuggestproduct(){


        Firebase firebase = new Firebase();
        firebase.fetchSuggestData(new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {

                RecyclerView recyclerView  = findViewById(R.id.shoppingcart_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                productAdapter productAdapter = new productAdapter(arrayList);
                recyclerView.setAdapter(productAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
            }
        });

    }

}