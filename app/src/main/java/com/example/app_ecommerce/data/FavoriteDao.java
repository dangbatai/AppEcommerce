package com.example.app_ecommerce.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEntity(Favorite favorite);

    @Delete
    void deleteEntity(Favorite favorite);

    @Query("SELECT isChecked FROM favorite WHERE id = :id AND userId = :userId")
    boolean isAddedToFavorite(int id, int userId);

    @Query("SELECT COUNT(*) FROM favorite WHERE userId = :userId")
    int getRowCount(int userId);

    @Query("UPDATE favorite SET isChecked = 0 WHERE id = :id AND userId = :userId")
    void updateEntity(int id, int userId);

    @Query("SELECT * FROM favorite WHERE id = :id AND userId = :userId LIMIT 1")
    Favorite searchForEntity(int id, int userId);
    @RawQuery
    List<Long> getEntities(SupportSQLiteQuery query);

    @Query("DELETE FROM favorite WHERE id = :id AND userId = :userId")
    int deleteById(int id, int userId);

    @Query("SELECT * FROM favorite WHERE userId = :userId")
    LiveData<List<Favorite>> getAllFavorites(int userId);

    @Query("DELETE FROM favorite WHERE userId = :userId")
    void deleteAllForUser(int userId);
}
