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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.models.Catalog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Catalog> mCatalogs;
    private CatalogType mCatalogType;

    public CatalogAdapter(Context mContext, ArrayList<Catalog> mCatalogs, CatalogType mCatalogType) {
        this.mContext = mContext;
        this.mCatalogs = mCatalogs;
        this.mCatalogType = mCatalogType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View catalogView = null;
        if(mCatalogType==CatalogType.Category){
            catalogView = layoutInflater.inflate(R.layout.row_catalog, parent, false);
        }
        else if(mCatalogType ==CatalogType.Home){
            catalogView = layoutInflater.inflate(R.layout.item_category, parent, false);
        }
        assert catalogView != null;
        return new ViewHolder(catalogView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Catalog catalog = mCatalogs.get(position);
        loadImageFromUrl(catalog.getmImageUrl(), holder.mImageCatalog);
        holder.mTvName.setText(catalog.getmName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(mContext, ListProductActivity.class);
//                    intent.putExtra("idCatalog", catalog.getmId());
                    intent.putExtra("catalog", catalog);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                    }else  mContext.startActivity(intent);
                }
            }
        });

//        holder.parentsLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "Click " + catalog.getmName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, ListProductActivity.class);
//                intent.putExtra("idCatalog", catalog.getmId());
//                Log.d("AAA", "idCatalog"+ catalog.getmId());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mCatalogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mImageCatalog;
        private TextView mTvName;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mImageCatalog = itemView.findViewById(R.id.imageView_catalog);
            mTvName = itemView.findViewById(R.id.textView_name_catalog);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    private void loadImageFromUrl(String url, ImageView imageView) {
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    private interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }

    public enum CatalogType {Category, Home}
}
