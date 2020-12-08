package com.example.tikicloneapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.captureActs.CaptureAct;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.handlers.HttpHandler;
import com.example.tikicloneapp.models.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class AdminManagementActivity extends AppCompatActivity {
    public static DBManager dbManager;
    public static DBVolley dbVolley;
    public static int role;

    public final static HttpHandler httpHandler = new HttpHandler();
    public static int idUser;
    private static final String TAG = AdminManagementActivity.class.getSimpleName();

    private ImageButton ibLogout, ibScan;
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
        ibScan = findViewById(R.id.imageButton_scan);

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

        ibScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
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

    public  void scanCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        } else {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setCaptureActivity(CaptureAct.class);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.setPrompt("Scanning Code");
            intentIntegrator.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder
                        .setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                scanCode();
                            }
                        })
                        .setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}