package com.example.app_ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.activity.DetailActivity2;


import com.example.app_ecommerce.databinding.ItemSpBinding;
import com.example.app_ecommerce.fragments.Categoryfragment;
import com.example.app_ecommerce.model.Product;

import java.util.ArrayList;

public class adapter_item_sp extends RecyclerView.Adapter<adapter_item_sp.SPViewHolder> {
    private Context context;
    private ArrayList<Product> productArrayList;
    private ArrayList<Product> filteredProductList; // Danh sách sản phẩm đã lọc
    public OnDataClickListener mListener;
    public void setOnProductClickListener(OnDataClickListener listener) {
        this.mListener = listener;
    }
    public adapter_item_sp(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.filteredProductList = new ArrayList<>(); // Khởi tạo danh sách đã lọc
        filterProducts(); // Lọc sản phẩm ban đầu
    }

    // Lọc sản phẩm theo danh mục mong muốn
    private void filterProducts() {
        filteredProductList.clear(); // Xóa danh sách đã lọc trước đó
        ArrayList<String> categories = new ArrayList<>(); // Danh sách các danh mục đã xuất hiện
        for (Product product : productArrayList) {
            if (!categories.contains(product.getCategory())) { // Kiểm tra xem danh mục đã được thêm vào danh sách chưa
                categories.add(product.getCategory()); // Thêm danh mục mới vào danh sách
                filteredProductList.add(product); // Thêm sản phẩm vào danh sách đã lọc
            }
        }
    }

    @NonNull
    @Override
    public SPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSpBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_sp,parent,false);
        return new SPViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SPViewHolder holder, int position) {
        Product product = filteredProductList.get(position); // Lấy sản phẩm từ danh sách đã lọc
        holder.binding.setProduct(product);
        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context,Categoryfragment.class);
//
//
//            context.startActivity(intent);
            if (mListener != null) {
                mListener.onDataClick(product.getCategory());
                Log.v("Tagy",product.getCategory());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size(); // Trả về số lượng sản phẩm trong danh sách đã lọc
    }

    public class SPViewHolder extends RecyclerView.ViewHolder {
        private ItemSpBinding binding;
        public SPViewHolder(ItemSpBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Xử lý sự kiện khi itemView được nhấp
                }
            });
        }
    }
}


