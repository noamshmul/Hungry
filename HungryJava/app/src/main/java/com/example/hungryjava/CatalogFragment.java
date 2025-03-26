package com.example.hungryjava;

import android.app.ActivityOptions;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.hungryjava.api.RetrofitClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Retrofit;

public class CatalogFragment extends Fragment {
  
    private RecipeAdapter adapter;
    private View view;
    private static final String TAG = "RecipesScreen";
    public List<RecipeItem> RecipesList = new ArrayList<>();
    private List<RecipeItem> filteredList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_catalog, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User Data", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "default_value");
        String password = sharedPreferences.getString("password", "default_value");
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(username, password, false);

        TextView helloText = view.findViewById(R.id.helloText);
        helloText.setText("Hello " + username);

        // logout button
        Button logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();

                // move to next login screen
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
      
        fetchRecipes();

        return view;
    }

    public void fetchRecipes() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);


        // Step 2: Create an instance of the API service
        FastApiService apiService = retrofit.create(FastApiService.class);

        // Step 3: Make the API call
        Call<Map<String, Object>> call = apiService.getFavorites();

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                // Log the response
                Log.d(TAG, "Response: " + response.code() + " " + response.message());

                if (response.isSuccessful()) {
                    // Handle the response
                    Map<String, Object> responseBody = response.body();

                    // Clear out before adding new recipes
                    RecipesList.clear();

                    if (responseBody != null) {
                        ArrayList<Map<String, Object>> recipes = (ArrayList<Map<String, Object>>)responseBody.get("recipes");
                        for (int i = 0; i < recipes.size(); i++)
                        {
                            String recipe_id = (String) recipes.get(i).get("_id");
                            String name = (String) recipes.get(i).get("name");
                            String Image_url = (String) recipes.get(i).get("image");
                            boolean favorite = (boolean) recipes.get(i).get("favorite");
                            RecipesList.add(new RecipeItem(
                                    recipe_id,Image_url, name, favorite
                            ));
                        }
                        // Create a filtered list
                        filteredList = new ArrayList<>(RecipesList);

                        adapter = new RecipeAdapter(view.getContext(), filteredList, (MainActivity) getActivity(), (MainActivity) getActivity());
                        recyclerView.setAdapter(adapter);

                    }
                    else {
                        Log.e(TAG, "Response body is null");
                    }
                }
                else {
                    // Handle the error response
                    Log.e(TAG, "Error: " + response.message());

                    Toast.makeText(view.getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);


                }

                // Initialize RecyclerView

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                // Log failure
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });


        // Initialize Adapter

        SearchView searchBar = view.findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }
    private void filter(String text)
    {
        filteredList.clear();
        for (RecipeItem recipe: RecipesList)
        {
            if(recipe.getCaption().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(recipe);
            }
        }
        adapter.notifyDataSetChanged();
    }
}