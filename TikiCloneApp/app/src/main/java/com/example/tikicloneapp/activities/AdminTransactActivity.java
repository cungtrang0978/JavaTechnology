package com.example.tikicloneapp.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.MyClass;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.CartProductAdapter;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Transact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.tikicloneapp.MyClass.getTextAddress;
import static com.example.tikicloneapp.activities.CartActivity.formatCurrency;
import static com.example.tikicloneapp.models.Transact.setTvStatus;

public class AdminTransactActivity extends AppCompatActivity {
    private TextView tvIdTransact, tvTimeCreated, tvStatus, tvUserName, tvPhoneNumber, tvAddress,
            tvPriceProvisional, tvPriceLast, tvShippingFee;
    private Button btnCancelTransact;
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
    }
    private void setEachView() {
        MyClass.callPanel(layout_loading, 700);
        tvIdTransact.setText(String.valueOf(transact.getmId()));

        setTransact();

        setCartProductAdapter();

//        btnCancelTransact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbVolley.updateStatusTransact(idTransact, Transact.STATUS_CANCEL);
//                MainActivity.dbVolley.getOrder_updateProduct(Transact.STATUS_CANCEL);
//                if (idActivity == R.layout.activity_order_success) {
//                    Intent intent = new Intent(TransactActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                } else if (idActivity == R.layout.activity_list_transact) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        finishAfterTransition();
//                    } else finish();
//                    setResult(RESULT_OK);
//                }
//            }
//        });

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
        AdminManagementActivity.dbVolley.getOrder(transact.getmId(), orderArrayList, cartProductAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setTransact(){
        setTvStatus(tvStatus, transact.getmStatus());

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

        tvShippingFee.setText(formatCurrency(transact.getShippingFee()));

        String price = formatCurrency(transact.getmAmount());

        tvPriceProvisional.setText(price);
        tvPriceLast.setText(price);
    }
}