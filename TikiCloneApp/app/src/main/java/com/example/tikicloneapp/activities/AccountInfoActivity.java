package com.example.tikicloneapp.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AccountInfoActivity extends AppCompatActivity {
    private static final String TAG = AccountInfoActivity.class.getSimpleName();

    private TextInputLayout ipLayoutName, ipLayoutEmail, ipLayoutPhone;
    private EditText edtBirthDate;
    private Button btnSave;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioButton rbMale, rbFemale;
    private LinearLayout layBtnExit;
    private TextView tvAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        initWidget();

        int idUser = MainActivity.idUser;
        setEachView(idUser);

    }

    private void initWidget() {
        ipLayoutName = findViewById(R.id.textInputLayout_name);
        ipLayoutEmail = findViewById(R.id.textInputLayout_email);
        ipLayoutPhone = findViewById(R.id.textInputLayout_phoneNumber);
        edtBirthDate = findViewById(R.id.editText_date);
        btnSave = findViewById(R.id.button_save);
        rbFemale = findViewById(R.id.radioButton_female);
        rbMale = findViewById(R.id.radioButton_male);
        layBtnExit = findViewById(R.id.layoutButton_exit);
        tvAmount = findViewById(R.id.textView_amount);
    }


    private void setEachView(final int idUser) {
        layBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
                else
                    finish();
            }
        });

        setEditText(idUser);

        edtBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                edtBirthDate.setText(date);
            }
        };

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(idUser);
            }
        });
    }

    private boolean validateName() {
        String nameInput = ipLayoutName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            ipLayoutName.setError("Họ tên phải từ 2 từ trở lên");
            return false;
        }

        char[] nameInputChar = nameInput.toCharArray();
        boolean check_nameMoreThan2Words = false;
        for (char c : nameInputChar) {
            if (c == ' ') {
                check_nameMoreThan2Words = true;
                break;
            }

        }
        if (!check_nameMoreThan2Words) {
            ipLayoutName.setError("Họ tên phải từ 2 từ trở lên");
            return false;
        }

        String VIETNAMESE_DIACRITIC_CHARACTERS
                = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";

        Pattern pattern = Pattern.compile("[^A-Z" + VIETNAMESE_DIACRITIC_CHARACTERS + " ]", Pattern.CASE_INSENSITIVE);
        boolean check = pattern.matcher(nameInput).find();
        if (check) {
            ipLayoutName.setError("Họ tên không bao gồm những ký tự đặc biệt và số");
            return false;
        }

        ipLayoutName.setError(null);
        return true;
    }

    private boolean validateEmail() {
        String emailInput = ipLayoutEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            ipLayoutEmail.setError(null);
            return true;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (emailInput.isEmpty() | !pattern.matcher(emailInput).find()) {
            ipLayoutEmail.setError("Email không hợp lệ");
            return false;
        }

        ipLayoutEmail.setError(null);
        return true;
    }


    private void confirmInput(int idUser) {
        if (!validateName() | !validateEmail()) {
            return;
        }
        updateAccount(idUser);
    }

    private void updateAccount(final int idUser) {
        final String name = ipLayoutName.getEditText().getText().toString().trim();
        final String date = edtBirthDate.getText().toString().trim();
        final String email = ipLayoutEmail.getEditText().getText().toString().trim();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_UPDATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "exist_email":
                        ipLayoutEmail.setError("Tài khoản email đã tồn tại");
                        break;
                    case "update_success":
                        Toast.makeText(AccountInfoActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        } else finish();
                        break;
                    case "update_error":
                        Toast.makeText(AccountInfoActivity.this, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                        break;
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
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();

                params.put("id", String.valueOf(idUser));

                if (ipLayoutEmail.getEditText().isEnabled() && !ipLayoutEmail.getEditText().getText().toString().isEmpty()){
                    params.put("email", email);
                }

                if (!name.isEmpty()) {
                    params.put("name", name);
                }

                if (!date.isEmpty()) {
                    params.put("birthdate", date);
                }

                if (rbMale.isChecked()) {
                    params.put("sex", "1");
                }
                if (rbFemale.isChecked()) {
                    params.put("sex", "2");
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void setEditText(final int idUser) {
        final int CODE_MALE = 1,
                CODE_FEMALE = 2;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);

                    ipLayoutName.getEditText().setText(userObject.getString("name"));
                    ipLayoutPhone.getEditText().setText(userObject.getString("phoneNumber"));

                    Log.d(TAG, "amount: " + userObject.getString("amount"));

                    String amount = CartActivity.formatCurrency(userObject.getInt("amount"));
                    tvAmount.setText(amount);

                    if (userObject.getString("email").equals("null")) {
                        ipLayoutEmail.getEditText().setText("");
                    } else {
                        ipLayoutEmail.getEditText().setText(userObject.getString("email"));
                        ipLayoutEmail.setEnabled(false);
                    }

                    if (userObject.getString("birthdate").equals("null")) {
                        edtBirthDate.setText("");
                    } else {
                        edtBirthDate.setText(userObject.getString("birthdate"));
                    }

                    if (userObject.getInt("sex") == CODE_MALE) {
                        rbMale.setChecked(true);
                    } else if (userObject.getInt("sex") == CODE_FEMALE) {
                        rbFemale.setChecked(true);
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
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}