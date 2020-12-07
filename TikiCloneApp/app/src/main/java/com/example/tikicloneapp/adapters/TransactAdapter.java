package com.example.tikicloneapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.TransactActivity;
import com.example.tikicloneapp.fragments.transacts.ListTransactFragment;
import com.example.tikicloneapp.models.Transact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.tikicloneapp.models.Transact.setTvStatus;

public class TransactAdapter extends RecyclerView.Adapter<TransactAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<Transact> mTransactArrayList;
    private final int mResource;

    public TransactAdapter(Context mContext, ArrayList<Transact> mTransactArrayList, int mResource) {
        this.mContext = mContext;
        this.mTransactArrayList = mTransactArrayList;
        this.mResource = mResource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(mResource, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Transact transact = mTransactArrayList.get(position);

        holder.tvIdTransact.setText(String.valueOf(transact.getmId()));

        Date createdAt = new Date(transact.getmCreated().getTime());
        @SuppressLint("SimpleDateFormat")
        String dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(createdAt);
        holder.tvTimeCreated.setText(dateFormat);

        setTvStatus(holder.tvStatus, transact.getmStatus());

        getNameTransact(holder, transact.getmId());

        if (transact.getmStatus() == Transact.STATUS_CANCEL) {
            holder.ivIconStatus.setImageResource(R.drawable.icons8_cancel_red_64);
        } else if (transact.getmStatus() == Transact.STATUS_SUCCESS) {
            holder.ivIconStatus.setImageResource(R.drawable.icons8_ok_64);
        } else if (transact.getmStatus() == Transact.STATUS_TIKI_RECEIVED) {
            holder.ivIconStatus.setImageResource(R.drawable.icons8_synchronize_64);
        } else if (transact.getmStatus() == Transact.STATUS_DELIVERING) {
            holder.ivIconStatus.setImageResource(R.drawable.icons8_motocross_64);
        } else if (transact.getmStatus() == Transact.STATUS_PICKING_GOODS) {
            holder.ivIconStatus.setImageResource(R.drawable.icons8_received_64);

        }

        holder.layItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransactActivity.class);
                intent.putExtra("activity", R.layout.activity_list_transact);
                intent.putExtra("idTransact", transact.getmId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((Activity) mContext).startActivityForResult(intent, ListTransactFragment.REQUEST_CODE_LIST_TRANSACT, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                } else
                    ((Activity) mContext).startActivityForResult(intent, ListTransactFragment.REQUEST_CODE_LIST_TRANSACT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTransactArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNameTransact, tvIdTransact, tvTimeCreated, tvStatus;
        final ImageView ivIconStatus;
        final LinearLayout layItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameTransact = itemView.findViewById(R.id.textView_nameTransact);
            tvIdTransact = itemView.findViewById(R.id.textView_idTransact);
            tvTimeCreated = itemView.findViewById(R.id.textView_timeCreated);
            tvStatus = itemView.findViewById(R.id.textView_status);
            ivIconStatus = itemView.findViewById(R.id.imageView_iconStatus);
            layItem = itemView.findViewById(R.id.layout_item);
        }
    }

    private void getNameTransact(final ViewHolder viewHolder, final int idTransact) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.dbVolley.URL_GET_NAME_TRANSACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("thang", "getNameTransact: "+ response);
                viewHolder.tvNameTransact.setText(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("thang", "getNameTransact: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idTransact));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
