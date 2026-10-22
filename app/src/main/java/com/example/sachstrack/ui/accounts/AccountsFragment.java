package com.example.sachstrack.ui.accounts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sachstrack.database.entities.Account;
import com.example.sachstrack.ui.adapters.AccountAdapter;

import java.util.List;

public class AccountsFragment extends Fragment {
    
    private static final String TAG = "AccountsFragment";
    private AccountsViewModel viewModel;
    private AccountAdapter accountAdapter;
    private RecyclerView safeRecyclerView;
    private TextView totalBalanceTextView; // Add reference for total balance
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called - building enhanced safe layout");
        try {
            // Create a better-looking layout
            LinearLayout mainLayout = new LinearLayout(getContext());
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            mainLayout.setPadding(48, 48, 48, 48);
            mainLayout.setBackgroundColor(0xFF1A1A1A); // Dark background
            
            // Title with better styling
            TextView titleText = new TextView(getContext());
            titleText.setText("Accounts");
            titleText.setTextSize(32);
            titleText.setTextColor(0xFFFFFFFF); // White text
            titleText.setPadding(0, 0, 0, 48);
            titleText.setTypeface(null, android.graphics.Typeface.BOLD);
            mainLayout.addView(titleText);
            
            // Balance Card Container
            LinearLayout balanceCard = new LinearLayout(getContext());
            balanceCard.setOrientation(LinearLayout.VERTICAL);
            balanceCard.setPadding(32, 32, 32, 32);
            balanceCard.setBackgroundColor(0xFF2A2A2A); // Card background
            
            // Balance Label
            TextView balanceLabel = new TextView(getContext());
            balanceLabel.setText("Total Balance");
            balanceLabel.setTextSize(16);
            balanceLabel.setTextColor(0xFF9E9E9E); // Gray text
            balanceLabel.setPadding(0, 0, 0, 16);
            balanceCard.addView(balanceLabel);
            
            // Balance Amount with better styling
            TextView balanceAmount = new TextView(getContext());
            balanceAmount.setId(View.generateViewId());
            this.totalBalanceTextView = balanceAmount; // Store reference
            balanceAmount.setText("Rs 0.00");  // Start with 0, will be updated by ViewModel
            balanceAmount.setTextSize(48);
            balanceAmount.setTextColor(0xFF4EDEA3); // Green color
            balanceAmount.setTypeface(null, android.graphics.Typeface.BOLD);
            balanceCard.addView(balanceAmount);
            
            mainLayout.addView(balanceCard);
            
            // Add some spacing
            View spacer = new View(getContext());
            spacer.setLayoutParams(new LinearLayout.LayoutParams(0, 48));
            mainLayout.addView(spacer);
            
            // Accounts List Label
            TextView listLabel = new TextView(getContext());
            listLabel.setText("Your Accounts");
            listLabel.setTextSize(20);
            listLabel.setTextColor(0xFFFFFFFF);
            listLabel.setTypeface(null, android.graphics.Typeface.BOLD);
            listLabel.setPadding(0, 0, 0, 24);
            mainLayout.addView(listLabel);
            
            // RecyclerView for accounts list
            RecyclerView recyclerView = new RecyclerView(getContext());
            recyclerView.setId(View.generateViewId());
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            recyclerView.setBackgroundColor(0xFF2A2A2A);
            recyclerView.setPadding(16, 16, 16, 16);
            mainLayout.addView(recyclerView);
            
            // Store reference for later use
            this.safeRecyclerView = recyclerView;
            
            // Add Account FAB Button
            com.google.android.material.floatingactionbutton.FloatingActionButton fabAddAccount = 
                new com.google.android.material.floatingactionbutton.FloatingActionButton(getContext());
            fabAddAccount.setImageResource(android.R.drawable.ic_input_add);
            fabAddAccount.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4EDEA3));
            fabAddAccount.setSize(com.google.android.material.floatingactionbutton.FloatingActionButton.SIZE_NORMAL);
            
            // Position FAB at bottom right
            android.widget.FrameLayout.LayoutParams fabParams = new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
            );
            fabParams.gravity = android.view.Gravity.BOTTOM | android.view.Gravity.END;
            fabParams.setMargins(16, 16, 16, 80); // 80dp bottom margin for nav bar
            fabAddAccount.setLayoutParams(fabParams);
            
            fabAddAccount.setOnClickListener(v -> {
                showAddAccountDialog();
            });
            
            // Wrap in FrameLayout to position FAB
            android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(getContext());
            frameLayout.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
            ));
            frameLayout.addView(mainLayout);
            frameLayout.addView(fabAddAccount);
            
            Log.d(TAG, "Enhanced safe layout created successfully");
            return frameLayout;
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating enhanced layout: " + e.getMessage(), e);
            // Ultimate fallback
            TextView errorText = new TextView(getContext());
            errorText.setText("Error loading accounts: " + e.getMessage());
            errorText.setTextColor(0xFFFFFFFF);
            return errorText;
        }
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called");
        
        try {
            // Initialize ViewModel
            viewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
            Log.d(TAG, "ViewModel created");
            
            // Setup RecyclerView
            setupSafeRecyclerView();
            Log.d(TAG, "RecyclerView setup done");
            
            // Observe data
            observeSafeViewModel();
            Log.d(TAG, "Observers setup done");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onViewCreated: " + e.getMessage(), e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void setupSafeRecyclerView() {
        try {
            if (safeRecyclerView != null) {
                accountAdapter = new AccountAdapter();
                safeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                safeRecyclerView.setAdapter(accountAdapter);
                
                // Set item click listener
                accountAdapter.setOnItemClickListener(account -> {
                    if (account != null && getContext() != null) {
                        Toast.makeText(getContext(), "Clicked: " + account.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                
                Log.d(TAG, "Safe RecyclerView setup completed");
                
                // DO NOT test with dummy data - load real data
                // testRecyclerViewWithDummyData() removed
                
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up safe RecyclerView: " + e.getMessage(), e);
        }
    }
    
    private void testRecyclerViewWithDummyData() {
        try {
            // Create dummy accounts to test if RecyclerView displays anything
            java.util.List<com.example.sachstrack.database.entities.Account> dummyAccounts = new java.util.ArrayList<>();
            
            com.example.sachstrack.database.entities.Account dummyAccount1 = 
                new com.example.sachstrack.database.entities.Account("Test Bank", "bank", "account_balance", 25000.0);
            dummyAccount1.setId(1);
            
            com.example.sachstrack.database.entities.Account dummyAccount2 = 
                new com.example.sachstrack.database.entities.Account("Test Cash", "cash", "payments", 5000.0);
            dummyAccount2.setId(2);
            
            dummyAccounts.add(dummyAccount1);
            dummyAccounts.add(dummyAccount2);
            
            // Submit dummy data first to test RecyclerView
            if (accountAdapter != null) {
                accountAdapter.submitList(dummyAccounts);
                Log.d(TAG, "Dummy data submitted to RecyclerView for testing");
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Testing RecyclerView with dummy data", Toast.LENGTH_SHORT).show();
                }
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error testing RecyclerView with dummy data: " + e.getMessage(), e);
        }
    }
    
    private void observeSafeViewModel() {
        try {
            // Observe accounts list with detailed logging
            viewModel.getAllActiveAccounts().observe(getViewLifecycleOwner(), accounts -> {
                try {
                    Log.d(TAG, "=== ACCOUNTS RECEIVED ===");
                    if (accounts == null) {
                        Log.d(TAG, "Accounts list is NULL");
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "No accounts found (NULL)", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                    
                    Log.d(TAG, "Accounts count: " + accounts.size());
                    for (int i = 0; i < accounts.size(); i++) {
                        com.example.sachstrack.database.entities.Account account = accounts.get(i);
                        Log.d(TAG, "Account " + i + ": " + 
                            (account != null ? account.getName() + " = Rs " + account.getBalance() : "NULL"));
                    }
                    
                    if (accounts.isEmpty()) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "No accounts found - Add accounts to get started", Toast.LENGTH_LONG).show();
                        }
                        // Clear adapter and show empty state
                        if (accountAdapter != null) {
                            accountAdapter.submitList(new java.util.ArrayList<>());
                        }
                        return;
                    }
                    
                    // Submit to adapter
                    if (accountAdapter != null) {
                        accountAdapter.submitList(accounts);
                        
                        // Calculate and update total balance
                        double totalBalance = 0.0;
                        for (Account account : accounts) {
                            if (account != null) {
                                totalBalance += account.getBalance();
                            }
                        }
                        
                        // Update on main thread
                        final double finalBalance = totalBalance;
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> updateTotalBalance(finalBalance));
                        }
                        
                        Log.d(TAG, "Accounts submitted, total: " + totalBalance);
                    } else {
                        Log.e(TAG, "AccountAdapter is NULL!");
                    }
                    
                } catch (Exception e) {
                    Log.e(TAG, "Error processing accounts list: " + e.getMessage(), e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error processing accounts: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            
            Log.d(TAG, "Safe ViewModel observers setup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up safe ViewModel observers: " + e.getMessage(), e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Observer setup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    
    // Method called from MainActivity when returning from other screens
    public void refreshData() {
        try {
            if (viewModel != null) {
                viewModel.refreshData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error refreshing data: " + e.getMessage(), e);
        }
    }
    
    private void updateTotalBalance(double balance) {
        try {
            if (totalBalanceTextView != null) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        totalBalanceTextView.setText(String.format("Rs %.2f", balance));
                    });
                } else {
                    totalBalanceTextView.setText(String.format("Rs %.2f", balance));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating total balance: " + e.getMessage(), e);
        }
    }
    
    private void showAddAccountDialog() {
        if (getActivity() == null) return;
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Account");
        
        // Create input fields
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        
        final android.widget.EditText inputName = new android.widget.EditText(getActivity());
        inputName.setHint("Account Name (e.g., My Bank)");
        layout.addView(inputName);
        
        final android.widget.EditText inputBalance = new android.widget.EditText(getActivity());
        inputBalance.setHint("Initial Balance");
        inputBalance.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputBalance);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String balanceStr = inputBalance.getText().toString().trim();
            
            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter account name", Toast.LENGTH_SHORT).show();
                return;
            }
            
            double balance = 0.0;
            if (!balanceStr.isEmpty()) {
                try {
                    balance = Double.parseDouble(balanceStr);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Invalid balance", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            
            // Create and save account
            final double finalBalance = balance;
            com.example.sachstrack.database.AppDatabase.databaseWriteExecutor.execute(() -> {
                try {
                    com.example.sachstrack.database.AppDatabase database = 
                        com.example.sachstrack.database.AppDatabase.getDatabase(getActivity());
                    
                    com.example.sachstrack.database.entities.Account newAccount = 
                        new com.example.sachstrack.database.entities.Account(name, "custom", "account_balance", finalBalance);
                    
                    database.accountDao().insertAccount(newAccount);
                    
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Account added!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error adding account: " + e.getMessage(), e);
                }
            });
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        safeRecyclerView = null;
    }
    
    private void insertSampleDataForTesting() {
        Log.d(TAG, "Inserting sample data for testing...");
        try {
            // Use database executor to insert sample data
            com.example.sachstrack.database.AppDatabase.databaseWriteExecutor.execute(() -> {
                try {
                    // Get database instance
                    com.example.sachstrack.database.AppDatabase database = 
                        com.example.sachstrack.database.AppDatabase.getDatabase(getContext());
                    
                    // Insert sample transactions for last 30 days
                    com.example.sachstrack.database.entities.Transaction[] sampleTransactions = {
                        // Income transactions
                        new com.example.sachstrack.database.entities.Transaction(50000.0, "income", "Salary", "Bank", "Monthly salary payment", "2026-09-01", "09:00"),
                        new com.example.sachstrack.database.entities.Transaction(5000.0, "income", "Freelance", "UPI", "Web development project", "2026-09-05", "14:30"),
                        new com.example.sachstrack.database.entities.Transaction(2000.0, "income", "Investment", "Bank", "Dividend payment", "2026-09-10", "11:15"),
                        
                        // Food & Dining expenses
                        new com.example.sachstrack.database.entities.Transaction(1200.0, "expense", "Food & Dining", "Cash", "Grocery shopping at supermarket", "2026-09-02", "18:30"),
                        new com.example.sachstrack.database.entities.Transaction(450.0, "expense", "Food & Dining", "Card", "Dinner at restaurant", "2026-09-03", "20:15"),
                        new com.example.sachstrack.database.entities.Transaction(320.0, "expense", "Food & Dining", "UPI", "Food delivery order", "2026-09-07", "13:45"),
                        new com.example.sachstrack.database.entities.Transaction(800.0, "expense", "Food & Dining", "Cash", "Weekly groceries", "2026-09-09", "17:20"),
                        
                        // Transport expenses
                        new com.example.sachstrack.database.entities.Transaction(150.0, "expense", "Transport", "UPI", "Bus fare for week", "2026-09-02", "08:15"),
                        new com.example.sachstrack.database.entities.Transaction(350.0, "expense", "Transport", "Card", "Fuel for car", "2026-09-04", "16:45"),
                        new com.example.sachstrack.database.entities.Transaction(80.0, "expense", "Transport", "Cash", "Auto rickshaw", "2026-09-06", "19:30"),
                        new com.example.sachstrack.database.entities.Transaction(200.0, "expense", "Transport", "UPI", "Metro card recharge", "2026-09-08", "07:50"),
                        
                        // Housing expenses
                        new com.example.sachstrack.database.entities.Transaction(15000.0, "expense", "Housing", "Bank", "Monthly rent payment", "2026-09-01", "10:00"),
                        new com.example.sachstrack.database.entities.Transaction(2500.0, "expense", "Housing", "Card", "Electricity bill", "2026-09-05", "12:30"),
                        new com.example.sachstrack.database.entities.Transaction(800.0, "expense", "Housing", "UPI", "Internet bill", "2026-09-07", "15:20"),
                        
                        // Shopping expenses
                        new com.example.sachstrack.database.entities.Transaction(1800.0, "expense", "Shopping", "Card", "Clothes shopping", "2026-09-03", "16:00"),
                        new com.example.sachstrack.database.entities.Transaction(650.0, "expense", "Shopping", "UPI", "Online electronics purchase", "2026-09-06", "21:15"),
                        new com.example.sachstrack.database.entities.Transaction(400.0, "expense", "Shopping", "Cash", "Books and stationery", "2026-09-09", "14:45"),
                        
                        // Entertainment expenses
                        new com.example.sachstrack.database.entities.Transaction(600.0, "expense", "Entertainment", "Card", "Movie tickets and snacks", "2026-09-03", "19:00"),
                        new com.example.sachstrack.database.entities.Transaction(1200.0, "expense", "Entertainment", "UPI", "Concert tickets", "2026-09-08", "20:30"),
                        new com.example.sachstrack.database.entities.Transaction(300.0, "expense", "Entertainment", "Cash", "Gaming arcade", "2026-09-10", "16:45"),
                        
                        // Transfer between accounts
                        new com.example.sachstrack.database.entities.Transaction(5000.0, "transfer", "Transfer", "Bank", "Transfer to savings account", "2026-09-05", "11:00"),
                        new com.example.sachstrack.database.entities.Transaction(2000.0, "transfer", "Transfer", "Savings", "Emergency fund transfer", "2026-09-07", "09:30")
                    };
                    
                    // Insert all transactions
                    for (com.example.sachstrack.database.entities.Transaction transaction : sampleTransactions) {
                        database.transactionDao().insertTransaction(transaction);
                    }
                    
                    Log.d(TAG, "Sample transactions inserted successfully");
                    
                    // Update account balances to reflect transactions
                    updateAccountBalancesAfterTransactions(database);
                    
                } catch (Exception e) {
                    Log.e(TAG, "Error inserting sample data: " + e.getMessage(), e);
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up sample data insertion: " + e.getMessage(), e);
        }
    }
    
    private void updateAccountBalancesAfterTransactions(com.example.sachstrack.database.AppDatabase database) {
        try {
            // Update account balances to reflect realistic amounts after transactions
            database.accountDao().updateAccountBalance(1, 45000.0);  // Bank: 50k salary - 15k rent - transfers + dividend
            database.accountDao().updateAccountBalance(2, -2800.0);  // Card: expenses for fuel, electricity, shopping, entertainment  
            database.accountDao().updateAccountBalance(3, 1500.0);   // Cash: reduced from groceries and other cash expenses
            database.accountDao().updateAccountBalance(4, 17000.0);  // Savings: increased from transfers
            database.accountDao().updateAccountBalance(5, 4300.0);   // UPI: freelance income - various UPI expenses
            
            Log.d(TAG, "Account balances updated successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error updating account balances: " + e.getMessage(), e);
        }
    }
    
    private void createDefaultAccountsIfEmpty() {
        Log.d(TAG, "Creating default accounts...");
        try {
            com.example.sachstrack.database.AppDatabase.databaseWriteExecutor.execute(() -> {
                try {
                    // Get database instance
                    com.example.sachstrack.database.AppDatabase database = 
                        com.example.sachstrack.database.AppDatabase.getDatabase(getContext());
                    
                    // Create default accounts with balances
                    com.example.sachstrack.database.entities.Account[] defaultAccounts = {
                        new com.example.sachstrack.database.entities.Account("Bank", "bank", "account_balance", 45000.0),
                        new com.example.sachstrack.database.entities.Account("Card", "card", "credit_card", -2800.0),
                        new com.example.sachstrack.database.entities.Account("Cash", "cash", "payments", 1500.0),
                        new com.example.sachstrack.database.entities.Account("Savings", "savings", "savings", 17000.0),
                        new com.example.sachstrack.database.entities.Account("UPI", "upi", "qr_code_scanner", 4300.0)
                    };
                    
                    // Insert accounts
                    for (com.example.sachstrack.database.entities.Account account : defaultAccounts) {
                        database.accountDao().insertAccount(account);
                    }
                    
                    Log.d(TAG, "Default accounts created successfully");
                    
                } catch (Exception e) {
                    Log.e(TAG, "Error creating default accounts: " + e.getMessage(), e);
                }
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Error setting up default accounts creation: " + e.getMessage(), e);
        }
    }
}