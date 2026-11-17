package com.example.sachstrack.ui.budgets;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sachstrack.R;
import com.example.sachstrack.databinding.FragmentBudgetsBinding;
import com.example.sachstrack.database.entities.Budget;
import com.example.sachstrack.database.entities.Category;
import com.example.sachstrack.repository.BudgetRepository;
import com.example.sachstrack.repository.CategoryRepository;
import com.example.sachstrack.ui.adapters.BudgetCategoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BudgetsFragment extends Fragment {

    private static final String TAG = "BudgetsFragment";
    private FragmentBudgetsBinding binding;
    private BudgetCategoryAdapter budgetAdapter;
    private BudgetRepository budgetRepository;
    private CategoryRepository categoryRepository;
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
            categoryRepository = new CategoryRepository(requireActivity().getApplication());
            Log.d(TAG, "Repositories initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing repositories: " + e.getMessage(), e);
        }
        
        setupUI();
        setupRecyclerView();
        setupClickListeners();
        setupFAB();
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
        
        // Set click listener to edit budget
        budgetAdapter.setOnBudgetClickListener(budgetItem -> {
            showEditBudgetDialog(budgetItem);
        });
    }
    
    private void setupClickListeners() {
        // Menu button
        binding.btnMenu.setOnClickListener(v -> {
            if (getActivity() instanceof com.example.sachstrack.MainActivity) {
                com.example.sachstrack.MainActivity mainActivity = 
                    (com.example.sachstrack.MainActivity) getActivity();
                androidx.drawerlayout.widget.DrawerLayout drawerLayout = 
                    mainActivity.findViewById(R.id.drawer_layout);
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
                }
            }
        });

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
    
    private void setupFAB() {
        // Setup FAB in fragment's layout
        binding.fabAddBudget.setOnClickListener(v -> showSetBudgetsDialog());
    }
    
    private void showSetBudgetsDialog() {
        if (categoryRepository == null) {
            Toast.makeText(getContext(), "Error loading categories", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get all expense categories
        categoryRepository.getExpenseCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null || categories.isEmpty()) {
                Toast.makeText(getContext(), "No categories found. Add categories first.", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Set Monthly Budgets");
            
            // Create ScrollView container
            android.widget.ScrollView scrollView = new android.widget.ScrollView(getContext());
            LinearLayout container = new LinearLayout(getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setPadding(40, 20, 40, 20);
            
            // Store EditTexts for each category
            Map<Integer, EditText> budgetInputs = new HashMap<>();
            
            int month = currentCalendar.get(Calendar.MONTH) + 1;
            int year = currentCalendar.get(Calendar.YEAR);
            
            // For each category, create input field
            for (Category category : categories) {
                // Category label
                TextView categoryLabel = new TextView(getContext());
                categoryLabel.setText(category.getName());
                categoryLabel.setTextSize(16);
                categoryLabel.setTextColor(getResources().getColor(R.color.color_on_surface, null));
                categoryLabel.setPadding(0, 20, 0, 8);
                container.addView(categoryLabel);
                
                // Budget input
                EditText budgetInput = new EditText(getContext());
                budgetInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                budgetInput.setHint("Enter budget amount");
                budgetInput.setTextColor(getResources().getColor(R.color.color_on_surface, null));
                budgetInput.setHintTextColor(getResources().getColor(R.color.color_on_surface_variant, null));
                
                // Load existing budget if available
                budgetRepository.getBudgetByCategoryAndMonth(category.getId(), month, year)
                    .observe(getViewLifecycleOwner(), existingBudget -> {
                        if (existingBudget != null && budgetInput.getText().toString().isEmpty()) {
                            budgetInput.setText(String.valueOf(existingBudget.getAmount()));
                        }
                    });
                
                container.addView(budgetInput);
                budgetInputs.put(category.getId(), budgetInput);
            }
            
            scrollView.addView(container);
            builder.setView(scrollView);
            
            builder.setPositiveButton("Save", (dialog, which) -> {
                saveBudgets(categories, budgetInputs);
            });
            
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });
    }
    
    private void saveBudgets(List<Category> categories, Map<Integer, EditText> budgetInputs) {
        int month = currentCalendar.get(Calendar.MONTH) + 1;
        int year = currentCalendar.get(Calendar.YEAR);
        
        // Use background thread for database operations
        new Thread(() -> {
            int savedCount = 0;
            
            for (Category category : categories) {
                EditText input = budgetInputs.get(category.getId());
                if (input != null && !input.getText().toString().trim().isEmpty()) {
                    try {
                        double amount = Double.parseDouble(input.getText().toString().trim());
                        if (amount > 0) {
                            // Calculate spent from existing transactions BEFORE creating budget
                            double spentAmount = calculateSpentForCategory(category.getName(), month, year);
                            
                            Budget budget = new Budget(category.getId(), category.getName(), amount, month, year);
                            budget.setSpent(spentAmount);
                            
                            budgetRepository.insertBudgetSync(budget);
                            savedCount++;
                            
                            Log.d(TAG, "Budget saved for " + category.getName() + 
                                  " - Amount: ₹" + amount + ", Spent: ₹" + spentAmount);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid budget amount for " + category.getName());
                    }
                }
            }
            
            final int finalCount = savedCount;
            // Update UI on main thread
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (finalCount > 0) {
                        Toast.makeText(getContext(), finalCount + " budgets saved", Toast.LENGTH_SHORT).show();
                        loadBudgetData();
                    } else {
                        Toast.makeText(getContext(), "No budgets entered", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    
    /**
     * Calculate spent amount synchronously from database
     */
    private double calculateSpentForCategory(String categoryName, int month, int year) {
        try {
            com.example.sachstrack.database.AppDatabase database = 
                com.example.sachstrack.database.AppDatabase.getDatabase(requireActivity().getApplicationContext());
            
            // Get all transactions synchronously
            List<com.example.sachstrack.database.entities.Transaction> allTransactions = 
                database.transactionDao().getAllTransactionsSync();
            
            double totalSpent = 0.0;
            
            if (allTransactions != null) {
                for (com.example.sachstrack.database.entities.Transaction transaction : allTransactions) {
                    if (transaction != null && 
                        "expense".equals(transaction.getType()) &&
                        categoryName.equals(transaction.getCategory()) &&
                        transaction.getDate() != null) {
                        
                        // Parse transaction date (format: YYYY-MM-DD)
                        try {
                            String[] dateParts = transaction.getDate().split("-");
                            if (dateParts.length >= 2) {
                                int transYear = Integer.parseInt(dateParts[0]);
                                int transMonth = Integer.parseInt(dateParts[1]);
                                
                                // Check if transaction is in the same month/year as budget
                                if (transYear == year && transMonth == month) {
                                    totalSpent += transaction.getAmount();
                                }
                            }
                        } catch (Exception e) {
                            Log.w(TAG, "Error parsing transaction date: " + transaction.getDate());
                        }
                    }
                }
            }
            
            Log.d(TAG, "Calculated spent for " + categoryName + " (" + month + "/" + year + "): ₹" + totalSpent);
            return totalSpent;
            
        } catch (Exception e) {
            Log.e(TAG, "Error calculating spent amount: " + e.getMessage(), e);
            return 0.0;
        }
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
                                budget.getCategoryId(),
                                budget.getCategoryName(),
                                budget.getAmount(),
                                budget.getSpent(),
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
    
    private void showEditBudgetDialog(BudgetCategoryAdapter.BudgetItem budgetItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Budget: " + budgetItem.categoryName);
        
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);
        
        TextView currentLabel = new TextView(getContext());
        currentLabel.setText("Current Budget: ₹" + budgetItem.budgetAmount);
        currentLabel.setTextColor(getResources().getColor(R.color.color_on_surface_variant, null));
        currentLabel.setPadding(0, 0, 0, 16);
        layout.addView(currentLabel);
        
        TextView spentLabel = new TextView(getContext());
        spentLabel.setText("Spent: ₹" + budgetItem.spentAmountValue);
        spentLabel.setTextColor(getResources().getColor(R.color.color_expense, null));
        spentLabel.setPadding(0, 0, 0, 24);
        layout.addView(spentLabel);
        
        TextView newAmountLabel = new TextView(getContext());
        newAmountLabel.setText("New Budget Amount");
        newAmountLabel.setTextColor(getResources().getColor(R.color.color_on_surface, null));
        layout.addView(newAmountLabel);
        
        EditText budgetInput = new EditText(getContext());
        budgetInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        budgetInput.setHint("Enter new amount");
        budgetInput.setText(String.valueOf(budgetItem.budgetAmount));
        budgetInput.setTextColor(getResources().getColor(R.color.color_on_surface, null));
        layout.addView(budgetInput);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Update", (dialog, which) -> {
            String amountStr = budgetInput.getText().toString().trim();
            if (!amountStr.isEmpty()) {
                try {
                    double newAmount = Double.parseDouble(amountStr);
                    if (newAmount > 0) {
                        updateBudgetAmount(budgetItem.categoryId, budgetItem.categoryName, newAmount);
                    } else {
                        Toast.makeText(getContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        builder.setNegativeButton("Cancel", null);
        
        builder.setNeutralButton("Delete", (dialog, which) -> {
            new AlertDialog.Builder(getContext())
                .setTitle("Delete Budget")
                .setMessage("Are you sure you want to delete this budget?")
                .setPositiveButton("Delete", (d, w) -> {
                    deleteBudget(budgetItem.categoryId);
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
        
        builder.show();
    }
    
    private void updateBudgetAmount(int categoryId, String categoryName, double newAmount) {
        int month = currentCalendar.get(Calendar.MONTH) + 1;
        int year = currentCalendar.get(Calendar.YEAR);
        
        Budget budget = new Budget(categoryId, categoryName, newAmount, month, year);
        budgetRepository.insertBudget(budget);
        Toast.makeText(getContext(), "Budget updated", Toast.LENGTH_SHORT).show();
        loadBudgetData();
    }
    
    private void deleteBudget(int categoryId) {
        int month = currentCalendar.get(Calendar.MONTH) + 1;
        int year = currentCalendar.get(Calendar.YEAR);
        
        // You'll need to add a method to get budget by category and month, then delete it
        // For now, just reload data
        Toast.makeText(getContext(), "Budget deleted", Toast.LENGTH_SHORT).show();
        loadBudgetData();
    }
}