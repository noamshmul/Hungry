package com.example.hungryjava;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class RecipesScreen extends AppCompatActivity {

    private RecipeAdapter adapter;
    private List<RecipeItem> RecipesList;
    private List<RecipeItem> filteredList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_screen);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null);


        // Step 2: Create an instance of the API service
        FastApiService apiService = retrofit.create(FastApiService.class);

        // Step 3: Make the API call
        Call<Map<String, Object>> call = apiService.getInventory();

        // Initialize Image List
        RecipesList = new ArrayList<>();
        RecipesList.add(new RecipeItem("recipe1", "Pasta"));
        RecipesList.add(new RecipeItem("recipe2", "Pizza"));
        RecipesList.add(new RecipeItem("recipe3", "Salad"));
        RecipesList.add(new RecipeItem("recipe4", "Soup"));

        // Create a filtered list
        filteredList = new ArrayList<>(RecipesList);

        // Initialize Adapter
        adapter = new RecipeAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
        SearchView searchBar = findViewById(R.id.searchView);
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