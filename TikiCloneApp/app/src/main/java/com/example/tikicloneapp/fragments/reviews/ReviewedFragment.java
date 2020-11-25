package com.example.tikicloneapp.fragments.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.adapters.ReviewedAdapter;
import com.example.tikicloneapp.adapters.WaitingReviewAdapter;
import com.example.tikicloneapp.models.Rate;

import java.util.ArrayList;

public class ReviewedFragment extends Fragment {
    private RecyclerView recyclerView;

    private ReviewedAdapter reviewedAdapter;
    private ArrayList<Rate> rateArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviewed, container, false);

        initWidget(view);
        setViews();

        return view;
    }

    private void initWidget(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_reviewed);
    }

    private void setViews() {
        setRecyclerView();
    }

    public void setRecyclerView() {
//        callLoadingPanel_parent();
        if (reviewedAdapter == null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider_product_cart));

            reviewedAdapter = new ReviewedAdapter(getContext(), rateArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(reviewedAdapter);
        }
        MainActivity.dbVolley.getReviewedProducts(rateArrayList, reviewedAdapter);
    }
}
