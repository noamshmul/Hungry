package com.example.hungryjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

// ItemAdapter.java
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;  // List to hold the data (strings)
    private Context context;

    // Constructor
    public ItemAdapter(Context context, List<Item> items) {
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
        Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.amount.setText(String.valueOf(item.getQuantity()));// Set the data on the TextView
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

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_ingredient_name);
            amount = itemView.findViewById(R.id.text_amount);
            add = itemView.findViewById(R.id.btn_plus);
            remove = itemView.findViewById(R.id.btn_minus);

        }

    }
}
