package com.example.hungryjava.api;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface FastApiService {
    @GET("/inventory")
    Call<Map<String, Object>> getInventory();

    @POST("/inventory")
    Call<Map<String, Object>> addToItem(@Query("name") String itemName, @Query("amount") int amount);

    @GET("/ingredients")
    Call<Map<String, Object>> getIngredients();

    @DELETE("/inventory")
    Call<Map<String, Object>> removeItem(@Query("name") String itemName, @Query("amount") int amount);

    
    @POST("/signup")
    Call<Map<String, Object>> postSignup(
            @Query("username") String inventory_id,
            @Query("password") String password
    );

    @GET("/hungry")
    Call<Map<String, Object>> getHungry();
}
