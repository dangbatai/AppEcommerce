package com.example.app_ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_ecommerce.data.Favorite;
import com.example.app_ecommerce.data.FavoriteRepository;

import java.util.List;

public class FavoriteFragmentViewModel extends AndroidViewModel {
    private final FavoriteRepository favoriteRepository;
    private LiveData<List<Favorite>> allContacts;

    public FavoriteFragmentViewModel(@NonNull Application application) {
        super(application);
        this.favoriteRepository = new FavoriteRepository(application);
    }

    public LiveData<List<Favorite>> getAllFavorite(int userId) {
        allContacts = favoriteRepository.getAllContacts(userId);
        return allContacts;
    }

    public void insertContacts(Favorite contacts) {
        favoriteRepository.addContact(contacts);
    }

    public void deleteContacts(Favorite contacts) {
        favoriteRepository.deleteContact(contacts);
    }

    public void deleteById(int id, int userId) {
        favoriteRepository.deleteById(id, userId);
    }
}
