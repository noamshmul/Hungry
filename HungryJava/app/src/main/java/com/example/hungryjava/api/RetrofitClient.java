package com.example.hungryjava.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Interceptor;
import okhttp3.Response;
import android.util.Base64;
import java.io.IOException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8000";
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitInstance(String inventory_id, String password, boolean flag) {
        if (retrofit == null || flag) {
            Log.d("MyAppTag", inventory_id);
            //create OkHttpClient with Interceptor to add the Authorization header
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String credentials = inventory_id + ":" + password;
                            String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                            //add the Authorization header to the request
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", authHeader)
                                    .build();

                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            // create the Retrofit instance with OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)  //attach the custom OkHttpClient
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
