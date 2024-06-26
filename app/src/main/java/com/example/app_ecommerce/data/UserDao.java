package com.example.app_ecommerce.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM user WHERE mobile = :mobile AND password = :password LIMIT 1")
    User login(String mobile, String password);

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    User getUserById(int userId);
    @Query("SELECT * FROM user WHERE mobile = :phonenumber")
    User getUserByMobile(String phonenumber);
}
