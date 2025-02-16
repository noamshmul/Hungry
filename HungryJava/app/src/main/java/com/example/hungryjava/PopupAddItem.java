package com.example.hungryjava;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PopupAddItem extends AppCompatActivity {
    private EditText editTextIngredient, editTextAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pop_up_add_item_screen);

        editTextIngredient = findViewById(R.id.editText_ingredient);
        editTextAmount = findViewById(R.id.editText_amount);

        Button addItemButton = findViewById(R.id.button_addItem);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ingredient = editTextIngredient.getText().toString().trim();
                String amountStr = editTextAmount.getText().toString().trim();

                if (ingredient.isEmpty() || amountStr.isEmpty()) {
                    Toast.makeText(PopupAddItem.this, "Please enter both ingredient and amount.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        Toast.makeText(PopupAddItem.this, "Ingredient: " + ingredient + ", Amount: " + amount, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (NumberFormatException e) {
                        Toast.makeText(PopupAddItem.this, "Please enter a valid number for the amount.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

