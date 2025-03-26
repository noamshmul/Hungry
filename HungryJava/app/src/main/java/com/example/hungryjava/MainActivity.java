package com.example.hungryjava;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hungryjava.api.RetrofitClient;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.HomeRefreshListener, RecipeAdapter.CatalogRefreshListener {

    private String TAG = "MainActivity";
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        checkAuthentication(sharedPreferences);

        Retrofit retrofit = getRetrofit(sharedPreferences);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.post(() -> viewPager.setCurrentItem(1, false));

        // Connect tabs with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Inventory");
                    break;
                case 1:
                    tab.setText("Home");
                    break;
                case 2:
                    tab.setText("Profile");
                    break;
            }
        }).attach();
    }

    private Retrofit getRetrofit(SharedPreferences sharedPreferences){
        String username = sharedPreferences.getString("username", "default_value");
        String password = sharedPreferences.getString("password", "default_value");
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(username, password, false);
        return retrofit;
    }

    private void checkAuthentication(SharedPreferences sharedPreferences) {

        boolean is_user_exist = sharedPreferences.contains("username");
        boolean is_password_exist = sharedPreferences.contains("password");

        if (!(is_user_exist && is_password_exist)){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRefreshHomeRecipes() {
        Log.d(TAG, "onRefreshHomeRecipes");
        Fragment fragment = adapter.getFragment(1);
        if (fragment != null && fragment.isAdded()) {
            Log.d(TAG, "onRefreshHomeRecipes - fetch");
            ((HomeFragment) fragment).fetchRecipes();
        }
    }

    @Override
    public void onRefreshCatalogRecipes() {
        Log.d(TAG, "onRefreshCatalogRecipes");
        Fragment fragment = adapter.getFragment(2);
        if (fragment != null && fragment.isAdded()) {
            Log.d(TAG, "onRefreshCatalogRecipes - fetch");
            ((CatalogFragment) fragment).fetchRecipes();
        }
    }
}