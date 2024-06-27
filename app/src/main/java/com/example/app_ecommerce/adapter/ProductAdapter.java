package com.example.app_ecommerce.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.activity.DetailActivity2;
import com.example.app_ecommerce.activity.Utils;
import com.example.app_ecommerce.data.Cart;
import com.example.app_ecommerce.data.CartRepository;
import com.example.app_ecommerce.data.Favorite;
import com.example.app_ecommerce.data.FavoriteRepository;
import com.example.app_ecommerce.databinding.ItemProductBinding;
import com.example.app_ecommerce.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemViewHolder> {
    private static final String TAG = "ProductAdapter";
    private final Context context;
    private final ArrayList<Product> productArrayList;
    private final FavoriteRepository favoriteRepository;
    private final CartRepository cartRepository;
    private final Set<Integer> favoriteProductIds;
    private final Set<Integer> cartProductIds;
    private final int userId;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, int userId) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.userId = userId;
        this.favoriteRepository = new FavoriteRepository((Application) context.getApplicationContext());
        this.cartRepository = new CartRepository((Application) context.getApplicationContext());
        this.favoriteProductIds = new HashSet<>();
        this.cartProductIds = new HashSet<>();
        loadFavoriteProductIds();
        loadCartProductIds();
    }

    private void loadFavoriteProductIds() {
        favoriteRepository.getAllContacts(userId).observeForever(favorites -> {
            favoriteProductIds.clear();
            for (Favorite favorite : favorites) {
                favoriteProductIds.add(favorite.getId());
            }
            notifyDataSetChanged();
        });
    }

    private void loadCartProductIds() {
        cartRepository.getAllCarts(userId).observeForever(carts -> {
            cartProductIds.clear();
            for (Cart cart : carts) {
                cartProductIds.add(cart.getId());
            }
            Log.d(TAG, "Loaded cart product IDs: " + cartProductIds);
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.binding.setProduct(product);
        holder.binding.executePendingBindings();

        boolean isFavorite = favoriteProductIds.contains(product.getId());
        holder.binding.checkBox.setChecked(isFavorite);

        holder.binding.checkBox.setOnClickListener(v -> {
            if (holder.binding.checkBox.isChecked()) {
                addToFavorites(product);
            } else {
                removeFromFavorites(product);
            }
            notifyItemChanged(holder.getAdapterPosition());
        });
        holder.binding.buttonAddToShopping.setOnClickListener(v -> {
            cartRepository.isCart(product.getId(), userId, isInCart -> {
                if (isInCart) {
                    Toast.makeText(context, "Product is already in the cart", Toast.LENGTH_SHORT).show();
                } else {
                    addToCart(product);
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();

                }
            });
        });


        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity2.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("PRODUCT", product);
            intent.putExtras(bundle);
            intent.putExtra("userId", userId); // Pass userId to DetailActivity2
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return productArrayList != null ? productArrayList.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ItemViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private void addToFavorites(Product product) {
        Utils.vibrateDevice(context);
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
        favoriteProductIds.add(product.getId());
        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites(Product product) {
           favoriteRepository.deleteById(product.getId(), userId);
              favoriteProductIds.remove(product.getId());
             Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
    }
    private void addToCart(Product product) {
        Utils.vibrateDevice(context);
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
    private String convertListToString(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(",", list);
    }
}
