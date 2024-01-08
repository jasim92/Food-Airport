
package com.example.foodairport.FoodModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {

    @SerializedName("data")
    @Expose
    private List<Data__1> data;

    public List<Data__1> getData() {
        return data;
    }

    public void setData(List<Data__1> data) {
        this.data = data;
    }
}
