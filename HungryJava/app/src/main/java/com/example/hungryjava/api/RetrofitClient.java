package com.example.hungryjava.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Interceptor;
import okhttp3.Response;

import android.os.Build;
import android.util.Base64;
import java.io.IOException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8000";
    private static final String PROD_URL = "http://192.168.211.128:8000";
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitInstance(String username, String password, boolean flag) {
        if (retrofit == null || flag) {
            Log.d("MyAppTag", username);
            //create OkHttpClient with Interceptor to add the Authorization header
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String credentials = username + ":" + password;
                            String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                            //add the Authorization header to the request
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", authHeader)
                                    .build();

                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            String base_url;
            if (isAndroidEmulator()) {
                base_url = BASE_URL;
            }
            else { base_url = PROD_URL; }


            // create the Retrofit instance with OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(client)  //attach the custom OkHttpClient
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static boolean isAndroidEmulator() {
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        boolean isEmulator = false;
        if (product != null) {
            isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
        }
        Log.d("Retrofit", "isEmulator=" + isEmulator);
        return isEmulator;
    }
}
