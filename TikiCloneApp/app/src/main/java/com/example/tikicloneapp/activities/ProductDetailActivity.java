package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.MyClass;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.ProductListAdapter;
import com.example.tikicloneapp.adapters.ImageViewPagerAdapter;
import com.example.tikicloneapp.adapters.ProductsAdapter;
import com.example.tikicloneapp.adapters.ReviewAdapter;
import com.example.tikicloneapp.fragments.SuccessAdded_BottomSheetDialog;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.models.Rate;
import com.example.tikicloneapp.transformers.ZoomOutPageTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.tikicloneapp.MyClass.setTextView_StrikeThrough;
import static com.example.tikicloneapp.database.DBVolley.getAddress;


public class ProductDetailActivity extends AppCompatActivity implements SuccessAdded_BottomSheetDialog.BottomSheetListener {
    private ViewPager viewPagerImageProduct;
    private ImageViewPagerAdapter pagerAdapter;
    private TextView tvName, tvPriceOrigin, tvPriceDiscount, tvDiscount, tvDescription, tvRateQuantity, tvAddress;
    private TextView tvRatePoint, tvReviewQuantity, tvFiveStars, tvFourStars, tvThreeStars, tvTwoStars, tvOneStar;
    private ImageButton ibBack, ibCart, ibSearch, ibHome;
    private Button btnAddProduct;
    private TextView tvViewAllReviews;
    private LinearLayout layoutRate, layoutReview;
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    private ImageView ivStarDetail_1, ivStarDetail_2, ivStarDetail_3, ivStarDetail_4, ivStarDetail_5;
    private RecyclerView rvReviews, rvRecommendedProducts;

    public LinearLayout lay_loading;

    final ArrayList<String> imageUrls = new ArrayList<>();
    final ArrayList<Rate> rates = new ArrayList<>();
    private ReviewAdapter reviewAdapter;


    private ArrayList<Product> recommendedProducts = new ArrayList<>();
    private ProductsAdapter recommendedProductsAdapter;

    private int CODE_ID_PRODUCT = 3;
    private Product product;

    public static int REQUEST_CODE_UPDATE_ADDRESS = 1234;
    public static int REQUEST_CODE_LOGIN = 1234;

    final Handler handler = new Handler();
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initWidget();
        requestQueue =  Volley.newRequestQueue(this);

        product = (Product) getIntent().getSerializableExtra("product");

