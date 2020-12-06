package com.example.tikicloneapp.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.TabLayoutViewPagerAdapter;
import com.example.tikicloneapp.adapters.TransactAdapter;
import com.example.tikicloneapp.fragments.AdminTransactFragment;
import com.example.tikicloneapp.models.Transact;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.tikicloneapp.activities.AdminManagementActivity.httpHandler;

public class TestManagementActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    AdminTransactFragment notConfirmFragment;
    AdminTransactFragment pickingGoodsFragment;
    AdminTransactFragment deliveringFragment;
    AdminTransactFragment deliveredFragment;
    AdminTransactFragment cancelledFragment;

    

    public TestManagementActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_management);

        initWidget();
        setViews();
    }

    private void initWidget() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ibBack = findViewById(R.id.imageButton_back);
    }

    private void setViews() {
        setOnClickView();
        setViewPager();
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

    }

    private void setViewPager() {
        TabLayoutViewPagerAdapter adapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(notConfirmFragment, "Chưa xác nhận");
        adapter.addFragment(pickingGoodsFragment, "Đang lấy hàng");
        adapter.addFragment(deliveringFragment, "Đang giao hàng");
        adapter.addFragment(deliveredFragment, "Đã nhận hàng");
        adapter.addFragment(cancelledFragment, "Đã hủy");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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