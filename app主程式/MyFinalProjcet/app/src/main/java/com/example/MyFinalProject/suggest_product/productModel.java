package com.example.MyFinalProject.suggest_product;

import android.os.Parcel;
import android.os.Parcelable;

public class productModel implements Parcelable {
    private String src,title,price, intro, id;

    public productModel(){

    }

    public productModel(String id,String src, String title , String price, String intro){
        this.price = price;
        this.title = title;
        this.src = src;
        this.intro = intro;
        this.id = id;

    }

    protected productModel(Parcel in) {
        src = in.readString();
        title = in.readString();
        price = in.readString();
        intro = in.readString();
        id = in.readString();
    }

    public static final Creator<productModel> CREATOR = new Creator<productModel>() {
        @Override
        public productModel createFromParcel(Parcel in) {
            return new productModel(in);
        }

        @Override
        public productModel[] newArray(int size) {
            return new productModel[size];
        }
    };

    public String getSrc() {
        return src;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getIntro() {
        return intro;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(src);
        parcel.writeString(title);
        parcel.writeString(price);
        parcel.writeString(intro);
        parcel.writeString(id);
    }
}

