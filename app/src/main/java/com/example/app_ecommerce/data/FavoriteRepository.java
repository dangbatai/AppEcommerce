// FavoriteRepository.java
package com.example.app_ecommerce.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteRepository {
    private final FavoriteDao favoriteDao;
    private final ExecutorService executorService;
    private final Handler handler;

    public FavoriteRepository(Application application) {
        favoriteDatabase contacts_database = favoriteDatabase.getInstance(application);
        this.favoriteDao = contacts_database.getContactDAO();
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void addContact(Favorite contacts) {
        executorService.execute(() -> favoriteDao.insertEntity(contacts));
    }

    public void deleteContact(Favorite contacts) {
        executorService.execute(() -> favoriteDao.deleteEntity(contacts));
    }

    public void deleteById(int id, int userId) {
        executorService.execute(() -> favoriteDao.deleteById(id, userId));
    }

    public void isFavorite(int id, int userId, FavoriteCheckCallback callback) {
        executorService.execute(() -> {
            boolean isFavorite = favoriteDao.searchForEntity(id, userId) != null;
            handler.post(() -> callback.onFavoriteChecked(isFavorite));
        });
    }

    public interface FavoriteCheckCallback {
        void onFavoriteChecked(boolean isFavorite);
    }

    public LiveData<List<Favorite>> getAllContacts(int userId) {
        return favoriteDao.getAllFavorites(userId);
    }
}
