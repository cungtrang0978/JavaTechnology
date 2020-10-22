package com.example.tikicloneapp.fragments.navigations;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.ProductsAdapter;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.tikicloneapp.activities.MainActivity.dbVolley;

public class HomeFragment extends Fragment {
    private LinearLayout laySearch, layViewedProducts, layRecommendedProducts;
    private ViewFlipper viewFlipper;
    private ArrayList<String> bannerArray = new ArrayList<>();
    private ArrayList<Product> viewedProducts = new ArrayList<>(),
            recommendedProducts = new ArrayList<>();
    private ImageButton ibCart;
    private RecyclerView rvViewedProducts, rvRecommendedProducts;
    private ProductsAdapter productsAdapter;
    private ProductsAdapter recommendedProductsAdapter;
    private Button btnViewAllViewed, btnViewAllRecommended;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initWidget(view);

        setEachView();

        return view;
    }

    private void initWidget(View view) {
        laySearch = view.findViewById(R.id.layout_searchName);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        ibCart = view.findViewById(R.id.imageButton_cart);
        layViewedProducts = view.findViewById(R.id.layout_viewedProducts);
        rvViewedProducts = view.findViewById(R.id.recyclerView_viewedProducts);
        btnViewAllViewed = view.findViewById(R.id.button_viewAllViewed);
        btnViewAllRecommended = view.findViewById(R.id.button_viewAllRecommended);
        layRecommendedProducts = view.findViewById(R.id.layout_recommendedProducts);
        rvRecommendedProducts = view.findViewById(R.id.recyclerView_recommendedProducts);
    }

    private void setEachView() {
        laySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).previous_Of_SearchFragment = R.id.menu_BottomHome;
                ((MainActivity) getActivity()).LoadFragment(R.id.menu_BottomSearch);
//                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                startActivity(intent);

            }
        });

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });

        setViewedProductsRV();
        setRecommendedProductsRV();
        setViewFlipper();

        btnViewAllViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListProductActivity.class);
                intent.putExtra("viewed", "viewed");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,
                            ActivityOptions
                                    .makeSceneTransitionAnimation((Activity) getActivity())
                                    .toBundle());
                } else
                    startActivity(intent);
            }
        });

    }

    private void setViewedProductsRV() {
        productsAdapter = new ProductsAdapter(getActivity(), R.layout.item_viewed_product, viewedProducts);
        rvViewedProducts.setAdapter(productsAdapter);
        rvViewedProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dbVolley.getViewedProducts(layViewedProducts, viewedProducts, productsAdapter, MainActivity.idUser, 10);
    }

    private void setRecommendedProductsRV() {
        rvRecommendedProducts.setNestedScrollingEnabled(false);
        recommendedProductsAdapter = new ProductsAdapter(getActivity(), R.layout.row_product, recommendedProducts);
        rvRecommendedProducts.setAdapter(recommendedProductsAdapter);
        rvRecommendedProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        dbVolley.getRecommendedProducts(layRecommendedProducts, recommendedProducts, recommendedProductsAdapter);
    }

    private void setViewFlipper() {
        DBManager dbManager = MainActivity.dbManager;
        ArrayList<String> bannerArray = dbManager.getAdvertisementsUrl();

        for (int i = 0; i < bannerArray.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            Picasso.get()
                    .load(bannerArray.get(i))
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            viewFlipper.addView(imageView);

            viewFlipper.setFlipInterval(2000);
            viewFlipper.setAutoStart(true);
            Animation animation_slide_in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
            Animation animation_slide_out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);

            viewFlipper.setInAnimation(animation_slide_in);
            viewFlipper.setOutAnimation(animation_slide_out);
        }
    }
}
