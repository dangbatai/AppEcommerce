package com.example.app_ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_ecommerce.model.Product;
import com.example.app_ecommerce.model.ProductRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private ProductRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository=new ProductRepository(application);
    }
    public LiveData<List<Product>> getAllProduct(){
        return repository.getMutableLiveData();
    }
}
