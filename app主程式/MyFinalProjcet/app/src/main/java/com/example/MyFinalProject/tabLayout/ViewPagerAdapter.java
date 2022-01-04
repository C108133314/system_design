package com.example.MyFinalProject.tabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;
    private ArrayList<ArrayList> productModelArrayList_all = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.productModelArrayList_all = productModelArrayList_all;
    }
    @NonNull @Override public Fragment createFragment(int position) {
        //return CardFragment.newInstance(position, productModelArrayList_all);
        return new CardFragment(position);
    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}