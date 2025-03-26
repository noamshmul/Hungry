package com.example.hungryjava.api;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import okhttp3.ResponseBody;

public interface FastApiService {
    @GET("/inventory")
    Call<Map<String, Object>> getInventory();

    @POST("/inventory")
    Call<Map<String, Object>> addToItem(@Query("name") String itemName, @Query("amount") int amount);

    @GET("/ingredients")
    Call<Map<String, Object>> getIngredients();

    @DELETE("/inventory")
    Call<Map<String, Object>> removeItem(@Query("name") String itemName, @Query("amount") int amount);

    @GET("/recipe")
    Call<Map<String, Object>> get_single_recipe(
            @Query("selected_recipe_name") String selected_recipe_name
    );
    @POST("/signup")
    Call<Map<String, Object>> postSignup(
            @Query("username") String inventory_id,
            @Query("password") String password
    );

    @GET("/recipes")
    Call<Map<String, Object>> getRecipes();

    @GET("/images/{image_id}")
    Call<ResponseBody> get_image(@Path("image_id") String image_id);

    @GET("/hungry")
    Call<Map<String, Object>> getHungry();

    @GET("/favorites")
    Call<Map<String, Object>> getFavorites();

    @PUT("/favorites")
    Call<Map<String, String>> addFavorites(
            @Query("recipe_id") String recipe_id
    );

    @DELETE("/favorites")
    Call<Map<String, String>> deleteFavorites(
            @Query("recipe_id") String recipe_id
    );

}
