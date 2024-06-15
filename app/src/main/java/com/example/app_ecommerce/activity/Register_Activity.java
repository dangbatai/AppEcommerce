package com.example.app_ecommerce.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.databinding.ActivityReginsterBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

public class Register_Activity extends AppCompatActivity {
    private AppCompatButton button;
    private ActivityReginsterBinding binding;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reginster);
        binding= ActivityReginsterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        binding.setViewModel(userViewModel);
        binding.setLifecycleOwner(this);

    }
}