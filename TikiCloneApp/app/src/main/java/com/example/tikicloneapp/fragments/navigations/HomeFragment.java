package com.example.tikicloneapp.fragments.navigations;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.CatalogAdapter;
import com.example.tikicloneapp.adapters.ImageViewPagerAdapter;
import com.example.tikicloneapp.adapters.ProductsAdapter;
import com.example.tikicloneapp.models.Catalog;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.transformers.ZoomOutPageTransformer;


import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.tikicloneapp.activities.MainActivity.dbVolley;

public class HomeFragment extends Fragment {
    private LinearLayout laySearch, layViewedProducts, layRecommendedProducts, layHotCategories;
    //    private ViewFlipper viewFlipper;
    private Button btnViewAllViewed, btnViewAllRecommended, btnViewHotCategories;
    private ImageButton ibCart;
    private RecyclerView rvViewedProducts, rvRecommendedProducts, rvHotCategories;
    private ViewPager vpAdverts;

    private ArrayList<String> advertImages = new ArrayList<>();
    private ArrayList<Product> viewedProducts = new ArrayList<>(), recommendedProducts = new ArrayList<>();
    private ArrayList<Catalog> hotCategories = new ArrayList<>();
    private ProductsAdapter productsAdapter, recommendedProductsAdapter;
    private ImageViewPagerAdapter imageViewPagerAdapter;
    private CatalogAdapter categoryAdapter;
    private Timer timer;
    private int page = 0;

    public static int REFRESH_CODE = 1212;

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
        vpAdverts = view.findViewById(R.id.viewPager_advertImages);
        ibCart = view.findViewById(R.id.imageButton_cart);
        layViewedProducts = view.findViewById(R.id.layout_viewedProducts);
        rvViewedProducts = view.findViewById(R.id.recyclerView_viewedProducts);
        btnViewAllViewed = view.findViewById(R.id.button_viewAllViewed);
        btnViewAllRecommended = view.findViewById(R.id.button_viewAllRecommended);
        layRecommendedProducts = view.findViewById(R.id.layout_recommendedProducts);
        rvRecommendedProducts = view.findViewById(R.id.recyclerView_recommendedProducts);
        rvHotCategories = view.findViewById(R.id.recyclerView_hotCategories);
        layHotCategories = view.findViewById(R.id.layout_hotCategories);
        btnViewHotCategories = view.findViewById(R.id.button_viewHotCategories);
    }

    private void setEachView() {
        laySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).previous_Of_SearchFragment = R.id.menu_BottomHome;
                ((MainActivity) getActivity()).LoadFragment(R.id.menu_BottomSearch);


            }
        });

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });

        setHotCategoriesRV();
        setViewedProductsRV();
        setRecommendedProductsRV();
        setViewPager();

        btnViewAllViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListProductActivity.class);
                intent.putExtra("viewed", "viewed");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, REFRESH_CODE,
                            ActivityOptions
                                    .makeSceneTransitionAnimation(getActivity())
                                    .toBundle());
                } else
                    startActivityForResult(intent, REFRESH_CODE);
            }
        });

    }

    private void setViewedProductsRV() {
        productsAdapter = new ProductsAdapter(getActivity(), R.layout.item_viewed_product, viewedProducts, ProductsAdapter.ProductType.CODE_NOT_LIST);
        rvViewedProducts.setAdapter(productsAdapter);
        rvViewedProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dbVolley.getViewedProducts(layViewedProducts, viewedProducts, productsAdapter, MainActivity.idUser, 10);
    }

    private void setRecommendedProductsRV() {
        rvRecommendedProducts.setNestedScrollingEnabled(false);
        recommendedProductsAdapter = new ProductsAdapter(getActivity(), R.layout.row_product, recommendedProducts, ProductsAdapter.ProductType.CODE_NOT_LIST);
        rvRecommendedProducts.setAdapter(recommendedProductsAdapter);
        rvRecommendedProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        dbVolley.getRecommendedProducts(layRecommendedProducts, recommendedProducts, recommendedProductsAdapter);
    }

    private void setHotCategoriesRV() {
        categoryAdapter = new CatalogAdapter(getActivity(), hotCategories, CatalogAdapter.CatalogType.Home);
        rvHotCategories.setAdapter(categoryAdapter);
        rvHotCategories.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        dbVolley.getCatalog(hotCategories, categoryAdapter, null, 50);
    }


    private void setViewPager() {
        //set Images for ViewPager
        imageViewPagerAdapter = new ImageViewPagerAdapter(getActivity(), advertImages);
        vpAdverts.setAdapter(imageViewPagerAdapter);
        vpAdverts.setPageTransformer(true, new ZoomOutPageTransformer());
        getAdvertImages();

        vpAdverts.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private static final float thresholdOffset = 0.5f;
            private boolean scrollStarted, checkDirection;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (checkDirection) {
                    if (thresholdOffset > positionOffset) {
                        Log.d("thang", "going left");
                    } else {
                        Log.d("thang", "going right");
                    }
                    checkDirection = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                    timer = new Timer();
                } else {
                    scrollStarted = false;
                }

            }

        });

    }

    public void pageSwitcher(int length, int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(length), 0, seconds * 1000); // delay
    }

    // this is an inner class...
    public class RemindTask extends TimerTask {

        int length;

        public RemindTask(int length) {
            this.length = length;
        }

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (page == length)
                            page = -1;

                        vpAdverts.setCurrentItem(page++, true);
                    }
                });
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESH_CODE) {
            dbVolley.getViewedProducts(layViewedProducts, viewedProducts, productsAdapter, MainActivity.idUser, 10);
            dbVolley.getRecommendedProducts(layRecommendedProducts, recommendedProducts, recommendedProductsAdapter);
            Log.d("thang", "onActivityResult: ");
        }

    }

    public void getAdvertImages() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, dbVolley.URL_GET_IMAGE_ADVERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray imageProductArray = new JSONArray(response);
                    if (imageProductArray.length() == 0) {
                        return;
                    }
                    advertImages.clear();
                    for (int i = 0; i < imageProductArray.length(); i++) {
                        String url = imageProductArray.getString(i);
                        advertImages.add(url);
                    }
                    imageViewPagerAdapter.notifyDataSetChanged();

                    pageSwitcher(advertImages.size(), 3);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", error.toString());
                    }
                });
        requestQueue.add(stringRequest);
    }


}
