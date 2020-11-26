package com.example.tikicloneapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.models.Rate;

import java.util.ArrayList;

public class ReviewAdapter extends  RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    final Context mContext;
    final ArrayList<Rate> rates;

    public ReviewAdapter(Context mContext, ArrayList<Rate> rates) {
        this.mContext = mContext;
        this.rates = rates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_review, parent, false);

        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Rate rate = rates.get(position);
        Log.d("thang", "onBindViewHolder: "+ rates);
        holder.tvTitle.setText(getTitle(rate.getRatePoint()));
        holder.tvComment.setText(rate.getComment());
        setStars(rate.getRatePoint(), holder);
        holder.tvUserFullName.setText(rate.getUserFullName());
//        holder.tvDate

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

    private void setStars(int ratePoint, ReviewAdapter.ViewHolder viewHolder) {
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

    @Override
    public int getItemCount() {
        return rates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvComment, tvDate, tvUserFullName;
        final ImageView star2, star3, star4, star5;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textView_titleReview);
            tvComment = itemView.findViewById(R.id.textView_comment);
            star2 = itemView.findViewById(R.id.imageView_star2);
            star3 = itemView.findViewById(R.id.imageView_star3);
            star4 = itemView.findViewById(R.id.imageView_star4);
            star5 = itemView.findViewById(R.id.imageView_star5);
            tvUserFullName = itemView.findViewById(R.id.textView_userFullName);
            tvDate = itemView.findViewById(R.id.textView_date);
        }

    }
}
