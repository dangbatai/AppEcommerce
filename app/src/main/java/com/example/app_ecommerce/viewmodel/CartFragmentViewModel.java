package com.example.app_ecommerce.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.app_ecommerce.data.Cart;
import com.example.app_ecommerce.data.CartRepository;
import com.example.app_ecommerce.data.Favorite;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartFragmentViewModel extends AndroidViewModel {
    private final CartRepository cartRepository;
    private LiveData<List<Cart>> allCarts;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final MutableLiveData<Double> totalAmount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCartListEmpty = new MutableLiveData<>();

    public CartFragmentViewModel(@NonNull Application application) {
        super(application);
        this.cartRepository = new CartRepository(application);
    }

    public LiveData<List<Cart>> getAllCarts(int userId) {
        allCarts = cartRepository.getAllCarts(userId);
        return allCarts;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public LiveData<Boolean> getIsCartListEmpty() {
        return isCartListEmpty;
    }

    public void insertCart(Cart cart) {
        cartRepository.addCart(cart);
    }

    public void deleteCartById(int id, int userId) {
        cartRepository.deleteById(id, userId);
        checkIfCartListEmpty(userId);
    }

    public void updateQuantity(boolean increaseQuantity, int id, int quantity, int userId) {
        executorService.execute(() -> {
            int updatedQuantity = quantity;
            if (increaseQuantity) {
                updatedQuantity++;
            } else if (!increaseQuantity && updatedQuantity > 1) {
                updatedQuantity--;
            }
            cartRepository.updateQuantity(id, updatedQuantity, userId);
            calculateTotalAmount(userId);
        });
    }

    public void calculateTotalAmount(int userId) {
        cartRepository.calculateTotal(userId, totalAmount::postValue);
    }


    private void checkIfCartListEmpty(int userId) {
        executorService.execute(() -> {
            List<Cart> carts = cartRepository.getAllCarts(userId).getValue();
            if (carts == null || carts.isEmpty()) {
                isCartListEmpty.postValue(true);
            } else {
                isCartListEmpty.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
