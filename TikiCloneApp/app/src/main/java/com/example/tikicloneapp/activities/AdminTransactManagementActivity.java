package com.example.tikicloneapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.AdminTransactAdapter;
import com.example.tikicloneapp.fragments.AdminTransactFragment;
import com.example.tikicloneapp.models.Transact;
import com.example.tikicloneapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.tikicloneapp.activities.AdminManagementActivity.httpHandler;

public class AdminTransactManagementActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageButton ibBack;
    private TextView tvNonConfirm,
            tvPickingGoods,
            tvDelivering,
            tvDelivered,
            tvCancelled;
    private LinearLayout layPanel_nonOrder, lay_loading;
    private RecyclerView rvTransacts;
    private SwipeRefreshLayout swipeRLTransact;

    private int lastIndex = 1;
    private ArrayList<Transact> transacts = new ArrayList<>();
    private AdminTransactAdapter transactAdapter;
    private static final String TAG = AdminTransactFragment.class.getSimpleName();

    public static int REQUEST_CODE_REFRESH = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transact_management);
        initWidget();

        setViews();
    }

    private void initWidget() {
        rvTransacts = findViewById(R.id.recyclerView_transact);
        layPanel_nonOrder = findViewById(R.id.layout_panel_nonOrder);
        ibBack = findViewById(R.id.imageButton_back);
        tvNonConfirm = findViewById(R.id.textView_nonConfirm);
        tvPickingGoods = findViewById(R.id.textView_pickingGoods);
        tvDelivering = findViewById(R.id.textView_delivering);
        tvDelivered = findViewById(R.id.textView_delivered);
        tvCancelled = findViewById(R.id.textView_cancelled);
        lay_loading = findViewById(R.id.loadingPanel_parent);
        swipeRLTransact = findViewById(R.id.swipeRefreshLayout);
    }

    private void setViews() {
        setRvTransacts(Transact.STATUS_TIKI_RECEIVED);

        setOnClickView();
    }


    private void setOnClickView() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else finish();
            }
        });

        tvNonConfirm.setOnClickListener(onClickListener(1));
        tvPickingGoods.setOnClickListener(onClickListener(2));
        tvDelivering.setOnClickListener(onClickListener(3));
        tvDelivered.setOnClickListener(onClickListener(4));
        tvCancelled.setOnClickListener(onClickListener(-1));

        swipeRLTransact.setOnRefreshListener(this);
        swipeRLTransact.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    View.OnClickListener onClickListener(final int status) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRvTransacts(status);
                lastIndex = status;
            }
        };
    }

    private void setTransactAdapter() {
        transactAdapter = new AdminTransactAdapter(this, transacts);
        rvTransacts.setLayoutManager(new LinearLayoutManager(this));
        rvTransacts.setAdapter(transactAdapter);
    }

    @Override
    public void onRefresh() {
        setRvTransacts(lastIndex);
    }

    private void setRvTransacts(int status) {
        if (AdminManagementActivity.role == User.ROLE_SHIPPER) {
            new FetchTransacts().execute(status, AdminManagementActivity.idUser);
        } else {
            new FetchTransacts().execute(status);
        }
    }

    private class FetchTransacts extends AsyncTask<Integer, Void, ArrayList<Transact>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            lay_loading.setVisibility(View.VISIBLE);
            swipeRLTransact.setRefreshing(true);
        }

        @Override
        protected ArrayList<Transact> doInBackground(Integer... integers) {
            if (integers.length > 1) {
                return getTransacts(integers[0], integers[1]);
            }
            return getTransacts(integers[0], null);
        }

        @Override
        protected void onPostExecute(ArrayList<Transact> _transacts) {
            super.onPostExecute(_transacts);
//            lay_loading.setVisibility(View.GONE);
            if (_transacts.isEmpty()) {
                layPanel_nonOrder.setVisibility(View.VISIBLE);
            } else {
                layPanel_nonOrder.setVisibility(View.GONE);
            }

            transacts = _transacts;
            setTransactAdapter();
            swipeRLTransact.setRefreshing(false);
        }

        private ArrayList<Transact> getTransacts(int status, Integer shipperId) {
            ArrayList<Transact> _transact = new ArrayList<>();

            HashMap<String, String> params = new HashMap<>();
            params.put("status", String.valueOf(status));
            String jsonStr = "";
            if (shipperId == null) {
                jsonStr = httpHandler.performPostCall(AdminManagementActivity.dbVolley.URL_GET_TRANSACT_BY_ADMIN, params);
            } else {
                params.put("idShipper", String.valueOf(shipperId));
                jsonStr = httpHandler.performPostCall(AdminManagementActivity.dbVolley.URL_GET_TRANSACT_BY_SHIPPER, params);
            }
            if (jsonStr != null) {
//            Log.d(TAG, "jsonStr: " + jsonStr);
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Transact transact = new Transact(
                                    object
                            );
                            _transact.add(transact);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return _transact;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REFRESH && resultCode == RESULT_OK) {
            int status = data.getIntExtra("status", 0);
            setRvTransacts(status);
        }
    }
}