package com.example.foodairport.FoodModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data__2 {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("attributes")
    @Expose
    private Attributes__3 attributes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Attributes__3 getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes__3 attributes) {
        this.attributes = attributes;
    }
}
