package com.example.tikicloneapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.TransactAdapter;
import com.example.tikicloneapp.models.Transact;

import java.util.ArrayList;

public class AdminTransactFragment extends Fragment {

    private final TransactStatus transactStatus;
    private int status;
    private final ArrayList<Transact> transacts;


    private static final String TAG = AdminTransactFragment.class.getSimpleName();

    public AdminTransactFragment(TransactStatus transactStatus, ArrayList<Transact> transacts) {
        this.transactStatus = transactStatus;
        this.transacts = transacts;
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

    private TransactAdapter transactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        setRecyclerView();

    }


    public void setRecyclerView() {
//        callLoadingPanel_parent();
        transactAdapter = new TransactAdapter(getContext(), transacts, R.layout.row_item_transact);
        rvTransacts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransacts.setAdapter(transactAdapter);
    }


}