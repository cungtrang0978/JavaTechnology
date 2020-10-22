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
import com.example.tikicloneapp.fragments.transacts.ListTransactFragment;
import com.example.tikicloneapp.fragments.transacts.VoucherFragment;
import com.example.tikicloneapp.models.Transact;
import com.google.android.material.tabs.TabLayout;

public class ListTransactActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton ibBack, ibCart;
    private TextView tvLabel;

    ListTransactFragment listTransactFragment = new ListTransactFragment();
    VoucherFragment voucherFragment = new VoucherFragment();

    public int status=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transact);
        initWidget();
        status = getIntent().getIntExtra("status",0);

        setEachView();

    }

    private void initWidget(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ibCart = findViewById(R.id.imageButton_cart);
        ibBack = findViewById(R.id.imageButton_back);
        tvLabel = findViewById(R.id.textView_label);
    }

    private void setEachView(){
        setViewPager();

        setTextLabel();

        setOnClickView();
    }

    private void setOnClickView(){
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else finish();
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
        adapter.addFragment(listTransactFragment, "Danh sách đơn");
        adapter.addFragment(voucherFragment, "Voucher của tôi");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setTextLabel(){
        String label = "";
        if(status== Transact.STATUS_TIKI_RECEIVED){
            label = "Đã tiếp nhận";
        }else
        if(status==Transact.STATUS_CANCEL){
            label = "Đã hủy";
        }else
        if(status==Transact.STATUS_SUCCESS){
            label = "Thành công";
        }else
        if(status==Transact.STATUS_DELIVERING){
            label = "Đang vận chuyển";
        }else
        if(status==Transact.STATUS_SELLER_RECEIVED){
            label = "Đang lấy hàng";
        }else
        if(status==Transact.CODE_GET_ALL){
            label = "Quản lý đơn hàng";
        }
        tvLabel.setText(label);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ListTransactFragment.REQUEST_CODE_LIST_TRANSACT && resultCode == ListTransactActivity.RESULT_OK) {
            listTransactFragment.setRecyclerView(status);
        }
    }
}