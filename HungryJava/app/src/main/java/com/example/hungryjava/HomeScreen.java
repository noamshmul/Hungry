package com.example.hungryjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class HomeScreen extends AppCompatActivity {
    String username = "/user/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        // Hello textview
        TextView wellcome = findViewById(R.id.welcomeView);
        wellcome.setText("Hello " + username);

        // logout button
        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked}
                Toast.makeText(HomeScreen.this, "Logout clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // fridge button
        Button fridge = findViewById(R.id.fridgeButton);
        fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, FridgeScreen.class);
                startActivity(intent);
            }
        });

        // I'm Hungry button
        Button hungry = findViewById(R.id.hungryButton);
        hungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked}
                Toast.makeText(HomeScreen.this, "i'm Hungry clicked", Toast.LENGTH_SHORT).show();
            }
        });




    }
}