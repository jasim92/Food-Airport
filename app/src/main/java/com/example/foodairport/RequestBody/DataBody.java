package com.example.foodairport.RequestBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataBody {
    @SerializedName("data")
    @Expose
    private ReqBody data;


    public DataBody(ReqBody data) {
        this.data = data;
    }
}
