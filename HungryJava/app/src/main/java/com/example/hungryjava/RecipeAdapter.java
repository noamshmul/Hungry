package com.example.hungryjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

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
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(item.getImageName(), "drawable", holder.itemView.getContext().getPackageName());

        if (imageResId != 0) // Ensure image exists
        {
            holder.imageView.setImageResource(imageResId);
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background); // Fallback image
        }

        holder.captionText.setText(item.getCaption());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + item.getCaption(), Toast.LENGTH_SHORT).show();
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

