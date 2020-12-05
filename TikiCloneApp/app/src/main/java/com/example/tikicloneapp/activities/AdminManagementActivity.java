package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;

public class AdminManagementActivity extends AppCompatActivity {
    public static DBManager dbManager;
    public static DBVolley dbVolley;
    public static int idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_management);

        dbManager = new DBManager(this);
        dbVolley = new DBVolley(this);

        checkIdUser();

    }

    void checkIdUser(){
        int idUserOld = dbManager.getIdUser();
        idUser = getIntent().getIntExtra("idUser", idUserOld);
        Log.d("thang", "checkIdUser: " + idUser + "; idUserOld: " + idUserOld);
//        if(idUserOld == 0){
//            dbManager.clearAllData_Order();
//        }

        if(idUser!=idUserOld){
            dbManager.updateData_User(idUser, idUserOld);
        }
    }
}