package com.example.MyFinalProject.banner_vp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyFinalProject.R;
import com.example.MyFinalProject.img_crawler.imgCrawler;

import java.util.ArrayList;

public class bannerAdapter extends RecyclerView.Adapter<bannerAdapter.Viewholder> {
    private Context context;
    private ArrayList<bannerModel> bannerModelArrayList;

    public bannerAdapter( ArrayList<bannerModel> bannerModelArrayList){
        //this.context = context;
        this.bannerModelArrayList = bannerModelArrayList;
    }

    @NonNull
    @Override
    public bannerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_vp_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        bannerModel model =  bannerModelArrayList.get(position);
        //holder.roundedImageView.setImageDrawable(R.drawable.ic_home_black_24dp);
        //Picasso.get().load(model.getSrc()).into(holder.ImageView);
        new imgCrawler(holder.ImageView)
                .execute(model.getSrc());
    }

    @Override
    public int getItemCount() {
        return bannerModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView ImageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.img_src);
        }
    }
}
