package com.example.app_ecommerce.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.activity.DetailActivity2;
import com.example.app_ecommerce.activity.Utils;
import com.example.app_ecommerce.data.Cart;
import com.example.app_ecommerce.data.CartRepository;
import com.example.app_ecommerce.data.Favorite;
import com.example.app_ecommerce.databinding.ItemFavoriteBinding;
import com.example.app_ecommerce.model.Product;
import com.example.app_ecommerce.viewmodel.FavoriteFragmentViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.favoriteVH> {
    private final Context context;
    private final ArrayList<Favorite> favorites;
    private final FavoriteFragmentViewModel favoriteFragmentViewModel;
    private final CartRepository cartRepository;
    private final int userId;

    public FavoriteAdapter(Context context, ArrayList<Favorite> favorites, int userId) {
        this.context = context;
        this.favorites = favorites;
        this.userId = userId;
        favoriteFragmentViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(FavoriteFragmentViewModel.class);
        this.cartRepository = new CartRepository((Application) context.getApplicationContext());
    }

    @NonNull
    @Override
    public favoriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_favorite, parent, false);
        return new favoriteVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull favoriteVH holder, int position) {
        Favorite favorite = favorites.get(position);
        holder.binding.setFavorite(favorite);

        holder.itemView.setOnClickListener(view -> {
            try {
                Product product = new Product(
                        favorite.getId(),
                        favorite.getTitle(),
                        favorite.getDescription(),
                        favorite.getPrice(),
                        favorite.getDiscountPercentage(),
                        favorite.getRating(),
                        favorite.getStock(),
                        favorite.getBrand(),
                        favorite.getCategory(),
                        favorite.getThumbnail(),
                        favorite.getWarrantyInformation(),
                        favorite.getShippingInformation(),
                        favorite.getAvailabilityStatus(),
                        favorite.getReturnPolicy()
                );

                if (favorite.getImages() == null) {
                    product.setImages(new ArrayList<>());
                } else {
                    product.setImages(new ArrayList<>(Collections.singleton(favorite.getImages())));
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("PRODUCT", product);
                Intent intent = new Intent(context, DetailActivity2.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.buttonAddToShopping.setOnClickListener(v -> {
            cartRepository.isCart(favorite.getId(), userId, isInCart -> {
                if (isInCart) {
                    Toast.makeText(context, "Product is already in the cart", Toast.LENGTH_SHORT).show();
                } else {
                    addToCarts(favorite);
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class favoriteVH extends RecyclerView.ViewHolder {
        private final ItemFavoriteBinding binding;

        public favoriteVH(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void addToCarts(Favorite favorite) {
        Utils.vibrateDevice(context);

        Cart cart = new Cart(
                favorite.getId(),
                favorite.getTitle(),
                favorite.getDiscountPercentage(),
                favorite.getDescription(),
                favorite.getPrice(),
                favorite.getRating(),
                favorite.getStock(),
                favorite.getBrand(),
                favorite.getWarrantyInformation(),
                favorite.getShippingInformation(),
                favorite.getReturnPolicy(),
                favorite.getMinimumOrderQuantity(),
                favorite.getThumbnail(),
                true,
                1,
                favorite.getCategory(),
                favorite.getImages(),
                favorite.getUserId()
        );
        cartRepository.addCart(cart);
    }

    public void deleteItem(int position) {
        favorites.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favorites.size());
    }

    public Favorite getItemInfo(int position) {
        return favorites.get(position);
    }
}
