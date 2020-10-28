package com.example.tikicloneapp.fragments.authentications;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.LoginActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.database.DBVolley;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {
    private TextInputLayout textInputName, textInputEmail, textInputPhone, textInputPw;
    private EditText edtDateOfBirth;
    private Button btnSignUp;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioButton rbMale, rbFemale;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        initWidget(view);
        setEachView();

        return view;
    }

    private void setEachView() {

        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
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
                edtDateOfBirth.setText(date);
            }
        };
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput();
            }
        });
    }

    private void initWidget(View view) {
        textInputName = view.findViewById(R.id.textInputLayout_name);
        textInputEmail = view.findViewById(R.id.textInputLayout_email);
        textInputPhone = view.findViewById(R.id.textInputLayout_phoneNumber);
        textInputPw = view.findViewById(R.id.textInputLayout_password);
        edtDateOfBirth = view.findViewById(R.id.editText_date);
        btnSignUp = view.findViewById(R.id.button_signup);
        rbMale = view.findViewById(R.id.radioButton_male);
        rbFemale = view.findViewById(R.id.radioButton_female);
    }

    public static boolean validateName(TextInputLayout textInputLayout) {
        String nameInput = textInputLayout.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            textInputLayout.setError("Họ tên phải từ 2 từ trở lên");
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
            textInputLayout.setError("Họ tên phải từ 2 từ trở lên");
            return false;
        }

        String VIETNAMESE_DIACRITIC_CHARACTERS
                = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";

        Pattern pattern = Pattern.compile("[^A-Z" + VIETNAMESE_DIACRITIC_CHARACTERS + " ]", Pattern.CASE_INSENSITIVE);
        boolean check = pattern.matcher(nameInput).find();
        if (check) {
            textInputLayout.setError("Họ tên không bao gồm những ký tự đặc biệt và số");
            return false;
        }

        textInputLayout.setError(null);
        return true;
    }

    public static boolean validateEmail(TextInputLayout textInputLayout) {
        String emailInput = textInputLayout.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputLayout.setError(null);
            return true;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (emailInput.isEmpty() | !pattern.matcher(emailInput).find()) {
            textInputLayout.setError("Email không hợp lệ");
            return false;
        }

        textInputLayout.setError(null);
        return true;
    }

    public static boolean validatePhoneNumber(TextInputLayout textInputLayout) {
        String phoneInput = textInputLayout.getEditText().getText().toString().trim();
        String phoneRegex = "(09[1|2|3|4|5|6|7|8|9]" +
                "|08[1|2|3|4|5|6|8|9]" +
                "|03[2|3|4|5|6|7|8|9]" +
                "|07[0|6|7|8|9]" +
                "|05[6|8|9])+([0-9]{7})\\b";
        Pattern pattern = Pattern.compile(phoneRegex);

        if (phoneInput.isEmpty() | !pattern.matcher(phoneInput).find()) {
            textInputLayout.setError("Số điện thoại không hợp lệ");
            return false;
        }

        textInputLayout.setError(null);
        return true;
    }

    public static boolean isValidPassword(TextInputLayout tilPassword) {
        String[] blackList = {
                "123456",
                "password",
                "123456789",
                "12345678",
                "12345",
                "111111",
                "1234567",
                "sunshine",
                "qwerty",
                "iloveyou",
                "princess",
                "admin",
                "welcome",
                "666666",
                "abc123",
                "football",
                "123123",
                "monkey",
                "654321",
                "!@#$%^&*",
                "charlie",
                "aa123456",
                "donald",
                "password1",
                "qwerty123",
                "135791113",
                "motdenchin",
                "matkhaunaycothutu",
                "fromonetonine",
        };

        boolean isValid = true;
        String specialChars = "(.*[@,#,$,%,*,&,!,^].*$)";
        if (!tilPassword.getEditText().getText().toString().matches(specialChars)) {
            tilPassword.setError("Phải chứa ít nhất một kí tự đặc biệt trong các kí tự: @ # $ % * & ! ^");
            isValid = false;
        }
        String numbers = "(.*[0-9].*)";
        if (!tilPassword.getEditText().getText().toString().matches(numbers)) {
            tilPassword.setError("Chứa ít nhất một chữ số.");
            isValid = false;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!tilPassword.getEditText().getText().toString().matches(upperCaseChars)) {
            tilPassword.setError("Chứa ít nhất một chữ cái in hoa.");
            isValid = false;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!tilPassword.getEditText().getText().toString().matches(lowerCaseChars)) {
            tilPassword.setError("Chứa ít nhất một chữ cái thường.");
            isValid = false;
        }
        if (tilPassword.getEditText().getText().length() > 15 || tilPassword.getEditText().getText().length() < 8) {
            tilPassword.setError("Mật khẩu phải từ 8 đến 15 kí tự.");
            isValid = false;
        }
        if (isValid) tilPassword.setError(null);
        return isValid;
    }

    private void confirmInput() {
        if (!validateName(textInputName) | !validateEmail(textInputEmail) | !validatePhoneNumber(textInputPhone) | !isValidPassword(textInputPw)) {
            return;
        }
        createAccount();
    }

    public void createAccount() {
        final String name = textInputName.getEditText().getText().toString().trim();
        final String phoneNum = textInputPhone.getEditText().getText().toString().trim();
        final String password = textInputPw.getEditText().getText().toString().trim();
        final String email = textInputEmail.getEditText().getText().toString().trim();
        final String date = edtDateOfBirth.getText().toString().trim();

        final String strCode_success = "success";
        final String strCode_error = "error";
        final String strCode_exist_phone = "exist_phone";
        final String strCode_exist_email = "exist_email";
        final String strCode_exist_phone_email = strCode_exist_phone + strCode_exist_email;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_INSERT_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", response);
                if (response.equals(strCode_success)) {
                    textInputPhone.setError(null);
                    textInputEmail.setError(null);
                    Toast.makeText(getContext(), "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    if (getContext() instanceof LoginActivity) {
                        ((LoginActivity) getContext()).returnTabLogin();
                    }
                    clearTextInput();

                    return;
                }
                if (response.equals(strCode_error)) {
                    Toast.makeText(getContext(), "Fail insertion", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.equals(strCode_exist_phone)) {
                    textInputEmail.setError(null);
                    textInputPhone.setError("Số điện thoại đã được sử dụng");
                    return;
                }
                if (response.equals(strCode_exist_email)) {
                    textInputPhone.setError(null);
                    textInputEmail.setError("Địa chỉ email đã được sử dụng");
                    return;
                }
                if (response.equals(strCode_exist_phone_email)) {
                    textInputPhone.setError("Số điện thoại đã được sử dụng");
                    textInputEmail.setError("Địa chỉ email đã được sử dụng");
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
                params.put("name", name);
                params.put("phoneNumber", phoneNum);
                params.put("password", password);
                long timeCreated = new Date().getTime(); //get current time
                params.put("created", String.valueOf(timeCreated));
                if (!email.isEmpty()) {
                    params.put("email", email);
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

    private void clearTextInput() {
        textInputName.getEditText().setText("");
        textInputEmail.getEditText().setText("");
        textInputPhone.getEditText().setText("");
        textInputPw.getEditText().setText("");
        edtDateOfBirth.setText("");
        rbFemale.setChecked(false);
        rbMale.setChecked(false);
    }
}
