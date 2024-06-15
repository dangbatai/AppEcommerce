package com.example.app_ecommerce.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.app_ecommerce.R;
import com.example.app_ecommerce.adapter.ProductAdapter;
import com.example.app_ecommerce.adapter.home_slide_adapter;
import com.example.app_ecommerce.databinding.FragmentHomefragmentBinding;
import com.example.app_ecommerce.model.Product;
import com.example.app_ecommerce.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class homefragment extends Fragment {
    private RecyclerView recyclerViewsp;
    private ArrayList<Product> productArrayList;
    private ArrayList<Product> arrayList;
    private ProductAdapter adapter_item;
    private FragmentHomefragmentBinding binding;
    private MainActivityViewModel viewModel;
    private EditText txtsearch;
    private ImageView[] dotImageViews;
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private int page_position = 0;
    private int dotscount;
    private home_slide_adapter home_slider;
    private Integer[] images = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4, R.drawable.slider5};

    private Timer timer;

    private int userId;

    public homefragment() {
        // Required empty public constructor
    }

    public static homefragment newInstance(int userId) {
        homefragment fragment = new homefragment();
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
        Log.v("hello", String.valueOf(userId));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomefragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerViewsp = binding.recyclersp;
        txtsearch = binding.txtsearch;
        viewPager = binding.viewPager;
        sliderDotspanel = binding.SliderDots;

        productArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();

        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        viewModel.getAllProduct().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                arrayList.clear();
                arrayList.addAll(products);
                productArrayList.clear();
                productArrayList.addAll(arrayList);
                updateRecyclerView();
            }
        });

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        home_slider = new home_slide_adapter(requireContext(), images);
        viewPager.setAdapter(home_slider);

        setupViewPagerDots();
        scheduleSlider();
        return view;
    }

    public void search(String text) {
        productArrayList.clear();
        if (text != null) {
            for (Product product : arrayList) {
                if ((product.getTitle() != null && product.getTitle().toLowerCase().contains(text.toLowerCase())) ||
                        (product.getDescription() != null && product.getDescription().toLowerCase().contains(text.toLowerCase())) ||
                        (product.getCategory() != null && product.getCategory().toLowerCase().contains(text.toLowerCase())) ||
                        (product.getBrand() != null && product.getBrand().toLowerCase().contains(text.toLowerCase()))) {
                    productArrayList.add(product);
                }
            }
            updateRecyclerView();
        }
    }

    private void updateRecyclerView() {
        if (adapter_item == null) {
            adapter_item = new ProductAdapter(requireContext(), productArrayList, userId); // Pass userId to adapter
            recyclerViewsp.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerViewsp.setItemAnimator(new DefaultItemAnimator());
            recyclerViewsp.setAdapter(adapter_item);
        } else {
            adapter_item.notifyDataSetChanged();
        }
    }

    public void scheduleSlider() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == dotscount - 1) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4000);
    }

    private void setupViewPagerDots() {
        dotscount = home_slider.getCount();
        dotImageViews = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dotImageViews[i] = new ImageView(getContext());
            if (getContext() != null) {
                dotImageViews[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dotImageViews[i], params);
        }

        if (getContext() != null) {
            dotImageViews[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (getContext() == null) return;

                for (int i = 0; i < dotscount; i++) {
                    dotImageViews[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                }
                dotImageViews[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
