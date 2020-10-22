package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.database.DBVolley;

public class SettingActivity extends AppCompatActivity {
    EditText edtIp;
    Button btnSave;
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initWidget();
        setEvent();
    }

    private void initWidget() {
        edtIp = findViewById(R.id.editText_ip);
        btnSave = findViewById(R.id.button_save);
        ibBack = findViewById(R.id.imageButton_back);
    }

    private void setEvent() {
        edtIp.setText(MainActivity.dbManager.getIPAddress());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbManager.updateIPAddress(edtIp.getText().toString().trim());
                MainActivity.ipAddress = edtIp.getText().toString().trim();
//                MainActivity.dbVolley.setIpAddress();
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SettingActivity.this).toBundle());
                } else startActivity(intent);
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });
    }

}