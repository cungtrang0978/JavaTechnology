package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.MyClass;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.CartProductAdapter;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Transact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.MyClass.convertTime;
import static com.example.tikicloneapp.MyClass.getTextAddress;
import static com.example.tikicloneapp.activities.CartActivity.formatCurrency;

import static com.example.tikicloneapp.activities.MainActivity.dbVolley;
import static com.example.tikicloneapp.models.Transact.setTvStatus;

public class TransactActivity extends AppCompatActivity {

    private TextView tvIdTransact, tvTimeCreated, tvStatus, tvUserName, tvPhoneNumber, tvAddress, tvPriceProvisional, tvPriceLast;
    private Button btnCancelTransact;
    private ImageButton ibBack;
    private RecyclerView rvCart;
    private LinearLayout layout_loading;

    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private CartProductAdapter cartProductAdapter;

    private int idTransact;
    private int idActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transact);
        initWidget();

        idTransact = getIntent().getIntExtra("idTransact", 0);
        idActivity = getIntent().getIntExtra("activity", 0);

        setEachView();
    }


    private void initWidget() {
        tvIdTransact = findViewById(R.id.textView_idTransact);
        tvTimeCreated = findViewById(R.id.textView_timeCreated);
        tvStatus = findViewById(R.id.textView_status);
        tvUserName = findViewById(R.id.textView_userName);
        tvPhoneNumber = findViewById(R.id.textView_phoneNumber);
        tvAddress = findViewById(R.id.textView_address);
        tvPriceProvisional = findViewById(R.id.textView_priceProvisional);
        tvPriceLast = findViewById(R.id.textView_priceLast);
        btnCancelTransact = findViewById(R.id.button_cancelTransact);
        rvCart = findViewById(R.id.recyclerView_cart);
        ibBack = findViewById(R.id.imageButton_back);
        layout_loading = findViewById(R.id.loadingPanel_parent);
    }

    private void setEachView() {
        MyClass.callPanel(layout_loading, 700);
        tvIdTransact.setText(String.valueOf(idTransact));

        setTransact();

        setCartProductAdapter();

        btnCancelTransact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbVolley.updateStatusTransact(idTransact, Transact.STATUS_CANCEL);
                MainActivity.dbVolley.getOrder_updateProduct(Transact.STATUS_CANCEL);
                if (idActivity == R.layout.activity_order_success) {
                    Intent intent = new Intent(TransactActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (idActivity == R.layout.activity_list_transact) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else finish();
                    setResult(RESULT_OK);
                }
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
    }

    private void setCartProductAdapter() {
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(rvCart.getContext(), R.drawable.divider_product_cart));

        cartProductAdapter = new CartProductAdapter(this, orderArrayList, R.layout.row_item_cart_transact);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
//        rvCart.addItemDecoration(itemDecoration);
        rvCart.setAdapter(cartProductAdapter);
        resetRvCart();
    }

    public void resetRvCart() {
        dbVolley.getOrder(idTransact, orderArrayList, cartProductAdapter);
    }

    public void setTransact() {
        final String non_post = "non_post";
        final String wrong_query = "wrong_query";
        final String non_value = "non_value";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "setTransact: "+response);

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
                    Transact transact = new Transact(
                            object
                    );

                    setTvStatus(tvStatus, transact.getmStatus());

                    if (transact.getmStatus() != Transact.STATUS_TIKI_RECEIVED) {
                        btnCancelTransact.setVisibility(View.GONE);
                    }

                    String address = getTextAddress(transact.getmProvince(), transact.getmDistrict(), transact.getmWard(), transact.getmAddress());
                    tvUserName.setText(transact.getmUser_name());
                    tvPhoneNumber.setText(transact.getmUser_phone());
                    tvAddress.setText(address);
                    Log.d("thang", "transact: " + transact.toString());


                    if (transact.getmCreated() != null) {
                        Date createdAt = new Date(transact.getmCreated().getTime());
                        @SuppressLint("SimpleDateFormat")
                        String dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(createdAt);
                        tvTimeCreated.setText(dateFormat);
                    }


                    String price = formatCurrency(transact.getmAmount());

                    tvPriceProvisional.setText(price);
                    tvPriceLast.setText(price);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "setTransact: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(idTransact));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}