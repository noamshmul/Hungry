package com.example.hungryjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FridgeScreen extends AppCompatActivity {
    static List<String> items = new ArrayList<>();
    static ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_activity);
        RecyclerView list = findViewById(R.id.fridge_list);
        // Sample data: List of strings (your fridge items)

        items.add("Milk");
        items.add("Eggs");
        items.add("Butter");
        items.add("Cheese");
        items.add("Yogurt");

        // Set up RecyclerView
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(this, items);
        list.setAdapter(adapter);

        Button add = findViewById(R.id.add_item);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupAddItem popup = new PopupAddItem();
                popup.show(getSupportFragmentManager(), "PopupAddItem");
            }
        });


    }
}