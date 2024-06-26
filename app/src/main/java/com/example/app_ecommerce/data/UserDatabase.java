package com.example.app_ecommerce.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class},version = 1)
public abstract  class UserDatabase extends RoomDatabase {
    public  abstract  UserDao getContactDAO();

    private static UserDatabase dbInstance;
    public  static  synchronized  UserDatabase getInstance(Context context){

        if (dbInstance ==null){
            dbInstance = Room.databaseBuilder(
                            context.getApplicationContext(),

                            UserDatabase.class,
                            "Image"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return dbInstance;
    }
}
