package com.example.app_ecommerce.data;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEntity(Cart cart);

    @RawQuery
    List<Long> getEntities(SupportSQLiteQuery query);

//    @Query("SELECT * FROM cart WHERE userId = :userId")
//    LiveData<List<Cart>> getAllEntities(int userId);
    @Query("SELECT * FROM cart WHERE userId = :userId")
    List<Cart> getAllEntities(int userId);

    @Query("DELETE FROM cart WHERE id = :id AND userId = :userId")
    int deleteById(int id, int userId);

    @Query("SELECT * FROM cart WHERE id = :id AND userId = :userId LIMIT 1")
    Cart searchForEntity(int id, int userId);

    @Query("UPDATE cart SET isAddedToCart = 0 WHERE id = :id AND userId = :userId")
    void updateEntity(int id, int userId);

    @Query("SELECT * FROM cart WHERE userId = :userId")
    LiveData<List<Cart>> getAllCarts(int userId);

    @Query("DELETE FROM cart WHERE userId = :userId")
    void deleteAllForUser(int userId);

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id AND userId = :userId")
    void updateQuantity(int id, int quantity, int userId);

    @Query("SELECT quantity FROM cart WHERE id = :id AND userId = :userId")
    int getQuantity(int id, int userId);

    @Query("SELECT price FROM cart WHERE id = :id AND userId = :userId")
    Double getPrice(int id, int userId);

    @Query("SELECT * FROM cart WHERE userId = :userId")
    Cursor getCursorAll(int userId);

    @Query("SELECT isAddedToCart FROM cart WHERE id = :id AND userId = :userId")
    boolean isAddedToCart(int id, int userId);
}

