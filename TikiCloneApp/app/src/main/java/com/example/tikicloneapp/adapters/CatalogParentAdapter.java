package com.example.tikicloneapp.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikicloneapp.R;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.models.Catalog;
import com.example.tikicloneapp.models.CatalogParent;
import com.example.tikicloneapp.others.ItemOffsetDecoration;

import java.util.ArrayList;

public class CatalogParentAdapter extends RecyclerView.Adapter<CatalogParentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CatalogParent> mCatalogParents;


    public CatalogParentAdapter(Context mContext, ArrayList<CatalogParent> mCatalogParents) {
        this.mContext = mContext;
        this.mCatalogParents = mCatalogParents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View cataParentView = layoutInflater.inflate(R.layout.row_catalog_parent, parent, false);
        return new ViewHolder(cataParentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CatalogParent catalogParent = mCatalogParents.get(position);

        holder.tvName.setText(catalogParent.getmName());

        final DBVolley dbVolley = new DBVolley(mContext);
        ArrayList<Catalog> catalogArrayList = new ArrayList<>();
        CatalogAdapter catalogAdapter;

        catalogAdapter = new CatalogAdapter(mContext, catalogArrayList, CatalogAdapter.CatalogType.Category);

//
        holder.rvCatalog.setAdapter(catalogAdapter);

        if (catalogParent.getmGrParents() == 0) {
            final int sold = 10;
            dbVolley.getCatalog(catalogArrayList, catalogAdapter, null, sold);
            holder.tvViewAll.setVisibility(View.GONE);
        } else {
            dbVolley.getCatalog(catalogArrayList, catalogAdapter, catalogParent.getmId(), null);

        }
        holder.lay_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catalogParent.getmGrParents() != 0) {
                    Intent intent = new Intent(mContext, ListProductActivity.class);
                    intent.putExtra("catalogParent", catalogParent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                    } else mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCatalogParents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvViewAll;
        private LinearLayout lay_item;
        private RecyclerView rvCatalog;


        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.textView_name_catalogParent);
            rvCatalog = itemView.findViewById(R.id.recyclerView_catalog);
            lay_item = itemView.findViewById(R.id.layout_item);
            tvViewAll = itemView.findViewById(R.id.textView_viewAll);

            rvCatalog.setLayoutManager(new GridLayoutManager(mContext, 3));
//            rvCatalog.addItemDecoration(new SpacesItemRecyclerViewDecoration(3, 20, true));
            rvCatalog.addItemDecoration(new ItemOffsetDecoration(5));


        }


    }


}
