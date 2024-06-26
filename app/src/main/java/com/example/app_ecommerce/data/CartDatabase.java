package com.example.app_ecommerce.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cart.class},version = 1)
public abstract  class CartDatabase extends RoomDatabase {
    public  abstract  CartDao getContactDAO();

    private static CartDatabase dbInstance;
    public  static  synchronized  CartDatabase getInstance(Context context){

        if (dbInstance ==null){
            dbInstance = Room.databaseBuilder(
                            context.getApplicationContext(),

                            CartDatabase.class,
                            "Cart"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }
}