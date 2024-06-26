package com.example.app_ecommerce.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {

    private final CartDao cartDao;
    private final ExecutorService executorService;
    private final Handler handler;

    public CartRepository(Application application) {
        CartDatabase cartDatabase = CartDatabase.getInstance(application);
        this.cartDao = cartDatabase.getContactDAO();
        this.executorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void addCart(Cart cart) {
        executorService.execute(() -> cartDao.insertEntity(cart));
    }

    public void deleteById(int id, int userId) {
        executorService.execute(() -> cartDao.deleteById(id, userId));
    }

    public void isCart(int id, int userId, CartCheckCallback callback) {
        executorService.execute(() -> {
            boolean isCart = cartDao.searchForEntity(id, userId) != null;
            handler.post(() -> callback.onCartChecked(isCart));
        });
    }

    public LiveData<List<Cart>> getAllCarts(int userId) {
        return cartDao.getAllCarts(userId);
    }

    public void calculateTotal(int userId, CartTotalCallback callback) {
        executorService.execute(() -> {
            List<Cart> cartList = cartDao.getAllEntities(userId);
            double total = 0.0;
            if (cartList != null) {
                for (Cart cart : cartList) {
                    total += cart.getQuantity() * cart.getPrice();
                }
            }
            double finalTotal = total;
            handler.post(() -> callback.onTotalCalculated(finalTotal));
        });
    }



    public void updateQuantity(int id, int quantity, int userId) {
        executorService.execute(() -> cartDao.updateQuantity(id, quantity, userId));
    }

    public void updateCart(Cart cart) {
        executorService.execute(() -> cartDao.updateQuantity(cart.getId(), cart.getQuantity(), cart.getUserId()));
    }

    public interface CartCheckCallback {
        void onCartChecked(boolean isCart);
    }

    public interface CartTotalCallback {
        void onTotalCalculated(double total);
    }
}

