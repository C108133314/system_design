package com.example.MyFinalProject.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.MyFinalProject.suggest_product.productModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Firebase {
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    public Firebase(){
        mDatabase = FirebaseFirestore.getInstance();
        Log.e("Firebase_TEST", "Firebase: " + mDatabase );
    }

    public interface Callback{
        void returnData(ArrayList arrayList);
    }

    public void fetchSuggestData( Callback callback){
        mDatabase.collection("suggest_product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<productModel> productModelArrayList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    callback.returnData(productModelArrayList);

                }
                else {
                    Log.e("test", "onComplete: error" );
                }
            }
        });
    }

    public void fetchBannerData(Callback callback){
        mDatabase.collection("banner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<productModel> productModelArrayList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    callback.returnData(productModelArrayList);

                }
                else {
                    Log.e("test", "onComplete: error" );
                }
            }
        });
    }
    public void fetchChosenProduct(Callback callback){
        mDatabase.collection("chosen_product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<productModel> productModelArrayList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    callback.returnData(productModelArrayList);

                }
                else {
                    Log.e("test", "onComplete: error" );
                }
            }
        });
    }
    public void fetchNewProduct(Callback callback){
        mDatabase.collection("new_product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<productModel> productModelArrayList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    callback.returnData(productModelArrayList);

                }
                else {
                    Log.e("test", "onComplete: error" );
                }
            }
        });
    }
    public void fetchPreorderProduct(Callback callback){
        mDatabase.collection("preorder_product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<productModel> productModelArrayList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    callback.returnData(productModelArrayList);

                }
                else {
                    Log.e("test", "onComplete: error" );
                }
            }
        });
    }
    public void fetchProduct(String target, Callback callback){

        mDatabase.collection(target).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            ArrayList<productModel> productModelArrayList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    callback.returnData(productModelArrayList);

                }
                else {
                    Log.e("test", "onComplete: error" );
                }
            }
        });
    }

    public void fethMyCart(Callback callback){
        FirebaseUser user = mAuth.getCurrentUser();
        ArrayList<productModel> productModelArrayList = new ArrayList<>();
        mDatabase.collection("userData").document(user.getUid()).collection("shoppingCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        productModelArrayList.add(
                                new productModel(
                                        queryDocumentSnapshot.getId(),
                                        queryDocumentSnapshot.getString("img"),
                                        queryDocumentSnapshot.getString("title"),
                                        queryDocumentSnapshot.getString("price"),
                                        queryDocumentSnapshot.getString("intro")));
                    }
                    Log.e("test", "onComplete: cart_item_success" );
                    Log.e("test", productModelArrayList.toString() );
                }
                else{
                    Log.e("test", "onComplete: error" );

                }
                callback.returnData(productModelArrayList);
            }
        });
    }

}
