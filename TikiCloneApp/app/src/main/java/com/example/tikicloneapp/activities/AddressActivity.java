package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.AddressDialogAdapter;
import com.example.tikicloneapp.models.Transact;
import com.example.tikicloneapp.models.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.tikicloneapp.fragments.authentications.RegistrationFragment.validateName;
import static com.example.tikicloneapp.fragments.authentications.RegistrationFragment.validatePhoneNumber;

public class AddressActivity extends AppCompatActivity {
    TextInputLayout tilSearch, tilName, tilPhoneNumber, tilAddress;
    TextInputLayout tilProvince, tilDistrict, tilWard;
    TextView tvNameAddress;
    ListView lvAddress;
    Button btnSave;
    ImageButton ibCancel, ibBack;
    AddressDialogAdapter dialogAdapter;
    ArrayList<String> listAddress = new ArrayList<>();

    int PROVINCE_KEY = 1;
    int DISTRICT_KEY = 2;
    int WARD_KEY = 3;

    boolean isTransact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initWidget();


        // getTransact if transact has address
        // else get user
        if (Objects.equals(getIntent().getStringExtra("type"), "transact")) {
            isTransact = true;
            tilName.setVisibility(View.VISIBLE);
            tilPhoneNumber.setVisibility(View.VISIBLE);
            getTransact();

        } else {
            getUser(false, null, null, null, null);
        }

