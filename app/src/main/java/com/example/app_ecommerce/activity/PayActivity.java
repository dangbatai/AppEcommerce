package com.example.app_ecommerce.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_ecommerce.R;
import com.example.app_ecommerce.databinding.ActivityPayBinding;

public class PayActivity extends AppCompatActivity {

    private ActivityPayBinding binding;
    private double total;
    private final int shippingFee = 2;
    private double totalAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve total amount from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("totalAmount")) {
            total = intent.getDoubleExtra("totalAmount", 0.0);
        }

        // Calculate total amount including shipping
        totalAmount = total + shippingFee;
        binding.total.setText(String.valueOf(total));
        binding.shippingAmount.setText(String.valueOf(shippingFee));
        binding.totalAmount.setText(String.valueOf(totalAmount));

        // Set up payment method selection
        binding.paymentGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.card_payment) {
                binding.payonline.setVisibility(View.VISIBLE);
                binding.cardnumber.setEnabled(true);
                binding.nameoncard.setEnabled(true);
            } else if (checkedId == R.id.cash_on_delivery) {
                binding.payonline.setVisibility(View.GONE);
                binding.cardnumber.setEnabled(false);
                binding.nameoncard.setEnabled(false);
            }
        });

        // Set up payment button click handler
        binding.payLl.setOnClickListener(v -> handlePayment());

        // Set up back button click handler
        binding.backButton.setOnClickListener(v -> finish());
    }

    // Handle payment
    private void handlePayment() {
        if (binding.paymentGroup.getCheckedRadioButtonId() == R.id.card_payment) {
            if (binding.cardnumber.getText().toString().isEmpty() || binding.nameoncard.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter complete card information!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        new AlertDialog.Builder(PayActivity.this)
                .setTitle("Confirm Payment")
                .setMessage("Are you sure you want to pay Rs. " + totalAmount + " ?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if (binding.paymentGroup.getCheckedRadioButtonId() == R.id.card_payment) {
                        Toast.makeText(PayActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                    } else if (binding.paymentGroup.getCheckedRadioButtonId() == R.id.cash_on_delivery) {
                        Toast.makeText(PayActivity.this, "Orders are being shipped", Toast.LENGTH_SHORT).show();
                    }

                    // Handle payment successful action here
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
