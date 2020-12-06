package com.example.tikicloneapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.adapters.TabLayoutViewPagerAdapter;
import com.example.tikicloneapp.fragments.AdminTransactFragment;
import com.google.android.material.tabs.TabLayout;

public class TransactManagementAdminActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    AdminTransactFragment notConfirmFragment = new AdminTransactFragment(AdminTransactFragment.TransactStatus.notConfirm);
    AdminTransactFragment pickingGoodsFragment = new AdminTransactFragment(AdminTransactFragment.TransactStatus.pickingGoods);
    AdminTransactFragment deliveringFragment = new AdminTransactFragment(AdminTransactFragment.TransactStatus.delivering);
    AdminTransactFragment deliveredFragment = new AdminTransactFragment(AdminTransactFragment.TransactStatus.delivered);
    AdminTransactFragment cancelledFragment = new AdminTransactFragment(AdminTransactFragment.TransactStatus.cancelled);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transact_management_admin);
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
}