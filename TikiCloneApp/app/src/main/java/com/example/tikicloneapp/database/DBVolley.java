package com.example.tikicloneapp.database;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.activities.CartActivity;
import com.example.tikicloneapp.activities.LoginActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.ProductDetailActivity;
import com.example.tikicloneapp.adapters.CartProductAdapter;
import com.example.tikicloneapp.adapters.CatalogAdapter;
import com.example.tikicloneapp.adapters.CatalogGrParentsAdapter;
import com.example.tikicloneapp.adapters.CatalogParentAdapter;
import com.example.tikicloneapp.adapters.ProductListAdapter;
import com.example.tikicloneapp.adapters.ProductsAdapter;
import com.example.tikicloneapp.adapters.ReviewAdapter;
import com.example.tikicloneapp.adapters.ReviewedAdapter;
import com.example.tikicloneapp.adapters.TransactAdapter;
import com.example.tikicloneapp.adapters.WaitingReviewAdapter;
import com.example.tikicloneapp.enums.OrderBy;
import com.example.tikicloneapp.models.Catalog;
import com.example.tikicloneapp.models.CatalogGrandParent;
import com.example.tikicloneapp.models.CatalogParent;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.models.Rate;
import com.example.tikicloneapp.models.Transact;
import com.example.tikicloneapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.tikicloneapp.MyClass.getTextAddress;
import static com.example.tikicloneapp.activities.ListProductActivity.underlineTextView;

public class DBVolley {

    //    public static String IP_ADDRESS = "http://192.168.1.101/";
//    public static String IP_ADDRESS = "http://192.168.43.57/"; // 4G mobile
//    public static String IP_ADDRESS = "http://192.168.1.105/"; // wifi ChoAnhLamQUenNha
//    public static String IP_ADDRESS = "http://192.168.0.117/"; //wifi QuocViet
//    public String IP_ADDRESS = "http://tikiclone.maitrongvinh.cf/";
//    public String IP_ADDRESS = "https://vinhmai.tk/tiki/";
//    public String IP_ADDRESS = "http://192.168.1.107/server/"; // wifi ChoAnhLamQUenNha
    public String IP_ADDRESS = "https://hdh.webtaunhanh.com/";

    public String URL_GET_CATALOG = IP_ADDRESS + "getCatalog.php";
    public String URL_INSERT_CATALOG = IP_ADDRESS + "insertCatalog.php";
    public String URL_GET_CATALOG_PARENTS = IP_ADDRESS + "getCatalogParents.php";
    public String URL_GET_CATALOG_GRANDPARENTS = IP_ADDRESS + "getCatalogGrParents.php";
    public String URL_GET_PRODUCT = IP_ADDRESS + "getProduct.php";
    public String URL_GET_IMAGE_PRODUCT = IP_ADDRESS + "getImageProduct.php";
    public String URL_UPDATE_PRODUCT = IP_ADDRESS + "updateProduct.php";
    public String URL_CHECK_LOGIN = IP_ADDRESS + "checkLogin.php";
    public String URL_INSERT_USER = IP_ADDRESS + "insertUser.php";
    public String URL_GET_IMAGE_ADVERT = IP_ADDRESS + "getImageAdvertisement.php";
    public String URL_GET_USER = IP_ADDRESS + "getUser.php";
    public String URL_UPDATE_USER = IP_ADDRESS + "updateUser.php";
    public String URL_UPDATE_USER_ADDRESS = IP_ADDRESS + "updateUserAddress.php";
    public String URL_CHECK_TRANSACT = IP_ADDRESS + "checkTransact.php";
    public String URL_INSERT_TRANSACT = IP_ADDRESS + "insertTransact.php";
    public String URL_INSERT_ORDER = IP_ADDRESS + "insertOrder.php";
    public String URL_CHECK_ORDER = IP_ADDRESS + "checkOrder.php";
    public String URL_UPDATE_ORDER = IP_ADDRESS + "updateOrder.php";
    public String URL_GET_ORDER = IP_ADDRESS + "getOrder.php";
    public String URL_GET_CART = IP_ADDRESS + "getCart.php";
    public String URL_UPDATE_TRANSACT = IP_ADDRESS + "updateTransact.php";
    public String URL_GET_TRANSACT = IP_ADDRESS + "getTransact.php";
    public String URL_GET_TRANSACT_BY_ADMIN = IP_ADDRESS + "getTransactByAdmin.php";
    public String URL_GET_TRANSACT_BY_SHIPPER = IP_ADDRESS + "getTransactByShipper.php";
    public String URL_DELETE_ORDER = IP_ADDRESS + "deleteOrder.php";
    public String URL_GET_NAME_PRODUCT = IP_ADDRESS + "getNameProduct.php";
    public String URL_GET_NAME_TRANSACT = IP_ADDRESS + "getNameTransact.php";
    public String URL_UPDATE_PASSWORD = IP_ADDRESS + "updatePassword.php";
    public String URL_CHECK_QTY_PRODUCT = IP_ADDRESS + "checkQtyProduct.php";
    public String URL_INSERT_VIEWED_PRODUCT = IP_ADDRESS + "insertViewedProduct.php";
    public String URL_GET_VIEWED_PRODUCT = IP_ADDRESS + "getViewedProduct.php";
    public String URL_INSERT_SEARCH_KEY = IP_ADDRESS + "insertSearchKey.php";
    public String URL_GET_SEARCHES = IP_ADDRESS + "getSearches.php";
    public String URL_GET_RECOMMENDED_PRODUCTS = IP_ADDRESS + "getRecommendedProducts.php";
    public String URL_GET_RECOMMENDED_PRODUCTS_BY_PRODUCT_ID = IP_ADDRESS + "getRecommendedProductsByProductId.php";
    public String URL_GET_REVIEW_PRODUCTS_BY_USER_ID = IP_ADDRESS + "getReviewProductsByIdUser.php";
    public String URL_INSERT_RATE = IP_ADDRESS + "insertRate.php";
    public String URL_GET_REVIEW_PRODUCTS_BY_PRODUCT_ID = IP_ADDRESS + "getReviewProductsByProductId.php";
    public String URL_INSERT_SHIPPING = IP_ADDRESS + "insertShipping.php";


