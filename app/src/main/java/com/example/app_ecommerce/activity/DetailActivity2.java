package com.example.app_ecommerce.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.adapter.adapter_image;
import com.example.app_ecommerce.data.Cart;
import com.example.app_ecommerce.data.CartRepository;
import com.example.app_ecommerce.data.Favorite;
import com.example.app_ecommerce.data.FavoriteRepository;
import com.example.app_ecommerce.databinding.ActivityDetail2Binding;
import com.example.app_ecommerce.model.Product;

import java.util.ArrayList;

public class DetailActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private adapter_image adapterImage;
    private ActivityDetail2Binding binding;
    private ArrayList<String> imageList = new ArrayList<>();
    private FavoriteRepository favoriteRepository;
    private CartRepository cartRepository;
    private Product product;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail2);
        recyclerView = binding.recyclersp;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT")) {
            product = (Product) intent.getSerializableExtra("PRODUCT");
            if (product != null) {
                if (product.getImages() != null) {
                    imageList.addAll(product.getImages());
                } else {
                    Log.d("DetailActivity", "Product images list is null");
                }
            }

            binding.setProduct(product);
            userId = intent.getIntExtra("userId", 1);
            if (!imageList.isEmpty()) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapterImage = new adapter_image(this, imageList);
                recyclerView.setAdapter(adapterImage);
            }

            binding.backButton.setOnClickListener(v -> finish());

        } else {
            Log.d("DetailActivity", "No product received");
        }

        favoriteRepository = new FavoriteRepository((Application) getApplicationContext());
        cartRepository = new CartRepository((Application) getApplicationContext());

        if (product != null) {
            favoriteRepository.isFavorite(product.getId(), userId, isFavorite -> {
                binding.checkBoxFavorite.setChecked(isFavorite);
            });
            cartRepository.isCart(product.getId(), userId, isCart -> {
                binding.checkBoxCart.setChecked(isCart);
            });
        }

        binding.checkBoxFavorite.setOnClickListener(v -> {
            if (binding.checkBoxFavorite.isChecked()) {
                addToFavorites(product);
            } else {
                removeFromFavorites(product.getId());
            }
        });

        binding.checkBoxCart.setOnClickListener(v -> {
            if (binding.checkBoxCart.isChecked()) {
                addToCart(product);
            } else {
                removeFromCart(product.getId());
            }
        });

        binding.buttonAddToShopping.setOnClickListener(v -> {
            cartRepository.isCart(product.getId(), userId, isInCart -> {
                if (isInCart) {
                    Toast.makeText(this, "Product is already in the cart", Toast.LENGTH_SHORT).show();
                } else {
                    addToCart(product);
                    Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    binding.checkBoxCart.setChecked(true);
                }
            });
        });
    }

    private void addToFavorites(Product product) {
        Utils.vibrateDevice(DetailActivity2.this);
        String images = convertListToString((ArrayList<String>) product.getImages());
        Favorite favorite = new Favorite(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getDiscountPercentage(),
                product.getRating(),
                product.getStock(),
                product.getBrand(),
                product.getWarrantyInformation(),
                product.getShippingInformation(),
                product.getReturnPolicy(),
                product.getMinimumOrderQuantity(),
                product.getThumbnail(),
                images,
                true,
                userId
        );
        favoriteRepository.addContact(favorite);
    }

    private void removeFromFavorites(int productId) {
        favoriteRepository.deleteById(productId, userId);
    }

    private void addToCart(Product product) {
        Utils.vibrateDevice(DetailActivity2.this);
        String images = convertListToString((ArrayList<String>) product.getImages());
        Cart cart = new Cart(
                product.getId(),
                product.getTitle(),
                product.getDiscountPercentage(),
                product.getDescription(),
                product.getPrice(),
                product.getRating(),
                product.getStock(),
                product.getBrand(),
                product.getWarrantyInformation(),
                product.getShippingInformation(),
                product.getReturnPolicy(),
                product.getMinimumOrderQuantity(),
                product.getThumbnail(),
                true,
                1,
                product.getCategory(),
                images,
                userId

        );
        cartRepository.addCart(cart);
    }

    private void removeFromCart(int productId) {
        cartRepository.deleteById(productId, userId);
    }

    private String convertListToString(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(",", list);
    }
}
