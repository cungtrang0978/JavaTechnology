package com.example.tikicloneapp.models;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transact implements Serializable {
    @SerializedName("id")
    private Integer mId;

    @SerializedName("status")
    private Integer mStatus;

    @SerializedName("id_user")
    private Integer mId_User;

    @SerializedName("user_name")
    private String mUser_name;

    @SerializedName("user_phone")
    private String mUser_phone;

    @SerializedName("province")
    private String mProvince;

    @SerializedName("district")
    private String mDistrict;

    @SerializedName("ward")
    private String mWard;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("qty")
    private Integer mQty;

    @SerializedName("amount")
    private Integer mAmount;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("created")
    private Long mCreated;

    public static int STATUS_CANCEL = -1;
    public static int STATUS_NOT_ORDER = 0;
    public static int STATUS_TIKI_RECEIVED = 1;
    public static int STATUS_SELLER_RECEIVED = 2;
    public static int STATUS_DELIVERING = 3;
    public static int STATUS_SUCCESS = 4;

    public static int CODE_GET_ALL = 1234;

    public Transact() {
    }

    public Transact(Integer mQty, Integer mAmount) {
        this.mQty = mQty;
        this.mAmount = mAmount;
    }

    public Transact(Integer mStatus, Integer mId_User, String mUser_name, String mUser_phone, String mProvince, String mDistrict, String mWard, String mAddress, Integer mQty, Integer mAmount, String mMessage, Long mCreated) {
        this.mStatus = mStatus;
        this.mId_User = mId_User;
        this.mUser_name = mUser_name;
        this.mUser_phone = mUser_phone;
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
        this.mAddress = mAddress;
        this.mQty = mQty;
        this.mAmount = mAmount;
        this.mMessage = mMessage;
        this.mCreated = mCreated;
    }

    public Transact(Integer mStatus, Integer mId_User, String mUser_name, String mUser_phone, String mProvince, String mDistrict, String mWard, String mAddress, Integer mQty, Integer mAmount, String mMessage) {
        this.mStatus = mStatus;
        this.mId_User = mId_User;
        this.mUser_name = mUser_name;
        this.mUser_phone = mUser_phone;
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
        this.mAddress = mAddress;
        this.mQty = mQty;
        this.mAmount = mAmount;
        this.mMessage = mMessage;
    }

    public Transact(Integer mId, Integer mStatus, Long mCreated) {
        this.mId = mId;
        this.mStatus = mStatus;
        this.mCreated = mCreated;
    }

    public Transact(String mUser_name, String mUser_phone, String mProvince, String mDistrict, String mWard, String mAddress) {
        this.mUser_name = mUser_name;
        this.mUser_phone = mUser_phone;
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
        this.mAddress = mAddress;
    }

    public Transact(Integer mId_User, String mUser_name, String mUser_phone, String mProvince, String mDistrict, String mWard, String mAddress, Integer mQty, Integer mAmount) {
        this.mId_User = mId_User;
        this.mUser_name = mUser_name;
        this.mUser_phone = mUser_phone;
        this.mProvince = mProvince;
        this.mDistrict = mDistrict;
        this.mWard = mWard;
        this.mAddress = mAddress;
        this.mQty = mQty;
        this.mAmount = mAmount;
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

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Integer getmQty() {
        return mQty;
    }

    public void setmQty(Integer mQty) {
        this.mQty = mQty;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getmStatus() {
        return mStatus;
    }

    public void setmStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }

    public Integer getmId_User() {
        return mId_User;
    }

    public void setmId_User(Integer mId_User) {
        this.mId_User = mId_User;
    }

    public String getmUser_name() {
        return mUser_name;
    }

    public void setmUser_name(String mUser_name) {
        this.mUser_name = mUser_name;
    }

    public String getmUser_phone() {
        return mUser_phone;
    }

    public void setmUser_phone(String mUser_phone) {
        this.mUser_phone = mUser_phone;
    }

    public Integer getmAmount() {
        return mAmount;
    }

    public void setmAmount(Integer mAmount) {
        this.mAmount = mAmount;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public Long getmCreated() {
        return mCreated;
    }

    public void setmCreated(Long mCreated) {
        this.mCreated = mCreated;
    }

    public static void setTvStatus(TextView tvStatus, int status){
        String staString = "";
        if(status==STATUS_TIKI_RECEIVED){
            staString = "Tiki đã tiếp nhận";
        }
        if(status==STATUS_CANCEL){
            staString = "Đã hủy";
        }
        if(status==STATUS_SUCCESS){
            staString = "Giao hàng thành công";
        }
        if(status==STATUS_DELIVERING){
            staString = "Đang giao";
        }
        if(status==STATUS_SELLER_RECEIVED){
            staString = "Đang lấy hàng";
        }
        tvStatus.setText(staString);
    }
}