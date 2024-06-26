package com.example.app_ecommerce.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_ecommerce.data.UserRepository;
import com.example.app_ecommerce.databinding.ActivityChangePasswordBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

public class Change_Password extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private UserViewModel userViewModel;
    private UserRepository userRepository;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userRepository = new UserRepository(this.getApplication());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", -1);
            try {
                userViewModel.loadUserData(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        binding.setViewModel(userViewModel);
        binding.setLifecycleOwner(this);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePassword() {
        String oldPassword = binding.txtCurrentPassword.getEditText().getText().toString();
        String newPassword = binding.txtNewPassword.getEditText().getText().toString();
        String confirmPassword = binding.txtConfirmNewPassword.getEditText().getText().toString();

        if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
            if (newPassword.equals(confirmPassword)) {
                try {
                    String storedPassword = userViewModel.getPasswordForUser(userId);
                    if (storedPassword.equals(oldPassword)) {
                        if (!storedPassword.equals(newPassword)) {
                            if (newPassword.length() >= 6) {
                                new AlertDialog.Builder(Change_Password.this)
                                        .setTitle("Change Password")
                                        .setMessage("Are you sure you want to change the password?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                userViewModel.changePassword(userId, newPassword);
                                                Toast.makeText(Change_Password.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }
                            else {
                                Toast.makeText(Change_Password.this, "Password must be greater than 6 numbers!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Change_Password.this, "New password equals Old password!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Change_Password.this, "Old password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Change_Password.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Change_Password.this, "New password and confirm password do not match!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Change_Password.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        binding.txtCurrentPassword.getEditText().setText("");
        binding.txtNewPassword.getEditText().setText("");
        binding.txtConfirmNewPassword.getEditText().setText("");
    }
}
