package com.example.app_ecommerce.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_ecommerce.databinding.ActivityReginsterBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

public class Register_Activity extends AppCompatActivity {
    private ActivityReginsterBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReginsterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setViewModel(userViewModel);
        binding.setLifecycleOwner(this);

        binding.termsConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.termsConditions.setChecked(true);
                    showPoliciesDialog();
                } else {
                    userViewModel.termsAccepted.setValue(false);
                }
            }
        });

        binding.signUpButton.setOnClickListener(v -> {
            if (binding.termsConditions.isChecked()) {
                userViewModel.onSignUpClicked();

            } else {
                Toast.makeText(this, "Please accept the terms and conditions to proceed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPoliciesDialog() {
        new AlertDialog.Builder(Register_Activity.this)
                .setTitle("Terms and Conditions")
                .setMessage("Users must commit to providing accurate information and must not use other people's accounts.\n" +
                        "The application may share user information with third parties in certain circumstances (e.g., delivery, payment).")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Check the checkbox after the user accepts the terms
                    binding.termsConditions.setChecked(true);
                    userViewModel.termsAccepted.setValue(true);
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // Uncheck the checkbox if the user doesn't accept the terms
                    binding.termsConditions.setChecked(false);
                    userViewModel.termsAccepted.setValue(false);
                })
                .show();
    }
}
