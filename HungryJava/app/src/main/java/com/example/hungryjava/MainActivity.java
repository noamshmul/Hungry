package com.example.hungryjava;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    Button btnTestConnection;
    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginscreen);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        // create the username and password textbox
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);

        // submit button
        btnTestConnection = findViewById(R.id.submit);
        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username_text = username.getText().toString();
                String password_text = password.getText().toString();

                Retrofit retrofit = RetrofitClient.getRetrofitInstance(username_text, password_text, true);

                // Step 2: Create an instance of the API service
                FastApiService apiService = retrofit.create(FastApiService.class);

                // Step 3: Make the API call
                Call<Map<String, Object>> call = apiService.getInventory();

                // Execute the request synchronously or asynchronously
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // get the response dictionary into "body"
                            Map<String, Object> body = response.body();
                            String status = (String) body.get("status");

                            // TODO: parse the response when we will know his type

                            // Store the username in SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username_text);
                            editor.apply(); // Commit the changes asynchronously

                            // move to next homescreen
                            Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


                /*
                new AlertDialog.Builder(MainActivity.this)
                        .setView(R.layout.pop_up_add_item_screen)
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
                */
            }
        });

        // fridge button
        Button fridge = findViewById(R.id.signup);
        fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_signup.class);
                startActivity(intent);
            }
        });
    }
}
