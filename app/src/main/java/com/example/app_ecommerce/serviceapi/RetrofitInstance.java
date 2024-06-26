package com.example.app_ecommerce.serviceapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;
    private static String Base_Url = "https://dummyjson.com/";

    public static ProductAppService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Base_Url).addConverterFactory(GsonConverterFactory.create()).build();


        }
         return retrofit.create(ProductAppService.class);
    }

}

