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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.hungryjava.api.RetrofitClient;
import retrofit2.Retrofit;


public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "default_value");
        String password = sharedPreferences.getString("password", "default_value");
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(username, password, false);

        // Hello textview
        TextView wellcome = findViewById(R.id.welcomeView);
        wellcome.setText("Username: " + username);

        // logout button
        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();

                // move to next login screen
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // fridge button
        Button fridge = findViewById(R.id.fridgeButton);
        fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, InventoryFragment.class);
                startActivity(intent);
            }
        });

        // I'm Hungry button
        Button btnHungry = findViewById(R.id.hungryButton);
        btnHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Log.d("hungry", "Response: " + response.code() + " " + body);
                }
                else if (response.code() == 400) {
                    Log.d("hungry", "Response: " + response.code() + " " + response.message());
                    Toast.makeText(HomeScreen.this, "Not enough items in your inventory", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("hungry", "Response: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}