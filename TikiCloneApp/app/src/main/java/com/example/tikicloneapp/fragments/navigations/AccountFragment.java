package com.example.tikicloneapp.fragments.navigations;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.MyClass;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.AccountInfoActivity;
import com.example.tikicloneapp.activities.AddressActivity;
import com.example.tikicloneapp.activities.ChangePasswordActivity;
import com.example.tikicloneapp.activities.ListTransactActivity;
import com.example.tikicloneapp.activities.LoginActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.PurchasedProductActivity;
import com.example.tikicloneapp.activities.ReviewsActivity;
import com.example.tikicloneapp.activities.SettingActivity;
import com.example.tikicloneapp.models.Transact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.MyClass.convertDate;


public class AccountFragment extends Fragment {

    private LinearLayout lay_login, lay_accountInfo, lay_showHide2, lay_showHide3, lay_showHide4;
    private LinearLayout layBtn_orderManagment, layBtn_purchased, layBtn_viewedProds, layBtn_favoritePros, layBtn_prosInCart, layBtn_myComments;
    private LinearLayout lay_transact_tiki_received, lay_transact_delivering, lay_transact_success, lay_transact_cancel, lay_setting, lay_seller_receive, lay_reviews;
    private LinearLayout lay_change_password;
    private LinearLayout layBtn_address;
    private View view_line_nonlogin;
    public static LinearLayout lay_loading;
    private TextView tvFullName, tvEmail, tvCreated;
    private Button btnLogout;
    private ImageButton ibCart;
    private int idUser;
    private int REQUEST_CODE_UPDATE = 123;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initWidget(view);

        idUser = MainActivity.idUser;

        setEachView(idUser);

        return view;
    }

    private void initWidget(View view) {
        lay_login = view.findViewById(R.id.layout_login);
        lay_accountInfo = view.findViewById(R.id.layout_accountInfo);
        lay_showHide2 = view.findViewById(R.id.layout_show_hide_2);
        lay_showHide3 = view.findViewById(R.id.layout_show_hide_3);
        lay_showHide4 = view.findViewById(R.id.layout_show_hide_4);
        btnLogout = view.findViewById(R.id.button_logout);
        layBtn_orderManagment = view.findViewById(R.id.layout_orderManagement);
        layBtn_purchased = view.findViewById(R.id.layoutButton_purchasedProducts);
        layBtn_viewedProds = view.findViewById(R.id.layoutButton_viewedProducts);
        layBtn_favoritePros = view.findViewById(R.id.layoutButton_favoriteProducts);
        layBtn_prosInCart = view.findViewById(R.id.layoutButton_productsInCart);
        layBtn_myComments = view.findViewById(R.id.layoutButton_comment);
        tvFullName = view.findViewById(R.id.textView_fullName);
        tvEmail = view.findViewById(R.id.textView_email);
        tvCreated = view.findViewById(R.id.textView_created);
        ibCart = view.findViewById(R.id.imageButton_cart);
        layBtn_address = view.findViewById(R.id.layout_address);

        lay_transact_cancel = view.findViewById(R.id.layout_transact_cancel);
        lay_transact_delivering = view.findViewById(R.id.layout_transact_delivering);
        lay_seller_receive = view.findViewById(R.id.layout_seller_receive);
        lay_transact_success = view.findViewById(R.id.layout_transact_success);
        lay_reviews = view.findViewById(R.id.layout_reviews);
        lay_transact_tiki_received = view.findViewById(R.id.layout_transact_tiki_received);

        view_line_nonlogin = view.findViewById(R.id.view_line_non_login);
        lay_loading = view.findViewById(R.id.loadingPanel_parent);
        lay_setting = view.findViewById(R.id.layout_setting);
        lay_change_password = view.findViewById(R.id.lay_changePassword);
    }

    private void setEachView(final int idUser) {
        MyClass.callPanel(lay_loading, 800);


        lay_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else startActivity(intent);
            }
        });

        if (idUser == 0) {
            view_non_login();
        } else view_login(idUser);
    }

    private void view_non_login() {
        lay_login.setVisibility(View.VISIBLE);
        lay_accountInfo.setVisibility(View.GONE);
        lay_showHide2.setVisibility(View.GONE);
        lay_showHide3.setVisibility(View.GONE);
        lay_showHide4.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        lay_change_password.setVisibility(View.GONE);

        lay_login.setOnClickListener(mOnClickLogin);
        layBtn_orderManagment.setOnClickListener(mOnClickLogin);
        layBtn_purchased.setOnClickListener(mOnClickLogin);
        layBtn_viewedProds.setOnClickListener(mOnClickLogin);
        layBtn_favoritePros.setOnClickListener(mOnClickLogin);
        layBtn_prosInCart.setOnClickListener(mOnClickLogin);
        layBtn_myComments.setOnClickListener(mOnClickLogin);
        ibCart.setOnClickListener(mOnClickLogin);
    }

    private void view_login(int idUser) {
        view_line_nonlogin.setVisibility(View.GONE);
        lay_login.setVisibility(View.GONE);
        lay_accountInfo.setVisibility(View.VISIBLE);
        lay_showHide2.setVisibility(View.VISIBLE);
        lay_showHide3.setVisibility(View.VISIBLE);
        lay_showHide4.setVisibility(View.VISIBLE);
        layBtn_viewedProds.setVisibility(View.GONE);
        layBtn_favoritePros.setVisibility(View.GONE);
        layBtn_prosInCart.setVisibility(View.GONE);
        layBtn_myComments.setVisibility(View.GONE);
        getUser(idUser);

        btnLogout.setVisibility(View.VISIBLE);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbManager.updateData_User(0, MainActivity.idUser);
                getActivity().finishAffinity();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        layBtn_purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PurchasedProductActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, REQUEST_CODE_UPDATE, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            }
        });

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });

        lay_accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountInfoActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivityForResult(intent, REQUEST_CODE_UPDATE, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                } else {
                    startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            }
        });

        layBtn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                intent.putExtra("type", "user");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else startActivity(intent);
            }
        });

        lay_transact_tiki_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransact(Transact.STATUS_TIKI_RECEIVED);
            }
        });

        lay_transact_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransact(Transact.STATUS_CANCEL);
            }
        });

        layBtn_orderManagment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransact(Transact.CODE_GET_ALL);
            }
        });


        lay_seller_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransact(Transact.STATUS_PICKING_GOODS);
            }
        });
        lay_transact_delivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransact(Transact.STATUS_DELIVERING);
            }
        });
        lay_transact_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransact(Transact.STATUS_SUCCESS);
            }
        });
        lay_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReviewsActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else startActivity(intent);
            }
        });

        lay_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else startActivity(intent);
            }
        });


    }

    private void onClickTransact(int status){
        Intent intent = new Intent(getContext(), ListTransactActivity.class);
        intent.putExtra("status", status);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }else startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE) {
//            Log.d("thang", "work");
            getUser(MainActivity.idUser);
        }
    }

    View.OnClickListener mOnClickLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            } else {
                startActivity(intent);
            }
        }
    };

    public void getUser(final int idUser) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);

                    tvFullName.setText(userObject.getString("name"));

                    tvCreated.setText("Thành viên từ " +convertDate(userObject.getLong("created")));
                    if (userObject.getString("email").equals("null")) {
                        tvEmail.setVisibility(View.GONE);
                    } else {
                        tvEmail.setText(userObject.getString("email"));
                        tvEmail.setVisibility(View.VISIBLE);
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
                params.put("idUser", String.valueOf(idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
