package com.example.hungryjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hungryjava.api.RetrofitClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Retrofit;

public class CatalogFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("CatalogFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User Data", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "default_value");
        String password = sharedPreferences.getString("password", "default_value");
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(username, password, false);

        TextView helloText = view.findViewById(R.id.helloText);
        helloText.setText("Hello " + username);

        // logout button
        Button logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();

                // move to next login screen
                Intent intent = new Intent(getActivity(), Activity_signup.class);
                startActivity(intent);
            }
        });

        return view;
    }
}