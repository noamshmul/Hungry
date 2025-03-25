package com.example.hungryjava;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText editTextAmount;
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
        editTextAmount = view.findViewById(R.id.editText_amount);
        Button changeItemButton = view.findViewById(R.id.button_change);
        if (!isRemove)
        {
            changeItemButton.setText("Add");
        }
        Retrofit comm = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = comm.create(FastApiService.class);
        changeItemButton.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter amount.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int amount = Integer.parseInt(amountStr);
                    // Handle addition or removal logic
                    if (isRemove) {
                        Call<Map<String, Object>> call = apiService.removeItem(FridgeScreen.items.get(itemPosition).getName(), amount);
                        call.enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.isSuccessful()) {
                                    FridgeScreen.items.get(itemPosition).setQuantity((int) (FridgeScreen.items.get(itemPosition).getQuantity() - amount));
                                    if (FridgeScreen.items.get(itemPosition).getQuantity() <= 0)
                                    {
                                        FridgeScreen.items.remove(itemPosition);
                                        FridgeScreen.adapter.notifyItemRemoved(itemPosition);
                                    }
                                    else
                                    {
                                        FridgeScreen.adapter.notifyItemChanged(itemPosition);
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
                        Call<Map<String, Object>> call = apiService.addToItem(FridgeScreen.items.get(itemPosition).getName(), amount);
                        call.enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.isSuccessful()) {
                                    FridgeScreen.items.get(itemPosition).setQuantity((FridgeScreen.items.get(itemPosition).getQuantity() + amount));
                                    FridgeScreen.adapter.notifyItemChanged(itemPosition);
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
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
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


