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
    EditText username;
    EditText password;
    EditText cnf_password;

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
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cnf_password = findViewById(R.id.cnf_password);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);

        // submit button
        btnTestConnection = findViewById(R.id.submit);
        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username_text = username.getText().toString();
                String password_text = password.getText().toString();
                String cnf_pasword_text = cnf_password.getText().toString();

                if (!password_text.equals(cnf_pasword_text)){
                    Toast.makeText(Activity_signup.this, "Not the same password", Toast.LENGTH_SHORT).show();
                }

                else {
                    // Checks if valid password
                    if (password_text.length() < 8) {
                        Toast.makeText(Activity_signup.this, "Password must be al least 8 characters", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Retrofit retrofit = RetrofitClient.getRetrofitInstance(username_text, username_text, false);

                        // Step 2: Create an instance of the API service
                        FastApiService apiService = retrofit.create(FastApiService.class);

                        // Step 3: Make the API call
                        Call<Map<String, Object>> call = apiService.postSignup(username_text, password_text);

                        // Execute the request synchronously or asynchronously
                        call.enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    // get the response dictionary into "body"
                                    Map<String, Object> body = response.body();
                                    String status = (String) body.get("status");

                                    if (status.equals("ok")) {
                                        // Store the username and password in SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", username_text);
                                        editor.putString("password", password_text);
                                        editor.apply(); // Commit the changes asynchronously

                                        // move to next homescreen
                                        Intent intent = new Intent(Activity_signup.this, HomeScreen.class);
                                        startActivity(intent);
                                    } else if (status.equals("conflict")) {
                                        Toast.makeText(Activity_signup.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Activity_signup.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
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
