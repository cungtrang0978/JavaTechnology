package com.example.tikicloneapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.AdminManagementActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.TransactAdapter;
import com.example.tikicloneapp.models.Transact;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.tikicloneapp.activities.AdminManagementActivity.httpHandler;

public class AdminTransactFragment extends Fragment {

    private final TransactStatus transactStatus;
    private int status;

    private static final String TAG = AdminTransactFragment.class.getSimpleName();

    public AdminTransactFragment(TransactStatus transactStatus) {
        this.transactStatus = transactStatus;
        switch (transactStatus) {
            case cancelled:
                status = -1;
                break;
            case notConfirm:
                status = 1;
                break;
            case pickingGoods:
                status = 2;
                break;
            case delivering:
                status = 3;
                break;
            case delivered:
                status = 4;
                break;
        }
    }

    public enum TransactStatus {cancelled, notConfirm, pickingGoods, delivering, delivered}

    private RecyclerView rvTransacts;
    private LinearLayout layPanel_nonOrder;

    private ArrayList<Transact> transacts = new ArrayList<>();
    private TransactAdapter transactAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_transact, container, false);

        initWidget(view);
        setViews();

        return view;
    }

    private void initWidget(View view) {
        rvTransacts = view.findViewById(R.id.recyclerView_transact);
        layPanel_nonOrder = view.findViewById(R.id.layout_panel_nonOrder);
    }

    private void setViews() {
//        setRecyclerView();

        new SetTransactsRV().execute();
    }

    public void setRecyclerView() {
//        callLoadingPanel_parent();
        if (transactAdapter == null) {
//            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//            itemDecoration.setDrawable(ContextCompat.getDrawable(rvTransacts.getContext(), R.drawable.divider_product_cart));
            transactAdapter = new TransactAdapter(getContext(), transacts, R.layout.row_item_transact);
            rvTransacts.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTransacts.setAdapter(transactAdapter);
        }
        if (status == Transact.CODE_GET_ALL) {
            MainActivity.dbVolley.getTransact(transacts, transactAdapter, null, MainActivity.idUser, layPanel_nonOrder);
        } else
            MainActivity.dbVolley.getTransact(transacts, transactAdapter, status, MainActivity.idUser, layPanel_nonOrder);
    }

    private class SetTransactsRV extends AsyncTask<Void, Void, ArrayList<Transact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ArrayList<Transact> doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put("status", String.valueOf(status));
            String jsonStr = httpHandler.performPostCall(AdminManagementActivity.dbVolley.URL_GET_TRANSACT_BY_ADMIN, params);
            if (jsonStr != null) {
                Log.d(TAG, "jsonStr: " + jsonStr);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Transact> _transacts) {
            super.onPostExecute(_transacts);
            transacts = _transacts;
            if (transactAdapter == null) {
//            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//            itemDecoration.setDrawable(ContextCompat.getDrawable(rvTransacts.getContext(), R.drawable.divider_product_cart));
                transactAdapter = new TransactAdapter(getContext(), transacts, R.layout.row_item_transact);
                rvTransacts.setLayoutManager(new LinearLayoutManager(getContext()));
                rvTransacts.setAdapter(transactAdapter);
            }else{
                transactAdapter.notifyDataSetChanged();
            }

        }
    }

}