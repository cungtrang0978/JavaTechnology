package com.example.tikicloneapp.models;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Catalog  implements Serializable{
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("idParents")
    private int mIdParents;
    @SerializedName("imageUrl")
    private String mImageUrl;

    public Catalog(int mId, String mName, int mIdParents, String mImageUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mIdParents = mIdParents;
        this.mImageUrl = mImageUrl;
    }

    public Catalog(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }
    public  Catalog(){}

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

    public int getmIdParents() {
        return mIdParents;
    }

    public void setmIdParents(int mIdParents) {
        this.mIdParents = mIdParents;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImage) {
        this.mImageUrl = mImage;
    }

    public void setData(Cursor cursor){
        mId = cursor.getInt(0);
        mName = cursor.getString(1);
        mIdParents = cursor.getInt(2);
        mImageUrl = cursor.getString(3);
    }

}

