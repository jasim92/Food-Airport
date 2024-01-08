package com.example.foodairport.FoodModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Restaurant1 {
    @SerializedName("data")
    @Expose
    private DataR data;

    public DataR getData() {
        return data;
    }

    public void setData(DataR data) {
        this.data = data;
    }
}
