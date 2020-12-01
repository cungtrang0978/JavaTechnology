package com.example.tikicloneapp.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.CartActivity;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.ProductDetailActivity;
import com.example.tikicloneapp.models.Order;
import com.example.tikicloneapp.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.MyClass.setTextView_StrikeThrough;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Order> mOrderArrayList;
    private int mResource;
    private int mActivity = -1;

    private int CODE_BUTTON_MINUS = 0;
    private int CODE_BUTTON_PLUS = 1;

    public static int CONFIRMATION_ACTIVITY = 0;
    public static int ORDER_SUCCESS_ACTIVITY = 1;

    public CartProductAdapter(Context mContext, ArrayList<Order> orders, int mResource) {
        this.mContext = mContext;
        this.mOrderArrayList = orders;
        this.mResource = mResource;
    }

    public CartProductAdapter(Context mContext, ArrayList<Order> orders, int mResource, int CODE_ACTIVITY) {
        this.mContext = mContext;
        this.mOrderArrayList = orders;
        this.mResource = mResource;
        this.mActivity = CODE_ACTIVITY;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View cartProductView = layoutInflater.inflate(mResource, parent, false);
        ViewHolder viewHolder = new ViewHolder(cartProductView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Order order = mOrderArrayList.get(position);
        final int idProduct = order.getmIdProduct();

        if (mActivity != ORDER_SUCCESS_ACTIVITY) {
            holder.tvCount.setText(order.getmQty() + "");
        }
        if (mResource == R.layout.row_cart) {
            if (order.getmQty() == 1) {
                holder.ibMinus.setEnabled(false);
            }
            holder.ibPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkQtyProduct(idProduct, order.getmId(), holder, CODE_BUTTON_PLUS);
                }
            });
            holder.ibMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkQtyProduct(idProduct, order.getmId(), holder, CODE_BUTTON_MINUS);
                }
            });

            holder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateQtyProduct_DeleteOrder(idProduct, holder, order.getmId());
                }
            });
        }

        if (mResource == R.layout.row_item_cart_transact) {
            holder.tvIdOrder.setText(String.valueOf(order.getmId()));
        }

        MainActivity.dbVolley.getFirstImageProduct(holder.ivProduct, idProduct);
        getProduct(idProduct, holder);
    }

    @Override
    public int getItemCount() {
        return mOrderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvName, tvPriceOrigin, tvPriceDiscount, tvCount, tvPrice, tvIdOrder;
        private ImageButton ibMinus, ibPlus, ibDelete;
        private LinearLayout layout_row_cart, layout_child_confirmation, layout_row_confirmation, layout_row_item_cart_transact;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.textView_nameProduct);
            ivProduct = itemView.findViewById(R.id.imageView_product);
            tvCount = itemView.findViewById(R.id.textView_count);


            if(mResource == R.layout.row_cart){
                tvName = itemView.findViewById(R.id.textView_nameProduct);
                ivProduct = itemView.findViewById(R.id.imageView_product);
                tvCount = itemView.findViewById(R.id.textView_count);
                tvPriceDiscount = itemView.findViewById(R.id.textView_priceDiscount);
                tvPriceOrigin = itemView.findViewById(R.id.textView_priceOrigin);
                ibMinus = itemView.findViewById(R.id.imageButton_minus);
                ibPlus = itemView.findViewById(R.id.imageButton_plus);
                layout_row_cart = itemView.findViewById(R.id.layout_rowCart);
                ibDelete = itemView.findViewById(R.id.imageButton_delete);
                setTextView_StrikeThrough(tvPriceOrigin);
                return;
            }

            if(mResource==R.layout.row_item_cart_transact){
                tvName = itemView.findViewById(R.id.textView_nameProduct);
                ivProduct = itemView.findViewById(R.id.imageView_product);
                tvCount = itemView.findViewById(R.id.textView_count);
                tvPriceDiscount = itemView.findViewById(R.id.textView_priceDiscount);
                tvIdOrder = itemView.findViewById(R.id.textView_idOrder);
                layout_row_item_cart_transact= itemView.findViewById(R.id.layout_row_item_cart_transact);
                return;
            }

            if(mResource==R.layout.row_comfirmation){
                tvName = itemView.findViewById(R.id.textView_nameProduct);
                ivProduct = itemView.findViewById(R.id.imageView_product);
                tvCount = itemView.findViewById(R.id.textView_count);
                tvPrice = itemView.findViewById(R.id.textView_price);
                layout_row_confirmation = itemView.findViewById(R.id.layout_row_confirmation);
                layout_child_confirmation = itemView.findViewById(R.id.layout_child_confirmation);
                if (mActivity == ORDER_SUCCESS_ACTIVITY) {
                    layout_child_confirmation.setVisibility(View.GONE);
                    layout_row_confirmation.setBackground(null);
                }
            }

            /*if (mResource == R.layout.row_cart || mResource == R.layout.row_item_cart_transact) {
                tvPriceDiscount = itemView.findViewById(R.id.textView_priceDiscount);
            }

            if (mResource == R.layout.row_cart) {
                tvPriceOrigin = itemView.findViewById(R.id.textView_priceOrigin);
                ibMinus = itemView.findViewById(R.id.imageButton_minus);
                ibPlus = itemView.findViewById(R.id.imageButton_plus);
                layout_rowCart = itemView.findViewById(R.id.layout_rowCart);
                ibDelete = itemView.findViewById(R.id.imageButton_delete);
                setTextView_StrikeThrough(tvPriceOrigin);
            } else if (mResource == R.layout.row_confirmation) {
                tvPrice = itemView.findViewById(R.id.textView_price);
                row_confirmation = itemView.findViewById(R.id.row_confirmation);
                layout_confirmation = itemView.findViewById(R.id.layout_child_confirmation);
                if (mActivity == ORDER_SUCCESS_ACTIVITY) {
                    layout_confirmation.setVisibility(View.GONE);
                    row_confirmation.setBackground(null);
                }
            } else if (mResource == R.layout.row_item_cart_transact) {
                tvIdOrder = itemView.findViewById(R.id.textView_idOrder);
            }*/
        }
    }

    private void gotoProductDetailActi(Product product){
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra("product", product);
        Activity activity = (Activity) mContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityForResult(intent, ListProductActivity.REQUEST_CODE, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
        } else
            activity.startActivityForResult(intent, ListProductActivity.REQUEST_CODE);
    }

    private void getProduct(final int idProduct, final ViewHolder holder) {
        String URL_GET_PRODUCT = MainActivity.dbVolley.URL_GET_PRODUCT;
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject productObject = jsonArray.getJSONObject(0);
                    final Product product = new Product(
                            productObject.getInt("id"),
                            productObject.getInt("idCatalog"),
                            productObject.getString("name"),
                            productObject.getString("description"),
                            productObject.getInt("price"),
                            productObject.getInt("discount"),
                            productObject.getInt("created"),
                            productObject.getInt("views"),
                            productObject.getInt("qty")
                    );

                    String name = productObject.getString("name");
                    holder.tvName.setText(name);

                    int discount = productObject.getInt("discount");
                    int priceOrigin = productObject.getInt("price");
                    int priceDiscount = priceOrigin - priceOrigin * discount / 100;

                    if (mResource == R.layout.row_cart) {
                        String priceOriginString = CartActivity.formatCurrency(priceOrigin);
                        String priceDiscountString = CartActivity.formatCurrency(priceDiscount);
                        holder.tvPriceOrigin.setText(priceOriginString);
                        holder.tvPriceDiscount.setText(priceDiscountString);
                        holder.layout_row_cart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoProductDetailActi(product);
                            }
                        });
                    } else if (mResource == R.layout.row_comfirmation) {
                        int price = Integer.parseInt(holder.tvCount.getText().toString()) * priceDiscount;
                        String priceString = CartActivity.formatCurrency(price);
                        holder.tvPrice.setText(priceString);
                        holder.layout_row_confirmation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoProductDetailActi(product);
                            }
                        });
                    }else if(mResource==R.layout.row_item_cart_transact){
                        String priceDiscountString = CartActivity.formatCurrency(priceDiscount);
                        holder.tvPriceDiscount.setText(priceDiscountString);
                        holder.layout_row_item_cart_transact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoProductDetailActi(product);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void checkQtyProduct(final int idProduct, final int idOrder, final ViewHolder holder, final int CODE) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "checkQtyProduct: " + response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    Product product = new Product(object.getInt("price"), object.getInt("discount"), object.getInt("qty"));
                    int qty = product.getQty();
                    int discount = product.getDiscount();
                    int priceOrigin = product.getPrice();
                    int priceDiscount = priceOrigin - priceOrigin * discount / 100;

                    int count = Integer.parseInt(holder.tvCount.getText().toString());

                    int timeDelayUpdateTransact = 250;
                    int timeDelayGetCart = 550;

                    if (CODE == CODE_BUTTON_PLUS) {
                        if (qty > count) {
                            count++;
                            holder.tvCount.setText(count + "");
                            int priceOrder = count * priceDiscount;
                            updateOrder("CODE_UPD_ORDER", idOrder, count, priceOrder);
//                            qty--;
//                            updateQtyProduct(idProduct, qty);

                            if (mContext instanceof CartActivity) {
                                ((CartActivity) mContext).callLoadingPanel();
                            }

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.dbVolley.updateTransact(MainActivity.idTransact);
                                }
                            }, timeDelayUpdateTransact);

                            holder.ibMinus.setEnabled(true);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mContext instanceof CartActivity) {
                                        ((CartActivity) mContext).getCart();
                                    }
                                }
                            }, timeDelayGetCart);
                        } else {
                            Toast.makeText(mContext, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (CODE == CODE_BUTTON_MINUS) {
                        count--;
                        if (count == 1) {
                            holder.ibMinus.setEnabled(false);
                        }
                        int priceOrder = count * priceDiscount;
                        updateOrder("CODE_UPD_ORDER", idOrder, count, priceOrder);
//                        qty++;
//                        updateQtyProduct(idProduct, qty);

                        if (mContext instanceof CartActivity) {
                            ((CartActivity) mContext).callLoadingPanel();
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.dbVolley.updateTransact(MainActivity.idTransact);
                            }
                        }, timeDelayUpdateTransact);

                        holder.tvCount.setText(count + "");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mContext instanceof CartActivity) {
                                    ((CartActivity) mContext).getCart();
                                }
                            }
                        }, timeDelayGetCart);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "checkQtyProduct: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void updateOrder(final String CODE_UPDATE, final int idOrder, final int qty, final int amount) {

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_UPDATE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.d("thang", "updateOrder: " + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateOrder: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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

    public void deleteOrder(final int idOrder, final String nameProduct) {
        final String delete_success = "delete_success";
        String delete_fail = "delete_fail";
        String non_post = "non_post";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_DELETE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "deleteOrder: " + response);
                if (response.equals(delete_success)) {
                    Toast.makeText(mContext, "Đã xóa " + nameProduct + '!', Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "deleteOrder: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idOrder));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateQtyProduct_DeleteOrder(final int idProduct, final ViewHolder viewHolder, final int idOrder) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "updateQtyProductAfterDeleteOrder: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject product = jsonArray.getJSONObject(0);

                    int qty = product.getInt("qty");
                    qty += Integer.parseInt(viewHolder.tvCount.getText().toString());

                    deleteOrder(idOrder, product.getString("name"));
//                    updateQtyProduct(idProduct, qty);

                    if (mContext instanceof CartActivity) {
                        ((CartActivity) mContext).callLoadingPanel();
                    }


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.dbVolley.updateTransact(MainActivity.idTransact);
                        }
                    }, 150);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mContext instanceof CartActivity) {
                                ((CartActivity) mContext).resetRvCart();
                                ((CartActivity) mContext).getCart();
                            }
                        }
                    }, 350);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateQtyProductAfterDeleteOrder: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateQtyProduct_DeleteOrder(final int idProduct, final int count, final int idOrder) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "updateQtyProductAfterDeleteOrder: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject product = jsonArray.getJSONObject(0);

                    int qty = product.getInt("qty");
                    qty += count;

                    deleteOrder(idOrder, product.getString("name"));
//                    updateQtyProduct(idProduct, qty);

                    if (mContext instanceof CartActivity) {
                        ((CartActivity) mContext).callLoadingPanel();
                    }


                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.dbVolley.updateTransact(MainActivity.idTransact);
                        }
                    }, 150);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mContext instanceof CartActivity) {
                                ((CartActivity) mContext).resetRvCart();
                                ((CartActivity) mContext).getCart();
                            }
                        }
                    }, 350);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateQtyProductAfterDeleteOrder: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idProduct", String.valueOf(idProduct));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}