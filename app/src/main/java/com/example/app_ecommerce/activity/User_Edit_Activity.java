package com.example.app_ecommerce.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_ecommerce.data.User;
import com.example.app_ecommerce.data.UserRepository;
import com.example.app_ecommerce.databinding.ActivityUserEditBinding;
import com.example.app_ecommerce.viewmodel.UserViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class User_Edit_Activity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

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
        userRepository = new UserRepository(this.getApplication());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", -1);
            try {
                userViewModel.loadUserData(userId);
                user = userRepository.getUserById(userId); // Load user data
                if (user != null && user.getImages() != null && !user.getImages().isEmpty()) {
                    currentPhotoPath = user.getImages();
                    setPic();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        binding.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        binding.setViewModel(userViewModel);
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
                showImagePickerDialog();
            }
        });
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option");
        builder.setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        dispatchPickPictureIntent();
                        break;
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.app_ecommerce.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    setPic();
                    break;
                case REQUEST_IMAGE_PICK:
                    if (data != null && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        currentPhotoPath = getRealPathFromUri(selectedImage);
                        binding.imageview.setImageURI(selectedImage);
                        binding.imageview.setTag(currentPhotoPath); // Store the image path in the tag
                    }
                    break;
            }
        }
    }

    private void setPic() {
        int targetW = binding.imageview.getWidth();
        int targetH = binding.imageview.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        binding.imageview.setImageBitmap(bitmap);
        binding.imageview.setTag(currentPhotoPath); // Store the image path in the tag
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    private void updateUser() {
        String fullname = binding.txtUserName.getEditText().getText().toString();
        String lastname = binding.txtlastName.getEditText().getText().toString();
        String mobienumber = binding.txtPhoneNumber.getEditText().getText().toString();
        String email = binding.txtEmail.getEditText().getText().toString();
        String address = binding.txtAddress.getEditText().getText().toString();
        int selectedId = binding.GenderRadioGr.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String gender = selectedRadioButton != null ? selectedRadioButton.getText().toString() : null;

        // Get the image path stored in the ImageView tag
        String imagePath = binding.imageview.getTag() != null ? binding.imageview.getTag().toString() : null;

        if (!fullname.isEmpty() && !lastname.isEmpty() && !mobienumber.isEmpty() && !email.isEmpty() && !address.isEmpty() && gender != null) {
            if (isValidPhoneNumber(mobienumber) && isEmailValid(email)) {
                try {
                    User existingUser = userRepository.getUserByMobile(mobienumber);
                    if (existingUser != null && existingUser.getId() != userId) {
                        // If the phone number already exists for another user, show a message and do not update
                        Toast.makeText(getApplication(), "Phone number already registered!", Toast.LENGTH_SHORT).show();
                    } else {
                        new AlertDialog.Builder(User_Edit_Activity.this)
                                .setTitle("Update User")
                                .setMessage("Are you sure you want to update this user?")
                                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                    user.setFname(fullname);
                                    user.setLname(lastname);
                                    user.setMobile(mobienumber);
                                    user.setEmail(email);
                                    user.setAddress(address);
                                    user.setGender(gender);
                                    user.setImages(imagePath); // Save the correct image path
                                    userRepository.update(user);
                                    Toast.makeText(User_Edit_Activity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "An error occurred while checking phone number!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(User_Edit_Activity.this, "Please enter a valid phone number and email address!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(User_Edit_Activity.this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show();
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

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^0\\d{9}$";
        return Pattern.matches(regex, phoneNumber);
    }

    private static boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
