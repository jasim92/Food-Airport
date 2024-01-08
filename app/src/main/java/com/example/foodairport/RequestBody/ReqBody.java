package com.example.foodairport.RequestBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReqBody {
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("foodName")
    @Expose
    private String foodName;
    @SerializedName("RestaurantId")
    @Expose
    private String restaurantId;
    @SerializedName("RestaurantName")
    @Expose
    private String restaurantName;
    @SerializedName("foodStatus")
    @Expose
    private String foodStatus;
    @SerializedName("confirmByRestaurant")
    @Expose
    private Boolean confirmByRestaurant;

    public ReqBody(String customerName, String address, Integer quantity, String phone,
                   Integer price, String foodName, String restaurantId, String restaurantName,
                   String foodStatus, Boolean confirmByRestaurant) {
        this.customerName = customerName;
        this.address = address;
        this.quantity = quantity;
        this.phone = phone;
        this.price = price;
        this.foodName = foodName;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.foodStatus = foodStatus;
        this.confirmByRestaurant = confirmByRestaurant;
    }

    public ReqBody() {
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setFoodStatus(String foodStatus) {
        this.foodStatus = foodStatus;
    }

    public void setConfirmByRestaurant(Boolean confirmByRestaurant) {
        this.confirmByRestaurant = confirmByRestaurant;
    }


}
