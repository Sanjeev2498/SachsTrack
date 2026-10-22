package com.example.sachstrack.ui.categories;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sachstrack.R;
import com.example.sachstrack.databinding.FragmentCategoriesNewBinding;
import com.example.sachstrack.model.CategoryItem;
import com.example.sachstrack.ui.adapters.CategoryItemAdapter;
import com.example.sachstrack.ui.addtransaction.AddTransactionActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoryItemAdapter.OnCategoryClickListener {

    private FragmentCategoriesNewBinding binding;
    private CategoryItemAdapter adapter;
    private List<CategoryItem> allCategories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoriesNewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupClickListeners();
        loadSampleData();
        updateUI();
    }

    private void setupRecyclerView() {
        adapter = new CategoryItemAdapter();
        adapter.setOnCategoryClickListener(this);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategories.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Menu button
        binding.btnMenu.setOnClickListener(v -> {
            // Open navigation drawer
            if (getActivity() instanceof com.example.sachstrack.MainActivity) {
                com.example.sachstrack.MainActivity mainActivity = 
                    (com.example.sachstrack.MainActivity) getActivity();
                if (mainActivity != null) {
                    androidx.drawerlayout.widget.DrawerLayout drawerLayout = 
                        mainActivity.findViewById(com.example.sachstrack.R.id.drawer_layout);
                    if (drawerLayout != null) {
                        drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
                    }
                }
            }
        });

        // Search button
        binding.btnSearch.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Search functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        // Add Category button
        binding.btnAddCategory.setOnClickListener(v -> {
            showAddCategoryDialog();
        });

        // FAB Add Transaction
        binding.fabAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            startActivity(intent);
        });
    }

    private void loadSampleData() {
        allCategories = new ArrayList<>();
        
        // Default expense categories
        allCategories.add(new CategoryItem("Food & Dining", R.drawable.ic_restaurant_24, "expense"));
        allCategories.add(new CategoryItem("Transport", R.drawable.ic_directions_car_24, "expense"));
        allCategories.add(new CategoryItem("Housing", R.drawable.ic_home_24, "expense"));
        allCategories.add(new CategoryItem("Shopping", R.drawable.ic_shopping_bag_24, "expense"));
        allCategories.add(new CategoryItem("Entertainment", R.drawable.ic_movie_24, "expense"));
        
        // Default income categories
        allCategories.add(new CategoryItem("Salary", R.drawable.ic_account_balance_wallet_24, "income"));
        allCategories.add(new CategoryItem("Freelance", R.drawable.ic_work_24, "income"));
        allCategories.add(new CategoryItem("Investment", R.drawable.ic_trending_up_24, "income"));

        adapter.updateCategories(allCategories);
    }

    private void updateUI() {
        // Calculate totals (sample data)
        binding.tvTotalBalance.setText("Rs 23,322");
        binding.tvTotalExpense.setText("Rs 23,223");
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_category, null);
        
        EditText etCategoryName = dialogView.findViewById(R.id.et_category_name);
        Spinner spinnerType = dialogView.findViewById(R.id.spinner_category_type);
        Spinner spinnerIcon = dialogView.findViewById(R.id.spinner_category_icon);

        // Setup type spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, 
            new String[]{"Expense", "Income"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // Setup icon spinner (simplified - using category names)
        ArrayAdapter<String> iconAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, 
            new String[]{"Restaurant", "Car", "Home", "Shopping", "Entertainment", "Work", "Trending Up"});
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIcon.setAdapter(iconAdapter);

        builder.setView(dialogView)
               .setTitle("Add New Category")
               .setPositiveButton("Add", (dialog, which) -> {
                   String categoryName = etCategoryName.getText().toString().trim();
                   if (!categoryName.isEmpty()) {
                       String type = spinnerType.getSelectedItem().toString().toLowerCase();
                       int iconResource = getIconResource(spinnerIcon.getSelectedItemPosition());
                       
                       CategoryItem newCategory = new CategoryItem(categoryName, iconResource, type);
                       allCategories.add(newCategory);
                       adapter.updateCategories(allCategories);
                       
                       Toast.makeText(getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(getContext(), "Please enter category name", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private int getIconResource(int position) {
        switch (position) {
            case 0: return R.drawable.ic_restaurant_24;
            case 1: return R.drawable.ic_directions_car_24;
            case 2: return R.drawable.ic_home_24;
            case 3: return R.drawable.ic_shopping_bag_24;
            case 4: return R.drawable.ic_movie_24;
            case 5: return R.drawable.ic_work_24;
            case 6: return R.drawable.ic_trending_up_24;
            default: return R.drawable.ic_category_24;
        }
    }

    @Override
    public void onCategoryClick(CategoryItem category, int position) {
        Toast.makeText(getContext(), "Selected: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryMenuClick(CategoryItem category, int position) {
        showCategoryOptionsDialog(category, position);
    }

    private void showCategoryOptionsDialog(CategoryItem category, int position) {
        String[] options = {"Edit", "Delete"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(category.getName())
               .setItems(options, (dialog, which) -> {
                   switch (which) {
                       case 0: // Edit
                           showEditCategoryDialog(category, position);
                           break;
                       case 1: // Delete
                           showDeleteConfirmDialog(category, position);
                           break;
                   }
               })
               .show();
    }

    private void showEditCategoryDialog(CategoryItem category, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_category, null);
        
        EditText etCategoryName = dialogView.findViewById(R.id.et_category_name);
        Spinner spinnerType = dialogView.findViewById(R.id.spinner_category_type);
        
        etCategoryName.setText(category.getName());
        
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, 
            new String[]{"Expense", "Income"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setSelection("income".equals(category.getType()) ? 1 : 0);

        builder.setView(dialogView)
               .setTitle("Edit Category")
               .setPositiveButton("Update", (dialog, which) -> {
                   String newName = etCategoryName.getText().toString().trim();
                   if (!newName.isEmpty()) {
                       category.setName(newName);
                       category.setType(spinnerType.getSelectedItem().toString().toLowerCase());
                       adapter.notifyItemChanged(position);
                       Toast.makeText(getContext(), "Category updated", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void showDeleteConfirmDialog(CategoryItem category, int position) {
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Category")
            .setMessage("Are you sure you want to delete '" + category.getName() + "'?")
            .setPositiveButton("Delete", (dialog, which) -> {
                allCategories.remove(position);
                adapter.updateCategories(allCategories);
                Toast.makeText(getContext(), "Category deleted", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    public void refreshData() {
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}