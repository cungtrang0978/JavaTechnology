package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.CartProductAdapter;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Transact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private ImageButton ibExit;
    private RecyclerView rvCart;
    private TextView tvName, tvPriceLast, tvPriceProvisional;
    private Button btnApply, btnOrder, btnShopping;
    private LinearLayout layLoadingPanel, layLoadingPanel_parent;
    private LinearLayout layPanel_nonOrder;


    private CartProductAdapter cartProductAdapter;
    private ArrayList<Order> orderArrayList = new ArrayList<>();

    public static int idTransact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initWidget();

//        Log.d("thang", MainActivity.dbManager.getWard("Huyện Phù Cát").toString());
        idTransact = getIntent().getIntExtra("idTransact", 0);
        MainActivity.idTransact = idTransact;

        setEachView();
        callLoadingPanel_parent();

    }

    private void initWidget() {
        ibExit = findViewById(R.id.imageButton_exit);
        rvCart = findViewById(R.id.recyclerView_cart);
        tvName = findViewById(R.id.textView_name);
        tvPriceLast = findViewById(R.id.textView_priceLast);
        tvPriceProvisional = findViewById(R.id.textView_priceProvisional);
        btnApply = findViewById(R.id.button_apply);
        btnOrder = findViewById(R.id.button_order);
        layLoadingPanel = findViewById(R.id.loadingPanel);
        layPanel_nonOrder = findViewById(R.id.layout_panel_nonOrder);
        layLoadingPanel_parent = findViewById(R.id.loadingPanel_parent);
        btnShopping = findViewById(R.id.button_shopping);
    }

    private void setEachView() {
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });


        getCart();

        setCartProductAdapter();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmationActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CartActivity.this).toBundle());
                } else startActivity(intent);
            }
        });

        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });
    }

    private void renameLabel(int qtyTransact) {
        if (qtyTransact == 0) {
            tvName.setText("Giỏ hàng");
            return;
        }
        String name = "Giỏ hàng (" + qtyTransact + ")";
        tvName.setText(name);
    }

    private void setCartProductAdapter() {
        rvCart.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(rvCart.getContext(), R.drawable.divider_product_cart));

        cartProductAdapter = new CartProductAdapter(this, orderArrayList, R.layout.row_cart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.addItemDecoration(itemDecoration);
        rvCart.setAdapter(cartProductAdapter);
        resetRvCart();
    }

    public void getCart() {
        final String non_post = "non_post";
        final String wrong_query = "wrong_query";
        final String non_value = "non_value";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals(non_post)) {
                    return;
                }
                if (response.equals(wrong_query)) {
                    return;
                }
                if (response.equals(non_value)) {
                    return;
                }

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    Transact transact = new Transact(object.getInt("qty"), object.getInt("amount"));

                    renameLabel(transact.getmQty());

                    if (transact.getmQty() == 0) {
                        layPanel_nonOrder.setVisibility(View.VISIBLE);

                    } else {
                        String price = formatCurrency(transact.getmAmount());

                        tvPriceProvisional.setText(price);
                        tvPriceLast.setText(price);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "getCart: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(idTransact));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static String formatCurrency(long number) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        return vn.format(number) + "đ";
    }

    public void callLoadingPanel() {
        layLoadingPanel.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layLoadingPanel.setVisibility(View.GONE);
            }
        }, 500);
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

    public void resetRvCart() {
        MainActivity.dbVolley.getOrder(idTransact, orderArrayList, cartProductAdapter);
    }



}