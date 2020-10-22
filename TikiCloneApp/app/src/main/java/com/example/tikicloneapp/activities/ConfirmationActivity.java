package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.tikicloneapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.MyClass.getTextAddress;

public class ConfirmationActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private TextView tvChangeCart, tvNamePhone, tvAddress, tvPriceProvisional,
            tvPriceLast, tvNonAddress, tvChangeInfoOrder;
    private Button btnOrder;
    private RecyclerView rvCart;

    private CartProductAdapter cartProductAdapter;
    private ArrayList<Order> orderArrayList = new ArrayList<>();

    private int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        initWidget();
        setEachView();

    }

    private void initWidget() {
        ibBack = findViewById(R.id.imageButton_back);
        rvCart = findViewById(R.id.recyclerView_cart);
        tvChangeCart = findViewById(R.id.textView_changeCart);
        tvNamePhone = findViewById(R.id.textView_name_phoneNumber);
        tvAddress = findViewById(R.id.textView_address);
        tvPriceProvisional = findViewById(R.id.textView_priceProvisional);
        tvPriceLast = findViewById(R.id.textView_priceLast);
        btnOrder = findViewById(R.id.button_order);
        tvNonAddress = findViewById(R.id.textView_nonAddress);
        tvChangeInfoOrder = findViewById(R.id.textView_changeInfoOrder);
    }

    private void setEachView() {
        setCartProductAdapter();

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });

        tvChangeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });

        getTransact();

        tvChangeInfoOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationActivity.this, AddressActivity.class);
                intent.putExtra("type", "transact");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, REQUEST_CODE, ActivityOptions.makeSceneTransitionAnimation(ConfirmationActivity.this).toBundle());
                } else startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAddress.getText().toString().isEmpty()) {
                    Toast.makeText(ConfirmationActivity.this, "Bạn chưa thêm địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long timeCreated = new Date().getTime(); //get current time
                Transact transact = new Transact();
                transact.setmStatus(Transact.STATUS_TIKI_RECEIVED);

                transact.setmCreated(timeCreated);
//                Log.d("thang", transact.getmCreated()+"");

                MainActivity.dbVolley.updateTransact(transact);
//                updateTransact(getApplicationContext(), Transact.STATUS_TIKI_RECEIVED, null, null, null, null, null, null);
                MainActivity.dbVolley.getOrder_updateProduct(Transact.STATUS_TIKI_RECEIVED); // update qty product

                Intent intent = new Intent(ConfirmationActivity.this, OrderSuccessActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ConfirmationActivity.this).toBundle());
                } else startActivity(intent);
            }
        });
    }

    public void getTransact() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "getTransact: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    Transact transact = new Transact(
                            object.getInt("id_user"),
                            object.getString("user_name"),
                            object.getString("user_phone"),
                            object.getString("province"),
                            object.getString("district"),
                            object.getString("ward"),
                            object.getString("address"),
                            object.getInt("qty"),
                            object.getInt("amount")
                    );
                    boolean goto_getUser = false;
                    if (transact.getmUser_name().equals("null")) {
                        goto_getUser = true;
                    } else {
                        setTextNamePhone(transact.getmUser_name(), transact.getmUser_phone());
                    }

                    // check address of transact. if isExist, will get address of transact. else get address of user
                    if (transact.getmProvince().equals("null")) {
                        goto_getUser = true;
                    } else {
                        String address = getTextAddress(transact.getmProvince(), transact.getmDistrict(), transact.getmWard(), transact.getmAddress());
                        tvAddress.setText(address);
                    }

                    if (goto_getUser) {
                        getUser(transact.getmId_User());
                    }
                    String price = CartActivity.formatCurrency(transact.getmAmount());

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
                        Log.d("thang", "getTransact: " + error.toString());
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

    private void getUser(final int idUser) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);
                    User user = new User(
                            userObject.getString("name"),
                            userObject.getString("phoneNumber"),
                            userObject.getString("province"),
                            userObject.getString("district"),
                            userObject.getString("ward"),
                            userObject.getString("address")
                    );
                    boolean updateNamePhone = false;
                    boolean updateAddress = false;
                    if (tvNamePhone.getText().toString().isEmpty()) {
                        setTextNamePhone(user.getmName(), user.getmPhoneNumber());
                        updateNamePhone = true;
                    }

                    if (tvAddress.getText().toString().isEmpty()) {
                        if (user.getmAddress().equals("null")) {
                            tvAddress.setVisibility(View.GONE);
                            tvNonAddress.setVisibility(View.VISIBLE);
                        } else {
                            tvNonAddress.setVisibility(View.GONE);
                            tvAddress.setVisibility(View.VISIBLE);
                            String address = getTextAddress(user.getmProvince(), user.getmDistrict(), user.getmWard(), user.getmAddress());
                            tvAddress.setText(address);
                            updateAddress = true;
                        }
                    }
                    if (updateNamePhone && updateAddress) {
                        updateTransact(getApplicationContext(), null, user.getmName(), user.getmPhoneNumber(), user.getmProvince(), user.getmDistrict(), user.getmWard(),
                                user.getmAddress());
                    } else if (updateNamePhone) {
                        updateTransact(getApplicationContext(), null, user.getmName(), user.getmPhoneNumber(), null, null, null, null);
                    } else if (updateAddress) {
                        updateTransact(getApplicationContext(), null, null, null, user.getmProvince(), user.getmDistrict(), user.getmWard(),
                                user.getmAddress());
                    }
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void updateTransact(Context context, @Nullable final Integer status,
                                      @Nullable final String user_name, @Nullable final String user_phone,
                                      @Nullable final String province, @Nullable final String district,
                                      @Nullable final String ward, @Nullable final String address) {
        final String update_success = "update_success";
        final String update_fail = "update_fail";
        final String non_value = "non_value";
        final String wrong_query = "wrong_query";
        final String non_post = "non_post";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_UPDATE_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "updateTransact: " + response);

                if (response.equals(update_success)) {
                    return;
                }
                if (response.equals(update_fail)) {
                    return;
                }
                if (response.equals(non_value)) {
                    return;
                }
                if (response.equals(wrong_query)) {
                    return;
                }
                if (response.equals(non_post)) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateTransact: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(MainActivity.idTransact));
                if (status != null) {
                    params.put("status", status.toString());
                }
                if (user_name != null) {
                    params.put("user_name", user_name);
                }
                if (user_phone != null) {
                    params.put("user_phone", user_phone);
                }
                if (province != null) {
                    params.put("province", province);
                }
                if (district != null) {
                    params.put("district", district);
                }
                if (ward != null) {
                    params.put("ward", ward);
                }
                if (address != null) {
                    params.put("address", address);
                }
                params.put("code", "CODE_UPDATE_TRANSACT");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setTextNamePhone(String name, String phone) {
        tvNamePhone.setText(name + " - " + phone);
    }


    private void setCartProductAdapter() {
        rvCart.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(rvCart.getContext(), R.drawable.divider_item_confirmation));

        cartProductAdapter = new CartProductAdapter(this, orderArrayList, R.layout.row_comfirmation, CartProductAdapter.CONFIRMATION_ACTIVITY);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.addItemDecoration(itemDecoration);
        rvCart.setAdapter(cartProductAdapter);
        MainActivity.dbVolley.getOrder(MainActivity.idTransact, orderArrayList, cartProductAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            tvNonAddress.setVisibility(View.GONE);
            tvAddress.setVisibility(View.VISIBLE);
            getTransact();
        }
    }


}