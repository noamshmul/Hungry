package com.example.hungryjava;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopupAddItem extends DialogFragment {
    private AutoCompleteTextView autoCompleteTextView;
    private NumberPicker numberPicker;
    private String selectedIngredient = null;
    private static final String TAG = "PopupAddItem";
    private static List<String> ingredients = new ArrayList<>(); // Made static to cache the list

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_up_add_item_screen, container, false);

        // Initialize views
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView_ingredient);
        numberPicker = view.findViewById(R.id.numberPicker_amount);
        Button addItemButton = view.findViewById(R.id.button_addItem);

        // Set up NumberPicker
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);
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

        // Only fetch ingredients if the list is empty
        if (ingredients.isEmpty()) {
            fetchIngredients();
        } else {
            setupAutoCompleteTextView();
        }

        // Set up add button
        addItemButton.setOnClickListener(v -> {
            if (selectedIngredient == null) {
                Toast.makeText(getContext(), "Please select an ingredient", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = numberPicker.getValue();
            boolean itemFound = false;

            // Check if item already exists
            for (int i = 0; i < FridgeScreen.items.size(); i++) {
                String[] parts = FridgeScreen.items.get(i).split("\n");
                if (parts[0].equals(selectedIngredient)) {
                    // Item exists, update the amount
                    int currentAmount = Integer.parseInt(parts[1]);
                    int newAmount = currentAmount + amount;
                    FridgeScreen.items.set(i, selectedIngredient + "\n" + newAmount);
                    FridgeScreen.adapter.notifyItemChanged(i);
                    itemFound = true;
                    break;
                }
            }

            // If item doesn't exist, add it as new
            if (!itemFound) {
                FridgeScreen.items.add(selectedIngredient + "\n" + amount);
                FridgeScreen.adapter.notifyItemInserted(FridgeScreen.items.size() - 1);
            }

            dismiss(); // Close the dialog
        });

        return view;
    }

    private void fetchIngredients() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);
        Call<Map<String, Object>> call = apiService.getIngredients();

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> responseBody = response.body();
                    ArrayList<Map<String, Object>> ingredientsList = (ArrayList<Map<String, Object>>) responseBody.get("ingredients");
                    if (ingredientsList != null) {
                        // Extract only the names from the ingredient objects
                        ingredients.clear();
                        for (Map<String, Object> ingredient : ingredientsList) {
                            String name = (String) ingredient.get("name");
                            if (name != null) {
                                ingredients.add(name);
                            }
                        }
                        setupAutoCompleteTextView();
                    } else {
                        Log.e(TAG, "Ingredients list is null in response");
                        Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Error: " + response.message());
                    Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ingredients
        ) {
            @Override
            public int getCount() {
                return Math.min(super.getCount(), 5); // Limit to 5 items
            }
        };
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            selectedIngredient = (String) parent.getItemAtPosition(position);
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
