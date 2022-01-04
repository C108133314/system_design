package com.example.MyFinalProject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class productdetail_fragment extends BottomSheetDialog {
    LayoutInflater inflater;

    public productdetail_fragment(@NonNull Context context) {
        super(context);
    }

    public productdetail_fragment(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected productdetail_fragment(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(getLayoutInflater().inflate(R.layout.productdetail_dialog_layout, null));
    }
}
