package com.example.tikicloneapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.handlers.HttpHandler;
import com.example.tikicloneapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminManagementActivity extends AppCompatActivity {
    public static DBManager dbManager;
    public static DBVolley dbVolley;
    public static int role;

    public final static HttpHandler httpHandler = new HttpHandler();
    public static int idUser;
    private static final String TAG = AdminManagementActivity.class.getSimpleName();

    private ImageButton ibLogout;
    private Button btnManageOrder, btnInsertShipper;
    private TextView tvLabel;
    private TextView tvName, tvPhoneNumber, tvAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_management);
        initWidget();

        dbManager = new DBManager(this);
        dbVolley = new DBVolley(this);
        role = getIntent().getIntExtra("role", 0);

        setViews();
        checkIdUser();
    }

    private void initWidget() {
        ibLogout = findViewById(R.id.imageButton_logout);
        btnManageOrder = findViewById(R.id.button_manageOrder);
        btnInsertShipper = findViewById(R.id.button_insertShipper);
        tvLabel = findViewById(R.id.textView_label);
        tvName = findViewById(R.id.textView_name);
        tvPhoneNumber = findViewById(R.id.textView_phoneNumber);
        tvAmount = findViewById(R.id.textView_amount);
    }

    @SuppressLint("SetTextI18n")
    private void setViews() {
        if (role == User.ROLE_SHIPPER) {
            tvLabel.setText("Trang Shipper");
            btnInsertShipper.setVisibility(View.GONE);
        }
        setOnClick();
    }

    private void setOnClick() {
        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.updateData_User(0, idUser);
                finishAffinity();
                Intent intent = new Intent(AdminManagementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnManageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManagementActivity.this, AdminTransactManagementActivity.class);
                startActivity(intent);
            }
        });
        btnInsertShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminManagementActivity.this, "Tính năng này sẽ được cập nhật trong tương lai!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void checkIdUser() {
        int idUserOld = dbManager.getIdUser();
        idUser = getIntent().getIntExtra("idUser", idUserOld);
//        Log.d("thang", "checkIdUser: " + idUser + "; idUserOld: " + idUserOld+ "; roleId: "+ role);

        if (idUser != idUserOld) {
            dbManager.updateData_User(idUser, idUserOld);
        }
        setAccount(idUser);
    }
    public void setAccount(final int idUser) {
        final int CODE_MALE = 1,
                CODE_FEMALE = 2;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);

                    tvName.setText(userObject.getString("name"));
                    tvPhoneNumber.setText(userObject.getString("phoneNumber"));

                    Log.d(TAG, "amount: " + userObject.getString("amount"));

                    String amount = CartActivity.formatCurrency(userObject.getInt("amount"));
                    tvAmount.setText(amount);
//
//                    if (userObject.getString("email").equals("null")) {
//                        ipLayoutEmail.getEditText().setText("");
//                    } else {
//                        ipLayoutEmail.getEditText().setText(userObject.getString("email"));
//                        ipLayoutEmail.setEnabled(false);
//                    }
//
//                    if (userObject.getString("birthdate").equals("null")) {
//                        edtBirthDate.setText("");
//                    } else {
//                        edtBirthDate.setText(userObject.getString("birthdate"));
//                    }
//
//                    if (userObject.getInt("sex") == CODE_MALE) {
//                        rbMale.setChecked(true);
//                    } else if (userObject.getInt("sex") == CODE_FEMALE) {
//                        rbFemale.setChecked(true);
//                    }

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
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}