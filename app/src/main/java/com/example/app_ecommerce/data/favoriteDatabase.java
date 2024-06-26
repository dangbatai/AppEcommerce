package com.example.app_ecommerce.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorite.class},version = 1)
public abstract  class favoriteDatabase extends RoomDatabase {
    public  abstract  FavoriteDao getContactDAO();

    private static favoriteDatabase dbInstance;
    public  static  synchronized  favoriteDatabase getInstance(Context context){

        if (dbInstance ==null){
            dbInstance = Room.databaseBuilder(
                            context.getApplicationContext(),

                            favoriteDatabase.class,
                            "favorite"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }
}