package com.example.tikicloneapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.MyClass;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.CartProductAdapter;
import com.example.tikicloneapp.handlers.HttpHandler;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Transact;
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.example.tikicloneapp.MyClass.getTextAddress;
import static com.example.tikicloneapp.activities.CartActivity.formatCurrency;
import static com.example.tikicloneapp.activities.MainActivity.dbVolley;
import static com.example.tikicloneapp.models.Transact.setTvStatus;

public class TransactActivity extends AppCompatActivity {

    private static final String TAG = TransactActivity.class.getSimpleName();

    private TextView tvIdTransact, tvTimeCreated, tvStatus, tvUserName, tvPhoneNumber,
            tvAddress, tvPriceProvisional, tvPriceLast, tvShippingFee;
    private Button btnCancelTransact;
    private ImageButton ibBack;
    private RecyclerView rvCart;
    private LinearLayout layout_loading;
    private ImageView ivQRCode;

    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private CartProductAdapter cartProductAdapter;

    private Bitmap bitmap;

    private int idTransact;
    private int idActivity;

    private HttpHandler httpHandler = new HttpHandler();
    final Handler handler = new Handler();

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
        ivQRCode = findViewById(R.id.imageView_qrCode);
        tvShippingFee = findViewById(R.id.textView_shippingFee);
    }

    private void setEachView() {
        MyClass.callPanel(layout_loading, 700);

        new WaitingForScanning().execute();

        tvIdTransact.setText(String.valueOf(idTransact));

        setTransact();

        setCartProductAdapter();

        btnCancelTransact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbVolley.updateStatusTransact(idTransact, Transact.STATUS_CANCEL, null);
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

    private void setIvQRCode() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(String.valueOf(idTransact), null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            ivQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
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
//                    Log.d("thang", "transact: " + transact.toString());

                    if (transact.getmCreated() != null) {
                        Date createdAt = new Date(transact.getmCreated().getTime());
                        @SuppressLint("SimpleDateFormat")
                        String dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(createdAt);
                        tvTimeCreated.setText(dateFormat);
                    }

                    if (transact.getmStatus() == Transact.STATUS_DELIVERING) {
                        setIvQRCode();
                    }

                    String priceLast = formatCurrency(transact.getmAmount() + transact.getShippingFee());

                    tvPriceProvisional.setText(formatCurrency(transact.getmAmount()));
                    tvPriceLast.setText(priceLast);
                    tvShippingFee.setText(formatCurrency(transact.getShippingFee()));
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

    private class WaitingForScanning extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... booleans) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "WaitingForScanning: loading");
            return isSuccessTransact();
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
                setResult(RESULT_OK);
            } else {
                new WaitingForScanning().execute();
            }
        }


        private Boolean isSuccessTransact() {
            HashMap<String, String> params = new HashMap<>();
            params.put("idTransact", String.valueOf(idTransact));

            String response = httpHandler.performPostCall(MainActivity.dbVolley.URL_GET_TRANSACT, params);

            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject object = jsonArray.getJSONObject(0);

                int status = object.getInt("status");
                if (status == Transact.STATUS_SUCCESS) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}