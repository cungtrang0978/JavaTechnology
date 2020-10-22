package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.ProductListAdapter;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.models.Transact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PurchasedProductActivity extends AppCompatActivity {
    RecyclerView rvProduct;
    LinearLayout layLoadingPanel_parent,
            layPanel_nonOrder;
    Button btnShopping;
    ImageButton ibBack;

    ProductListAdapter productAdapter;
    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_product);

        initWidget();

        setEvent();
    }

    void initWidget() {
        rvProduct = findViewById(R.id.recyclerView_product);
        layPanel_nonOrder = findViewById(R.id.layout_panel_nonOrder);
        layLoadingPanel_parent = findViewById(R.id.loadingPanel_parent);
        btnShopping = findViewById(R.id.button_shopping);
        ibBack = findViewById(R.id.imageButton_back);


    }

    void setEvent() {
        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });
        setRvProduct();
    }

    void setRvProduct() {
        callLoadingPanel_parent();
        if (productAdapter == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(ContextCompat.getDrawable(rvProduct.getContext(), R.drawable.divider_product_cart));

            productAdapter = new ProductListAdapter(this, R.layout.row_item_product_purchased, products);
            rvProduct.setLayoutManager(new LinearLayoutManager(this));
            rvProduct.addItemDecoration(itemDecoration);
            rvProduct.setAdapter(productAdapter);
        }

        getProduct(products, productAdapter, Transact.STATUS_SUCCESS, MainActivity.idUser, layPanel_nonOrder);
    }

    public void callLoadingPanel_parent() {
        layLoadingPanel_parent.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layLoadingPanel_parent.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void getProduct(final ArrayList<Product> productArrayList, final ProductListAdapter productListAdapter,
                            @Nullable final Integer status,
                            @Nullable final Integer idUser,
                            @Nullable final LinearLayout layPanel_nonOrder) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    productArrayList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Product product = new Product(
                                        object.getInt("id"),
                                        object.getInt("idCatalog"),
                                        object.getString("name"),
                                        object.getString("description"),
                                        object.getInt("price"),
                                        object.getInt("discount"),
                                        object.getInt("created"),
                                        object.getInt("views"),
                                        object.getInt("qty")
                                );
                                productArrayList.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        productListAdapter.notifyDataSetChanged();
                    } else {
                        assert layPanel_nonOrder != null;
                        layPanel_nonOrder.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "getProduct: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (idUser != null) {
                    params.put("idUser", String.valueOf(idUser));
                }
                if (status != null) {
                    params.put("status", String.valueOf(status));
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}