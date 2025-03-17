package com.example.hungryjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// ItemAdapter.java
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String> items;  // List to hold the data (strings)
    private Context context;

    // Constructor
    public ItemAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to the view (called by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = items.get(position);
        String[] parts = item.split("\n");
        
        // Set the ingredient name
        holder.nameTextView.setText(parts[0]);
        
        // Set the amount
        if (parts.length > 1) {
            holder.amountTextView.setText(parts[1]);
        }
    }

    // Return the size of the data list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder class to hold references to item views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_ingredient_name);
            amountTextView = itemView.findViewById(R.id.text_amount);
        }
    }
}
