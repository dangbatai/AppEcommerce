package com.example.app_ecommerce.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.data.User;
import com.example.app_ecommerce.data.UserRepository;
import com.example.app_ecommerce.databinding.ActivityUserEditBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class User_Edit_Activity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;

    private ActivityUserEditBinding binding;
    private UserViewModel userViewModel;
    private User user;
    private UserRepository userRepository;
    private int userId;
    private String currentPhotoPath;

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
                    if (user.getImages() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(user.getImages());
                        binding.imageview.setImageBitmap(bitmap);
                        binding.imageview.setTag(user.getImages());
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        binding.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        binding.setLifecycleOwner(this);

        binding.btncancel.setOnClickListener(new View.OnClickListener() {
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

        binding.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageGallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String imagePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(this)
                            .load(imagePath)
                            .placeholder(R.drawable.usercamera) // Placeholder image
                            .error(R.drawable.usercamera) // Error image
                            .into(binding.imageview);

                    binding.imageview.setTag(imagePath); // Lưu đường dẫn hình ảnh vào ImageView tag

                    // Cập nhật đường dẫn hình ảnh vào đối tượng User
                    if (user != null) {
                        user.setImages(imagePath);
                        userViewModel.updateUser(user);
                    } else {
                        user = new User();
                        user.setImages(imagePath);
                    }
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

        // Lấy đường dẫn hình ảnh từ ImageView tag
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
                    user.setImages(imagePath); // Lưu đường dẫn hình ảnh vào đối tượng User

                    // Lưu hoặc cập nhật người dùng
                    if (userId != -1) {
                        user.setId(userId);
                        userViewModel.updateUser(user);
                        Log.v("hihihi",user.getImages().toString());
                    } else {
                        // Xử lý khi userId không hợp lệ
                        Toast.makeText(User_Edit_Activity.this, "Không thể cập nhật người dùng!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(User_Edit_Activity.this, "Đã xảy ra lỗi khi cập nhật người dùng!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(User_Edit_Activity.this, "Vui lòng nhập số điện thoại và địa chỉ email hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(User_Edit_Activity.this, "Vui lòng điền đầy đủ các trường bắt buộc!", Toast.LENGTH_SHORT).show();
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
        binding.imageview.setTag(null); // Xóa đường dẫn hình ảnh khỏi ImageView tag
    }
}
