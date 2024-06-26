package com.example.app_ecommerce.viewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.app_ecommerce.activity.LoginActivity;
import com.example.app_ecommerce.activity.MainActivity;

import com.example.app_ecommerce.activity.Register_Activity;
import com.example.app_ecommerce.data.User;
import com.example.app_ecommerce.data.UserRepository;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    public MutableLiveData<String> fullName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> images =new MutableLiveData<>();
    public MutableLiveData<Boolean> termsAccepted = new MutableLiveData<>(false);

private int userId;
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Event handler for sign-up button click
    public void onSignUpClicked() {
        String fullNameValue = fullName.getValue();
        String lastNameValue = lastName.getValue();
        String mobileNumberValue = mobileNumber.getValue();
        String passwordValue = password.getValue();
        String emailValue = email.getValue();
        String addressValue = address.getValue();
        String genderValue = gender.getValue();
        String imagesValue=images.getValue();

        if (fullNameValue != null && mobileNumberValue != null && passwordValue != null) {
            if (isValidPhoneNumber(mobileNumberValue) && passwordValue.length() >= 6) {
                // Kiểm tra xem số điện thoại đã tồn tại trong cơ sở dữ liệu chưa
                try {
                    User existingUser = userRepository.getUserByMobile(mobileNumberValue);
                    if (existingUser != null) {
                        // Nếu số điện thoại đã tồn tại, hiển thị thông báo và không đăng kí
                        Toast.makeText(getApplication(), "Phone number already registered!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Nếu số điện thoại chưa tồn tại, tiến hành đăng kí
                        User newUser = new User();
                        newUser.setFname(fullNameValue);
                        newUser.setLname(lastNameValue);
                        newUser.setMobile(mobileNumberValue);
                        newUser.setPassword(passwordValue);
                        newUser.setEmail(emailValue);
                        newUser.setAddress(addressValue);
                        newUser.setGender(genderValue);
                        newUser.setImages(imagesValue);
                        userRepository.insert(newUser);
                        Toast.makeText(getApplication(), "Registered successfully!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "An error occurred while checking phone number!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplication(), "Please enter a valid phone number and password!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication(), "Please enter complete information!", Toast.LENGTH_SHORT).show();}
    }

    public void onAlreadyUserClicked() {
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void onRegisterClicked() {
        Intent intent = new Intent(getApplication(), Register_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    public void updateUser(User user) {
        try {
            User existingUser = userRepository.getUserByMobile(user.getMobile());
            if (existingUser != null && existingUser.getId() != user.getId()) {
                // Nếu số điện thoại đã tồn tại cho người dùng khác, hiển thị thông báo và không cập nhật
                Toast.makeText(getApplication(), "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
            } else {
                userRepository.update(user);
                Toast.makeText(getApplication(), "Cập nhật người dùng thành công!", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Đã xảy ra lỗi khi kiểm tra số điện thoại!", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^0\\d{9}$";
        return Pattern.matches(regex, phoneNumber);
    }

    public static boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public void onLoginClicked(String mobile, String password) {
        try {
//            User user = userRepository.getUserById(userId);
//            userId = user.getId();
            User loggedInUser = login(mobile, password);
            if (loggedInUser != null) {
                int userId = loggedInUser.getId();
                // Lưu userId cho việc sử dụng sau này
                this.userId = userId;
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("userId", userId); // Truyền userId vào intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().startActivity(intent);
            } else {
                Toast.makeText(getApplication(), "Invalid mobile number or password!", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "An error occurred during login!", Toast.LENGTH_SHORT).show();
        }
    }


    public void loadUserData(int userId) throws ExecutionException, InterruptedException {
        User user = userRepository.getUserById(userId);
        //this.userId = user.getId();
        fullName.setValue(user.getFname());
        lastName.setValue(user.getLname());
        mobileNumber.setValue(user.getMobile());
        email.setValue(user.getEmail());
        address.setValue(user.getAddress());
        gender.setValue(user.getGender());
        images.setValue(user.getImages());
    }

    public User login(String mobile, String password) throws ExecutionException, InterruptedException {
        return userRepository.login(mobile, password);
    }
    public String getPasswordForUser(int userId) {
        User user = null;
        try {
            user = userRepository.getUserById(userId);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return user != null ? user.getPassword() : null;
    }

    public void changePassword(int userId, String newPassword) {
        User user = null;
        try {
            user = userRepository.getUserById(userId);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.update(user);
        }
    }
}
