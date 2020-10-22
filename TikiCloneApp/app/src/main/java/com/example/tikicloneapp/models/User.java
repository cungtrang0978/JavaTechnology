package com.example.tikicloneapp.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private Integer mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("phoneNumber")
    private String mPhoneNumber;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("sex")
    private Integer mSex;

    @SerializedName("province")
    private String mProvince;

    @SerializedName("district")
    private String mDistrict;

    @SerializedName("ward")
    private String mWard;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("imageUrl")
    private String mImageUrl;

    @SerializedName("created")
    private Long mCreated;

    @SerializedName("birthdate")
    private String mBirthDate;

    public User(Integer mId) {
        this.mId = mId;
    }

    public User(String mName, String mPhoneNumber, String mProvince, String mDistrict, String mWard, String mAddress) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
        this.mAddress = mAddress;
    }

    public User(String mProvince, String mDistrict, String mWard, String mAddress) {
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
        this.mAddress = mAddress;
    }

    public User(String mProvince, String mDistrict, String mWard) {
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
    }


    public String getmProvince() {
        return mProvince;
    }

    public void setmProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public void setmDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
    }

    public String getmWard() {
        return mWard;
    }

    public void setmWard(String mWard) {
        this.mWard = mWard;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public Integer getmSex() {
        return mSex;
    }

    public void setmSex(Integer mSex) {
        this.mSex = mSex;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public Long getmCreated() {
        return mCreated;
    }

    public void setmCreated(Long mCreated) {
        this.mCreated = mCreated;
    }

    public String getmBirthDate() {
        return mBirthDate;
    }

    public void setmBirthDate(String mBirthDate) {
        this.mBirthDate = mBirthDate;
    }
}
