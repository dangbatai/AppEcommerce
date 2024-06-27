package com.example.app_ecommerce.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.activity.Change_Password;
import com.example.app_ecommerce.activity.LoginActivity;
import com.example.app_ecommerce.activity.User_Edit_Activity;
import com.example.app_ecommerce.data.User;
import com.example.app_ecommerce.data.UserRepository;
import com.example.app_ecommerce.databinding.FragmentPersonalfragmentBinding;

import java.util.concurrent.ExecutionException;

public class personalfragment extends Fragment {
    private FragmentPersonalfragmentBinding binding;
    private int userId;
    private UserRepository userRepository;

    public personalfragment() {
        // Required empty public constructor
    }

    public static personalfragment newInstance(String param1, String param2) {
        personalfragment fragment = new personalfragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalfragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        userRepository = new UserRepository(getActivity().getApplication());

        loadUserData();

        binding.Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(requireContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish(); // Finish the current activity
                                Toast.makeText(requireContext(), "See you again!", Toast.LENGTH_SHORT).show();
                            }, 200); // Delay for 200 milliseconds
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });


        binding.AccountInformation.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), User_Edit_Activity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        binding.changepass.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Change_Password.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData(); // Refresh user data, including the profile image
    }

    private void loadUserData() {
        try {
            User user = userRepository.getUserById(userId);
            if (user != null) {
                binding.MyAccountFragmentUserName.setText(user.getFname());

                String imagePath = user.getImages();
                if (imagePath != null && !imagePath.isEmpty()) {
                    Glide.with(requireContext())
                            .load(imagePath)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.star)
                                    .error(R.drawable.usercamera))
                            .into(binding.MyAccountFragmentUserLogo);

                    Log.d("personalfragment", "ImagePath: " + imagePath);
                } else {
                    binding.MyAccountFragmentUserLogo.setImageResource(R.drawable.star);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e("personalfragment", "Error loading user data", e);
        }
    }
}
