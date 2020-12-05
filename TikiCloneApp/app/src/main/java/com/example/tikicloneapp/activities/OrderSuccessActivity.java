package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderSuccessActivity extends AppCompatActivity {
    private TextView tvUserName, tvAmount, tvIdTransact, tvGoToTransact;
    private RecyclerView rvCart;
    private Button btnHome;
    private LinearLayout layout_loading;

    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private CartProductAdapter cartProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        initWidget();
        setEachView();

    }

    private void initWidget(){
        tvUserName = findViewById(R.id.textView_userName);
        tvAmount = findViewById(R.id.textView_amount);
        rvCart = findViewById(R.id.recyclerView_cart);
        btnHome = findViewById(R.id.button_home);
        tvIdTransact = findViewById(R.id.textView_idTransact);
        tvGoToTransact = findViewById(R.id.textView_goToTransact);
        layout_loading = findViewById(R.id.loadingPanel_parent);
    }

    private void setEachView(){
        MyClass.callPanel(layout_loading, 700);
        setCartProductAdapter();
        rvCart.setNestedScrollingEnabled(false);

        setInfoTransact();
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tvGoToTransact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this, TransactActivity.class);
                intent.putExtra("activity", R.layout.activity_order_success);
                intent.putExtra("idTransact", MainActivity.idTransact);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(OrderSuccessActivity.this).toBundle());
                } else startActivity(intent);
            }
        });
    }

    private void setCartProductAdapter() {
        cartProductAdapter = new CartProductAdapter(this, orderArrayList, R.layout.row_comfirmation, CartProductAdapter.ORDER_SUCCESS_ACTIVITY);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartProductAdapter);
        MainActivity.dbVolley.getOrder(MainActivity.idTransact, orderArrayList, cartProductAdapter);
    }

    public void setInfoTransact() {
        final String non_post = "non_post";
        final String wrong_query = "wrong_query";
        final String non_value = "non_value";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "getCart: ");

                if(response.equals(non_post)){
                    return;
                }
                if (response.equals(wrong_query)){
                    return;
                }
                if(response.equals(non_value)){
                    return;
                }

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject objectTransact = jsonArray.getJSONObject(0);

                    int amount = objectTransact.getInt("amount");
                    String userName = "Chúc mừng " + objectTransact.getString("user_name") +'!';

                    tvAmount.setText(CartActivity.formatCurrency(amount));
                    tvUserName.setText(userName);

                    tvIdTransact.setText(String.valueOf(objectTransact.getInt("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "getCart: "+ error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(MainActivity.idTransact));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}