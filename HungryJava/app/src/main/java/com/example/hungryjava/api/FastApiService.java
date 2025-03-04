package com.example.hungryjava.api;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
public interface FastApiService {
    @GET("/users")
    Call<Map<String, Object>> getInventory();
}
