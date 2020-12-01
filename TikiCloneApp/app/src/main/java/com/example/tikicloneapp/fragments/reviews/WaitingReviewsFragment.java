package com.example.tikicloneapp.fragments.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.WaitingReviewAdapter;
import com.example.tikicloneapp.models.Product;

import java.util.ArrayList;

public class WaitingReviewsFragment extends Fragment {

    private RecyclerView recyclerView;

    private WaitingReviewAdapter waitingReviewAdapter;
    private ArrayList<Product> productArrayList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_reviews, container, false);
        initWidget(view);

        setViews();
        return view;
    }

    private void initWidget(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_waitingReviews);
    }

    private void setViews() {

        setRecyclerView();
    }

    public void setRecyclerView() {
//        callLoadingPanel_parent();
        if (waitingReviewAdapter == null) {
//            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//            itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider_product_cart));

            waitingReviewAdapter = new WaitingReviewAdapter(getContext(), productArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(waitingReviewAdapter);
        }
        MainActivity.dbVolley.getWaitingReviewProducts(productArrayList, waitingReviewAdapter);
    }


}
