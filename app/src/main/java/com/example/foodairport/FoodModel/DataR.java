package com.example.foodairport.FoodModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataR {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("attributes")
    @Expose
    private AttributesR attributes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttributesR getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesR attributes) {
        this.attributes = attributes;
    }
}
