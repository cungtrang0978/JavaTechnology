package com.example.tikicloneapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {
    @SerializedName("id")
    private int mId;

    @SerializedName("idTransact")
    private int mIdTransact;

    @SerializedName("idProduct")
    private int mIdProduct;

    @SerializedName("qty")
    private int mQty;

    @SerializedName("amount")
    private int mAmount;

    public Order(int mId, int mIdTransact, int mIdProduct, int mQty, int mAmount) {
        this.mId = mId;
        this.mIdTransact = mIdTransact;
        this.mIdProduct = mIdProduct;
        this.mQty = mQty;
        this.mAmount = mAmount;
    }

    public Order(int mId, int mIdProduct, int mQty, int mAmount) {
        this.mId = mId;
        this.mIdProduct = mIdProduct;
        this.mQty = mQty;
        this.mAmount = mAmount;
    }

    public Order(int mIdProduct, int mQty, int mAmount) {
        this.mIdProduct = mIdProduct;
        this.mQty = mQty;
        this.mAmount = mAmount;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmIdTransact() {
        return mIdTransact;
    }

    public void setmIdTransact(int mIdTransact) {
        this.mIdTransact = mIdTransact;
    }

    public int getmIdProduct() {
        return mIdProduct;
    }

    public void setmIdProduct(int mIdProduct) {
        this.mIdProduct = mIdProduct;
    }

    public int getmQty() {
        return mQty;
    }

    public void setmQty(int mQty) {
        this.mQty = mQty;
    }

    public int getmAmount() {
        return mAmount;
    }

    public void setmAmount(int mAmount) {
        this.mAmount = mAmount;
    }
}
