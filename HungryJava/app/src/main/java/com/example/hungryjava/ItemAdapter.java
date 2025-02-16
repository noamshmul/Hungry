package com.example.hungryjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// ItemAdapter.java
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<String> items;  // List to hold the data (strings)
    private Context context;

    // Constructor
    public ItemAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
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
        String item = items.get(position);
        holder.textView.setText(item);  // Set the data on the TextView
    }

    // Return the size of the data list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class to hold references to item views
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);
        }
    }
}
