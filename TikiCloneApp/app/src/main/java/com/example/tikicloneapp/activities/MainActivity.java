package com.example.tikicloneapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.database.DBManager;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.fragments.navigations.AccountFragment;
import com.example.tikicloneapp.fragments.navigations.CategoryFragment;
import com.example.tikicloneapp.fragments.navigations.HomeFragment;
import com.example.tikicloneapp.fragments.navigations.NotificationFragment;
import com.example.tikicloneapp.fragments.navigations.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static com.example.tikicloneapp.activities.ProductDetailActivity.REQUEST_CODE_LOGIN;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackToExitPressedOnce = false;

    String NAME_HOME_FRAGMENT = "HOME_FRAGMENT",
            NAME_CATEGORY_FRAGMENT = "CATEGORY_FRAGMENT",
            NAME_SEARCH_FRAGMENT = "SEARCH_FRAGMENT",
            NAME_NOTIFICATION_FRAGMENT = "NOTIFICATION_FRAGMENT",
            NAME_ACCOUNT_FRAGMENT = "ACCOUNT_FRAGMENT";
    final HomeFragment homeFragment = new HomeFragment();
/*    final CategoryFragment categoryFragment = new CategoryFragment();
    final SearchFragment searchFragment = new SearchFragment();
    final NotificationFragment notificationFragment = new NotificationFragment();
    final AccountFragment accountFragment = new AccountFragment();
    final FragmentManager fm = getSupportFragmentManager();*/

    public int previous_Of_SearchFragment;

    public static String ipAddress = "is updating";
    public static int idUser;
    public static DBManager dbManager;
    public static DBVolley dbVolley;
    public static int idTransact = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();

        dbManager = new DBManager(this);
//        ipAddress = dbManager.getIPAddress();

        dbVolley = new DBVolley(this);
        checkIdUser();


        ReLoadFragment(homeFragment, NAME_HOME_FRAGMENT);
        bottomNavigationView.setOnNavigationItemSelectedListener(_mOnNavigationItemSelectedListener);
    }


    private void initWidget() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
    void checkIdUser(){
        int idUserOld = dbManager.getIdUser();
        idUser = getIntent().getIntExtra("idUser", idUserOld);
        Log.d("thang", "checkIdUser: " + idUser + "; idUserOld: " + idUserOld);
//        if(idUserOld == 0){
//            dbManager.clearAllData_Order();
//        }

        if(idUser!=idUserOld){
            dbManager.updateData_User(idUser, idUserOld);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void LoadFragment(int index) {
        bottomNavigationView.setSelectedItemId(index);
    }

    public void setShowKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener _mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            if (item.getItemId() != R.id.menu_BottomSearch) {
                previous_Of_SearchFragment = item.getItemId();
            }
            switch (item.getItemId()) {
                case R.id.menu_BottomHome:
                    fragment = homeFragment;
                    ReLoadFragment(fragment, NAME_HOME_FRAGMENT);
                    return true;
                case R.id.menu_BottomCategory:
                    fragment = new CategoryFragment();
                    ReLoadFragment(fragment, NAME_CATEGORY_FRAGMENT);
                    return true;
                case R.id.menu_BottomSearch:
                    fragment = new SearchFragment();
                    ReLoadFragment(fragment, NAME_SEARCH_FRAGMENT);
                    return true;
                case R.id.menu_BottomNotification:
                    fragment = new NotificationFragment();
                    ReLoadFragment(fragment, NAME_NOTIFICATION_FRAGMENT);
                    return true;
                case R.id.menu_BottomAccount:
                    fragment = new AccountFragment();
                    ReLoadFragment(fragment, NAME_ACCOUNT_FRAGMENT);
                    return true;
            }
            return false;
        }
    };

    private void ReLoadFragment(Fragment fragment, final String name) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if (!name.equals(NAME_HOME_FRAGMENT)) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged  callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack(NAME_CATEGORY_FRAGMENT, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);

                    fragmentManager.popBackStack(NAME_SEARCH_FRAGMENT, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);

                    fragmentManager.popBackStack(NAME_NOTIFICATION_FRAGMENT, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);

                    fragmentManager.popBackStack(NAME_ACCOUNT_FRAGMENT, POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_account);
//        if (fragment != null) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){
            assert data != null;
            MainActivity.idUser = data.getIntExtra("idUser",0);
        }
    }

/*    protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }*/
}