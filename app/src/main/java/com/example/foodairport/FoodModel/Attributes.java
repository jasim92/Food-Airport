
package com.example.foodairport.FoodModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("foodName")
    @Expose
    private String foodName;

    @SerializedName("foodId")
    @Expose
    private String foodId;

    @SerializedName("foodPrice")
    @Expose
    private Integer foodPrice;

    @SerializedName("foodQuantity")
    @Expose
    private Integer foodQuantity;

    @SerializedName("foodDescription")
    @Expose
    private String foodDescription;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;

    @SerializedName("foodPhoto")
    @Expose
    private FoodPhoto foodPhoto;

    @SerializedName("restaurant")
    @Expose
    private Restaurant1 restaurant;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public Integer getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Integer foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public FoodPhoto getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(FoodPhoto foodPhoto) {
        this.foodPhoto = foodPhoto;
    }

    public Restaurant1 getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant1 restaurant) {
        this.restaurant = restaurant;
    }

}
