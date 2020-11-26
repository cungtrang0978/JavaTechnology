package com.example.tikicloneapp.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.tikicloneapp.activities.CartActivity;
import com.example.tikicloneapp.activities.ListProductActivity;
import com.example.tikicloneapp.activities.MainActivity;
import com.example.tikicloneapp.activities.ProductDetailActivity;
import com.example.tikicloneapp.database.DBVolley;
import com.example.tikicloneapp.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.tikicloneapp.MyClass.setTextView_StrikeThrough;
import static com.example.tikicloneapp.adapters.ProductsAdapter.setRate;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private final Context mContext;
    private final int mResource;
    private final ArrayList<Product> mProducts;

    public ProductListAdapter(Context mContext, int mResource, ArrayList<Product> mProducts) {
        this.mContext = mContext;
        this.mResource = mResource;
        this.mProducts = mProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View productView = layoutInflater.inflate(mResource, parent, false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = mProducts.get(position);
        holder.tvName.setText(product.getName());
        int discount = product.getDiscount();
        int price = product.getPrice() - product.getPrice() * discount / 100;

        holder.tvPrice.setText(CartActivity.formatCurrency(price));
        if (mResource == R.layout.row_item_product_purchased) {
            if (discount == 0) {
                holder.tvPriceOrigin.setVisibility(View.INVISIBLE);
            }
            holder.tvPriceOrigin.setText(CartActivity.formatCurrency(product.getPrice()));
            if (product.getQty() > 0) {
                holder.tvCheckQty.setText("Còn hàng");

            } else {
                holder.tvCheckQty.setText("Đã hết hàng");
                holder.tvCheckQty.setTextColor(Color.parseColor("#FBC02D"));
            }
        } else {
            if (product.getRateQty() != 0) {
                holder.llReview.setVisibility(View.VISIBLE);
                setRate(holder.ivStar1, holder.ivStar2, holder.ivStar3, holder.ivStar4, holder.ivStar5, product.getRate());
                holder.tvRateQuantity.setText("(" + product.getRateQty() + ")");
            } else {
                holder.llReview.setVisibility(View.GONE);
            }
        }

        if (discount == 0) {
            holder.tvDiscount.setVisibility(View.GONE);
        } else
            holder.tvDiscount.setText(formatPercent(discount));

        DBVolley dbVolley = MainActivity.dbVolley;
        dbVolley.getFirstImageProduct(holder.ivProduct, product.getId());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("product", product);
                    Activity activity = (Activity) mContext;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.startActivityForResult(intent, ListProductActivity.REQUEST_CODE, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                    } else
                        activity.startActivityForResult(intent, ListProductActivity.REQUEST_CODE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProduct;
        private TextView tvName;
        private TextView tvPrice, tvPriceOrigin, tvCheckQty;
        private TextView tvDiscount, tvRateQuantity;
        private ItemClickListener itemClickListener;
        private LinearLayout llReview;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (mResource == R.layout.row_product) {
                ivProduct = itemView.findViewById(R.id.imageView_product);
                tvName = itemView.findViewById(R.id.textView_nameProduct);
                tvPrice = itemView.findViewById(R.id.textView_priceProduct);
                tvDiscount = itemView.findViewById(R.id.textView_discountProduct);
                ivStar1 = itemView.findViewById(R.id.imageView_star1);
                ivStar2 = itemView.findViewById(R.id.imageView_star2);
                ivStar3 = itemView.findViewById(R.id.imageView_star3);
                ivStar4 = itemView.findViewById(R.id.imageView_star4);
                ivStar5 = itemView.findViewById(R.id.imageView_star5);
                tvRateQuantity = itemView.findViewById(R.id.textView_rateQuantity);
                llReview = itemView.findViewById(R.id.linearLayout_review);
            }
            if (mResource == R.layout.row_item_product_purchased) {
                ivProduct = itemView.findViewById(R.id.imageView_product);
                tvName = itemView.findViewById(R.id.textView_nameProduct);
                tvDiscount = itemView.findViewById(R.id.textView_discountProduct);
                tvPrice = itemView.findViewById(R.id.textView_priceProduct);
                tvPriceOrigin = itemView.findViewById(R.id.textView_priceOrigin);
                tvCheckQty = itemView.findViewById(R.id.textView_check_qty);
                setTextView_StrikeThrough(tvPriceOrigin);
            }

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

    private interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public static void loadImageFromUrl(String url, ImageView imageView) {
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public static String formatPercent(int percent) {
        return "-" + percent + "%";
    }

}
