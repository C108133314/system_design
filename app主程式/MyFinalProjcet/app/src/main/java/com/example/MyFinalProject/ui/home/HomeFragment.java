package com.example.MyFinalProject.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.MyFinalProject.R;
import com.example.MyFinalProject.banner_vp2.bannerAdapter;
import com.example.MyFinalProject.banner_vp2.bannerModel;
import com.example.MyFinalProject.databinding.FragmentHomeBinding;
import com.example.MyFinalProject.firebase.Firebase;
import com.example.MyFinalProject.shopping_cart.cartItemAdapter;
import com.example.MyFinalProject.suggest_product.productAdapter;
import com.example.MyFinalProject.suggest_product.productModel;
import com.example.MyFinalProject.tabLayout.ViewPagerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ArrayList<bannerModel> bannerModelArrayList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseFirestore mDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Firebase.fetchSuggestData();
        ViewPager2 bannerVp = view.findViewById(R.id.banner_vp);
        bannerModelArrayList = new ArrayList<>();
        bannerModelArrayList.add(new bannerModel("https://im1.book.com.tw/image/getImage?i=https://addons.books.com.tw/G/ADbanner/marketing/2021/09/55555/05_950x250.jpg&v=615c2583&w=&h="));
        bannerModelArrayList.add(new bannerModel("https://im1.book.com.tw/image/getImage?i=https://addons.books.com.tw/G/ADbanner/2021/10/seriesbook_950x350.gif&v=6184ab96&w=&h="));
        bannerModelArrayList.add(new bannerModel("https://im1.book.com.tw/image/getImage?i=https://www.books.com.tw/G/ADbanner/2021/10/EBOOK/yuanliou_790250.jpg&v=6178d501&w=&h="));
        bannerAdapter bannerAdapter = new bannerAdapter( bannerModelArrayList);
        LinearLayoutManager linearLayoutManager_banner = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false);
        bannerVp.setAdapter(bannerAdapter);
        Log.e("onviewcreated", "good");

        bannerVp.setClipToPadding(false);
        bannerVp.setClipChildren(false);
        bannerVp.setOffscreenPageLimit(4);
        bannerVp.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer( 25));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

            }
        });

        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        bannerVp.setPageTransformer(compositePageTransformer);

        initSuggestproduct(view);
        initproduct(view);

        FirebaseUser user = mAuth.getCurrentUser();
        ImageView imageView = getView().findViewById(R.id.shoppingcart_btn);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(user != null){

                    showBottomsheet();
                }
                else{

                    Toast.makeText(view.getContext(),"請先登入帳號", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void  showBottomsheet(){
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.bottomsheet_layout);
        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //dialog.getWindow().setGravity(Gravity.BOTTOM);

        ArrayList<productModel> productModelArrayList = new ArrayList<productModel>();
        productModelArrayList.add(new productModel("test","https://im2.book.com.tw/image/getImage?i=https://www.books.com.tw/img/001/090/76/0010907642_bc_01.jpg&v=6181218f&w=348&h=348","致未來的男孩們：掙脫「男子氣概」的枷鎖【作者親簽版】","550","產品內容物與規格說明/保固資訊\n" +
                "中文品名：ORIGINS 品木宣言 泥娃娃活性碳面膜(75ml)-國際航空版\n" +
                "規格：75ml\n" +
                "◆清潔毛孔油脂及汙垢\n" +
                "◆毛孔深層淨化\n" +
                "◆黑頭白頭粉刺剋星！\n" +
                "\n" +
                "★使用方式：臉部清潔後，適量塗抹於臉部肌膚，避開眼睛四周，可搭配按摩，靜候10分鐘後，再用清水洗淨。\n" +
                "★保存方法：請置於陰涼處，請勿直接陽光照射，以免變質。\n" +
                "☆溫馨提醒：\n" +
                "✔本產品屬於私人消耗性產品，已拆封或使用過、無法恢復原狀、商品外盒損壞等均恕無法辦理退換貨。\n" +
                "✔使用後若有過敏請即刻停止使用，並請教醫生。\n" +
                "✔退貨時務必附回原完整商品、贈品、包裝外盒均齊全。\n" +
                "✔商品建議下訂前先實際試色、試用後再購買，若因顏色不符或皮膚不適等症狀，恕不接受退換。\n" +
                "✔個人電腦螢幕差異、照片拍攝關係造成色差，請以實際商品為準。"));

        FirebaseUser user = mAuth.getCurrentUser();
        Firebase firebase = new Firebase();
        firebase.fethMyCart(new Firebase.Callback() {
            @Override
            public void returnData(ArrayList arrayList) {

                RecyclerView recyclerView = dialog.findViewById(R.id.shoppingcart_recyclerview);

                Log.e("TEST", arrayList.toString());
                recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext(),LinearLayoutManager.VERTICAL,false));
                cartItemAdapter cartItemAdapter = new cartItemAdapter(arrayList);
                cartItemAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(cartItemAdapter);
            }
        });
       RecyclerView recyclerView = dialog.findViewById(R.id.shoppingcart_recyclerview);


        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext(),LinearLayoutManager.VERTICAL,false));
        cartItemAdapter cartItemAdapter = new cartItemAdapter(productModelArrayList);
        recyclerView.setAdapter(cartItemAdapter);
        //cartItemAdapter productAdapter = new productAdapter(productModelArrayList);
        //recyclerView.setAdapter(productAdapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        dialog.show();
    }
    private void initproduct(View view){

        //dataGatherer dataGatherer = new dataGatherer();
        //ArrayList<ArrayList> productModelArrayList_all = dataGatherer.getTabProduct();

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position){
                            case 0:
                                tab.setText("新書焦點");
                                break;
                            case 1:
                                tab.setText("搶先預購");
                                break;
                            case 2:
                                tab.setText("本月選書");
                                break;
                        }
                    }
                }).attach();
        tabLayout.setTabRippleColor(null);
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
                //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
            }
        });

    }

}