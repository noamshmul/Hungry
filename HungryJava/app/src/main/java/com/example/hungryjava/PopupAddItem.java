package com.example.hungryjava;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PopupAddItem extends DialogFragment {
    private EditText editTextIngredient, editTextAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_up_add_item_screen, container, false);

        editTextIngredient = view.findViewById(R.id.editText_ingredient);
        editTextAmount = view.findViewById(R.id.editText_amount);
        Button addItemButton = view.findViewById(R.id.button_addItem);

        addItemButton.setOnClickListener(v -> {
            String ingredient = editTextIngredient.getText().toString().trim();
            String amountStr = editTextAmount.getText().toString().trim();

            if (ingredient.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both ingredient and amount.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double amount = Double.parseDouble(amountStr);
                    Toast.makeText(getContext(), "Ingredient: " + ingredient + ", Amount: " + amount, Toast.LENGTH_SHORT).show();
                    FridgeScreen.items.add(ingredient + " " + amountStr);
                    FridgeScreen.adapter.notifyItemInserted(FridgeScreen.items.size() - 1);
                    dismiss(); // Close the dialog
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid number for the amount.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true); // Close when clicking outside
        return dialog;
    }
}
