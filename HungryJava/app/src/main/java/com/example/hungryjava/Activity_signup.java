package com.example.hungryjava;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class Activity_signup extends AppCompatActivity {
    Button btnTestConnection;
    EditText inventory_id;
    EditText password;
    EditText cnf_password;

    //private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        // create the username and password textbox
        inventory_id = findViewById(R.id.inventory_id);
        password = findViewById(R.id.password);
        cnf_password = findViewById(R.id.cnf_password);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);

        // submit button
        btnTestConnection = findViewById(R.id.submit);
        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inventory_id_text = inventory_id.getText().toString();
                String password_text = password.getText().toString();
                String cnf_pasword_text = cnf_password.getText().toString();

                if (!password_text.equals(cnf_pasword_text)){
                    Toast.makeText(Activity_signup.this, "Not the same password", Toast.LENGTH_SHORT).show();
                }

                else {
                    Retrofit retrofit = RetrofitClient.getRetrofitInstance(inventory_id_text, password_text);

                    // Step 2: Create an instance of the API service
                    FastApiService apiService = retrofit.create(FastApiService.class);

                    // Step 3: Make the API call
                    Call<Map<String, String>> call = apiService.postSignup(inventory_id_text, password_text);

                    // Log the API request
                    // Log.d(TAG, "API call started...");

                    // Execute the request synchronously or asynchronously

                    call.enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // get the response dictionary into "body"
                                Map<String, String> body = response.body();
                                String status = body.get("status");

                                Toast.makeText(Activity_signup.this, status, Toast.LENGTH_SHORT).show();
                                if (status.equals("ok")) {
                                    // Store the inventory_id in SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("inventory_id", inventory_id_text);
                                    editor.apply(); // Commit the changes asynchronously
                                    editor.putString("password", password_text);
                                    editor.apply(); // Commit the changes asynchronously

                                    Toast.makeText(Activity_signup.this, "GREAT!", Toast.LENGTH_SHORT).show();
                                    // move to next homescreen
                                    Intent intent = new Intent(Activity_signup.this, HomeScreen.class);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(Activity_signup.this, "fucked up", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }


                /*
                new AlertDialog.Builder(MainActivity.this)
                        .setView(R.layout.pop_up_add_item_screen)
                        .setCancelable(false)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();

                 */
            }



        });
    }
}