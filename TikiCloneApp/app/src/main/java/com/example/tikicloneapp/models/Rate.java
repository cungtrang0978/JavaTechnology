package com.example.tikicloneapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Rate {
    private int id;
    private int ratePoint;
    private String comment;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private int idProduct;
    private String productName;
    private int price;
    private int discount;
    private String imageProductUrl;
    private int idUser;
    private int userFullName;

    public Rate(int ratePoint, String comment, int idProduct, int idUser) {
        this.ratePoint = ratePoint;
        this.comment = comment;
        this.idProduct = idProduct;
        this.idUser = idUser;
    }

    public Rate(int id, int ratePoint, String comment, int idProduct, String productName, int price, int discount, String imageProductUrl, int idUser) {
        this.id = id;
        this.ratePoint = ratePoint;
        this.comment = comment;
        this.idProduct = idProduct;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.imageProductUrl = imageProductUrl;
        this.idUser = idUser;
    }

    public Rate(int id, int ratePoint, String comment, Timestamp createdAt, Timestamp modifiedAt, int idProduct, String productName, String imageProductUrl, int idUser, int userFullName) {
        this.id = id;
        this.ratePoint = ratePoint;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.idProduct = idProduct;
        this.productName = productName;
        this.imageProductUrl = imageProductUrl;
        this.idUser = idUser;
        this.userFullName = userFullName;
    }

    public Rate(int id, int ratePoint, String comment, int idProduct, String imageProductUrl) {
        this.id = id;
        this.ratePoint = ratePoint;
        this.comment = comment;
        this.idProduct = idProduct;
        this.imageProductUrl = imageProductUrl;
    }

    public Rate(JSONObject jsonRate) throws JSONException {
        this.id = jsonRate.getInt("id");
        this.ratePoint = jsonRate.getInt("ratePoint");
        this.comment = jsonRate.getString("comment");
        this.idProduct = jsonRate.getInt("idProduct");
        this.imageProductUrl = jsonRate.getString("imageProductUrl");
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

    public int getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(int userFullName) {
        this.userFullName = userFullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageProductUrl() {
        return imageProductUrl;
    }

    public void setImageProductUrl(String imageProductUrl) {
        this.imageProductUrl = imageProductUrl;
    }

    public int getRatePoint() {
        return ratePoint;
    }

    public void setRatePoint(int ratePoint) {
        this.ratePoint = ratePoint;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
