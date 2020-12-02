package com.example.tikicloneapp.adapters;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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

        private TextView tvPriceDiscount, tvDiscountProduct, tvNameProduct, tvQty, tvRateQuantity;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private ImageView ivProduct;
        private LinearLayout parentLayout, llReview;
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
                ivStar1 = itemView.findViewById(R.id.imageView_star1);
                ivStar2 = itemView.findViewById(R.id.imageView_star2);
                ivStar3 = itemView.findViewById(R.id.imageView_star3);
                ivStar4 = itemView.findViewById(R.id.imageView_star4);
                ivStar5 = itemView.findViewById(R.id.imageView_star5);
                tvRateQuantity = itemView.findViewById(R.id.textView_rateQuantity);
                llReview = itemView.findViewById(R.id.linearLayout_review);

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
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Product product = mProducts.get(position);
        Log.d("thang", "onBindViewHolder: "+ product);

        ProductListAdapter.loadImageFromUrl(product.getImageUrl(), holder.ivProduct);
        holder.tvPriceDiscount.setText(CartActivity.formatCurrency(product.getPriceDiscount()));
        if (mResource == R.layout.row_product) {
            holder.tvNameProduct.setText(product.getName());

            if(product.getRateQty()!=0){
                holder.llReview.setVisibility(View.VISIBLE);
                setRate(holder.ivStar1, holder.ivStar2, holder.ivStar3, holder.ivStar4, holder.ivStar5, product.getRate());
                holder.tvRateQuantity.setText("("+product.getRateQty()+")");
            }else{
                holder.llReview.setVisibility(View.GONE);
            }
            if (product.getSold() != 0) {
                holder.cvSold.setVisibility(View.VISIBLE);
                holder.tvQty.setText("Đã bán " + product.getSold());
            }
            if (product.getDiscount() != 0) {
                holder.tvDiscountProduct.setText(-product.getDiscount() + "%");
            }
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

    public static void setRate(ImageView iv1, ImageView iv2, ImageView iv3, ImageView iv4, ImageView iv5, double rates) {
        iv1.setImageResource(R.drawable.star_on);
        iv2.setImageResource(R.drawable.star_off);
        iv3.setImageResource(R.drawable.star_off);
        iv4.setImageResource(R.drawable.star_off);
        iv5.setImageResource(R.drawable.star_off);

//set star 2
        if (rates > 1.7) {
            iv2.setImageResource(R.drawable.star_on);
        } else if (rates > 1.2) {
            iv2.setImageResource(R.drawable.star_haft);
        }

//set star 3
        if (rates > 2.7) {
            iv3.setImageResource(R.drawable.star_on);
        } else if (rates > 2.2) {
            iv3.setImageResource(R.drawable.star_haft);
        }

//set star 4
        if (rates > 3.7) {
            iv4.setImageResource(R.drawable.star_on);
        } else if (rates > 3.2) {
            iv4.setImageResource(R.drawable.star_haft);
        }

//set star 5
        if (rates > 4.7) {
            iv5.setImageResource(R.drawable.star_on);
        } else if (rates > 4.2) {
            iv5.setImageResource(R.drawable.star_haft);
        }
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