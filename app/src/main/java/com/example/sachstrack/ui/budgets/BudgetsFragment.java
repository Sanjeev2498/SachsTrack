package com.example.sachstrack.ui.budgets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sachstrack.R;
import com.example.sachstrack.databinding.FragmentBudgetsBinding;
import com.example.sachstrack.repository.BudgetRepository;
import com.example.sachstrack.ui.adapters.BudgetCategoryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BudgetsFragment extends Fragment {

    private static final String TAG = "BudgetsFragment";
    private FragmentBudgetsBinding binding;
    private BudgetCategoryAdapter budgetAdapter;
    private BudgetRepository budgetRepository;
    private Calendar currentCalendar;
    private SimpleDateFormat monthYearFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBudgetsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize calendar and repository
        currentCalendar = Calendar.getInstance();
        monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        
        try {
            budgetRepository = new BudgetRepository(requireActivity().getApplication());
            Log.d(TAG, "Budget repository initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing budget repository: " + e.getMessage(), e);
        }
        
        setupUI();
        setupRecyclerView();
        setupClickListeners();
        loadBudgetData();
    }
    
    private void setupUI() {
        updateMonthDisplay();
        
        // Set initial loading state
        binding.tvTotalBudget.setText("Loading...");
        binding.tvTotalSpent.setText("Loading...");
    }
    
    private void updateMonthDisplay() {
        String monthYear = monthYearFormat.format(currentCalendar.getTime());
        binding.tvCurrentMonth.setText(monthYear);
    }
    
    private void setupRecyclerView() {
        budgetAdapter = new BudgetCategoryAdapter();
        binding.rvBudgetCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvBudgetCategories.setAdapter(budgetAdapter);
    }
    
    private void setupClickListeners() {
        binding.btnPreviousMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateMonthDisplay();
            loadBudgetData();
        });
        
        binding.btnNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateMonthDisplay();
            loadBudgetData();
        });
    }
    
    private void loadBudgetData() {
        try {
            if (budgetRepository == null) {
                Log.w(TAG, "Budget repository is null, showing empty state");
                showEmptyState();
                return;
            }
            
            int month = currentCalendar.get(Calendar.MONTH) + 1;
            int year = currentCalendar.get(Calendar.YEAR);
            
            Log.d(TAG, "Loading budgets for: " + month + "/" + year);
            
            // Load budgets from database for current month
            budgetRepository.getBudgetsByMonth(month, year).observe(getViewLifecycleOwner(), budgets -> {
                try {
                    if (budgets != null && !budgets.isEmpty()) {
                        Log.d(TAG, "Loaded " + budgets.size() + " budgets from database");
                        
                        // Convert to adapter items
                        List<BudgetCategoryAdapter.BudgetItem> budgetItems = new ArrayList<>();
                        double totalBudget = 0.0;
                        double totalSpent = 0.0;
                        
                        for (com.example.sachstrack.database.entities.Budget budget : budgets) {
                            double remaining = budget.getRemainingAmount();
                            int progress = (int) budget.getSpentPercentage();
                            
                            budgetItems.add(new BudgetCategoryAdapter.BudgetItem(
                                budget.getCategoryName(),
                                String.format(Locale.getDefault(), "Spent: ₹%.2f", budget.getSpent()),
                                String.format(Locale.getDefault(), "Remaining: ₹%.2f", remaining),
                                String.format(Locale.getDefault(), "Limit: ₹%.2f", budget.getAmount()),
                                progress,
                                getCategoryIcon(budget.getCategoryName())
                            ));
                            
                            totalBudget += budget.getAmount();
                            totalSpent += budget.getSpent();
                        }
                        
                        // Update summary
                        binding.tvTotalBudget.setText(String.format(Locale.getDefault(), "₹%.2f", totalBudget));
                        binding.tvTotalSpent.setText(String.format(Locale.getDefault(), "₹%.2f", totalSpent));
                        
                        // Update adapter
                        budgetAdapter.updateBudgetItems(budgetItems);
                        showBudgets();
                        
                    } else {
                        Log.d(TAG, "No budgets found for current month");
                        showEmptyState();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing budget data: " + e.getMessage(), e);
                    showEmptyState();
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Error loading budget data: " + e.getMessage(), e);
            showEmptyState();
        }
    }
    
    private int getCategoryIcon(String categoryName) {
        // Map category names to icons
        if (categoryName == null) return R.drawable.ic_receipt_24;
        
        String normalizedName = categoryName.toLowerCase();
        if (normalizedName.contains("food")) {
            return R.drawable.ic_restaurant_24;
        } else if (normalizedName.contains("transport")) {
            return R.drawable.ic_directions_car_24;
        } else if (normalizedName.contains("housing") || normalizedName.contains("home")) {
            return R.drawable.ic_receipt_24;
        } else if (normalizedName.contains("shopping")) {
            return R.drawable.ic_shopping_bag_24;
        } else {
            return R.drawable.ic_receipt_24;
        }
    }
    
    private void showEmptyState() {
        // Show empty state UI
        binding.tvTotalBudget.setText("₹0.00");
        binding.tvTotalSpent.setText("₹0.00");
        budgetAdapter.updateBudgetItems(new ArrayList<>());
    }
    
    private void showBudgets() {
        // Budgets are visible by default
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}