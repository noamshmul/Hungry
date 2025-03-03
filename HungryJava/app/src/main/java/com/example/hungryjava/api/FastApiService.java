package com.example.hungryjava.api;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
public interface FastApiService {
    @GET("/inventory")
    Call<Map<String, Object>> getInventory();

    @DELETE("/inventory")
    Call<Map<String, Object>> removeItem();

}