        setViews();
        setOnClick();
    }


    private void initWidget() {
        viewPagerImageProduct = findViewById(R.id.viewPager_imageProduct);
        tvName = findViewById(R.id.textView_name);
        tvPriceOrigin = findViewById(R.id.textView_priceOrigin);
        tvPriceDiscount = findViewById(R.id.textView_priceDiscount);
        tvDiscount = findViewById(R.id.textView_discountProduct);
        tvDescription = findViewById(R.id.textView_description);
        tvRateQuantity = findViewById(R.id.textView_rateQuantity);
        ibBack = findViewById(R.id.imageButton_back);
        btnAddProduct = findViewById(R.id.button_addToCart);
        tvAddress = findViewById(R.id.textView_address);
        ibCart = findViewById(R.id.imageButton_cart);
        ibSearch = findViewById(R.id.imageButton_search);
        lay_loading = findViewById(R.id.loadingPanel_parent);
        ibHome = findViewById(R.id.imageButton_home);
        layoutRate = findViewById(R.id.linearLayout_rate);
        ivStar1 = findViewById(R.id.imageView_star1);
        ivStar2 = findViewById(R.id.imageView_star2);
        ivStar3 = findViewById(R.id.imageView_star3);
        ivStar4 = findViewById(R.id.imageView_star4);
        ivStar5 = findViewById(R.id.imageView_star5);
        rvReviews = findViewById(R.id.recyclerView_reviews);
        tvRatePoint = findViewById(R.id.textView_ratePoint);
        tvReviewQuantity = findViewById(R.id.textView_reviewQuantity);
        tvFiveStars = findViewById(R.id.textView_fiveStars);
        tvFourStars = findViewById(R.id.textView_fourStars);
        tvThreeStars = findViewById(R.id.textView_threeStars);
        tvTwoStars = findViewById(R.id.textView_twoStars);
        tvOneStar = findViewById(R.id.textView_oneStar);
        ivStarDetail_1 = findViewById(R.id.imageView_starDetail_1);
        ivStarDetail_2 = findViewById(R.id.imageView_starDetail_2);
        ivStarDetail_3 = findViewById(R.id.imageView_starDetail_3);
        ivStarDetail_4 = findViewById(R.id.imageView_starDetail_4);
        ivStarDetail_5 = findViewById(R.id.imageView_starDetail_5);
        layoutReview = findViewById(R.id.linearLayout_review);
        rvRecommendedProducts = findViewById(R.id.recyclerView_recommendedProducts);
        tvViewAllReviews = findViewById(R.id.textView_viewAllReviews);
    }

    Runnable runnableInsertViewed = new Runnable() {
        @Override
        public void run() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_INSERT_VIEWED_PRODUCT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                switch (response) {
//                    case "insert_success":
//                        Log.d("insertViewedProduct", "insert viewed Product success" + idProduct);
//                        break;
//                    case "insert_fail":
//                        Log.d("insertViewedProduct", "insert viewed Product fail");
//                        break;
//                    case "seen":
//                        Log.d("insertViewedProduct", "insert viewed Product seen");
//                        break;
//                }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("thang", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("idProduct", String.valueOf(product.getId()));

                    params.put("idUser", String.valueOf(MainActivity.idUser));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    };

    private void setViews() {
        //Update views of product

//        dbVolley.updateViewsProduct(product);

        setViewPager();

        viewPagerImageProduct.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return false;
            }
        });

        //set Product Information
        getProduct(product.getId());

        getAddress(getApplicationContext(), tvAddress);

        setTextView_StrikeThrough(tvPriceOrigin);

        insertViewedProduct();

        setRecommendedProductsAdapter();

        setRvReviews();

    }


    private void setViewPager() {
        //set Images for ViewPager
        pagerAdapter = new ImageViewPagerAdapter(this, imageUrls);
        viewPagerImageProduct.setAdapter(pagerAdapter);
        viewPagerImageProduct.setPageTransformer(true, new ZoomOutPageTransformer());
        getImageProduct(product.getId());

    }


    private void setOnClick() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart(product.getId());
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, AddressActivity.class);
                intent.putExtra("type", "productDetail");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, REQUEST_CODE_UPDATE_ADDRESS, ActivityOptions.makeSceneTransitionAnimation(ProductDetailActivity.this).toBundle());
                } else startActivityForResult(intent, REQUEST_CODE_UPDATE_ADDRESS);
            }
        });

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ProductDetailActivity.this).toBundle());
                } else startActivity(intent);
            }
        });

        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void setBottomSheetDialog() {
        SuccessAdded_BottomSheetDialog bottomSheetDialog = new SuccessAdded_BottomSheetDialog(product);
        bottomSheetDialog.show(getSupportFragmentManager(), "addedBottomSheetDialog");
    }

    private void callPanel(final LinearLayout panel, int millisecondsDelay) {
        panel.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setBottomSheetDialog();
                panel.setVisibility(View.GONE);
            }
        }, millisecondsDelay);


    }

    private void getProduct(final int idProduct) {
        String URL_GET_PRODUCT = MainActivity.dbVolley.URL_GET_PRODUCT;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PRODUCT, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject jsonProduct = jsonArray.getJSONObject(0);
                    Product product = new Product(jsonProduct);
                    int priceDiscount = product.getPrice() - product.getPrice() * product.getDiscount() / 100;

                    tvName.setText(product.getName());
                    tvRatePoint.setText(product.getRate() + "");
                    tvRateQuantity.setText("(Xem " + product.getRateQty() + " đánh giá)");
                    tvReviewQuantity.setText(product.getRateQty() + " nhận xét");
                    if (product.getDiscount() == 0) {
                        tvDiscount.setVisibility(View.INVISIBLE);
                        tvPriceOrigin.setVisibility(View.INVISIBLE);
                    } else {
                        tvDiscount.setText(ProductListAdapter.formatPercent(product.getDiscount()));
                        tvPriceOrigin.setText(CartActivity.formatCurrency(product.getPrice()));
                    }


                    tvPriceDiscount.setText(CartActivity.formatCurrency(priceDiscount));
                    tvDescription.setText(product.getDescription());
//                    tvViews.setText(views + "");

                    if (product.getRate() > 0) {
                        Log.d("thang", "rateProduct: " + product.getRate());
                        setRate(ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, product.getRate());
                        setRate(ivStarDetail_1, ivStarDetail_2, ivStarDetail_3, ivStarDetail_4, ivStarDetail_5, product.getRate());
                    } else {
                        layoutRate.setVisibility(View.GONE);
                        layoutReview.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getImageProduct(final int idProduct) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_IMAGE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray imageProductArray = new JSONArray(response);
                    for (int i = 0; i < imageProductArray.length(); i++) {
                        try {
                            JSONObject imgUrl = imageProductArray.getJSONObject(i);
                            String url = imgUrl.getString("imageUrl");

                            imageUrls.add(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    pagerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onButtonClicked(String text) {
        if (text.equals("go_to_cart")) {
            MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
        }
    }

    private void addToCart(Product product) {
        int idUser = MainActivity.idUser;
        int qty = 1;
        int discount = product.getDiscount();
        int idProduct = product.getId();
        int discountPrice = product.getPrice() - product.getPrice() * discount / 100;

        if (idUser == 0) { //No Account
            Log.d("thang", "noAccount");

//            Order order = new Order(idProduct, qty, amount);
//            MainActivity.dbManager.insertData_Order(order);
        } else {
            Log.d("thang", "Account");
            MainActivity.dbVolley.checkTransact(idUser, idProduct, qty, discountPrice);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_ADDRESS && resultCode == RESULT_OK) {
            getAddress(getApplicationContext(), tvAddress);
        }
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {

            getAddress(getApplicationContext(), tvAddress);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }

    private void setRate(ImageView iv1, ImageView iv2, ImageView iv3, ImageView iv4, ImageView iv5, double rates) {
        iv1.setImageResource(R.drawable.star_on);
        iv2.setImageResource(R.drawable.star_off);
        iv3.setImageResource(R.drawable.star_off);
        iv4.setImageResource(R.drawable.star_off);
        iv5.setImageResource(R.drawable.star_off);

//set star 2
        if (rates > 1.7) {
            iv2.setImageResource(R.drawable.star_on);
        } else if (rates > 1.2) {
            iv2.setImageResource(R.drawable.star_haft);
        }

//set star 3
        if (rates > 2.7) {
            iv3.setImageResource(R.drawable.star_on);
        } else if (rates > 2.2) {
            iv3.setImageResource(R.drawable.star_haft);
        }

//set star 4
        if (rates > 3.7) {
            iv4.setImageResource(R.drawable.star_on);
        } else if (rates > 3.2) {
            iv4.setImageResource(R.drawable.star_haft);
        }

//set star 5
        if (rates > 4.7) {
            iv5.setImageResource(R.drawable.star_on);
        } else if (rates > 4.2) {
            iv5.setImageResource(R.drawable.star_haft);
        }
    }

    public void checkQtyProduct(final Integer idProduct, final Integer qty) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_CHECK_QTY_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("out_of_stock")) {
                    Toast.makeText(ProductDetailActivity.this, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    if (MainActivity.idUser != 0) {
                        addToCart(product);
                        MyClass.callPanel(lay_loading, 500);
                        callPanel(lay_loading, 500);
                    } else {
                        Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                        intent.putExtra("CODE", 1);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivityForResult(intent, REQUEST_CODE_LOGIN, ActivityOptions.makeSceneTransitionAnimation(ProductDetailActivity.this).toBundle());
                        } else startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    }

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (idProduct != null) {
                    params.put("idProduct", String.valueOf(idProduct));
                }
                if (qty != null) {
                    params.put("qty", String.valueOf(qty));
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void insertViewedProduct() {

        if (MainActivity.idUser == 0) return;

        handler.postDelayed(runnableInsertViewed, 19000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableInsertViewed);
    }

    public void addProductToCart(final int idProduct) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int qty = 1;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Order order = new Order(
                                    object.getInt("id"),
                                    object.getInt("idProduct"),
                                    object.getInt("qty"),
                                    object.getInt("amount")
                            );
                            if (idProduct == order.getmIdProduct()) {
                                qty += order.getmQty();
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    checkQtyProduct(idProduct, qty);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(MainActivity.idTransact));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void setRvReviews() {
//        callLoadingPanel_parent();
        if (reviewAdapter == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(rvReviews.getContext(), R.drawable.divider_product_cart)));

            reviewAdapter = new ReviewAdapter(this, rates);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.addItemDecoration(itemDecoration);
            rvReviews.setAdapter(reviewAdapter);
        }

        getReviewedProductsByProductId();
    }

    public void setRecommendedProductsAdapter() {
//        callLoadingPanel_parent();
        if (recommendedProductsAdapter == null) {
            recommendedProductsAdapter = new ProductsAdapter(this, R.layout.row_recommended_product, recommendedProducts, ProductsAdapter.ProductType.CODE_PRODUCT_LIST);
            rvRecommendedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvRecommendedProducts.setAdapter(recommendedProductsAdapter);
        }

        MainActivity.dbVolley.getRecommendedProductsByProductId(product.getId(), recommendedProducts, recommendedProductsAdapter);
    }

    public void getReviewedProductsByProductId() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_REVIEW_PRODUCTS_BY_PRODUCT_ID,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Rate> _rates = new ArrayList<>(),
                                    oneStarRates = new ArrayList<>(),
                                    twoStarsRates = new ArrayList<>(),
                                    threeStarRates = new ArrayList<>(),
                                    fourStarsRates = new ArrayList<>(),
                                    fiveStarRates = new ArrayList<>();
                            Log.d("thang", "getReviewedProductsByProductId: " + response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonRate = jsonArray.getJSONObject(i);
                                    Rate rate;
                                    if (jsonRate.getString("modifiedAt").equals("null")) {
                                        rate = new Rate(
                                                jsonRate.getInt("id"),
                                                jsonRate.getInt("ratePoint"),
                                                jsonRate.getString("comment"),
                                                Timestamp.valueOf(jsonRate.getString("createdAt")),
                                                null,
                                                jsonRate.getInt("idUser"),
                                                jsonRate.getString("userFullName")
                                        );
                                    } else {
                                        rate = new Rate(
                                                jsonRate.getInt("id"),
                                                jsonRate.getInt("ratePoint"),
                                                jsonRate.getString("comment"),
                                                Timestamp.valueOf(jsonRate.getString("createdAt")),
                                                Timestamp.valueOf(jsonRate.getString("modifiedAt")),
                                                jsonRate.getInt("idUser"),
                                                jsonRate.getString("userFullName")
                                        );
                                    }
                                    _rates.add(rate);
                                    switch (rate.getRatePoint()) {
                                        case 1:
                                            oneStarRates.add(rate);
                                            break;
                                        case 2:
                                            twoStarsRates.add(rate);
                                            break;
                                        case 3:
                                            threeStarRates.add(rate);
                                            break;
                                        case 4:
                                            fourStarsRates.add(rate);
                                            break;
                                        case 5:
                                            fiveStarRates.add(rate);
                                            break;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            rates.clear();
                            rates.addAll(_rates);
                            reviewAdapter.notifyDataSetChanged();

                            tvFiveStars.setText(fiveStarRates.size() + "");
                            tvFourStars.setText(fourStarsRates.size() + "");
                            tvThreeStars.setText(threeStarRates.size() + "");
                            tvTwoStars.setText(twoStarsRates.size() + "");
                            tvOneStar.setText(oneStarRates.size() + "");

                            tvViewAllReviews.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ProductDetailActivity.this, ProductReviewsActivity.class);
                                    Bundle extra = new Bundle();
                                    extra.putSerializable("rates", rates);
                                    intent.putExtra("extra", extra);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(product.getId()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



}
