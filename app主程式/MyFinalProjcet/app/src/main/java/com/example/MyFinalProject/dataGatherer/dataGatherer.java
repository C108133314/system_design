package com.example.MyFinalProject.dataGatherer;

import com.example.MyFinalProject.firebase.Firebase;


import java.util.ArrayList;

public class dataGatherer {





    public ArrayList<ArrayList> getTabProduct(){

        ArrayList<ArrayList> productModelArrayList_all = new ArrayList<>();
        Firebase firebase = new Firebase();
        firebase.fetchNewProduct(new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {
                productModelArrayList_all.add(arrayList);
            }
        });

        firebase.fetchPreorderProduct(new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {
                productModelArrayList_all.add(arrayList);
            }
        });

        firebase.fetchChosenProduct(new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {
                productModelArrayList_all.add(arrayList);
            }
        });



        return productModelArrayList_all;
    }
}
