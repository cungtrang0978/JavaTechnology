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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;

import com.example.tikicloneapp.activities.ReviewsActivity;
import com.example.tikicloneapp.activities.WriteReviewActivity;

import com.example.tikicloneapp.models.Product;

import java.util.ArrayList;

public class WaitingReviewAdapter extends RecyclerView.Adapter<WaitingReviewAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product> mProducts;


    public WaitingReviewAdapter(Context mContext, ArrayList<Product> mProducts) {
        this.mContext = mContext;
        this.mProducts = mProducts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_waiting_review_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = mProducts.get(position);
        holder.tvNameProduct.setText(product.getName());
        ProductListAdapter.loadImageFromUrl(product.getImageUrl(), holder.ivProduct);

        holder.layReview.setOnClickListener(onClickListener(0, product));
        holder.ivStar1.setOnClickListener(onClickListener(1, product));
        holder.ivStar2.setOnClickListener(onClickListener(2, product));
        holder.ivStar3.setOnClickListener(onClickListener(3, product));
        holder.ivStar4.setOnClickListener(onClickListener(4, product));
        holder.ivStar5.setOnClickListener(onClickListener(5, product));
    }

    View.OnClickListener onClickListener(final int ratePoint, final Product product) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WriteReviewActivity.class);
                intent.putExtra("ratePoint", ratePoint);
                intent.putExtra("product", product);
//                intent.putExtra("activity", R.layout.activity_list_transact);
//                intent.putExtra("idTransact", transact.getmId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((Activity) mContext).startActivityForResult(intent, ReviewsActivity.REFRESH_CODE, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                } else
                    ((Activity) mContext).startActivityForResult(intent, ReviewsActivity.REFRESH_CODE);
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNameProduct;
        final ImageView ivProduct;
        final ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        final LinearLayout layReview;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.textView_nameProduct);
            ivProduct = itemView.findViewById(R.id.imageView_product);
            layReview = itemView.findViewById(R.id.linearLayout_row_waiting_review);
            ivStar1 = itemView.findViewById(R.id.imageView_star1);
            ivStar2 = itemView.findViewById(R.id.imageView_star2);
            ivStar3 = itemView.findViewById(R.id.imageView_star3);
            ivStar4 = itemView.findViewById(R.id.imageView_star4);
            ivStar5 = itemView.findViewById(R.id.imageView_star5);
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}
