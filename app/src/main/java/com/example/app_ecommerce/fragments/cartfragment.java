package com.example.app_ecommerce.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.activity.PayActivity;
import com.example.app_ecommerce.adapter.CartAdapter;
import com.example.app_ecommerce.data.Cart;
import com.example.app_ecommerce.data.User;
import com.example.app_ecommerce.data.UserRepository;
import com.example.app_ecommerce.databinding.FragmentCartfragmentBinding;
import com.example.app_ecommerce.utils.SwipeHelper;
import com.example.app_ecommerce.viewmodel.CartFragmentViewModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class cartfragment extends Fragment implements SwipeHelper.SwipeListener {

    private FragmentCartfragmentBinding binding;
    private ArrayList<Cart> carts = new ArrayList<>();
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private CartFragmentViewModel viewModel;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int userId;
    private UserRepository userRepository;

    public cartfragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartfragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupRecyclerView();
        observeTotalAmount();
        setupSwipeHelper();
        setupPayButton();
    }

    private void setupViewModel() {
        if (getArguments() != null) {
            userId = getArguments().getInt("userId");
        }

        userRepository = new UserRepository(getActivity().getApplication()); // Initialize userRepository here
        viewModel = new ViewModelProvider(this).get(CartFragmentViewModel.class);
        viewModel.getAllCarts(userId).observe(getViewLifecycleOwner(), this::updateCartList);
        observeTotalAmount();
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(requireContext(), carts, userId);
        cartAdapter.setViewModel(viewModel); // Set the ViewModel for the adapter
        recyclerView = binding.recyclerViewCart;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);
    }

    private void observeTotalAmount() {
        viewModel.getTotalAmount().observe(getViewLifecycleOwner(), total -> {
            String formattedTotal = decimalFormat.format(total);
            binding.txttotal.setText(formattedTotal);
        });
    }

    private void updateCartList(List<Cart> cartList) {
        carts.clear();
        carts.addAll(cartList);
        cartAdapter.notifyDataSetChanged();
        updateUI(cartList);
        viewModel.calculateTotalAmount(userId);
    }

    private void updateUI(List<Cart> cartList) {
        binding.progressBar.setVisibility(cartList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        binding.emptyListMessage.setVisibility(cartList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupSwipeHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelper(requireContext(), recyclerView, this) {
            @Override
            protected void onConfirmed(int position) {
                onDeleteConfirmed(position);
            }

            @Override
            public List<SwipeHelper.UnderlayButton> instantiateUnderlayButton(int position) {
                List<SwipeHelper.UnderlayButton> buttons = new ArrayList<>();
                buttons.add(deleteButton(position));
                return buttons;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private SwipeHelper.UnderlayButton deleteButton(int position) {
        return new SwipeHelper.UnderlayButton(
                requireContext(),
                "Delete",
                14.0f,
                android.R.color.holo_red_light,
                () -> onDeleteConfirmed(position)
        );
    }

    @Override
    public void onDeleteConfirmed(int position) {
        showDeleteConfirmationDialog(position);
    }

    private void showDeleteConfirmationDialog(int position) {
        Cart cart = carts.get(position);
        if (cart != null) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Cart")
                    .setMessage("Are you sure you want to delete " + cart.getTitle() + " ?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteCart(position))
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    private void deleteCart(int position) {
        Cart cart = carts.get(position);
        if (cart != null) {
            viewModel.deleteCartById(cart.getId(), userId);
        }
    }

    private void setupPayButton() {
        Button buttonPurchase = binding.buttonPurchase;
        buttonPurchase.setOnClickListener(view -> {
            String totalAmountStr = binding.txttotal.getText().toString();

            if (totalAmountStr.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty. Add items to proceed.", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalAmount;
            try {
                totalAmount = Double.parseDouble(totalAmountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid total amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (totalAmount == 0.0) {
                Toast.makeText(requireContext(), "Cart is empty. Add items to proceed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve user data in a background thread
            new Thread(() -> {
                try {
                    User user = userRepository.getUserById(userId);

                    requireActivity().runOnUiThread(() -> {
                        if (user == null) {
                            Toast.makeText(requireContext(), "User not found.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (user.getAddress() == null || user.getAddress().isEmpty()) {
                            Toast.makeText(requireContext(), "Please enter address information!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(requireContext(), PayActivity.class);
                            intent.putExtra("totalAmount", totalAmount);
                            startActivity(intent);
                        }
                    });

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });
    }

}
