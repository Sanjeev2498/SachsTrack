package com.example.sachstrack.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sachstrack.databinding.ItemCategoryNoBudgetBinding;

import java.util.ArrayList;
import java.util.List;

public class SimpleCategoryAdapter extends RecyclerView.Adapter<SimpleCategoryAdapter.CategoryViewHolder> {

    private List<String> categories = new ArrayList<>();

    public void updateCategories(List<String> newCategories) {
        this.categories.clear();
        this.categories.addAll(newCategories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryNoBudgetBinding binding = ItemCategoryNoBudgetBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.binding.tvCategoryName.setText(category);
        
        // Set different icon colors for different categories  
        int[] colors = {
            0xFFE91E63, // Pink for Beauty
            0xFF424242, // Gray for Bills  
            0xFF4CAF50, // Green for Donation
            0xFF2196F3, // Blue for Education
            0xFF9C27B0  // Purple for Entertainment
        };
        
        if (position < colors.length) {
            holder.binding.cardCategoryIcon.setCardBackgroundColor(colors[position]);
        }
        
        holder.binding.btnSetBudget.setOnClickListener(v -> {
            // Handle set budget click
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryNoBudgetBinding binding;

        public CategoryViewHolder(@NonNull ItemCategoryNoBudgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}