    private final Context context;

    public DBVolley(Context context) {
        this.context = context;
    }

    public void getCatalog(final ArrayList<Catalog> catalogArrayList, final CatalogAdapter catalogAdapter,
                           @Nullable final Integer idParents, @Nullable final Integer sold) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CATALOG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    catalogArrayList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Catalog catalog = new Catalog(
                                    object.getInt("id"),
                                    object.getString("name"),
                                    object.getInt("idParents"),
                                    object.getString("imageUrl")
                            );

                            catalogArrayList.add(catalog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("thang", response);
                    catalogAdapter.notifyDataSetChanged();
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
                if (idParents != null) {
                    params.put("idParents", String.valueOf(idParents));
                }
                if (sold != null) {
                    params.put("sold", String.valueOf(sold));
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public ArrayList<Catalog> getCatalog(final int idParents) {
        final ArrayList<Catalog> catalogArrayList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CATALOG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Catalog catalog = new Catalog(
                                    object.getInt("id"),
                                    object.getString("name"),
                                    object.getInt("idParents"),
                                    object.getString("imageUrl")
                            );
                            catalogArrayList.add(catalog);
//                            Log.d("thang", catalogArrayList.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("idParents", String.valueOf(idParents));
                return params;
            }
        };
        requestQueue.add(stringRequest);
        return catalogArrayList;
    }

    public void insertCatalog() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_CATALOG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Log.d("thang", response);
                        } else {
                            Log.d("thang", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "Error\n" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", "2");
                params.put("idParents", "2");
                params.put("imageUrl", "2");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getCatalogGrParents(final ArrayList<CatalogGrandParent> grandParents, final CatalogGrParentsAdapter adapter) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CATALOG_GRANDPARENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    grandParents.clear();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            CatalogGrandParent _grParent = new CatalogGrandParent(
                                    object.getInt("id"),
                                    object.getString("name"),
                                    object.getString("imageUrl")
                            );
//                            Log.d("thang", _grParent.toString());
                            grandParents.add(_grParent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
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
                })/*{
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idGrand", String.valueOf(idGrand));

                return params;
            }
        }*/;
        requestQueue.add(stringRequest);
    }

    public void getCatalogParents(final ArrayList<CatalogParent> catalogParentArrayList, final CatalogParentAdapter catalogParentAdapter, final int idGrand) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CATALOG_PARENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    catalogParentArrayList.clear();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            CatalogParent _Parent = new CatalogParent(
                                    object.getInt("id"),
                                    object.getString("name"),
                                    object.getInt("idCatalog_gr_parents")
                            );
