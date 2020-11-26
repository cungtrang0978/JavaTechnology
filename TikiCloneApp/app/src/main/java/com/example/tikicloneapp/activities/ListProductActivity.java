package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
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
import com.example.tikicloneapp.adapters.ProductsAdapter;
import com.example.tikicloneapp.database.DBVolley;

import com.example.tikicloneapp.models.Catalog;
import com.example.tikicloneapp.models.CatalogGrandParent;
import com.example.tikicloneapp.models.CatalogParent;
import com.example.tikicloneapp.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.example.tikicloneapp.database.DBVolley.getAddress;

public class ListProductActivity extends AppCompatActivity {
    RecyclerView rvProductList;
    ProductListAdapter productAdapter;
    ProductsAdapter productsAdapter;
    ArrayList<Product> products = new ArrayList<>();
    DBVolley dbVolley = new DBVolley(this);
    Catalog catalog;
    CatalogParent cataParent;
    CatalogGrandParent cataGrand;
    TextView tvNonProduct, tvTitle;

    String keySearch;

    public static int REQUEST_CODE = 234;

    private ImageButton ibBack, ibCart, ibMenu;
    private LinearLayout layoutSearchName;
    public LinearLayout lay_loading_parent;
    private TextView tvName, tvAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        initWidget();
        setEachView();

    }

    private void setEachView() {

        MyClass.callPanel(lay_loading_parent, 1000);
        if (getIntent().getSerializableExtra("catalog") != null) {
            catalog = (Catalog) getIntent().getSerializableExtra("catalog");
            tvName.setText(catalog.getmName());
            setAdapterRecyclerView(null);
        }
        if (getIntent().getSerializableExtra("catalogParent") != null) {
            cataParent = (CatalogParent) getIntent().getSerializableExtra("catalogParent");
            tvName.setText(cataParent.getmName());
            setAdapterRecyclerView(null);
        }

        if (getIntent().getStringExtra("keySearch") != null) {
            keySearch = getIntent().getStringExtra("keySearch");
            tvName.setText(keySearch);
            getNameProduct();
        }

        if (getIntent().getStringExtra("viewed") != null) {
            setViewedProductsAdapter();
            layoutSearchName.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
        }

        getAddress(getApplicationContext(), tvAddress);
        setOnClick();
    }

    private void setAdapterRecyclerView(@Nullable ArrayList<Integer> idArray) {
        productAdapter = new ProductListAdapter(this, R.layout.row_product, products);
        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvProductList.getContext(), DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_recyclerview));
//        rvProductList.addItemDecoration(itemDecoration);
        rvProductList.setAdapter(productAdapter);

        if (catalog != null) {
            dbVolley.getProducts(products, productAdapter, tvNonProduct, catalog.getmId(), null, null, null, null, null);
            return;
        }
        if (cataParent != null) {
            dbVolley.getProducts(products, productAdapter, tvNonProduct, null, null, null, cataParent.getmId(), null, null);
            return;
        }
        dbVolley.getProducts(products, productAdapter, tvNonProduct, null, idArray, null, null, null, null);
    }

    private void setViewedProductsAdapter() {
        productsAdapter = new ProductsAdapter(this, R.layout.row_product, products, ProductsAdapter.ProductType.CODE_PRODUCT_LIST);
        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductList.setAdapter(productsAdapter);

        dbVolley.getViewedProducts(null, products, productsAdapter, MainActivity.idUser, null);
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

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProductActivity.this, AddressActivity.class);
                intent.putExtra("type", "listProduct");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, REQUEST_CODE, ActivityOptions.makeSceneTransitionAnimation(ListProductActivity.this).toBundle());
                } else startActivityForResult(intent, REQUEST_CODE);
            }
        });

        layoutSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProductActivity.this, SearchActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ListProductActivity.this).toBundle());
                } else startActivity(intent);
            }
        });
    }

    private void initWidget() {
        rvProductList = findViewById(R.id.recyclerView_productList);
        ibBack = findViewById(R.id.imageButton_back);
        ibCart = findViewById(R.id.imageButton_cart);
        ibMenu = findViewById(R.id.imageButton_menu);
        layoutSearchName = findViewById(R.id.layout_searchName);
        tvName = findViewById(R.id.textView_searchName);
        tvAddress = findViewById(R.id.textView_address);
        lay_loading_parent = findViewById(R.id.loadingPanel_parent);
        tvAddress = findViewById(R.id.textView_address);
        tvNonProduct = findViewById(R.id.textView_nonProduct);
        tvTitle = findViewById(R.id.textView_titleReview);
    }

    public static void underlineTextView(TextView tv, String address) {
        SpannableString content = new SpannableString(address);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv.setText(content);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getAddress(getApplicationContext(), tvAddress);
        }
    }


    public void getNameProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_NAME_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Integer> idArray = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(response);

                    String convert_KeySearch = convertName(keySearch);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Product product = new Product(object.getInt("id"), object.getString("name"));

                            String convert_name = convertName(product.getName());

                            if (convert_name.contains(convert_KeySearch)) {
                                idArray.add(product.getId());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    if (idArray.size() > 0) {
                        Toast.makeText(ListProductActivity.this, "Có " + idArray.size() + " sản phẩm!", Toast.LENGTH_SHORT).show();
                        setAdapterRecyclerView(idArray);
                    } else
                        tvNonProduct.setVisibility(View.VISIBLE);
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
                });
        requestQueue.add(stringRequest);
    }

    public static String convertName(String name) {
        String convert;
        String name_nonSpace = name.replaceAll("\\s+", "");

        String temp = Normalizer.normalize(name_nonSpace, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        convert = pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
        return convert.toUpperCase();
    }
}
