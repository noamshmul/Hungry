package com.example.hungryjava;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungryjava.api.FastApiService;
import com.example.hungryjava.api.RetrofitClient;


import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import okhttp3.ResponseBody;

import android.content.Intent;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<RecipeItem> RecipesList;
    private final Context context;

    public RecipeAdapter(Context context, List<RecipeItem> RecipesList) {
        this.RecipesList = RecipesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipes, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeItem item = RecipesList.get(position);

        SingleRecipeScreen.loadImage(item.getImageName(), holder.imageView);

        holder.captionText.setText(item.getCaption());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SingleRecipeScreen.class);
            intent.putExtra("recipeName", item.getCaption()); // Pass the recipe name
            context.startActivity(intent);
        });

    }



    @Override
    public int getItemCount() {
        return RecipesList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView captionText;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            captionText = itemView.findViewById(R.id.captionText);
        }
    }

}




