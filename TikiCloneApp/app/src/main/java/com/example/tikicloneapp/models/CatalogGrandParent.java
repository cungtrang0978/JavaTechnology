package com.example.tikicloneapp.models;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CatalogGrandParent implements Serializable {
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("imageUrl")
    private String mImageUrl;

    public CatalogGrandParent() {
    }

    public CatalogGrandParent(int mId, String mName, String mImageUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setData(Cursor cursor){
        mId = cursor.getInt(0);
        mName = cursor.getString(1);
        mImageUrl = cursor.getString(2);
    }
}
