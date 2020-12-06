package com.example.tikicloneapp.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.TransactAdapter;
import com.example.tikicloneapp.fragments.AdminTransactFragment;
import com.example.tikicloneapp.models.Transact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.tikicloneapp.activities.AdminManagementActivity.httpHandler;

public class TransactManagementAdminActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private TextView tvNonConfirm,
            tvPickingGoods,
            tvDelivering,
            tvDelivered,
            tvCancelled;
    private LinearLayout layPanel_nonOrder;
    private RecyclerView rvTransacts;


    private ArrayList<Transact> transacts = new ArrayList<>();
    private TransactAdapter transactAdapter;
    private static final String TAG = AdminTransactFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transact_management_admin);
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
    }

    private void setViews() {
//        new FetchTransacts().execute();

        setOnClickView();
//        new FetchTransacts().execute(1);

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


    }

    View.OnClickListener onClickListener(final int status) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchTransacts().execute(status);
            }
        };
    }

    private class FetchTransacts extends AsyncTask<Integer, Void, ArrayList<Transact>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ArrayList<Transact> doInBackground(Integer... integers) {
            return getTransacts(integers[0]);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Transact> _transacts) {
            super.onPostExecute(_transacts);
            transacts = _transacts;
            Log.d(TAG, "onPostExecute: "+ transacts.toString());
            transactAdapter = new TransactAdapter(getApplicationContext(), transacts, R.layout.row_item_transact);
            rvTransacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvTransacts.setAdapter(transactAdapter);
//            transactAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Transact> getTransacts(int status) {
        ArrayList<Transact> _transact = new ArrayList<>();

        HashMap<String, String> params = new HashMap<>();
        params.put("status", String.valueOf(status));
        String jsonStr = httpHandler.performPostCall(AdminManagementActivity.dbVolley.URL_GET_TRANSACT_BY_ADMIN, params);
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