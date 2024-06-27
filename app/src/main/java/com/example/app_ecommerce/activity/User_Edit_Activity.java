package com.example.app_ecommerce.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.data.User;
import com.example.app_ecommerce.data.UserRepository;
import com.example.app_ecommerce.databinding.ActivityUserEditBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

import java.util.concurrent.ExecutionException;

public class User_Edit_Activity extends AppCompatActivity {
    private UserRepository userRepository;
    private static final int REQUEST_IMAGE_GET = 1;

    private ActivityUserEditBinding binding;
    private UserViewModel userViewModel;
    private User user;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userRepository = new UserRepository(getApplication());
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", -1);
            try {
                userViewModel.loadUserData(userId);
                user = userRepository.getUserById(userId);
                if (user != null) {
                    binding.setViewModel(userViewModel);
                    loadImage(user.getImages());
                }
            } catch (ExecutionException | InterruptedException e) {
                Log.e("User_Edit_Activity", "Error loading user data", e);
            }
        }

        binding.btnedit.setOnClickListener(v -> updateUser());

        binding.setLifecycleOwner(this);

        binding.btncancel.setOnClickListener(v -> clearFields());

        binding.backButton.setOnClickListener(v -> finish());

        binding.imageview.setOnClickListener(v -> openImageGallery());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String imagePath = cursor.getString(columnIndex);
                    cursor.close();

                    loadImage(imagePath);

                    if (user != null) {
                        user.setImages(imagePath);
                    } else {
                        user = new User();
                        user.setImages(imagePath);
                    }
                } else {
                    Log.e("User_Edit_Activity", "Cursor is null");
                }
            }
        }
    }

    private void openImageGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    private void updateUser() {
        String fullName = binding.txtUserName.getEditText().getText().toString();
        String lastName = binding.txtlastName.getEditText().getText().toString();
        String mobileNumber = binding.txtPhoneNumber.getEditText().getText().toString();
        String email = binding.txtEmail.getEditText().getText().toString();
        String address = binding.txtAddress.getEditText().getText().toString();
        int selectedId = binding.GenderRadioGr.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String gender = selectedRadioButton != null ? selectedRadioButton.getText().toString() : null;

        String imagePath = binding.imageview.getTag() != null ? binding.imageview.getTag().toString() : null;

        if (!fullName.isEmpty() && !lastName.isEmpty() && !mobileNumber.isEmpty() && !email.isEmpty() && !address.isEmpty() && gender != null) {
            if (UserViewModel.isValidPhoneNumber(mobileNumber) && UserViewModel.isEmailValid(email)) {
                try {
                    if (user == null) {
                        user = new User();
                    }
                    user.setFname(fullName);
                    user.setLname(lastName);
                    user.setMobile(mobileNumber);
                    user.setEmail(email);
                    user.setAddress(address);
                    user.setGender(gender);
                    user.setImages(imagePath);

                    if (userId != -1) {
                        user.setId(userId);
                        userViewModel.updateUser(user);
                    } else {
                        Toast.makeText(User_Edit_Activity.this, R.string.update_user_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("User_Edit_Activity", "Error updating user", e);
                    Toast.makeText(User_Edit_Activity.this, R.string.update_user_error, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(User_Edit_Activity.this, R.string.invalid_phone_or_email, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(User_Edit_Activity.this, R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        binding.txtUserName.getEditText().setText("");
        binding.txtlastName.getEditText().setText("");
        binding.txtPhoneNumber.getEditText().setText("");
        binding.txtEmail.getEditText().setText("");
        binding.txtAddress.getEditText().setText("");
        binding.GenderRadioGr.clearCheck();
        binding.imageview.setImageResource(android.R.color.transparent);
        binding.imageview.setTag(null);
    }

    private void loadImage(String imagePath) {
        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.usercamera) // Placeholder image
                .error(R.drawable.usercamera) // Error image
                .into(binding.imageview);

        binding.imageview.setTag(imagePath); // Save image path to ImageView tag
    }
}
