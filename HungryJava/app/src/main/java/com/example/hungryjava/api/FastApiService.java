package com.example.hungryjava.api;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import okhttp3.ResponseBody;

public interface FastApiService {
    @GET("/inventory")
    Call<Map<String, Object>> getInventory();


    @DELETE("/inventory")
    Call<Map<String, Object>> removeItem();

    
    @POST("/signup")
    Call<Map<String, Object>> postSignup(
            @Query("username") String inventory_id,
            @Query("password") String password
    );

    @GET("/recipes")
    Call<Map<String, Object>> getRecipes();

    @GET("/images/{image_id}")
    Call<ResponseBody> get_image(@Path("image_id") String image_id);

}
