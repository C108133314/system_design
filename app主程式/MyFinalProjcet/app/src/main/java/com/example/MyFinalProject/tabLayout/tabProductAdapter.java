package com.example.MyFinalProject.tabLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyFinalProject.R;
import com.example.MyFinalProject.firebase.Firebase;
import com.example.MyFinalProject.img_crawler.imgCrawler;
import com.example.MyFinalProject.productdetail_fragment;
import com.example.MyFinalProject.suggest_product.productAdapter;
import com.example.MyFinalProject.suggest_product.productModel;

import java.util.ArrayList;

public class tabProductAdapter extends RecyclerView.Adapter<tabProductAdapter.Viewholder> {
    private ArrayList<productModel> productModelArrayList;
    public tabProductAdapter(ArrayList<productModel> productModelArrayList){
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public tabProductAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_product_card,parent,false);
        return new Viewholder(view, new itemOnClicker() {
            @Override
            public void OnClick(int p) {
                productdetail_fragment productdetail_fragment = new productdetail_fragment(view.getContext(), R.style.BottomSheetDialog);
                LayoutInflater inflater  = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.productdetail_dialog_layout, null);

                new imgCrawler(dialogView.findViewById(R.id.product_img)).execute(productModelArrayList.get(p).getSrc());
                TextView title =  dialogView.findViewById(R.id.parduct_title);
                title.setText(productModelArrayList.get(p).getTitle());
                TextView price = dialogView.findViewById(R.id.parduct_price);
                String dollar =  view.getResources().getString(R.string.us);
                price.setText(dollar + productModelArrayList.get(p).getPrice());

                TextView intro = dialogView.findViewById(R.id.product_intro);
                intro.setText(productModelArrayList.get(p).getIntro());
                NumberPicker picker = dialogView.findViewById(R.id.numberpicker_main_picker);
                picker.setMaxValue(5);
                picker.setMinValue(1);

                productdetail_fragment.requestWindowFeature(Window.FEATURE_NO_TITLE);
                productdetail_fragment.setContentView(dialogView);
                productdetail_fragment.show();

                initSuggestproduct(dialogView);
            }
        });
    }

    private void initSuggestproduct(View view){


        Firebase firebase = new Firebase();
        firebase.fetchSuggestData(new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {

                RecyclerView recyclerView  = view.findViewById(R.id.suggest_good_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
                productAdapter productAdapter = new productAdapter(arrayList);
                recyclerView.setAdapter(productAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
            }
        });
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        productModel model = productModelArrayList.get(position);
        String dollar =  holder.itemView.getResources().getString(R.string.us);
        holder.product_title.setText(model.getTitle());
        holder.procudt_price.setText(dollar + model.getPrice());
        new imgCrawler(holder.product_img).execute(model.getSrc());
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView product_img;
        TextView product_title, procudt_price;
        itemOnClicker itemOnClicker;

        public Viewholder(@NonNull View itemView, itemOnClicker itemOnClicker) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            procudt_price = itemView.findViewById(R.id.parduct_price);
            product_title = itemView.findViewById(R.id.parduct_title);
            itemView.setOnClickListener(this::onClick);
            this.itemOnClicker = itemOnClicker;
        }

        @Override
        public void onClick(View view) {
            itemOnClicker.OnClick(this.getAdapterPosition());
        }
    }

    public interface itemOnClicker{
        void OnClick(int p);

    }
}
