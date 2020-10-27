package com.example.tikicloneapp.adapters;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.CartActivity;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.ProductDetailActivity;
import com.example.tikicloneapp.fragments.navigations.HomeFragment;
import com.example.tikicloneapp.models.Product;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private final Context mContext;
    private final int mResource;
    private final ArrayList<Product> mProducts;
    private ProductType mProductType;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView tvPriceDiscount, tvDiscountProduct, tvNameProduct, tvQty;
        private ImageView ivProduct;
        private LinearLayout parentLayout;
        private CardView cvSold;

        public MyViewHolder(View itemView) {
            super(itemView);

            if (mResource == R.layout.row_product) {
                parentLayout = itemView.findViewById(R.id.linearLayout_itemProduct);
                tvNameProduct = itemView.findViewById(R.id.textView_nameProduct);
                tvPriceDiscount = itemView.findViewById(R.id.textView_priceProduct);
                tvDiscountProduct = itemView.findViewById(R.id.textView_discountProduct);
                ivProduct = itemView.findViewById(R.id.imageView_product);
                tvQty = itemView.findViewById(R.id.textView_sold);
                cvSold = itemView.findViewById(R.id.cardView_sold);
            } else if (mResource == R.layout.item_viewed_product) {
                tvPriceDiscount = itemView.findViewById(R.id.textView_priceDiscount);
                ivProduct = itemView.findViewById(R.id.imageView_product);
                parentLayout = itemView.findViewById(R.id.linearLayout_itemViewed);

            }

        }
    }

    public ProductsAdapter(Context mContext, int mResource, ArrayList<Product> mProducts, ProductType mProductType) {
        this.mContext = mContext;
        this.mResource = mResource;
        this.mProducts = mProducts;
        this.mProductType = mProductType;
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View productView = layoutInflater.inflate(mResource, parent, false);
        return new MyViewHolder(productView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Product product = mProducts.get(position);

        if (mResource == R.layout.item_viewed_product) {
            holder.tvPriceDiscount.setText(CartActivity.formatCurrency(product.getPriceDiscount()));

            ProductListAdapter.loadImageFromUrl(product.getImageUrl(), holder.ivProduct);

        } else if (mResource == R.layout.row_product) {
            holder.tvNameProduct.setText(product.getName());
            holder.tvPriceDiscount.setText(CartActivity.formatCurrency(product.getPriceDiscount()));
            if (product.getSold() != 0) {
                holder.cvSold.setVisibility(View.VISIBLE);
                holder.tvQty.setText("Đã bán " + product.getSold());
            }
            if (product.getDiscount() != 0) {
                holder.tvDiscountProduct.setText(-product.getDiscount() + "%");
            }
            ProductListAdapter.loadImageFromUrl(product.getImageUrl(), holder.ivProduct);

        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("product", product);
                int requestCode = -1;


                if (mProductType == ProductType.CODE_PRODUCT_LIST) {
                    requestCode = ListProductActivity.REQUEST_CODE;
                } else if (mProductType == ProductType.CODE_NOT_LIST) {
                    requestCode = HomeFragment.REFRESH_CODE;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((FragmentActivity) view.getContext()).startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(((FragmentActivity) view.getContext())).toBundle());
                } else
                    ((FragmentActivity) view.getContext()).startActivityForResult(intent, requestCode);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public enum ProductType {
        CODE_PRODUCT_LIST,
        CODE_NOT_LIST
    }
}