package com.example.app_ecommerce.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_ecommerce.adapter.CategoryAdapter;
import com.example.app_ecommerce.adapter.OnDataClickListener;
import com.example.app_ecommerce.adapter.adapter_item_sp;
import com.example.app_ecommerce.databinding.FragmentCategoryfragmentBinding;
import com.example.app_ecommerce.model.Product;
import com.example.app_ecommerce.viewmodel.MainActivityViewModel;
import java.util.ArrayList;
import java.util.List;

public class Categoryfragment extends Fragment implements OnDataClickListener {

    private RecyclerView recyclerViewcategory, recyclerViewsp;
    private adapter_item_sp adapterItemSp;
    private CategoryAdapter adapterItem;
    private ArrayList<Product> productList;
    private String selectedCategory;
    private FragmentCategoryfragmentBinding binding;
    private MainActivityViewModel viewModel;
    private int userId;
    public Categoryfragment() {
        // Required empty public constructor
    }

    public static Categoryfragment newInstance(String param1, String param2) {
        Categoryfragment fragment = new Categoryfragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedCategory = getArguments().getString("selectedCategory");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryfragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerViewsp = binding.recyclerviewsp;
        recyclerViewcategory = binding.recyclerViewCategory;
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }
        viewModel.getAllProduct().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productList = new ArrayList<>(products);
                adapterItemSp = new adapter_item_sp(requireContext(), productList);
                recyclerViewcategory.setItemAnimator(new DefaultItemAnimator());
                recyclerViewcategory.setAdapter(adapterItemSp);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                recyclerViewcategory.setLayoutManager(linearLayoutManager);
                adapterItemSp.notifyDataSetChanged();
                adapterItemSp.setOnProductClickListener(Categoryfragment.this);

                if (selectedCategory != null) {
                    setUpAdapter(recyclerViewsp, selectedCategory);
                }
            }
        });

        return view;
    }

    private void setUpAdapter(RecyclerView recyclerView, String category) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }

        if (!filteredList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            adapterItem = new CategoryAdapter(requireContext(), filteredList, category,userId);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapterItem);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            adapterItem.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDataClick(String category) {
        selectedCategory = category;
       setUpAdapter(recyclerViewsp, selectedCategory);
        Log.v("CategoryFragment", "Selected category: " + selectedCategory);
    }
}
