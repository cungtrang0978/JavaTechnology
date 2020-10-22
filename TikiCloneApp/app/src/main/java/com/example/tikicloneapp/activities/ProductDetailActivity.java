package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.tikicloneapp.adapters.ProductViewPagerAdapter;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.fragments.SuccessAdded_BottomSheetDialog;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.models.Transact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.MyClass.setTextView_StrikeThrough;
import static com.example.tikicloneapp.database.DBVolley.getAddress;


public class ProductDetailActivity extends AppCompatActivity implements SuccessAdded_BottomSheetDialog.BottomSheetListener {
    private ViewPager viewPagerImageProduct;
    private ProductViewPagerAdapter pagerAdapter;
    private TextView tvName, tvPriceOrigin, tvPriceDiscount, tvDiscount, tvDescription, tvViews, tvAddress;
    private ImageButton ibBack, ibCart, ibSearch, ibHome;
    private Button btnAddProduct;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private int CODE_ID_PRODUCT = 3;
    private Product product;

    public LinearLayout lay_loading;

    public static int REQUEST_CODE_UPDATE_ADDRESS = 1234;
    public static int REQUEST_CODE_LOGIN = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initWidget();

        product = (Product) getIntent().getSerializableExtra("product");

        setEachView(product);
        setOnClick();
    }

    private void initWidget() {
        viewPagerImageProduct = findViewById(R.id.viewPager_imageProduct);
        tvName = findViewById(R.id.textView_name);
        tvPriceOrigin = findViewById(R.id.textView_priceOrigin);
        tvPriceDiscount = findViewById(R.id.textView_priceDiscount);
        tvDiscount = findViewById(R.id.textView_discountProduct);
        tvDescription = findViewById(R.id.textView_description);
        tvViews = findViewById(R.id.textView_views);
        ibBack = findViewById(R.id.imageButton_back);
        btnAddProduct = findViewById(R.id.button_addToCart);
        tvAddress = findViewById(R.id.textView_address);
        ibCart = findViewById(R.id.imageButton_cart);
        ibSearch = findViewById(R.id.imageButton_search);
        lay_loading = findViewById(R.id.loadingPanel_parent);
        ibHome = findViewById(R.id.imageButton_home);
    }

    private void setEachView(final Product product) {
        //Update views of product
        DBVolley dbVolley = new DBVolley(this);
        dbVolley.updateViewsProduct(product);

        setViewPager();

        //set Product Information
        getProduct(product.getId());

        getAddress(getApplicationContext(), tvAddress);

        setTextView_StrikeThrough(tvPriceOrigin);

        insertViewedProduct(MainActivity.idUser, product.getId());
    }

    private void setViewPager() {
        //set Images for ViewPager
        pagerAdapter = new ProductViewPagerAdapter(this, imageUrls);
        viewPagerImageProduct.setAdapter(pagerAdapter);
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
                addProduct(product.getId());
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
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject product = jsonArray.getJSONObject(0);
                    String name = product.getString("name");
                    int discount = product.getInt("discount");
                    int priceOrigin = product.getInt("price");
                    int priceDiscount = priceOrigin - priceOrigin * discount / 100;
                    String description = product.getString("description");
                    int views = product.getInt("views");

                    tvName.setText(name);
                    if (discount == 0) {
                        tvDiscount.setVisibility(View.INVISIBLE);
                        tvPriceOrigin.setVisibility(View.INVISIBLE);
                    } else {
                        tvDiscount.setText(ProductListAdapter.formatPercent(discount));
                        tvPriceOrigin.setText(CartActivity.formatCurrency(priceOrigin));
                    }


                    tvPriceDiscount.setText(CartActivity.formatCurrency(priceDiscount));
                    tvDescription.setText(description);
                    tvViews.setText(views + "");
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
        int amount = product.getPrice() - product.getPrice() * discount / 100;

        if (idUser == 0) { //No Account
            Log.d("thang", "noAccount");

            Order order = new Order(idProduct, qty, amount);
            MainActivity.dbManager.insertData_Order(order);
        } else {
            Log.d("thang", "Account");
            MainActivity.dbVolley.checkTransact(idUser, idProduct, qty, amount);
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


    public void insertViewedProduct(final Integer idUser, final Integer idProduct) {
        if (MainActivity.idUser == 0) return;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_INSERT_VIEWED_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "insert_success":
                        Log.d("insertViewedProduct", "insert viewed Product success" + idProduct);
                        break;
                    case "insert_fail":
                        Log.d("insertViewedProduct", "insert viewed Product fail");
                        break;
                    case "seen":
                        Log.d("insertViewedProduct", "insert viewed Product seen");
                        break;
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

                if (idUser != null) {
                    params.put("idUser", String.valueOf(idUser));
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void addProduct(final int idProduct) {
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
}
