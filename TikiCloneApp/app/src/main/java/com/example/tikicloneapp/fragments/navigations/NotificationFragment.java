package com.example.tikicloneapp.fragments.navigations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.MainActivity;

public class NotificationFragment extends Fragment {
    ImageButton ibCart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);
        initWidget(view);

        setEachView();

        return view;
    }

    private void initWidget(View view){
        ibCart = view.findViewById(R.id.imageButton_cart);
    }

    private void setEachView(){
        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbVolley.checkTransact_goToCart(MainActivity.idUser);
            }
        });
    }
}
