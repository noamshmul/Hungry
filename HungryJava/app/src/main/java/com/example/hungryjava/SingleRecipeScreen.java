package com.example.hungryjava;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SingleRecipeScreen extends AppCompatActivity {

    private static final String TAG = "SingleRecipeScreen";
    private ImageView recipeImage;
    private TextView recipeName;
    private TextView recipeApproxTime;
    private TextView recipeIngredients;
    private TextView recipeInstructions;
    private TextView recipeSize;
    private FloatingActionButton fabBack;
    private Button makeIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe_screen);

        fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(v -> {
            // Start the second activity with the shared element transition
            finish(); // Finish the activity and go back to the previous screen
        });

        // Initialize views
        recipeImage = findViewById(R.id.recipe_image);
        recipeName = findViewById(R.id.recipe_name);
        recipeApproxTime = findViewById(R.id.recipe_approx_time);
        recipeIngredients = findViewById(R.id.recipe_ingredients);
        recipeInstructions = findViewById(R.id.recipe_instructions);
        recipeSize = findViewById(R.id.recipe_size);
        makeIt = findViewById(R.id.make_it);

        //get recipe name from intent
        String recipeNameStr = getIntent().getStringExtra("recipeName");
        if (recipeNameStr != null && !recipeNameStr.isEmpty()) {
            //gake api call
            getRecipe(recipeNameStr);
        } else {
            Log.e(TAG, "Recipe name is null or empty");
        }
    }

    private void getRecipe(String recipeNameStr) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);
        Call<Map<String, Object>> call = apiService.get_single_recipe(recipeNameStr);

        Log.d(TAG, "API call started for recipe: " + recipeNameStr);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d(TAG, "Response: " + response.code() + " " + response.message());

                if (response.isSuccessful()) {
                    Map<String, Object> recipe = response.body();
                    if (recipe != null) {
                        updateUI(recipe);
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    Log.e(TAG, "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    private void updateUI(Map<String, Object> recipe) {
        // Update UI elements with recipe data
        if (recipe.containsKey("name")) {
            recipeName.setText((String) recipe.get("name"));
        }
        if (recipe.containsKey("approx_time")) {
            if(recipe.get("approx_time") != null) {
                recipeApproxTime.setText("Approx Time: " + recipe.get("approx_time"));
                recipeApproxTime.setVisibility(View.VISIBLE);
            }
            else
                recipeApproxTime.setVisibility(View.INVISIBLE);
        }
        if (recipe.containsKey("size")) {
            if(recipe.get("size") != null) {
                recipeSize.setText("Size: " + recipe.get("size"));
                recipeSize.setVisibility(View.VISIBLE);
            }
            else
                recipeSize.setVisibility(View.INVISIBLE);
        }

        if (recipe.containsKey("image")) {
            String imageUrl = (String) recipe.get("image");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                loadImage(imageUrl, recipeImage); // call load image function instead of AsyncTask
            }
        }

        if (recipe.containsKey("ingredients")) {
            Map<String, Map<String, Object>> ingredientsMap = (Map<String, Map<String, Object>>) recipe.get("ingredients");
            StringBuilder ingredientsText = new StringBuilder();
            for (Map<String, Object> ingredient : ingredientsMap.values()) {
                if (ingredient.containsKey("original_quantity")) {
                    ingredientsText.append(ingredient.get("original_quantity")).append("\n");
                }
            }
            recipeIngredients.setText(ingredientsText.toString());
        }

        if (recipe.containsKey("instructions")) {
            ArrayList<String> instructions = (ArrayList<String>) recipe.get("instructions");
            StringBuilder instructionsText = new StringBuilder();
            for (String instruction : instructions) {
                instructionsText.append("- ").append(instruction).append("\n");
            }
            recipeInstructions.setText(instructionsText.toString());
        }

        // Get recipe_id and set OnClickListener for the "Make It" button
        String recipeId = (String) recipe.get("_id"); // Assuming recipe id is stored with key "id"
        makeIt.setOnClickListener(v -> {
            if (recipeId != null && !recipeId.isEmpty()) {
                deleteIngredientsByRecipe(recipeId); // Trigger the deleteIngredientsByRecipe method
            } else {
                Log.e(TAG, "Recipe ID is null or empty");
            }
        });
    }

    private void deleteIngredientsByRecipe(String recipeId) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);

        // Make the API call to delete ingredients by recipe id
        Call<Map<String, Object>> call = apiService.remove_ingredients_by_recipe(recipeId);

        Log.d(TAG, "Delete ingredients API call started for recipe id: " + recipeId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(makeIt.getContext(), "Bon appétit!", Toast.LENGTH_SHORT).show(); // Added toast message here
                    Log.d(TAG, "Ingredients deleted successfully for recipe id: " + recipeId);
                    updateInventoryScreen();
                } else {
                    Log.e(TAG, "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    private void updateInventoryScreen(){
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);

        // Make the API call
        Call<Map<String, Object>> call = apiService.getInventory();
        Log.d(TAG, "API call started...");

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d(TAG, "Response: " + response.code() + " " + response.message());

                if (response.isSuccessful()) {
                    Map<String, Object> responseBody = response.body();
                    if (responseBody != null) {
                        ArrayList<Map<String, Object>> inv = (ArrayList<Map<String, Object>>)responseBody.get("items");
                        InventoryFragment.items.clear(); // Clear existing items before adding new ones
                        for (int i = 0; i < inv.size(); i++) {
                            Item item = new Item((String)inv.get(i).get("ingredient_name"), (double)inv.get(i).get("quantity"));
                            InventoryFragment.items.add(item);
                        }
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    Log.e(TAG, "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });
    }
    static public void loadImage(String imageUrl, ImageView xmlObject) {

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);
        String imageId = imageUrl;
        Call<ResponseBody> call = apiService.get_image(imageId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    xmlObject.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "Failed to load image: " + (response.errorBody() != null ? response.errorBody().toString() : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Image load failure: " + t.getMessage());
            }
        });
    }
}