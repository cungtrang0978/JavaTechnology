package com.example.tikicloneapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.fragments.navigations.CategoryFragment;
import com.example.tikicloneapp.models.CatalogGrandParent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CatalogGrParentsAdapter extends RecyclerView.Adapter<CatalogGrParentsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CatalogGrandParent> mCatalogGrandParents;


    public CatalogGrParentsAdapter(Context mContext, ArrayList<CatalogGrandParent> mCatalogGrandParents) {
        this.mContext = mContext;
        this.mCatalogGrandParents = mCatalogGrandParents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View grParentView = layoutInflater.inflate(R.layout.row_catalog_gr_parents, parent, false);
        ViewHolder viewHolder = new ViewHolder(grParentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CatalogGrandParent catalogGrandParent = mCatalogGrandParents.get(position);

        loadImageFromUrl(catalogGrandParent.getmImageUrl(), holder.ivGrand);
        holder.tvName.setText(catalogGrandParent.getmName());

/*        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    if (position != CategoryFragment.grandIndex) {
//                        Toast.makeText(mContext, "Click " + catalogGrandParent.getmName(), Toast.LENGTH_SHORT).show();

                        CategoryFragment.setOnClickGrand(catalogGrandParent);
                        CategoryFragment.grandIndex = position;
//                        Toast.makeText(mContext, CategoryFragment.grParentArrayList.size() +"", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
        holder.layoutGrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position!= CategoryFragment.grandIndex){
                    CategoryFragment.setOnClickGrand(catalogGrandParent);
                    CategoryFragment.grandIndex = position;
                }
            }
        });

//        if (CategoryFragment.grandIndex == position) {
//            holder.layoutGrand.setBackgroundColor(Color.parseColor("#567845"));
//            holder.tvName.setTextColor(Color.parseColor("#ffffff"));
//        } else {
//            holder.layoutGrand.setBackgroundColor(Color.parseColor("#ffffff"));
//            holder.tvName.setTextColor(Color.parseColor("#000000"));
//        }
    }

    @Override
    public int getItemCount() {
        return mCatalogGrandParents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ivGrand;
        private final TextView tvName;
        private final LinearLayout layoutGrand;

        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGrand = itemView.findViewById(R.id.imageView_catalogGrParents);
            tvName = itemView.findViewById(R.id.textView_name_catalogGrParent);
            layoutGrand = itemView.findViewById(R.id.linearLayout_grand);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    private void loadImageFromUrl(String url, ImageView imageView) {
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
    }
}
