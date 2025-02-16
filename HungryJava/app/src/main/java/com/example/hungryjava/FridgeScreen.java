package com.example.hungryjava;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FridgeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_activity);
        RecyclerView list = findViewById(R.id.fridge_list);
        // Sample data: List of strings (your fridge items)
        List<String> items = new ArrayList<>();
        items.add("Milk");
        items.add("Eggs");
        items.add("Butter");
        items.add("Cheese");
        items.add("Yogurt");

        // Set up RecyclerView
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new ItemAdapter(this, items));


    }
}