package com.example.app_ecommerce.adapter;

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
import com.example.app_ecommerce.data.Cart;
import com.example.app_ecommerce.databinding.ItemCartBinding;
import com.example.app_ecommerce.model.Product;
import com.example.app_ecommerce.viewmodel.CartFragmentViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static final String TAG = "CartAdapter";
    private final Context context;
    private final ArrayList<Cart> carts;
    private CartFragmentViewModel viewModel;
    private final int userId;

    public CartAdapter(Context context, ArrayList<Cart> carts, int userId) {
        this.context = context;
        this.carts = carts;
        this.userId = userId;
    }

    public void setViewModel(CartFragmentViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_cart, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);
        if (cart != null) {
            // Ensure the quantity is at least 1
            if (cart.getQuantity() == 0) {
                cart.setQuantity(1);
                viewModel.updateQuantity(true, cart.getId(), 0, userId);
            }

            holder.binding.setCart(cart);

            if (viewModel != null) {
                holder.binding.productQuantityMinus.setOnClickListener(v -> {
                    try {
                        viewModel.updateQuantity(false, cart.getId(), cart.getQuantity(), userId);
                        notifyItemChanged(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.binding.productQuantityPlus.setOnClickListener(v -> {
                    try {
                        viewModel.updateQuantity(true, cart.getId(), cart.getQuantity(), userId);
                        notifyItemChanged(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            holder.itemView.setOnClickListener(view -> {
                try {
                    Product product = new Product(
                            cart.getId(),
                            cart.getTitle(),
                            cart.getDescription(),
                            cart.getPrice(),
                            cart.getDiscountPercentage(),
                            cart.getRating(),
                            cart.getStock(),
                            cart.getBrand(),
                            cart.getCategory(),
                            cart.getThumbnail(),
                            cart.getWarrantyInformation(),
                            cart.getShippingInformation(),
                            cart.getAvailabilityStatus(),
                            cart.getReturnPolicy()
                    );

                    if (cart.getImages() == null) {
                        product.setImages(new ArrayList<>());
                    } else {
                        product.setImages(new ArrayList<>(Collections.singleton(cart.getImages())));
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PRODUCT", product);
                    Intent intent = new Intent(context, DetailActivity2.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error opening product details", e);
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void deleteItem(int position) {
        if (position < 0 || position >= carts.size()) {
            return;
        }

        Cart cart = carts.get(position);
        if (cart != null) {
            viewModel.deleteCartById(cart.getId(), userId);
        }
        carts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, carts.size());
    }
}
