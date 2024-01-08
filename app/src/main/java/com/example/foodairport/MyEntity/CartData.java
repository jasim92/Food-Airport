package com.example.foodairport.MyEntity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "CartData", indices = @Index(value = {"id"}, unique = true))
public class CartData {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @Expose
    public int id;

    @Expose
    @ColumnInfo(name = "name")
    public String name;

    @Expose
    @ColumnInfo(name = "url")
    public String url;

    @Expose
    @ColumnInfo(name = "price")
    public int price;

    @Expose
    @ColumnInfo(name = "quantity")
    public int quantity;

    @Expose
    @ColumnInfo(name = "RestaurantName")
    public String rName;

    @Expose
    @ColumnInfo(name = "RestaurantId")
    public String rId;

    public CartData(int id, String name, String url, int price, int quantity, String rName, String rId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.price = price;
        this.quantity = quantity;
        this.rName = rName;
        this.rId = rId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
