package com.example.tikicloneapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("idCatalog")
    private int idCatalog;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private int price;
    @SerializedName("discount")
    private int discount;
    @SerializedName("created")
    private int created;
    @SerializedName("views")
    private int views;
    @SerializedName("qty")
    private int qty;
    private int sold;
    private String imageUrl;

    public Product(){}

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Product(int id, int idCatalog, String name, String description, int price, int discount, int created, int views) {
        this.id = id;
        this.idCatalog = idCatalog;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.created = created;
        this.views = views;
    }

    public Product(int id, int idCatalog, String name, String description, int price, int discount, int created, int views, int qty) {
        this.id = id;
        this.idCatalog = idCatalog;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.created = created;
        this.views = views;
        this.qty = qty;
    }

    public Product(int price, int discount, int qty) {
        this.price = price;
        this.discount = discount;
        this.qty = qty;
    }

    public Product(int id, int idCatalog, String name, int price, int discount, int qty, String imageUrl) {
        this.id = id;
        this.idCatalog = idCatalog;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.qty = qty;
        this.imageUrl = imageUrl;
    }

    public Product(int id, int idCatalog, String name, int price, int discount, int qty, int sold, String imageUrl) {
        this.id = id;
        this.idCatalog = idCatalog;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.qty = qty;
        this.sold = sold;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", idCatalog=" + idCatalog +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", created=" + created +
                ", view=" + views +
                ", qty=" + qty +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCatalog() {
        return idCatalog;
    }

    public void setIdCatalog(int idCatalog) {
        this.idCatalog = idCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPriceDiscount(){
        return price- price*discount/100;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
