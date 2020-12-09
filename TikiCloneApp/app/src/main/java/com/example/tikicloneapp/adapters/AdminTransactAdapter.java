package com.example.tikicloneapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.AdminTransactActivity;
import com.example.tikicloneapp.models.Transact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.tikicloneapp.activities.AdminTransactManagementActivity.REQUEST_CODE_REFRESH;

public class AdminTransactAdapter extends RecyclerView.Adapter<AdminTransactAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Transact> mTransactArrayList;


    public AdminTransactAdapter(Context mContext, ArrayList<Transact> mTransactArrayList) {
        this.mContext = mContext;
        this.mTransactArrayList = mTransactArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_admin_transact, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transact transact = mTransactArrayList.get(position);

        holder.tvIdTransact.setText(String.valueOf(transact.getmId()));
        holder.tvUserId.setText(String.valueOf(transact.getmId_User()));

        Date createdAt = new Date(transact.getmCreated().getTime());
        Date modifiedAt = new Date(transact.getmModified().getTime());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy");

        String dateFormat = simpleDateFormat.format(createdAt);
        holder.tvTimeCreated.setText(dateFormat);

        if (transact.getmModified() != null) {
            holder.tvModified.setText(simpleDateFormat.format(modifiedAt));
        } else {
            holder.tvModified.setText("Đang cập nhật");
            holder.tvModified.setTypeface(holder.tvModified.getTypeface(), Typeface.ITALIC);
        }

        if (transact.getShipperId() != null) {
            holder.tvShipperId.setText(String.valueOf(transact.getShipperId()));
        } else {
            holder.tvShipperId.setText("Đang cập nhật");
            holder.tvShipperId.setTypeface(holder.tvModified.getTypeface(), Typeface.ITALIC);
        }

        holder.layItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdminTransactActivity.class);
                intent.putExtra("transact", transact);
                Activity activity = (Activity) mContext;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_REFRESH, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                } else
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_REFRESH);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTransactArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvUserId, tvIdTransact, tvTimeCreated, tvModified, tvShipperId;
        //        final ImageView ivIconStatus;
        final LinearLayout layItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.textView_userId);
            tvIdTransact = itemView.findViewById(R.id.textView_idTransact);
            tvTimeCreated = itemView.findViewById(R.id.textView_timeCreated);
            tvModified = itemView.findViewById(R.id.textView_modified);

            layItem = itemView.findViewById(R.id.layout_item);
            tvShipperId = itemView.findViewById(R.id.textView_shipperId);
        }
    }


}
