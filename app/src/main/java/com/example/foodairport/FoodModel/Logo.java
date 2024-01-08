package com.example.foodairport.FoodModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Logo {

    @SerializedName("data")
    @Expose
    private Data__2 data;

    public Data__2 getData() {
        return data;
    }

    public void setData(Data__2 data) {
        this.data = data;
    }
}
