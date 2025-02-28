package com.example.hungryjava.api;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
public interface FastApiService {
    @GET("/users")
    Call<Map<String, Object>> getInventory();

    @GET("/users")
    Call<Map<String, Object>> addItem();

    @DELETE("/inventory")
    Call<Map<String, Object>> removeItem();

}
