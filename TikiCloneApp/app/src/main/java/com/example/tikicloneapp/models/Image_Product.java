package com.example.tikicloneapp.models;

import com.google.gson.annotations.SerializedName;

public class Image_Product {
    @SerializedName("id")
    private int mId;
    @SerializedName("idProduct")
    private int mIdProduct;
    @SerializedName("imageUrl")
    private int mImageUrl;

    public Image_Product() {
    }

    public Image_Product(int mId, int mIdProduct, int mImageUrl) {
        this.mId = mId;
        this.mIdProduct = mIdProduct;
        this.mImageUrl = mImageUrl;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmIdProduct() {
        return mIdProduct;
    }

    public void setmIdProduct(int mIdProduct) {
        this.mIdProduct = mIdProduct;
    }

    public int getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(int mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
