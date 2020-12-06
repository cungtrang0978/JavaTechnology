package com.example.tikicloneapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import static com.example.tikicloneapp.models.Transact.setTvStatus;

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
        @SuppressLint("SimpleDateFormat")
        String dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(createdAt);
        holder.tvTimeCreated.setText(dateFormat);

        setTvStatus(holder.tvStatus, transact.getmStatus());

        holder.layItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdminTransactActivity.class);
                intent.putExtra("transact", transact);
                mContext.startActivity(intent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    ((Activity) mContext).startActivityForResult(intent, ListTransactFragment.REQUEST_CODE_LIST_TRANSACT, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
//                } else
//                    ((Activity) mContext).startActivityForResult(intent, ListTransactFragment.REQUEST_CODE_LIST_TRANSACT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTransactArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvUserId, tvIdTransact, tvTimeCreated, tvStatus;
        //        final ImageView ivIconStatus;
        final LinearLayout layItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.textView_userId);
            tvIdTransact = itemView.findViewById(R.id.textView_idTransact);
            tvTimeCreated = itemView.findViewById(R.id.textView_timeCreated);
            tvStatus = itemView.findViewById(R.id.textView_status);
//            ivIconStatus = itemView.findViewById(R.id.imageView_iconStatus);
            layItem = itemView.findViewById(R.id.layout_item);
        }
    }


}
