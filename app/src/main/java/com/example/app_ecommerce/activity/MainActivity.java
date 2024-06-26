package com.example.app_ecommerce.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.databinding.ActivityMainBinding;
import com.example.app_ecommerce.fragments.Categoryfragment;
import com.example.app_ecommerce.fragments.cartfragment;
import com.example.app_ecommerce.fragments.favoritefragment;
import com.example.app_ecommerce.fragments.homefragment;
import com.example.app_ecommerce.fragments.personalfragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    private homefragment homeFragment;
    private Categoryfragment categoryFragment;
    private favoritefragment favoriteFragment;
    private cartfragment cartFragment;
    private personalfragment personalFragment;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // Nhận userId từ Intent
        if (getIntent() != null && getIntent().hasExtra("userId")) {
            userId = getIntent().getIntExtra("userId", -1);
        }
        fragmentManager = getSupportFragmentManager();

        // Initialize fragments
        homeFragment = new homefragment();
        categoryFragment = new Categoryfragment();
        favoriteFragment = new favoritefragment();
        cartFragment = new cartfragment();
        personalFragment = new personalfragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);

        homeFragment.setArguments(bundle);
        categoryFragment.setArguments(bundle);
        favoriteFragment.setArguments(bundle);
        cartFragment.setArguments(bundle);
        personalFragment.setArguments(bundle);
        // Add fragments initially if not present
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.frameLayout, homeFragment, "HomeFragment")
                    .add(R.id.frameLayout, categoryFragment, "CategoryFragment").hide(categoryFragment)
                    .add(R.id.frameLayout, favoriteFragment, "FavoriteFragment").hide(favoriteFragment)
                    .add(R.id.frameLayout, cartFragment, "CartFragment").hide(cartFragment)
                    .add(R.id.frameLayout, personalFragment, "PersonalFragment").hide(personalFragment)
                    .commit();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    switchFragment(homeFragment, "HomeFragment");
                    return true;
                } else if (id == R.id.action_category) {
                    switchFragment(categoryFragment, "CategoryFragment");
                    return true;
                } else if (id == R.id.action_favorites) {
                    switchFragment(favoriteFragment, "FavoriteFragment");
                    return true;
                } else if (id == R.id.action_cart) {
                    switchFragment(cartFragment, "CartFragment");
                    return true;
                } else if (id == R.id.action_profile) {
                    switchFragment(personalFragment, "PersonalFragment");
                    return true;
                }
                return false;
            }
        });
    }

    private void switchFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .hide(homeFragment)
                .hide(categoryFragment)
                .hide(favoriteFragment)
                .hide(cartFragment)
                .hide(personalFragment)
                .show(fragmentManager.findFragmentByTag(tag) != null ? fragmentManager.findFragmentByTag(tag) : fragment)
                .commit();
    }
}
