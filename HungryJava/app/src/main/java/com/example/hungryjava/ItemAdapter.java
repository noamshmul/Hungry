package com.example.hungryjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// ItemAdapter.java
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;  // List to hold the data (strings)
    private Context context;

    static Map<String, Long> ids = new HashMap<>();

    // Constructor
    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);
        Call<Map<String, Object>> call = apiService.getIngredients();

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> responseBody = response.body();
                    ArrayList<Map<String, Object>> ingredientsList = (ArrayList<Map<String, Object>>) responseBody.get("ingredients");
                    if (ingredientsList != null) {
                        for (Map<String, Object> ingredient : ingredientsList) {
                            String name = (String) ingredient.get("name");
                            if (name != null) {
                                ids.put(name, Math.round((double)ingredient.get("id")));
                            }
                        }
                    } else {
                        //Log.e(TAG, "Ingredients list is null in response");
                        //Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Log.e(TAG, "Error: " + response.message());
                    //Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                //Log.e(TAG, "Failure: " + t.getMessage());
                //Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(view);
    }

    // Bind the data to the view (called by the layout manager)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.amount.setText(String.valueOf((int)item.getQuantity()));// Convert to int to remove decimal points
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        holder.add.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition(); // Get position
            if (clickedPosition != RecyclerView.NO_POSITION) {
                PopupChangeItem popup = PopupChangeItem.newInstance(false, clickedPosition);
                popup.show(fragmentManager, "PopupChangeItem");
            }
        });
        holder.remove.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition(); // Get position
            if (clickedPosition != RecyclerView.NO_POSITION) {
                PopupChangeItem popup = PopupChangeItem.newInstance(true, clickedPosition);
                popup.show(fragmentManager, "PopupChangeItem");
            }
        });

        loadImage(Long.toString(ids.get(item.getName())), holder.itemImage);
    }

    // Return the size of the data list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class to hold references to item views
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;

        ImageButton add;
        ImageButton remove;

        ImageView itemImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_ingredient_name);
            amount = itemView.findViewById(R.id.text_amount);
            add = itemView.findViewById(R.id.btn_plus);
            remove = itemView.findViewById(R.id.btn_minus);
            itemImage = itemView.findViewById(R.id.item_image);

        }

    }
    private void loadImage(String imageUrl, ImageView xmlObject) {

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(null, null, false);
        FastApiService apiService = retrofit.create(FastApiService.class);
        String imageId = imageUrl + ".jpg";
        Call<ResponseBody> call = apiService.get_ingredient_image(imageId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    xmlObject.setImageBitmap(bitmap);
                } else {
                    //Log.e(TAG, "Failed to load image: " + (response.errorBody() != null ? response.errorBody().toString() : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e(TAG, "Image load failure: " + t.getMessage());
            }
        });
    }
}
