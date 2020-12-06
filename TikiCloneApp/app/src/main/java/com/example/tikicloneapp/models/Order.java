package com.example.tikicloneapp.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

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

    private String name;
    private int price;
    private int discount;
    private String imageUrl;

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

    public Order(JSONObject jsonOrder) throws JSONException {
        this.mId = jsonOrder.getInt("id");
        this.mIdTransact = jsonOrder.getInt("idTransact");
        this.mIdProduct = jsonOrder.getInt("idProduct");
        this.mQty = jsonOrder.getInt("qty");
        this.mAmount = jsonOrder.getInt("amount");

        if (!jsonOrder.getString("name").equals("null")) {
            this.name = jsonOrder.getString("name");
        }

        this.price = jsonOrder.getInt("price");
        this.discount = jsonOrder.getInt("discount");

        if (!jsonOrder.getString("imageUrl").equals("null")) {
            this.imageUrl = jsonOrder.getString("imageUrl");
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Order{" +
                "mId=" + mId +
                ", mIdTransact=" + mIdTransact +
                ", mIdProduct=" + mIdProduct +
                ", mQty=" + mQty +
                ", mAmount=" + mAmount +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
