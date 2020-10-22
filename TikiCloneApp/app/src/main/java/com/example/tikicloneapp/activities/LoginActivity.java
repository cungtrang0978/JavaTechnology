package com.example.tikicloneapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tikicloneapp.R;

import com.example.tikicloneapp.adapters.TabLayoutViewPagerAdapter;
import com.example.tikicloneapp.fragments.authentications.LoginFragment;
import com.example.tikicloneapp.fragments.authentications.RegistrationFragment;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout layBtn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
        setEachView();
    }

    private void initWidget() {
        tabLayout = findViewById(R.id.tabLayout_login);
        viewPager = findViewById(R.id.viewPager_login);
        layBtn_exit = findViewById(R.id.layoutButton_exit);
    }

    private void setEachView() {
        layBtn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else
                    finish();
            }
        });

        setViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViewPager() {
        TabLayoutViewPagerAdapter adapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new LoginFragment(), "Đăng nhập");
        adapter.addFragment(new RegistrationFragment(), "Đăng ký");
        viewPager.setAdapter(adapter);
    }

    public void returnTabLogin() {
        TabLayout.Tab tabLogin = tabLayout.getTabAt(0);
        tabLogin.select();
    }
}
