package com.example.tikicloneapp.fragments.transacts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ListTransactActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.TransactAdapter;
import com.example.tikicloneapp.models.Transact;

import java.util.ArrayList;

public class ListTransactFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayout lay_loading, layPanel_nonOrder;
    private Button btnShopping;

    private TransactAdapter transactAdapter;
    private ArrayList<Transact> transactArrayList = new ArrayList<>();

    private int statusTransact;

    public static int REQUEST_CODE_LIST_TRANSACT = 45645;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_transact, container, false);
        initWidget(view);

        statusTransact = getActivity().getIntent().getIntExtra("status", 0);

        setEvent();
        return view;
    }

    private void initWidget(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_transact);
        lay_loading = view.findViewById(R.id.loadingPanel);
        layPanel_nonOrder = view.findViewById(R.id.layout_panel_nonOrder);
        btnShopping = view.findViewById(R.id.button_shopping);

    }

    public void setEvent() {
        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().finishAfterTransition();
                } else getActivity().finish();
            }
        });
        setRecyclerView(statusTransact);

    }

    public void setRecyclerView(int statusTransact) {
        callLoadingPanel_parent();
        if (transactAdapter == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider_product_cart));

            transactAdapter = new TransactAdapter(getContext(), transactArrayList, R.layout.row_item_transact);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(transactAdapter);
        }
        if (statusTransact == Transact.CODE_GET_ALL) {
            MainActivity.dbVolley.getTransact(transactArrayList, transactAdapter, null, MainActivity.idUser, layPanel_nonOrder);
        } else
            MainActivity.dbVolley.getTransact(transactArrayList, transactAdapter, statusTransact, MainActivity.idUser, layPanel_nonOrder);

    }

    public void callLoadingPanel_parent() {
        lay_loading.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lay_loading.setVisibility(View.GONE);
            }
        }, 800);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LIST_TRANSACT && resultCode == ListTransactActivity.RESULT_OK) {
            setRecyclerView(statusTransact);
        }
    }
}
