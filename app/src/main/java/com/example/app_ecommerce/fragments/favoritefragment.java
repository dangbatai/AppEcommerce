package com.example.app_ecommerce.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_ecommerce.adapter.FavoriteAdapter;
import com.example.app_ecommerce.data.Favorite;
import com.example.app_ecommerce.databinding.FragmentFavoritefragmentBinding;
import com.example.app_ecommerce.utils.SwipeHelper;
import com.example.app_ecommerce.viewmodel.FavoriteFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class favoritefragment extends Fragment implements SwipeHelper.SwipeListener {
    private FragmentFavoritefragmentBinding binding;
    private ArrayList<Favorite> favorites = new ArrayList<>();
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private FavoriteFragmentViewModel viewModel;
    private int userId;

    public favoritefragment() {
        // Required empty public constructor
    }

    public static favoritefragment newInstance(int userId) {
        favoritefragment fragment = new favoritefragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
        }
        Log.v("hi", String.valueOf(userId));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritefragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        Log.v("hi", String.valueOf(userId));
//        if (getArguments() != null) {
//            userId = getArguments().getInt("userId");
//        }

        recyclerView = binding.favoriteRecyclerView;
        favoriteAdapter = new FavoriteAdapter(requireContext(), favorites, userId);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel = new ViewModelProvider(this).get(FavoriteFragmentViewModel.class);
        viewModel.getAllFavorite(userId).observe(getViewLifecycleOwner(), favoriteList -> {
            favorites.clear();
            favorites.addAll(favoriteList);
            favoriteAdapter.notifyDataSetChanged();

            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.emptyList.setVisibility(favoriteList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelper(requireContext(), recyclerView, this) {
            @Override
            protected void onConfirmed(int position) {
                Favorite favorite = favorites.get(position);
                if (favorite != null) {
                    viewModel.deleteContacts(favorite);
                    Log.v("Favorite Deleted", favorite.getTitle());
                }
            }

            @Override
            public List<SwipeHelper.UnderlayButton> instantiateUnderlayButton(int position) {
                List<SwipeHelper.UnderlayButton> buttons = new ArrayList<>();
                buttons.add(deleteButton(position));
                return buttons;
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }

    private SwipeHelper.UnderlayButton deleteButton(int position) {
        return new SwipeHelper.UnderlayButton(
                requireContext(),
                "Delete",
                14.0f,
                android.R.color.holo_red_light,
                () -> showDeleteConfirmationDialog(position)
        );
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Favorite")
                .setMessage("Are you sure you want to delete " + favorites.get(position).getTitle() + " ?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Favorite favorite = favorites.get(position);
                    if (favorite != null) {
                        viewModel.deleteContacts(favorite);
                        Log.v("Favorite Deleted", favorite.getTitle());
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDeleteConfirmed(int position) {
        showDeleteConfirmationDialog(position);
    }
}
