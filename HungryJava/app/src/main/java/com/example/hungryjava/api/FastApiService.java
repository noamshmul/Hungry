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

    
    @POST("/signup")
    Call<Map<String, String>> postSignup(
            @Query("inventory_id") String inventory_id,
            @Query("password") String password
    );

}
