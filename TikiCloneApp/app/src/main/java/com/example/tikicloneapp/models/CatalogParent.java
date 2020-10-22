package com.example.tikicloneapp.models;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CatalogParent implements Serializable {
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("idCatalog_gr_parents")
    private int mGrParents;

    public CatalogParent(){}

    public CatalogParent(int mId, String mName, int mGrParents) {
        this.mId = mId;
        this.mName = mName;
        this.mGrParents = mGrParents;
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

    public int getmGrParents() {
        return mGrParents;
    }

    public void setmGrParents(int mGrParents) {
        this.mGrParents = mGrParents;
    }

    public void setData(Cursor cursor){
        mId = cursor.getInt(0);
        mName = cursor.getString(1);
        mGrParents = cursor.getInt(2);
    }
}
