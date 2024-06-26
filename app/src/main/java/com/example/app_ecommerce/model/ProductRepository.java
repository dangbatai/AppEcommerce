package com.example.app_ecommerce.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.app_ecommerce.serviceapi.ProductAppService;
import com.example.app_ecommerce.serviceapi.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
        private ArrayList<Product> products=new ArrayList<>();
        private MutableLiveData<List<Product>> mutableLiveData=new MutableLiveData<>();
        private Application application;
        public  ProductRepository(Application application){
            this.application=application;
        }
        public  MutableLiveData<List<Product>> getMutableLiveData(){
            ProductAppService productAppService= RetrofitInstance.getService();
            Call<MyData> call=productAppService.getProduct();
            call.enqueue(new Callback<MyData>() {
                @Override
                public void onResponse(Call<MyData> call, Response<MyData> response) {
                    MyData myData=response.body();
                    if (myData !=null && myData.getProducts()!=null){
                        products= (ArrayList<Product>) myData.getProducts();

                        mutableLiveData.setValue(products);
                    }
                }

                @Override
                public void onFailure(Call<MyData> call, Throwable t) {
                    Log.v("TAGY",t.getMessage());
                }
            });
               return mutableLiveData;
        }
}
