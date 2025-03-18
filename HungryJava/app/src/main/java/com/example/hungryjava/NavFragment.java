package com.example.hungryjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Toast;

import android.content.Context;
import android.content.SharedPreferences;



public class NavFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nav, container, false);

        Button logout_btn = rootView.findViewById(R.id.logoutButton);
        Button inventory_btn = rootView.findViewById(R.id.inventory_button);
        Button home_btn = rootView.findViewById(R.id.home);

        // set buttons fonts
        inventory_btn.setTypeface(Typeface.create("cursive", Typeface.BOLD));
        logout_btn.setTypeface(Typeface.create("cursive", Typeface.BOLD));
        home_btn.setTypeface(Typeface.create("cursive", Typeface.BOLD));

        // mark the current screen
        if (getActivity() instanceof FridgeScreen) {
            // Set the color of the "inventory" button to red
            inventory_btn.setTextColor(Color.RED);
        }
        // TODO: change "homescreen" in or's recipes screen
        else if (getActivity() instanceof HomeScreen) {
            // Set the color of the "inventory" button to red
            inventory_btn.setTextColor(Color.RED);
        }

        // Set OnClickListener for logout button
        logout_btn.setOnClickListener(v -> {
            // Code to execute when the button is clicked
            // Get the SharedPreferences instance
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User Data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("username");
            editor.remove("password");
            editor.apply();

            // go to login screen
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Set OnClickListener for inventory button
        inventory_btn.setOnClickListener(v -> {
            // Handle navigating to Inventory (or any other action)
            Toast.makeText(getActivity(), "Inventory Button Clicked", Toast.LENGTH_SHORT).show();
        });

        // Set OnClickListener for home button
        home_btn.setOnClickListener(v -> {
            // go to home screen
            // TODO: change "homescreen" in or's recipes screen
            Intent intent = new Intent(getActivity(), HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return rootView;
    }
}
