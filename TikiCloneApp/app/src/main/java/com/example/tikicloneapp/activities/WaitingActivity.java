package com.example.tikicloneapp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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
import com.example.tikicloneapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WaitingActivity extends AppCompatActivity {
    final Handler handler = new Handler();
    DBManager dbManager;
    DBVolley dbVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);


        dbManager = new DBManager(this);
        dbVolley = new DBVolley(this);
        int idUserOld = dbManager.getIdUser();
        if (idUserOld != 0) {
            getUser(idUserOld);
        } else {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WaitingActivity.this, MainActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WaitingActivity.this).toBundle());
                    } else startActivity(intent);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finishAffinity();
                        }
                    }, 1500);
                    //goto Next Activity;
                }
            }, 3000);
        }


    }

    public void getUser(final int idUser) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);
                    final User user = new User(userObject.getInt("id"), userObject.getInt("roleId"));

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            if (user.getmRoleId() == User.ROLE_ADMIN) {
                                intent.putExtra("role", User.ROLE_USER);
                                intent = new Intent(WaitingActivity.this, AdminManagementActivity.class);
                            } else if (user.getmRoleId() == User.ROLE_USER) {

                                intent = new Intent(WaitingActivity.this, MainActivity.class);
                            } else if (user.getmRoleId() == User.ROLE_SHIPPER) {
                                intent.putExtra("role", User.ROLE_USER);
                                intent = new Intent(WaitingActivity.this, AdminManagementActivity.class);
                            }

                            //goto Next Activity;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WaitingActivity.this).toBundle());
                            } else startActivity(intent);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finishAffinity();
                                }
                            }, 1500);
                        }
                    }, 3000);

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
}
