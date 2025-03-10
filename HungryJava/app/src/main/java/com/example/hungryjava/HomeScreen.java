package com.example.hungryjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);

        // Retrieve the inventory_id and password
        String inventory_id = sharedPreferences.getString("inventory_id", "default_value");
        String password = sharedPreferences.getString("password", "default_value");

        // Hello textview
        TextView wellcome = findViewById(R.id.welcomeView);
        wellcome.setText("inventory id: " + inventory_id);

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
        Button btnHungry = findViewById(R.id.hungryButton);
        btnHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked}
                Toast.makeText(HomeScreen.this, "i'm Hungry clicked", Toast.LENGTH_SHORT).show();
                hungry();
            }
        });




    }

    private void hungry() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);
        Call<Map<String, Object>> call = apiService.getHungry();

        // Log the API request
        Log.d("hungry", "API call started...");

        // Execute the request synchronously or asynchronously
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // get the response dictionary into "body"
                    Map<String, Object> body = response.body();
                    // TODO: parse the response when we will know his type
                    Log.d("hungry", "Response: " + response.code() + " " + response.body());
                }
                else {
                    Toast.makeText(HomeScreen.this, "Hungry is fucked up", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}