package com.example.tikicloneapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.handlers.HttpHandler;

import java.util.HashMap;


public class AdminManagementActivity extends AppCompatActivity {
    public static DBManager dbManager;
    public static DBVolley dbVolley;
    public static int role;

    public final static HttpHandler httpHandler = new HttpHandler();
    public static int idUser;
    private static final String TAG = AdminManagementActivity.class.getSimpleName();

    private ImageButton ibLogout;
    private Button btnManageOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_management);
        initWidget();
        setViews();
        dbManager = new DBManager(this);
        dbVolley = new DBVolley(this);
        role = getIntent().getIntExtra("role", 0);

        checkIdUser();
    }

    private void initWidget() {
        ibLogout = findViewById(R.id.imageButton_logout);
        btnManageOrder = findViewById(R.id.button_manageOrder);
    }

    private void setViews() {
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
    }

    void checkIdUser() {
        int idUserOld = dbManager.getIdUser();
        idUser = getIntent().getIntExtra("idUser", idUserOld);
        Log.d("thang", "checkIdUser: " + idUser + "; idUserOld: " + idUserOld);
//        if(idUserOld == 0){
//            dbManager.clearAllData_Order();
//        }

        if (idUser != idUserOld) {
            dbManager.updateData_User(idUser, idUserOld);
        }
//        new TestTask().execute();
    }

    private class TestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put("idUser", String.valueOf(idUser));
            String jsonStr = httpHandler.performPostCall(dbVolley.URL_GET_USER, params);
            if (jsonStr != null) {
                Log.d(TAG, "checkIdUser: " + jsonStr);
            }

            return null;
        }
    }


}