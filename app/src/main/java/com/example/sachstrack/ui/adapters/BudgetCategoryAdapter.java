package com.example.sachstrack.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sachstrack.databinding.ItemBudgetCategoryBinding;
import com.example.sachstrack.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetCategoryAdapter extends RecyclerView.Adapter<BudgetCategoryAdapter.BudgetViewHolder> {

    public static class BudgetItem {
        public String categoryName;
        public String spentAmount;
        public String remainingAmount;
        public String limitAmount;
        public int progress;
        public int iconResource;

        public BudgetItem(String categoryName, String spentAmount, String remainingAmount, 
                         String limitAmount, int progress, int iconResource) {
            this.categoryName = categoryName;
            this.spentAmount = spentAmount;
            this.remainingAmount = remainingAmount;
            this.limitAmount = limitAmount;
            this.progress = progress;
            this.iconResource = iconResource;
        }
    }

    private List<BudgetItem> budgetItems = new ArrayList<>();

    public void updateBudgetItems(List<BudgetItem> newItems) {
        this.budgetItems.clear();
        this.budgetItems.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetCategoryBinding binding = ItemBudgetCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BudgetViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetItem item = budgetItems.get(position);
        
        holder.binding.tvCategoryName.setText(item.categoryName);
        holder.binding.tvSpentAmount.setText(item.spentAmount);
        holder.binding.tvRemainingAmount.setText(item.remainingAmount);
        holder.binding.tvLimitAmount.setText(item.limitAmount);
        holder.binding.progressBudget.setProgress(item.progress);
        holder.binding.ivCategoryIcon.setImageResource(item.iconResource);
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        private final ItemBudgetCategoryBinding binding;

        public BudgetViewHolder(@NonNull ItemBudgetCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}