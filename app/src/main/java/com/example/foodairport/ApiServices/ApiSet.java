package com.example.foodairport.ApiServices;

import com.example.foodairport.FoodModel.Food;
import com.example.foodairport.FoodModel.Restaurant;
import com.example.foodairport.PostModels.PostOrder;
import com.example.foodairport.RequestBody.DataBody;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiSet {

    @GET("/api/restaurants?populate=*")
    Call<Restaurant> getRestaurants();

    @GET("/api/foods?populate[0]=foodPhoto&populate[1]=restaurant")
    Call<Food> getFoods();

    @POST("api/orders")
    Call<PostOrder> order(@Body DataBody requestBody);
}
