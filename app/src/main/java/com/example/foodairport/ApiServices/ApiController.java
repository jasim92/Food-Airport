package com.example.foodairport.ApiServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {
    public static String url = "http://192.168.0.137:1337";

    private static Retrofit retrofit;

    private static ApiController apiController;

    public ApiController() {
        //-----------------------------These Lines of code is used to Logging the retrofit networking and message from server-----//
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        //-----------------------------------------------------------------------------------------------------
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized ApiController getInstance()
    {
        if (apiController==null)
            apiController = new ApiController();
        return apiController;
    }

    public ApiSet getApi()
    {
        return retrofit.create(ApiSet.class);
    }
}
