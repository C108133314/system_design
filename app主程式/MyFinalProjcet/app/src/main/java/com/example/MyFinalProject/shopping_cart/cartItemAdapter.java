package com.example.MyFinalProject.shopping_cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyFinalProject.R;
import com.example.MyFinalProject.img_crawler.imgCrawler;
import com.example.MyFinalProject.productdetail_fragment;
import com.example.MyFinalProject.suggest_product.productModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cartItemAdapter extends RecyclerView.Adapter<cartItemAdapter.Viewholder> {

    private ArrayList<productModel> productModelArrayList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public cartItemAdapter(ArrayList<productModel> productModelArrayList){
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public cartItemAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_item,parent,false);

        return new Viewholder(view, new itemOnClickLIstener() {
            @Override
            public void onClick(int p) {
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
                //initSuggestproduct(dialogView);


                Button add_item_btn = dialogView.findViewById(R.id.add_item_to_cart);
                add_item_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null){
                            Map<String, Object> item = new HashMap<>();
                            productModel target = productModelArrayList.get(p);
                            int count = picker.getValue();
                            String pid = target.getId();
                            item.put("count", count);
                            item.put("pid", pid);
                            item.put("img", target.getSrc());
                            item.put("title", target.getTitle());
                            item.put("intro", target.getIntro());
                            item.put("price", target.getPrice());
                            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
                            mDatabase.collection("userData").document(user.getUid()).collection("shoppingCart").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(view.getContext(),"成功加入購物車", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else
                        {
                            Toast.makeText(view.getContext(),"請先登入帳號", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull cartItemAdapter.Viewholder holder, int position) {
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

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView product_img;
        TextView product_title, procudt_price;

        itemOnClickLIstener lIstener;
        public Viewholder(@NonNull View itemView, itemOnClickLIstener lIstener) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            procudt_price = itemView.findViewById(R.id.parduct_price);
            product_title = itemView.findViewById(R.id.parduct_title);
            itemView.setOnClickListener(this::onClick);
            this.lIstener = lIstener;
        }

        @Override
        public void onClick(View view) {
            lIstener.onClick(this.getLayoutPosition());
        }
    }


    public interface itemOnClickLIstener{
        void onClick(int p);
    }
}
