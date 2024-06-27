package com.example.app_ecommerce.serviceapi;

import com.example.app_ecommerce.model.MyData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAppService {

    @GET("products")
    Call<MyData> getProduct();
}
