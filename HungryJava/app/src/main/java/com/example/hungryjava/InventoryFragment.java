package com.example.hungryjava;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InventoryFragment extends Fragment {
    private static final String TAG = "InventoryFragment";
    static List<Item> items = new ArrayList<>();
    static ItemAdapter adapter;
    private RecyclerView list;

    static Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Initialize RecyclerView
        list = view.findViewById(R.id.inventory_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Create adapter if it doesn't exist
        if (adapter == null) {
            adapter = new ItemAdapter(requireContext(), items);
            list.setAdapter(adapter);
        }

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("User Data", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "default_value");
        String password = sharedPreferences.getString("password", "default_value");
        Log.d("username: ", username);

        context = requireContext();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);

        // Make the API call
        Call<Map<String, Object>> call = apiService.getInventory();
        Log.d(TAG, "API call started...");

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d(TAG, "Response: " + response.code() + " " + response.message());

                if (response.isSuccessful()) {
                    Map<String, Object> responseBody = response.body();
                    if (responseBody != null) {
                        ArrayList<Map<String, Object>> inv = (ArrayList<Map<String, Object>>)responseBody.get("items");
                        items.clear(); // Clear existing items before adding new ones
                        for (int i = 0; i < inv.size(); i++) {
                            Item item = new Item((String)inv.get(i).get("ingredient_name"), (double)inv.get(i).get("quantity"));
                            items.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    Log.e(TAG, "Error: " + response.message());
                }

                list = view.findViewById(R.id.inventory_list);
                list.setLayoutManager(new LinearLayoutManager(context));
                adapter = new ItemAdapter(requireContext(), items);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.getMessage());
            }
        });

        // Set up add button
        ExtendedFloatingActionButton add = view.findViewById(R.id.add_item);
        add.setOnClickListener(v -> {
            PopupAddItem popup = new PopupAddItem();
            popup.show(requireActivity().getSupportFragmentManager(), "PopupAddItem");
        });

        return view;
    }
}