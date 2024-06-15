package com.example.app_ecommerce.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_ecommerce.databinding.ActivityLoginBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using data binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Set ViewModel and lifecycle owner for data binding
        binding.setViewModel(userViewModel);
        binding.setLifecycleOwner(this);

    }
}
