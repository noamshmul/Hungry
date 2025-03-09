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
public class PopupRemoveItem extends DialogFragment {
    private EditText editTextAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_up_remove_item, container, false);

        editTextAmount = view.findViewById(R.id.editText_amount);
        Button addItemButton = view.findViewById(R.id.button_remove);

        addItemButton.setOnClickListener(v -> {

            String amountStr = editTextAmount.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both ingredient and amount.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double amount = Double.parseDouble(amountStr);
                    FridgeScreen.items.add(amountStr);
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

