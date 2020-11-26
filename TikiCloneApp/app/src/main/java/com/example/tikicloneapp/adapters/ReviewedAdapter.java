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
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ProductDetailActivity;
import com.example.tikicloneapp.models.Product;
import com.example.tikicloneapp.models.Rate;

import java.util.ArrayList;

public class ReviewedAdapter extends RecyclerView.Adapter<ReviewedAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Rate> rates;

    public ReviewedAdapter(Context mContext, ArrayList<Rate> rates) {
        this.mContext = mContext;
        this.rates = rates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_reviewed_item, parent, false);

        return new ReviewedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Rate rate = rates.get(position);

        ProductListAdapter.loadImageFromUrl(rate.getImageProductUrl(), holder.ivProduct);
        holder.tvTitle.setText(getTitle(rate.getRatePoint()));
        holder.tvComment.setText(rate.getComment());
        setStars(rate.getRatePoint(), holder);
        holder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                Product product = new Product(rate.getIdProduct(), rate.getProductName(), rate.getPrice(), rate.getDiscount(), rate.getImageProductUrl());
                intent.putExtra("product", product);
                Activity activity = (Activity) mContext;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                } else
                    activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    private String getTitle(int ratePoint) {
        switch (ratePoint) {
            case 1:
                return "Rất không hài lòng";

            case 2:
                return "Không hài lòng";

            case 3:
                return "Bình thường";

            case 4:
                return "Hài lòng";

            case 5:
                return "Rất hài lòng";
        }
        return "";
    }

    public static void setStars(int ratePoint, ViewHolder viewHolder) {
        if (ratePoint < 5) {
            viewHolder.star5.setImageResource(R.drawable.star_off);
        }
        if (ratePoint < 4) {
            viewHolder.star4.setImageResource(R.drawable.star_off);
        }
        if (ratePoint < 3) {
            viewHolder.star3.setImageResource(R.drawable.star_off);
        }
        if (ratePoint < 2) {
            viewHolder.star2.setImageResource(R.drawable.star_off);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvComment;
        final ImageView ivProduct;
        final ImageView star2, star3, star4, star5;
        final LinearLayout llRow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textView_titleReview);
            tvComment = itemView.findViewById(R.id.textView_comment);
            ivProduct = itemView.findViewById(R.id.imageView_product);
            star2 = itemView.findViewById(R.id.imageView_star2);
            star3 = itemView.findViewById(R.id.imageView_star3);
            star4 = itemView.findViewById(R.id.imageView_star4);
            star5 = itemView.findViewById(R.id.imageView_star5);
            llRow = itemView.findViewById(R.id.linearLayout_row_viewed);
        }

    }


}
