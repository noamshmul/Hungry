package com.example.hungryjava;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopupChangeItem extends DialogFragment {
    private NumberPicker numberPicker;
    private boolean isRemove;
    private int itemPosition;

    public static PopupChangeItem newInstance(boolean isRemove, int position) {
        PopupChangeItem fragment = new PopupChangeItem();
        Bundle args = new Bundle();
        args.putBoolean("isRemove", isRemove);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isRemove = getArguments().getBoolean("isRemove");
            itemPosition = getArguments().getInt("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_up_change_amount, container, false);
        numberPicker = view.findViewById(R.id.numberPicker_amount);
        Button changeItemButton = view.findViewById(R.id.button_change);
        
        if (!isRemove) {
            changeItemButton.setText("Add");
        }

        // Set up NumberPicker
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(!isRemove ? 1000 : (int) InventoryFragment.items.get(itemPosition).getQuantity());
        numberPicker.setValue(1);
        numberPicker.setWrapSelectorWheel(false);

        // Customize NumberPicker appearance
        for (int i = 0; i < numberPicker.getChildCount(); i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextSize(16);
                textView.setPadding(0, 8, 0, 8);
            }
        }

        Retrofit comm = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = comm.create(FastApiService.class);
        changeItemButton.setOnClickListener(v -> {
            int amount = numberPicker.getValue();
            // Handle addition or removal logic
            if (isRemove) {
                Call<Map<String, Object>> call = apiService.removeItem(InventoryFragment.items.get(itemPosition).getName(), amount);
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            InventoryFragment.items.get(itemPosition).setQuantity((int) (InventoryFragment.items.get(itemPosition).getQuantity() - amount));
                            if (InventoryFragment.items.get(itemPosition).getQuantity() <= 0)
                            {
                                InventoryFragment.items.remove(itemPosition);
                                InventoryFragment.adapter.notifyItemRemoved(itemPosition);
                            }
                            else
                            {
                                InventoryFragment.adapter.notifyItemChanged(itemPosition);
                            }
                        }
                        else {
                            // Handle the error response
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            } else {
                Call<Map<String, Object>> call = apiService.addToItem(InventoryFragment.items.get(itemPosition).getName(), amount);
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            InventoryFragment.items.get(itemPosition).setQuantity((InventoryFragment.items.get(itemPosition).getQuantity() + amount));
                            InventoryFragment.adapter.notifyItemChanged(itemPosition);
                        }
                        else {
                            // Handle the error response
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}