        setEachView();

    }

    private void initWidget() {
        tilName = findViewById(R.id.textInputLayout_name);
        tilPhoneNumber = findViewById(R.id.textInputLayout_phoneNumber);
        tilProvince = findViewById(R.id.textInputLayout_province);
        tilDistrict = findViewById(R.id.textInputLayout_district);
        tilWard = findViewById(R.id.textInputLayout_ward);
        tilAddress = findViewById(R.id.textInputLayout_address);
        ibBack = findViewById(R.id.imageButton_back);
        btnSave = findViewById(R.id.button_save);
    }

    private void setEachView() {
        tilProvince.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddress(PROVINCE_KEY);
            }
        });
        tilDistrict.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tilProvince.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(AddressActivity.this, "Bạn phải điền tỉnh/thành trước!", Toast.LENGTH_SHORT).show();
                } else
                    showDialogAddress(DISTRICT_KEY);
            }
        });
        tilWard.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tilDistrict.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(AddressActivity.this, "Bạn phải điền quận/huyện trước!", Toast.LENGTH_SHORT).show();
                } else
                    showDialogAddress(WARD_KEY);
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });
    }

    private void showDialogAddress(final int key) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_address);
        initWidgetDialogAddress(dialog);

        if (key == PROVINCE_KEY) {
            listAddress = MainActivity.dbManager.getProvince();
            tvNameAddress.setText("Chọn " + tilProvince.getHint().toString());
        } else if (key == DISTRICT_KEY) {
            listAddress = MainActivity.dbManager.getDistrict(tilProvince.getEditText().getText().toString());
            tvNameAddress.setText("Chọn " + tilDistrict.getHint().toString());
        } else if (key == WARD_KEY) {
            listAddress = MainActivity.dbManager.getWard(tilDistrict.getEditText().getText().toString());
            tvNameAddress.setText("Chọn " + tilWard.getHint().toString());
        }
        setAdapterDialog();

        tilSearch.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchName = tilSearch.getEditText().getText().toString().trim();
                if (searchName.equals("")) {
                    if (key == PROVINCE_KEY) {
                        listAddress = MainActivity.dbManager.getProvince();
                    } else if (key == DISTRICT_KEY) {
                        String province = tilProvince.getEditText().getText().toString();
                        listAddress = MainActivity.dbManager.getDistrict(province);
                    } else if (key == WARD_KEY) {
                        String district = tilDistrict.getEditText().getText().toString();
                        listAddress = MainActivity.dbManager.getWard(district);
                    }
                } else {
                    if (key == PROVINCE_KEY) {
                        listAddress = MainActivity.dbManager.getProvince(searchName);
                    } else if (key == DISTRICT_KEY) {
                        String province = tilProvince.getEditText().getText().toString();
                        listAddress = MainActivity.dbManager.getDistrict(province, searchName);
                    } else if (key == WARD_KEY) {
                        String district = tilDistrict.getEditText().getText().toString();
                        listAddress = MainActivity.dbManager.getWard(district, searchName);
                    }
                }
                setAdapterDialog();
                dialogAdapter = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (key == PROVINCE_KEY) {
                    tilProvince.getEditText().setText(listAddress.get(position));
                    tilDistrict.getEditText().setText("");
                    tilWard.getEditText().setText("");
                } else if (key == DISTRICT_KEY) {
                    tilDistrict.getEditText().setText(listAddress.get(position));
                    tilWard.getEditText().setText("");
                } else if (key == WARD_KEY) {
                    tilWard.getEditText().setText(listAddress.get(position));
                }
                dialog.cancel();
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
        dialogAdapter = null;
    }

    private void setAdapterDialog() {
        if (dialogAdapter == null) {
            dialogAdapter = new AddressDialogAdapter(this, R.layout.row_address, listAddress);
            lvAddress.setAdapter(dialogAdapter);
        } else {
            dialogAdapter.notifyDataSetChanged();
        }
    }

    private void initWidgetDialogAddress(Dialog dialog) {
        tilSearch = dialog.findViewById(R.id.textInputLayout_searchAddress);
        lvAddress = dialog.findViewById(R.id.listView_address);
        ibCancel = dialog.findViewById(R.id.imageButton_cancel);
        tvNameAddress = dialog.findViewById(R.id.textView_nameAddress);
    }

    public void getTransact() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "getTransact: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    Transact transact = new Transact(
                            object.getString("user_name"),
                            object.getString("user_phone"),
                            object.getString("province"),
                            object.getString("district"),
                            object.getString("ward"),
                            object.getString("address")
                    );

                    if (!transact.getmUser_name().equals("null")) {
                        tilName.getEditText().setText(transact.getmUser_name());
                    }
                    if (!transact.getmUser_phone().equals("null")) {
                        tilPhoneNumber.getEditText().setText(transact.getmUser_phone());
                    }
                    if (!transact.getmProvince().equals("null")) {
                        tilProvince.getEditText().setText(transact.getmProvince());
                    }
                    if (!transact.getmDistrict().equals("null")) {
                        tilDistrict.getEditText().setText(transact.getmDistrict());
                    }
                    if (!transact.getmWard().equals("null")) {
                        tilWard.getEditText().setText(transact.getmWard());
                    }
                    if (!transact.getmAddress().equals("null")) {
                        tilAddress.getEditText().setText(transact.getmAddress());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "getTransact: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idTransact", String.valueOf(MainActivity.idTransact));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void confirmInput() {
        if (!validateName(tilName) && isTransact)
            return;
        if (!validatePhoneNumber(tilPhoneNumber) && isTransact)
            return;
        if (tilProvince.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn tỉnh/thành.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tilDistrict.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn quận/huyện.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tilWard.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Bạn chưa chọn phường/xã.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!tilAddress.getEditText().getText().toString().trim().contains(" ")) {
            Toast.makeText(this, "Địa chỉ nhà phải trên 2 từ.", Toast.LENGTH_SHORT).show();
            return;
        }

        String province = tilProvince.getEditText().getText().toString().trim();
        String district = tilDistrict.getEditText().getText().toString().trim();
        String ward = tilWard.getEditText().getText().toString().trim();
        String address = tilAddress.getEditText().getText().toString();

        if (isTransact) {
            String name = tilName.getEditText().getText().toString().trim();
            String phoneNumber = tilPhoneNumber.getEditText().getText().toString().trim();
            ConfirmationActivity.updateTransact(getApplicationContext(), null, name, phoneNumber, province,
                    district, ward, address);
            getUser(true, province, district, ward, address);
        } else {
            updateUser(province, district, ward, address);
        }

        Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else finish();
        setResult(RESULT_OK);

    }


    public void updateUser(final String province, final String district, final String ward, final String address) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_UPDATE_USER_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("thang", "updateUser: " + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "updateUser: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", MainActivity.idUser + "");
                params.put("province", province);
                params.put("district", district);
                params.put("ward", ward);
                params.put("address", address);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getUser(final boolean checkAddress, @Nullable final String province, @Nullable final String district, @Nullable final String ward, @Nullable final String address) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObject = new JSONObject(response);
                    if (checkAddress) {
                        if (userObject.getString("address").equals("null")) {
                            updateUser(province, district, ward, address);
                        }
                    }
                    User user = new User(
                            userObject.getString("province"),
                            userObject.getString("district"),
                            userObject.getString("ward"),
                            userObject.getString("address")
                    );
                    if (!user.getmAddress().equals("null")) {
                        tilProvince.getEditText().setText(user.getmProvince());
                        tilDistrict.getEditText().setText(user.getmDistrict());
                        tilWard.getEditText().setText(user.getmWard());
                        tilAddress.getEditText().setText(user.getmAddress());
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
                params.put("idUser", String.valueOf(MainActivity.idUser));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}