package com.example.tikicloneapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.CartProductAdapter;
import com.example.tikicloneapp.captureActs.CaptureAct;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Transact;
import com.example.tikicloneapp.models.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.tikicloneapp.MyClass.getTextAddress;
import static com.example.tikicloneapp.activities.CartActivity.formatCurrency;
import static com.example.tikicloneapp.models.Transact.setTvStatus;

public class AdminTransactActivity extends AppCompatActivity {
    private static final String TAG = AdminTransactActivity.class.getSimpleName();

    private TextView tvIdTransact, tvTimeCreated, tvStatus, tvUserName, tvPhoneNumber, tvAddress,
            tvPriceProvisional, tvPriceLast, tvShippingFee,
            tvShipperId, tvShipperName, tvShipperPhoneNumber;
    private Button btnCancelTransact, btnConfirmTransact;
    private ImageButton ibBack;
    private RecyclerView rvCart;
    private LinearLayout layout_loading;

    private Transact transact;

    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private CartProductAdapter cartProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transact);

        transact = (Transact) getIntent().getSerializableExtra("transact");

        initWidget();
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
        tvShippingFee = findViewById(R.id.textView_shippingFee);
        btnConfirmTransact = findViewById(R.id.button_confirmTransact);
        tvShipperId = findViewById(R.id.textView_shipperId);
        tvShipperName = findViewById(R.id.textView_shipperName);
        tvShipperPhoneNumber = findViewById(R.id.textView_shipperPhoneNumber);
    }

    @SuppressLint("SetTextI18n")
    private void setEachView() {


        btnCancelTransact.setVisibility(View.GONE);
        if (AdminManagementActivity.role == User.ROLE_ADMIN) {
            if (transact.getmStatus() != Transact.STATUS_TIKI_RECEIVED) {
                btnConfirmTransact.setVisibility(View.GONE);
            }
        } else if (AdminManagementActivity.role == User.ROLE_SHIPPER) {
            if (transact.getmStatus() != Transact.STATUS_PICKING_GOODS && transact.getmStatus() != Transact.STATUS_DELIVERING) {
                btnConfirmTransact.setVisibility(View.GONE);
            }
        }

        setTransact();

        setCartProductAdapter();

        btnConfirmTransact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AdminManagementActivity.role == User.ROLE_SHIPPER) {
                    switch (transact.getmStatus()) {
                        case Transact.STATUS_PICKING_GOODS:
                            AdminManagementActivity.dbVolley.updateStatusTransact(transact.getmId(), Transact.STATUS_DELIVERING, null);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAfterTransition();
                            } else finish();

                            Intent intent = new Intent();
                            intent.putExtra("status", transact.getmStatus());
                            setResult(RESULT_OK, intent);

                            break;
                        case Transact.STATUS_DELIVERING:
                            scanCode();
                            break;
                    }

                } else if (AdminManagementActivity.role == User.ROLE_ADMIN) {
                    new InsertShipping().execute();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else finish();
                    Intent intent = new Intent();
                    intent.putExtra("status", transact.getmStatus());
                    setResult(RESULT_OK, intent);
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

    private class InsertShipping extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put("idTransact", transact.getmId() + "");
            String response = AdminManagementActivity.httpHandler.performPostCall(AdminManagementActivity.dbVolley.URL_INSERT_SHIPPING, params);
//            Log.d(TAG, "InsertShipping: " + response);
            return Integer.valueOf(response);
        }

        @Override
        protected void onPostExecute(Integer shipperId) {
            super.onPostExecute(shipperId);
            AdminManagementActivity.dbVolley.updateStatusTransact(transact.getmId(), Transact.STATUS_PICKING_GOODS, shipperId);
        }
    }

    private void setCartProductAdapter() {
//        MyClass.callPanel(layout_loading, 700);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(rvCart.getContext(), R.drawable.divider_product_cart));

        cartProductAdapter = new CartProductAdapter(this, orderArrayList, R.layout.row_item_cart_transact);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
//        rvCart.addItemDecoration(itemDecoration);
        rvCart.setAdapter(cartProductAdapter);
        resetRvCart();
    }

    public void resetRvCart() {
        AdminManagementActivity.dbVolley.getOrder(transact.getmId(), orderArrayList, cartProductAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setTransact() {
        setTvStatus(tvStatus, transact.getmStatus());

        tvIdTransact.setText(String.valueOf(transact.getmId()));
        if (transact.getmStatus() != Transact.STATUS_TIKI_RECEIVED) {
            btnCancelTransact.setVisibility(View.GONE);
        }

        String address = getTextAddress(transact.getmProvince(), transact.getmDistrict(), transact.getmWard(), transact.getmAddress());
        tvUserName.setText(transact.getmUser_name());
        tvPhoneNumber.setText(transact.getmUser_phone());
        tvAddress.setText(address);

        if (transact.getmCreated() != null) {
            Date createdAt = new Date(transact.getmCreated().getTime());
            @SuppressLint("SimpleDateFormat")
            String dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(createdAt);
            tvTimeCreated.setText(dateFormat);
        }

        if (transact.getShipperId() != null) {
            tvShipperId.setText(String.valueOf(transact.getShipperId()));

        }
        tvShipperName.setText("Đang cập nhật");
        tvShipperPhoneNumber.setText("Đang cập nhật");


        tvShippingFee.setText(formatCurrency(transact.getShippingFee()));

        String price = formatCurrency(transact.getmAmount());
        String priceLast = formatCurrency(transact.getmAmount() + transact.getShippingFee());

        tvPriceProvisional.setText(price);
        tvPriceLast.setText(priceLast);
    }

    void scanCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        } else {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setCaptureActivity(CaptureAct.class);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.setPrompt("Scanning Code");
            intentIntegrator.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {

                if (result.getContents().equals(String.valueOf(transact.getmId()))) {
                    AdminManagementActivity.dbVolley.updateStatusTransact(transact.getmId(), Transact.STATUS_SUCCESS, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else finish();
                    Intent intent = new Intent();
                    intent.putExtra("status", transact.getmStatus());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(this, "Giao hàng thành công!!!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Quét thất bại!!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}