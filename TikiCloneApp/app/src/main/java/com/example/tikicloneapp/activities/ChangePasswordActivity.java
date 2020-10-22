package com.example.tikicloneapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputLayout tilOldPassword, tilNewPassword, tilNewPasswordConfirm;
    Button btnSave;
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initWidget();

        setEvent();
    }

    void setEvent() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConfirmPassword()) {
                    String oldPassword = tilOldPassword.getEditText().getText().toString().trim();
                    String newPassword = tilNewPassword.getEditText().getText().toString().trim();
                    updatePassword(MainActivity.idUser, oldPassword, newPassword);
                }
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });
    }

    private void initWidget() {
        tilOldPassword = findViewById(R.id.textInputLayout_oldPassword);
        tilNewPassword = findViewById(R.id.textInputLayout_newPassword);
        tilNewPasswordConfirm = findViewById(R.id.textInputLayout_newPasswordConfirm);
        btnSave = findViewById(R.id.button_save);
        ibBack = findViewById(R.id.imageButton_back);
    }

    private boolean checkConfirmPassword() {
        if (!isValidPassword(tilNewPassword)) {
            return false;
        }
        String oldPassword = tilOldPassword.getEditText().getText().toString().trim();
        String newPassword = tilNewPassword.getEditText().getText().toString().trim();
        String newPasswordConfirm = tilNewPasswordConfirm.getEditText().getText().toString().trim();

        tilOldPassword.setError("");
        tilNewPasswordConfirm.setError("");
        if (oldPassword.equals(newPassword)) {
            tilNewPassword.setError("Mật khẩu mới không được trùng mật khẩu cũ!");
            return false;
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            tilNewPasswordConfirm.setError("Mật khẩu mới không khớp nhau!");
            tilNewPassword.setError("Mật khẩu mới không khớp nhau!");
            return false;
        }

        return true;
    }

  /*  private boolean isValidPassword(TextInputLayout til) {
        String passwordInput = til.getEditText().getText().toString().trim();
        if (til != tilNewPassword && passwordInput.isEmpty()) {
            til.setError("Còn trống!");
            return false;
        }

        if (til == tilNewPassword && passwordInput.length() < 6) {
            til.setError("Mật khẩu phải từ 6 kí tự trở lên");
            return false;
        }
        til.setError(null);
        return true;
    }*/

    public static boolean isValidPassword(TextInputLayout tilPassword)
    {
        boolean isValid = true;
        String specialChars = "(.*[@,#,$,%,*,&,!,^].*$)";
        if (!tilPassword.getEditText().getText().toString().matches(specialChars ))
        {
            tilPassword.setError("Phải chứa ít nhất một kí tự đặc biệt trong các kí tự: @#$%*&!^");
            isValid = false;
        }
        String numbers = "(.*[0-9].*)";
        if (!tilPassword.getEditText().getText().toString().matches(numbers ))
        {
            tilPassword.setError("Chứa ít nhất một chữ số.");
            isValid = false;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!tilPassword.getEditText().getText().toString().matches(upperCaseChars ))
        {
            tilPassword.setError("Chứa ít nhất một chữ cái in hoa.");
            isValid = false;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!tilPassword.getEditText().getText().toString().matches(lowerCaseChars ))
        {
            tilPassword.setError("Chứa ít nhất một chữ cái thường.");
            isValid = false;
        }
        if (tilPassword.getEditText().getText().length() > 15 || tilPassword.getEditText().getText().length() < 8)
        {
            tilPassword.setError("Mật khẩu phải từ 8 đến 15 kí tự.");
            isValid = false;
        }
        if(isValid) tilPassword.setError(null);
        return isValid;
    }

    public void updatePassword(final int idUser, final String oldPassword, final String newPassword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_UPDATE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "updatePassword: " + response);

                if (response.equals("update_success")) {
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else finish();
                }
                if (response.equals("wrong_password")) {
                    tilOldPassword.setError("Sai mật khẩu!");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updatePassword: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idUser));
                params.put("newPassword", newPassword);
                params.put("oldPassword", oldPassword);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}