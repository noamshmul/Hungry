package com.example.hungryjava;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import okhttp3.ResponseBody;

import android.content.Intent;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<RecipeItem> RecipesList;
    private final Context context;
    private HomeRefreshListener homeRefreshListener; // Add this line
    private CatalogRefreshListener catalogRefreshListener; // Add this line

    public interface HomeRefreshListener {
        void onRefreshHomeRecipes();
    }

    public interface CatalogRefreshListener {
        void onRefreshCatalogRecipes();
    }

    public RecipeAdapter(Context context, List<RecipeItem> RecipesList, HomeRefreshListener homeRefreshListener, CatalogRefreshListener catalogRefreshListener) {
        this.RecipesList = RecipesList;
        this.context = context;
        this.homeRefreshListener = homeRefreshListener;
        this.catalogRefreshListener = catalogRefreshListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipes, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeItem item = RecipesList.get(position);

        SingleRecipeScreen.loadImage(item.getImageName(), holder.imageView);
        holder.captionText.setText(item.getCaption());
        if (item.getFavorite()){
            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else holder.starIcon.setImageResource(android.R.drawable.btn_star_big_off);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SingleRecipeScreen.class);
            intent.putExtra("recipeName", item.getCaption());
            context.startActivity(intent);
        });

        // Handle favorite button click
        holder.starIcon.setOnClickListener(v -> {
            if (item.getFavorite()) {
                // Remove from favorites
                // in db
                Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
                FastApiService apiService = retrofit.create(FastApiService.class);
                Call<Map<String, String>> call = apiService.deleteFavorites(item.getRecipeId());

                // Log the API request
                Log.d("RecipeAdapter", "API call started...");

                // Execute the request synchronously or asynchronously
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // get the response dictionary into "body"
                            Map<String, String> body = response.body();
                            Log.d("RecipeAdapter", "Response: " + response.code() + " " + body);

                            item.setFavorite(false);
                            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_off);

                            if (context instanceof HungryPopupActivity) {
                                triggerHomeRefresh();
                                triggerCatalogRefresh();
                            }
                            else if (context instanceof Activity) {
                                FragmentActivity  activity = (FragmentActivity)context;
                                TabLayout tabLayout = activity.findViewById(R.id.tabLayout);
                                ViewPager2 viewPager = activity.findViewById(R.id.viewPager);
                                if (tabLayout != null) {
                                    TabLayout.Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                                    int selectedTabIndex = tabLayout.getSelectedTabPosition();
                                    ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
                                    if (selectedTab != null && selectedTab.getText() != null) {
                                        if (selectedTab.getText() == "Profile"){
                                            triggerHomeRefresh();

                                            // Remove item from the list
                                            int index = RecipesList.indexOf(item);
                                            if (index != -1) {
                                                RecipesList.remove(index);
                                                notifyItemRemoved(index);
                                                notifyItemRangeChanged(index, RecipesList.size()); // Update the indices of remaining items
                                            }
                                        }
                                        if (selectedTab.getText() == "Home"){
                                            triggerCatalogRefresh();
                                        }

                                    }
                                }
                            }

                            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        }
                        else if (response.code() == 400) {
                            Log.d("RecipeAdapter", "Response: " + response.code() + " " + response.message());
                        }
                        else {
                            Log.d("RecipeAdapter", "Response: " + response.code() + " " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            } else {
                // Add to favorites
                // in db
                Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
                FastApiService apiService = retrofit.create(FastApiService.class);
                Call<Map<String, String>> call = apiService.addFavorites(item.getRecipeId());

                // Log the API request
                Log.d("RecipeAdapter", "API call started...");

                // Execute the request synchronously or asynchronously
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // get the response dictionary into "body"
                            Map<String, String> body = response.body();
                            Log.d("RecipeAdapter", "Response: " + response.code() + " " + body);

                            item.setFavorite(true);
                            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_on);

                            if (context instanceof HungryPopupActivity) {
                                triggerHomeRefresh();
                                triggerCatalogRefresh();
                            }
                            else if (context instanceof Activity) {
                                FragmentActivity  activity = (FragmentActivity)context;
                                TabLayout tabLayout = activity.findViewById(R.id.tabLayout);
                                ViewPager2 viewPager = activity.findViewById(R.id.viewPager);
                                if (tabLayout != null) {
                                    TabLayout.Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                                    int selectedTabIndex = tabLayout.getSelectedTabPosition();
                                    ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
                                    if (selectedTab != null && selectedTab.getText() != null) {
                                        if (selectedTab.getText() == "Catalog"){
                                            triggerHomeRefresh();
                                        }
                                        if (selectedTab.getText() == "Home"){
                                            triggerCatalogRefresh();
                                        }

                                    }
                                }
                            }
                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                        }
                        else if (response.code() == 400) {
                            Log.d("RecipeAdapter", "Response: " + response.code() + " " + response.message());
                        }
                        else {
                            Log.d("RecipeAdapter", "Response: " + response.code() + " " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return RecipesList.size();
    }

    private void triggerHomeRefresh() {
        if (homeRefreshListener != null) {
            homeRefreshListener.onRefreshHomeRecipes();
        }
    }

    private void triggerCatalogRefresh() {
        if (catalogRefreshListener != null) {
            catalogRefreshListener.onRefreshCatalogRecipes();
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView starIcon;
        TextView captionText;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            starIcon = itemView.findViewById(R.id.starIcon);
            captionText = itemView.findViewById(R.id.captionText);
        }
    }
}
