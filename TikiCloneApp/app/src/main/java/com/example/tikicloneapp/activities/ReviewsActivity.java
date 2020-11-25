package com.example.tikicloneapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.TabLayoutViewPagerAdapter;
import com.example.tikicloneapp.fragments.reviews.ReviewedFragment;
import com.example.tikicloneapp.fragments.reviews.WaitingReviewsFragment;
import com.google.android.material.tabs.TabLayout;

public class ReviewsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton ibBack, ibCart;
    private TextView tvLabel;

    ReviewedFragment reviewedFragment = new ReviewedFragment();
    WaitingReviewsFragment waitingReviewsFragment = new WaitingReviewsFragment();

    public static int REFRESH_CODE = 1515;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        initWidget();
        setViews();
    }

    private void initWidget() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ibCart = findViewById(R.id.imageButton_cart);
        ibBack = findViewById(R.id.imageButton_back);
        tvLabel = findViewById(R.id.textView_label);
    }

    private void setViews() {
        setViewPager();
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

        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });
    }

    private void setViewPager() {
        TabLayoutViewPagerAdapter adapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(waitingReviewsFragment, "Chờ đánh giá");
        adapter.addFragment(reviewedFragment, "Đã đánh giá");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REFRESH_CODE && resultCode == ReviewsActivity.RESULT_OK){
            //refresh
            waitingReviewsFragment.setRecyclerView();
            reviewedFragment.setRecyclerView();
        }
    }
}