//                            Log.d("thang", _Parent.toString());
                            catalogParentArrayList.add(_Parent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    catalogParentAdapter.notifyDataSetChanged();
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idGrand", String.valueOf(idGrand));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getProducts(final ArrayList<Product> productArrayList, final ProductListAdapter productListAdapter,
                            final TextView tvNonProduct, @Nullable final Integer idCatalog, @Nullable final ArrayList<Integer> idArray,
                            @Nullable final Integer views, @Nullable final Integer idParents, @Nullable final Integer status,
                            @Nullable final Integer idUser,
                            @Nullable final Integer priceFrom, @Nullable final Integer priceTo, @Nullable final Integer rate,
                            @Nullable final OrderBy orderCreated, @Nullable final OrderBy orderPrice,
                            @Nullable final OrderBy orderRate, @Nullable final OrderBy orderDiscount,
                            @Nullable final Integer limit, @Nullable final Integer start) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Product> productList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Product product = new Product(
                                    object.getInt("id"),
                                    object.getInt("idCatalog"),
                                    object.getString("name"),
//                                        object.getString("description"),
                                    object.getInt("price"),
                                    object.getInt("discount"),
//                                        object.getInt("created"),
//                                        object.getInt("views"),
                                    object.getInt("qty"),
                                    object.getDouble("rate"),
                                    object.getInt("rateQty")
                            );
                            productList.add(product);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    productArrayList.clear();

                    if (!productList.isEmpty()) {
                        tvNonProduct.setVisibility(View.GONE);
                        Toast.makeText(context, "Có " + productList.size() + " sản phẩm!", Toast.LENGTH_SHORT).show();
                        productArrayList.addAll(productList);
                    } else
                        tvNonProduct.setVisibility(View.VISIBLE);

                    productListAdapter.notifyDataSetChanged();
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
                if (idCatalog != null) {
                    params.put("idCatalog", String.valueOf(idCatalog));
                }
                if (idArray != null) {
                    params.put("idArray", idArray.toString());
                }
                if (views != null) {
                    params.put("views", String.valueOf(views));
                }
                if (idParents != null) {
                    params.put("idParents", String.valueOf(idParents));
                }
                if (idUser != null) {
                    params.put("idUser", String.valueOf(idUser));
                }
                if (status != null) {
                    params.put("status", String.valueOf(status));
                }
                if (priceFrom != null) {
                    params.put("priceFrom", String.valueOf(priceFrom));
                }
                if (priceTo != null) {
                    params.put("priceTo", String.valueOf(priceTo));
                }
                if (rate != null) {
                    params.put("rate", String.valueOf(rate));
                }
                if (orderCreated != null) {
                    switch (orderCreated) {
                        case ASC:
                            params.put("orderCreated", "ASC");
                            break;
                        case DESC:
                            params.put("orderCreated", "DESC");
                    }
                }

                if (orderPrice != null) {
                    switch (orderPrice) {
                        case ASC:
                            params.put("orderPrice", "ASC");
                            break;
                        case DESC:
                            params.put("orderPrice", "DESC");
                    }
                }

                if (orderRate != null) {
                    switch (orderRate) {
                        case ASC:
                            params.put("orderRate", "ASC");
                            break;
                        case DESC:
                            params.put("orderRate", "DESC");
                    }
                }

                if (orderDiscount != null) {
                    switch (orderDiscount) {
                        case ASC:
                            params.put("orderDiscount", "ASC");
                            break;
                        case DESC:
                            params.put("orderDiscount", "DESC");
                    }
                }

                if (limit != null) {
                    params.put("limit", String.valueOf(limit));
                }
                if (start != null) {
                    params.put("start", String.valueOf(start));
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getFirstImageProduct(final ImageView imageView, final int idProduct) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_IMAGE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray imageProductArray = new JSONArray(response);
                    JSONObject imgUrl = imageProductArray.getJSONObject(0);
                    String url = imgUrl.getString("imageUrl");
                    ProductListAdapter.loadImageFromUrl(url, imageView);

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
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateViewsProduct(final Product product) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                    Log.d("thang", "updateViewsProduct: " + response);
            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateViewsProduct: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(product.getId()));
                params.put("CODE", "UPDATE_VIEWS_PRODUCT");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getUserAccount(final String username, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "OnErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", String.valueOf(username));
                params.put("password", String.valueOf(password));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void checkTransact(final int idUser, final int idProduct, final int qty, final int amount) {
        final String non_exist = "non_exist";
        final String wrong_query = "wrong_query";
        final String non_account = "non_account";
        final String non_idUser = "non_idUser";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "checkTransact: "+ response);
                if (response.equals(non_exist)) {
                    insertTransact(idUser, idProduct, qty, amount);
                    return;
                }
                if (response.equals(wrong_query)) {
                    return;
                }
                if (response.equals(non_account)) {
                    return;
                }
                if (response.equals(non_idUser)) {
                    return;
                }

                // response equal idTransact
                int idTransact_current = Integer.parseInt(response);

                checkOrder(idTransact_current, idProduct, qty, amount);
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

    public void insertTransact(final int idUser, final int idProduct, final int qty, final int amount) {
        final String insert_success = "insert_success";
        final String insert_fail = "insert_fail";
        final String non_idUser = "non_idUser";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "InsertTransact: " + response);

                if (response.equals(insert_fail)) {
                    Toast.makeText(context, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.equals(non_idUser)) {
                    return;
                }

                //response equal insert_success
                checkTransact(idUser, idProduct, qty, amount);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "InsertTransact: " + error.toString());
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

    public void insertOrder(final int idTransact, final int idProduct, final int qty, final int amount) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.d("thang", "Insert order: " + response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "Insert order: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(idTransact));
                params.put("idProduct", String.valueOf(idProduct));
                params.put("qty", String.valueOf(qty));
                params.put("amount", String.valueOf(amount));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void checkOrder(final int idTransact, final int idProduct, final int qty, final int amount) {
        final String not_exist = "not_exist";
        final String wrong_query = "wrong_query";
        final String non_post = "non_post";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "checkOrder: " + response);
                if (response.equals(not_exist)) {
                    insertOrder(idTransact, idProduct, qty, amount);
                    return;
                }
                if (response.equals(wrong_query)) {

                    return;
                }
                if (response.equals(non_post)) {

                    return;
                }

                // response is idOrder
                int idOrder = Integer.parseInt(response);
                String CODE_UPDATE_ORDER_ADD_ONE = "CODE_UPD_ORDER_ADD_ONE";
                updateOrder(CODE_UPDATE_ORDER_ADD_ONE, idOrder, qty, amount);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "checkOrder: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(idTransact));
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateOrder(final String CODE_UPDATE, final int idOrder, final int qty, final int amount) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "updateOrder: " + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateOrder: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("CODE", CODE_UPDATE);
                params.put("id", String.valueOf(idOrder));
                params.put("qty", String.valueOf(qty));
                params.put("amount", String.valueOf(amount));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getOrder(final int idTransact, final ArrayList<Order> orderArrayList, final CartProductAdapter adapter) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    orderArrayList.clear();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Order order = new Order(
                                    object
                            );
//                            Log.d("thang", _grParent.toString());
                            orderArrayList.add(order);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
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

    public void getTransact(final ArrayList<Transact> transactArrayList, final TransactAdapter adapter,
                            @Nullable final Integer status, @Nullable final Integer idUser, @Nullable final LinearLayout layPanel_nonOrder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("thang", "getTransact: ");

                    transactArrayList.clear();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Transact transact = new Transact(
                                    object
                            );
//                            Log.d("thang", _grParent.toString());
                            if (transact.getmStatus() != 0) {
                                transactArrayList.add(transact);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (transactArrayList.isEmpty()) {
                        assert layPanel_nonOrder != null;
                        layPanel_nonOrder.setVisibility(View.VISIBLE);
                    } else {
                        layPanel_nonOrder.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (status != null) {
                    params.put("status", String.valueOf(status));
                }
                if (idUser != null) {
                    params.put("id_user", String.valueOf(idUser));
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void checkTransact_goToCart(final int idUser) {
        if (idUser == 0) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("CODE", 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Activity) context).startActivityForResult(intent, ProductDetailActivity.REQUEST_CODE_LOGIN, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
            } else
                ((Activity) context).startActivityForResult(intent, ProductDetailActivity.REQUEST_CODE_LOGIN);

            return;
        }
        final String non_exist = "non_exist";
        final String wrong_query = "wrong_query";
        final String non_account = "non_account";
        final String non_idUser = "non_idUser";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "checkTransact: "+ response);
                if (response.equals(non_exist)) {
                    insertTransact_goToCart(idUser);
                    return;
                }
                if (response.equals(wrong_query)) {
                    return;
                }
                if (response.equals(non_account)) {
                    return;
                }
                if (response.equals(non_idUser)) {
                    return;
                }

                // response equal idTransact
                int idTransact_current = Integer.parseInt(response);
                updateTransact(idTransact_current);

                Intent intent = new Intent(context, CartActivity.class);
                intent.putExtra("idTransact", idTransact_current);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
                } else
                    context.startActivity(intent);
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

    public void insertTransact_goToCart(final int idUser) {
        final String insert_success = "insert_success";
        final String insert_fail = "insert_fail";
        final String non_idUser = "non_idUser";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "InsertTransact: " + response);

                if (response.equals(insert_fail)) {
                    Toast.makeText(context, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.equals(non_idUser)) {
                    return;
                }

                //response equal insert_success
                checkTransact_goToCart(idUser);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "InsertTransact: " + error.toString());
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

    public void updateTransact(final int idTransact) {
        final String update_success = "update_success";
        final String update_fail = "update_fail";
        final String non_value = "non_value";
        final String wrong_query = "wrong_query";
        final String non_post = "non_post";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "updateTransact: "+ response);
                Log.d("thang", "DBVolley - updateTransact: " + response + "; idTransact: " + idTransact +
                        "");

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
                    return;
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
                params.put("idTransact", String.valueOf(idTransact));
                params.put("code", "CODE_UPDATE_PRICE");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateStatusTransact(final int idTransact, final Integer status, final Integer shipperId) {
        final String update_success = "update_success";
        final String update_fail = "update_fail";
        final String non_value = "non_value";
        final String wrong_query = "wrong_query";
        final String non_post = "non_post";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "updateTransact: " + response);

                if (response.equals(update_success)) {

                    if (status == Transact.STATUS_CANCEL) {
                        Toast.makeText(context, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                    }
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
                    return;
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
                params.put("idTransact", String.valueOf(idTransact));
                if (status != null) {
                    params.put("status", status.toString());
                }
                Timestamp modified = new Timestamp(System.currentTimeMillis());
                params.put("modified", modified.toString());
                if (shipperId != null) {
                    params.put("idShipper", shipperId.toString());
                }

                params.put("code", "CODE_UPDATE_TRANSACT");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateTransact(final Transact transact) {
        final String update_success = "update_success";
        final String update_fail = "update_fail";
        final String non_value = "non_value";
        final String wrong_query = "wrong_query";
        final String non_post = "non_post";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "updateTransact: " + response);
//                Log.d("thang", transact.getmCreated()+"");

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
                    return;
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
                Log.d("thang", "updateTransactBefore: " + transact.toString());
                params.put("idTransact", String.valueOf(MainActivity.idTransact));
                if (transact.getmStatus() != null) {
                    params.put("status", String.valueOf(transact.getmStatus()));
                }
                if (transact.getmUser_name() != null) {
                    params.put("user_name", transact.getmUser_name());
                }
                if (transact.getmUser_phone() != null) {
                    params.put("user_phone", transact.getmUser_phone());
                }
                if (transact.getmProvince() != null) {
                    params.put("province", transact.getmProvince());
                }
                if (transact.getmDistrict() != null) {
                    params.put("district", transact.getmDistrict());
                }
                if (transact.getmWard() != null) {
                    params.put("ward", transact.getmWard());
                }
                if (transact.getmAddress() != null) {
                    params.put("address", transact.getmAddress());
                }
                if (transact.getmQty() != null) {
                    params.put("qty", String.valueOf(transact.getmQty()));
                }
                if (transact.getmAmount() != null) {
                    params.put("amount", String.valueOf(transact.getmAmount()));
                }
                if (transact.getmMessage() != null) {
                    params.put("message", transact.getmMessage());
                }
                if (transact.getmCreated() != null) {
                    params.put("created", transact.getmCreated().toString());
                    Log.d("created", transact.getmCreated() + "");
                }
                params.put("code", "CODE_UPDATE_TRANSACT");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public static void getAddress(Context context, final TextView textView) {
        if (MainActivity.idUser == 0) {
            underlineTextView(textView, "");
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);
                    if (userObject.getString("province").equals("null")) {
                        underlineTextView(textView, "Thêm địa chỉ giao hàng");
                    } else {
                        User user = new User(
                                userObject.getString("province"),
                                userObject.getString("district"),
                                userObject.getString("ward")
                        );
                        underlineTextView(textView, getTextAddress(user.getmProvince(), user.getmDistrict(), user.getmWard()));
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(MainActivity.idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getViewedProducts(final LinearLayout layout, final ArrayList<Product> viewedProducts,
                                  final ProductsAdapter productsAdapter, final int idUser,
                                  final Integer limit) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_VIEWED_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            viewedProducts.clear();
//                            Log.d(TAG, "onResponse: "+ response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonProduct = jsonArray.getJSONObject(i);
                                    Product _product = new Product(
                                            jsonProduct.getInt("id"),
                                            jsonProduct.getInt("idCatalog"),
                                            jsonProduct.getString("name"),
                                            jsonProduct.getInt("price"),
                                            jsonProduct.getInt("discount"),
                                            jsonProduct.getInt("qty"),
                                            jsonProduct.getDouble("rate"),
                                            jsonProduct.getInt("rateQty"),
                                            jsonProduct.getString("imageUrl")
                                    );
                                    viewedProducts.add(_product);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (viewedProducts.size() == 0 && layout != null) {
                                layout.setVisibility(View.GONE);
                            } else {
                                productsAdapter.notifyDataSetChanged();
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
                        Log.d("thang", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(idUser));
                if (limit != null) {
                    params.put("limit", String.valueOf(limit));
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getRecommendedProducts(final LinearLayout layout, final ArrayList<Product> products,
                                       final ProductsAdapter recommendedAdapter) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_RECOMMENDED_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            ArrayList<Product> _products = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject product = jsonArray.getJSONObject(i);
                                    Product _product = new Product(
                                            product.getInt("id"),
                                            product.getInt("idCatalog"),
                                            product.getString("name"),
                                            product.getInt("price"),
                                            product.getInt("discount"),
                                            product.getInt("qty"),
                                            product.getInt("sold"),
                                            product.getString("imageUrl"),
                                            product.getDouble("rate"),
                                            product.getInt("rateQty")

                                    );
                                    _products.add(_product);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (_products.isEmpty() && layout != null) {
                                layout.setVisibility(View.GONE);
                            } else {
                                products.clear();
                                products.addAll(_products);
                                recommendedAdapter.notifyDataSetChanged();
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
                        Log.d("thang", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(MainActivity.idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getRecommendedProductsByProductId(final int idProduct, final ArrayList<Product> products,
                                                  final ProductsAdapter recommendedAdapter) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_RECOMMENDED_PRODUCTS_BY_PRODUCT_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d(TAG, "getRecommendedProductsByProductId: " + response);

                            ArrayList<Product> _products = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject product = jsonArray.getJSONObject(i);
                                    Product _product = new Product(
                                            product.getInt("id"),
                                            product.getInt("idCatalog"),
                                            product.getString("name"),
                                            product.getInt("price"),
                                            product.getInt("discount"),
                                            product.getInt("qty"),
                                            product.getString("imageUrl"),
                                            product.getDouble("rate"),
                                            product.getInt("rateQty")
                                    );
                                    _products.add(_product);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (_products.isEmpty()) {

                            } else {
                                products.clear();
                                products.addAll(_products);
                                recommendedAdapter.notifyDataSetChanged();
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
                        Log.d("thang", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getWaitingReviewProducts(final ArrayList<Product> products,
                                         final WaitingReviewAdapter waitingReviewAdapter,
                                         final LinearLayout layout_none) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_REVIEW_PRODUCTS_BY_USER_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse: " + response);
                        try {
                            ArrayList<Product> _products = new ArrayList<>();
//                            Log.d("thang", "getReviewProducts: " + response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject product = jsonArray.getJSONObject(i);
                                    Product _product = new Product(
                                            product.getInt("id"),
                                            product.getString("name"),
                                            product.getInt("price"),
                                            product.getInt("discount"),
                                            product.getString("imageUrl")

                                    );

                                    _products.add(_product);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            products.clear();
                            if (_products.isEmpty()) {
                                layout_none.setVisibility(View.VISIBLE);
                            } else {
                                layout_none.setVisibility(View.GONE);
                                products.addAll(_products);
                            }
                            waitingReviewAdapter.notifyDataSetChanged();

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(MainActivity.idUser));
                params.put("status", "1"); // status =1: waitingReviews
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getReviewedProductsByIdUser(final ArrayList<Rate> rates,
                                            final ReviewedAdapter reviewedAdapter,
                                            final LinearLayout layout_none) {
        //status =1: waitingReviews ; 2: reviewed

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_REVIEW_PRODUCTS_BY_USER_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse: " + response);
                        try {
                            ArrayList<Rate> _rates = new ArrayList<>();
//                            Log.d("thang", "getReviewedProducts: " + response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonRate = jsonArray.getJSONObject(i);
                                    Rate rate = new Rate(
                                            jsonRate.getInt("id"),
                                            jsonRate.getInt("ratePoint"),
                                            jsonRate.getString("comment"),
                                            jsonRate.getInt("idProduct"),
                                            jsonRate.getString("productName"),
                                            jsonRate.getInt("price"),
                                            jsonRate.getInt("discount"),
                                            jsonRate.getString("imageProductUrl"),
                                            jsonRate.getInt("idUser")
                                    );
                                    _rates.add(rate);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            rates.clear();
                            if (_rates.isEmpty()) {
                                layout_none.setVisibility(View.VISIBLE);
                            } else {
                                layout_none.setVisibility(View.GONE);
                                rates.addAll(_rates);
                            }
                            reviewedAdapter.notifyDataSetChanged();

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(MainActivity.idUser));
                params.put("status", "2");// reviewed
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getReviewedProductsByProductId(final ArrayList<Rate> rates,
                                               final ReviewAdapter reviewAdapter, final int productId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_REVIEW_PRODUCTS_BY_PRODUCT_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Rate> _rates = new ArrayList<>();
                            Log.d("thang", "getReviewedProductsByProductId: " + response);
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonRate = jsonArray.getJSONObject(i);
                                    Rate rate = new Rate(
                                            jsonRate.getInt("id"),
                                            jsonRate.getInt("ratePoint"),
                                            jsonRate.getString("comment"),
                                            Timestamp.valueOf(jsonRate.getString("createdAt")),
                                            Timestamp.valueOf(jsonRate.getString("modifiedAt")),
                                            jsonRate.getInt("idUser"),
                                            jsonRate.getString("userFullName")
                                    );
                                    _rates.add(rate);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            rates.clear();
                            rates.addAll(_rates);
                            reviewAdapter.notifyDataSetChanged();

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(productId));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void insertRate(final Rate rate) {
        final String insert_success = "insert_success";
        final String insert_fail = "insert_fail";
        final String non_idUser = "non_idUser";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_RATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "insertRate: " + response);

                if (response.equals(insert_fail)) {
                    Toast.makeText(context, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
                //response equal insert_success
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "InsertTransact: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(rate.getIdUser()));
                params.put("idProduct", String.valueOf(rate.getIdProduct()));
                params.put("ratePoint", String.valueOf(rate.getRatePoint()));
                params.put("comment", rate.getComment());
                params.put("createdAt", rate.getCreatedAt().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //example code volley
    public void example(final int idUser) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_IMAGE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "example: " + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "example: " + error.toString());
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

    public void getOrder_updateProduct(final int status) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Order order = new Order(
                                    object.getInt("id"),
                                    object.getInt("idProduct"),
                                    object.getInt("qty"),
                                    object.getInt("amount")
                            );
                            if (status == Transact.STATUS_CANCEL) {
                                updateQtyProduct(order.getmIdProduct(), order.getmQty());
                            } else if (status == Transact.STATUS_TIKI_RECEIVED) {
                                updateQtyProduct(order.getmIdProduct(), -order.getmQty());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                        Log.d("thang", error.toString());
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


    public void updateQtyProduct(final int idProduct, final int qty) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "updateQtyProduct: " + response);
            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateQtyProduct: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idProduct));
                params.put("CODE", "UPDATE_PRODUCT");
                params.put("qty", qty + "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}

