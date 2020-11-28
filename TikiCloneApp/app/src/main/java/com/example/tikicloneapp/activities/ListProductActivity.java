package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.tikicloneapp.enums.OrderBy;
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

    String keySearch;
    Integer priceFrom, priceTo, rate, limit, start;
    OrderBy orderCreated, orderPrice;

    Boolean isFrom5Stars = false, isFrom4Stars = false, isFrom3Stars = false;

    public static int REQUEST_CODE = 234;

    private ImageButton ibBack, ibCart, ibMenu;
    private LinearLayout layoutSearchName;
    public LinearLayout lay_loading_parent, lay_loading_recyclerView;
    private TextView tvName, tvAddress;
    ImageButton ibFilter;
    TextView tvNonProduct, tvTitle;
    TextView tvPrice, tvCreated;

    //dialog
    TextView tvExit, tvUnSelect;
    EditText edtPriceFrom, edtPriceTo;
    CardView cv5Stars, cv4Stars, cv3Stars;
    Button btnFilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        initWidget();
        setEachView();

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
        tvCreated = findViewById(R.id.textView_created);
        tvPrice = findViewById(R.id.textView_price);
        ibFilter = findViewById(R.id.imageButton_filter);
        lay_loading_recyclerView = findViewById(R.id.loadingPanel_recyclerView);
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

    private void refreshRecyclerView() {
//        MyClass.callPanel(lay_loading_parent, 1000);
        MyClass.callPanel(lay_loading_recyclerView, 4000);
        if (catalog != null || cataParent != null) {
            setAdapterRecyclerView(null);
            return;
        }
        if (keySearch != null) {
            getNameProduct();
        }
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_filter);
        initWidgetFilterDialog(dialog);
        setFilterDialogViews();

        int selectedColor = -1, unSelectedColor = -1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            selectedColor = this.getColor(R.color.colorSelectedStar);
            unSelectedColor = this.getColor(R.color.colorUnselectedStar);
        }
        final int finalSelectedColor = selectedColor;
        final int finalUnSelectedColor = unSelectedColor;
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        tvUnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPriceFrom.setText(null);
                edtPriceTo.setText(null);
                isFrom3Stars = isFrom4Stars = isFrom5Stars = false;
                cv5Stars.setCardBackgroundColor(finalUnSelectedColor);
                cv4Stars.setCardBackgroundColor(finalUnSelectedColor);
                cv3Stars.setCardBackgroundColor(finalUnSelectedColor);
            }
        });


        cv5Stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv5Stars.setCardBackgroundColor(finalSelectedColor);
                cv4Stars.setCardBackgroundColor(finalUnSelectedColor);
                cv3Stars.setCardBackgroundColor(finalUnSelectedColor);

                isFrom5Stars = true;
                isFrom4Stars = false;
                isFrom3Stars = false;
            }
        });

        cv4Stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv5Stars.setCardBackgroundColor(finalUnSelectedColor);
                cv4Stars.setCardBackgroundColor(finalSelectedColor);
                cv3Stars.setCardBackgroundColor(finalUnSelectedColor);

                isFrom5Stars = false;
                isFrom4Stars = true;
                isFrom3Stars = false;
            }
        });

        cv3Stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv5Stars.setCardBackgroundColor(finalUnSelectedColor);
                cv4Stars.setCardBackgroundColor(finalUnSelectedColor);
                cv3Stars.setCardBackgroundColor(finalSelectedColor);

                isFrom5Stars = false;
                isFrom4Stars = false;
                isFrom3Stars = true;
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (edtPriceFrom.getText().toString().isEmpty()) {
                    priceFrom = null;
                } else {
                    priceFrom = Integer.valueOf(edtPriceFrom.getText().toString());
                }

                if (edtPriceTo.getText().toString().isEmpty()) {
                    priceTo = null;
                } else {
                    priceTo = Integer.valueOf(edtPriceTo.getText().toString());
                }

                if (isFrom3Stars) {
                    rate = 3;
                } else if (isFrom4Stars) {
                    rate = 4;
                } else if (isFrom5Stars) {
                    rate = 5;
                } else {
                    rate = null;
                }
                refreshRecyclerView();

            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void setFilterDialogViews() {
        int selectedColor = -1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            selectedColor = this.getColor(R.color.colorSelectedStar);
        }
        final int finalSelectedColor = selectedColor;

        if (priceFrom != null) {
            edtPriceFrom.setText(priceFrom + "");
        }
        if (priceTo != null) {
            edtPriceTo.setText(priceTo + "");
        }

        if (isFrom3Stars) {
            cv3Stars.setCardBackgroundColor(finalSelectedColor);
        } else if (isFrom4Stars) {
            cv4Stars.setCardBackgroundColor(finalSelectedColor);
        } else if (isFrom5Stars) {
            cv5Stars.setCardBackgroundColor(finalSelectedColor);

        }
    }

    private void initWidgetFilterDialog(Dialog dialog) {
        tvExit = dialog.findViewById(R.id.tv_exit);
        tvUnSelect = dialog.findViewById(R.id.tv_unSelect);
        edtPriceFrom = dialog.findViewById(R.id.editText_priceFrom);
        edtPriceTo = dialog.findViewById(R.id.editText_priceTo);
        cv5Stars = dialog.findViewById(R.id.cardView_5stars);
        cv4Stars = dialog.findViewById(R.id.cardView_4stars);
        cv3Stars = dialog.findViewById(R.id.cardView_3stars);
        btnFilter = dialog.findViewById(R.id.button_filter);
    }

    private void setAdapterRecyclerView(@Nullable ArrayList<Integer> idArray) {
        productAdapter = new ProductListAdapter(this, R.layout.row_product, products);
        rvProductList.setLayoutManager(new GridLayoutManager(this, 2));
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(rvProductList.getContext(), DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_recyclerview));
//        rvProductList.addItemDecoration(itemDecoration);
        rvProductList.setAdapter(productAdapter);

        if (catalog != null) {
            dbVolley.getProducts(products, productAdapter, tvNonProduct, catalog.getmId(), null,
                    null, null, null, null, priceFrom, priceTo,
                    rate, orderCreated, orderPrice, limit, start);
            return;
        }
        if (cataParent != null) {
            dbVolley.getProducts(products, productAdapter, tvNonProduct, null, null,
                    null, cataParent.getmId(), null, null, priceFrom, priceTo,
                    rate, orderCreated, orderPrice, limit, start);
            return;
        }
        dbVolley.getProducts(products, productAdapter, tvNonProduct, null, idArray,
                null, null, null, null, priceFrom, priceTo,
                rate, orderCreated, orderPrice, limit, start);
    }

    private void setViewedProductsAdapter() {
        productsAdapter = new ProductsAdapter(this, R.layout.row_product, products,
                ProductsAdapter.ProductType.CODE_PRODUCT_LIST);
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
        tvCreated.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (orderCreated == null) {
                    orderCreated = OrderBy.DESC;
                    tvCreated.setText("Mới nhất");
                } else if (orderCreated == OrderBy.DESC) {
                    orderCreated = OrderBy.ASC;
                    tvCreated.setText("Cũ nhất");
                } else {
                    orderCreated = OrderBy.DESC;
                    tvCreated.setText("Mới nhất");
                }
                refreshRecyclerView();
            }
        });
        tvPrice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (orderPrice == null) {
                    orderPrice = OrderBy.DESC;
                    tvPrice.setText("Giá giảm dần");
                } else if (orderPrice == OrderBy.DESC) {
                    orderPrice = OrderBy.ASC;
                    tvPrice.setText("Giá tăng dần");
                } else {
                    orderPrice = OrderBy.DESC;
                    tvPrice.setText("Giá giảm dần");
                }
                refreshRecyclerView();
            }
        });
        ibFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("thang", "onClick: showdialog");
                showFilterDialog();
            }
        });
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
                        tvNonProduct.setVisibility(View.GONE);
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
