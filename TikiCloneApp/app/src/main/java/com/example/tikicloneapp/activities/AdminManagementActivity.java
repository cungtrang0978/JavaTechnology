package com.example.tikicloneapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.handlers.HttpHandler;
import com.example.tikicloneapp.models.User;


public class AdminManagementActivity extends AppCompatActivity {
    public static DBManager dbManager;
    public static DBVolley dbVolley;
    public static int role;

    public final static HttpHandler httpHandler = new HttpHandler();
    public static int idUser;
    private static final String TAG = AdminManagementActivity.class.getSimpleName();

    private ImageButton ibLogout;
    private Button btnManageOrder;
    private TextView tvLabel;


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
        tvLabel = findViewById(R.id.textView_label);


    }

    @SuppressLint("SetTextI18n")
    private void setViews() {
        if (role == User.ROLE_SHIPPER) {
            tvLabel.setText("Trang Shipper");
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
    }


    void checkIdUser() {
        int idUserOld = dbManager.getIdUser();
        idUser = getIntent().getIntExtra("idUser", idUserOld);
//        Log.d("thang", "checkIdUser: " + idUser + "; idUserOld: " + idUserOld+ "; roleId: "+ role);

        if (idUser != idUserOld) {
            dbManager.updateData_User(idUser, idUserOld);
        }
    }


}