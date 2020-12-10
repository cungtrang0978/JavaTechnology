package com.example.tikicloneapp.fragments.authentications;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.example.tikicloneapp.activities.AdminManagementActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.models.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private TextInputLayout edtUsername, edtPassword;
    private Button btnLogin, btnShowHide, btnForgotPw;

    LinearLayout lay_loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initWidget(view);
        setOnClick();

        return view;
    }

    private void setOnClick() {
        btnShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnShowHide.getText().toString().equals("Hiện")) {
                    btnShowHide.setText("Ẩn");
                    edtPassword.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    btnShowHide.setText("Hiện");
                    edtPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUsername() && validatePassword()) {
                    MyClass.callPanel(lay_loading, 1000);
                    checkUserAccount();
                }
            }
        });
    }

    private void initWidget(View view) {
        edtUsername = view.findViewById(R.id.textInputLayout_username);
        edtPassword = view.findViewById(R.id.textInputLayout_password);
        btnForgotPw = view.findViewById(R.id.button_forgot_password);
        btnLogin = view.findViewById(R.id.button_login);
        btnShowHide = view.findViewById(R.id.button_show_hide_password);
        lay_loading = view.findViewById(R.id.loadingPanel_parent);

    }

    private boolean validateUsername() {
        String user = edtUsername.getEditText().getText().toString();
        if (user.isEmpty()) {
            edtUsername.setError("Vui lòng nhập vào email hoặc só điện thoại");
            return false;
        }

        edtUsername.setError(null);
        return true;
    }

    private boolean validatePassword() {
        String password = edtPassword.getEditText().getText().toString();
        if (password.isEmpty()) {
            edtPassword.setError("Xin vui lòng nhập vào mật khẩu");
            return false;
        }

        edtPassword.setError(null);
        return true;
    }


    public void checkUserAccount() {
        final String username = edtUsername.getEditText().getText().toString().trim();
        final String password = edtPassword.getEditText().getText().toString().trim();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_CHECK_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", response);

                if (response.equals("wrong password")) {
                    edtUsername.setError(null);
                    edtPassword.setError("Mật khẩu không đúng");
                    return;
                }

                if (response.equals("not exist")) {
                    edtUsername.setError("Tài khoản không tồn tại");
                    edtPassword.setError(null);
                    return;
                }
                if (response.equals("fail connection")) {
                    Toast.makeText(getContext(), "Kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = getActivity().getIntent().getIntExtra("CODE", 0);
                try {
                    JSONObject jsonUser = new JSONObject(response);
                    User user = new User(jsonUser.getInt("id"), jsonUser.getInt("roleId"));
                    int idUser = user.getmId();

                    if (user.getmRoleId() == User.ROLE_ADMIN || user.getmRoleId() == User.ROLE_SHIPPER) {
                        getActivity().finishAffinity();
                        Intent intent = new Intent(getContext(), AdminManagementActivity.class);
                        intent.putExtra("idUser", idUser);
                        intent.putExtra("role", user.getmRoleId());
                        startActivity(intent);
                    } else if (user.getmRoleId() == User.ROLE_USER) {
                        if (code == 0) {
                            //finish and exit activity
                            getActivity().finishAffinity();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("idUser", idUser);
                            startActivity(intent);
                        } else {


                            MainActivity.idUser = idUser;
                            int idUserOld = MainActivity.dbManager.getIdUser();
                            MainActivity.dbManager.updateData_User(idUser, idUserOld);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getActivity().finishAfterTransition();
                            } else getActivity().finish();

                            Intent intent = new Intent();
                            intent.putExtra("idUser", idUser);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "OnErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
