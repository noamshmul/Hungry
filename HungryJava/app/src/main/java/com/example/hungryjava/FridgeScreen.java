package com.example.hungryjava;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.text.UStringsKt;
import retrofit2.Call;
import retrofit2.Retrofit;

import android.util.Log;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;


public class FridgeScreen extends AppCompatActivity {
    static List<Item> items = new ArrayList<>();
    private static final String TAG = "FridgeScreen";

    static ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_activity);


        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);


        // Step 2: Create an instance of the API service
        FastApiService apiService = retrofit.create(FastApiService.class);

        // Step 3: Make the API call
        Call<Map<String, Object>> call = apiService.getInventory();

        // Log the API request
        Log.d(TAG, "API call started...");

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                // Log the response
                Log.d(TAG, "Response: " + response.code() + " " + response.message());

                if (response.isSuccessful()) {
                    // Handle the response
                    Map<String, Object> responseBody = response.body();
                    if (responseBody != null) {
                        ArrayList<Map<String, Object>> inv = (ArrayList<Map<String, Object>>)responseBody.get("items");
                        for (int i = 0; i < inv.size(); i++)
                        {
                            Item item = new Item((String)inv.get(i).get("ingredient_name"), (Double)inv.get(i).get("quantity"));
                            items.add(item);
                        }
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    // Handle the error response
                    Log.e(TAG, "Error: " + response.message());
                }

                RecyclerView list = findViewById(R.id.fridge_list);


                // Set up RecyclerView
                list.setLayoutManager(new LinearLayoutManager(FridgeScreen.this));
                adapter = new ItemAdapter(FridgeScreen.this, items);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                // Log failure
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });



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