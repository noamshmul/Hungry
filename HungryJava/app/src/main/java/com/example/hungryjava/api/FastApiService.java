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


    @DELETE("/inventory")
    Call<Map<String, Object>> removeItem();

    @GET("/recipe")
    Call<Map<String,Object>> get_single_recipe(
            @Query("selected_recipe_name") String selected_recipe_name
    );
    
    @POST("/signup")
    Call<Map<String, Object>> postSignup(
            @Query("username") String inventory_id,
            @Query("password") String password
    );

}
