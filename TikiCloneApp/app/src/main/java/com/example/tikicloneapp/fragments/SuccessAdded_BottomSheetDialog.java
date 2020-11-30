package com.example.tikicloneapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.CartActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.models.Product;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SuccessAdded_BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mBottomSheetListener;
    private Button btnGotoCart;
    private ImageView ivProduct;
    private TextView tvName, tvPrice;
    private LinearLayout layBtnCancel;

    private Product mProduct;

    public SuccessAdded_BottomSheetDialog(Product mProduct) {
        this.mProduct = mProduct;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_added_product, container, false);
        initWidget(view);
        setEachViewInside(view);

        return view;
    }

    private void initWidget(View view) {
        btnGotoCart = view.findViewById(R.id.button_goToCart);
        tvName = view.findViewById(R.id.textView_name);
        tvPrice = view.findViewById(R.id.textView_price);
        ivProduct = view.findViewById(R.id.imageView_product);
        layBtnCancel = view.findViewById(R.id.layoutButton_cancel);
    }

    private void setEachViewInside(View view) {
        btnGotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetListener.onButtonClicked("go_to_cart");
                dismiss();
            }
        });

        //setImage Product
        DBVolley dbVolley = MainActivity.dbVolley;
        dbVolley.getFirstImageProduct(ivProduct, mProduct.getId());

        tvName.setText(mProduct.getName());
        int price = mProduct.getPrice() - mProduct.getPrice() * mProduct.getDiscount() / 100;
        tvPrice.setText(CartActivity.formatCurrency(price));

        layBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

}
