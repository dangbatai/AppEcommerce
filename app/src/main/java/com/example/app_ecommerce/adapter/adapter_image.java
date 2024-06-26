package com.example.app_ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.databinding.ItemImageBinding;

import java.util.ArrayList;

public class adapter_image extends RecyclerView.Adapter<adapter_image.ImageViewHolder> {
    private Context context;
    private ArrayList<String> imageList;

    public adapter_image(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ItemImageBinding binding;

        public ImageViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String imageUrl) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.star) // Placeholder image
                    .error(R.drawable.star) // Error image
                    .into(binding.viewEachImage);
        }
    }
}

