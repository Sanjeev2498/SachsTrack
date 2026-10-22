# Personal Finance Tracker Android App - Requirements Document

## Project Overview
Convert existing HTML UI design to a fully functional Android application using Java, XML layouts, and Room Database for offline personal finance tracking.

## Functional Requirements

### 1. Dashboard (Home Screen)
- **Display Summary Cards**:
  - Total Balance (Income - Expense)
  - Total Income (current month)
  - Total Expense (current month)
- **Recent Transactions**: Show last 5-10 transactions
- **Empty State**: Display when no transactions exist
- **Month Selector**: Navigate between months
- **Auto-refresh**: Update values when transactions change

### 2. Add Transaction
- **Calculator Interface**: Custom number pad for amount entry
- **Transaction Type**: Segmented control (Income/Expense/Transfer)
- **Amount Display**: Large, prominent number display
- **Account Selection**: Dropdown/dialog for account selection
- **Category Selection**: Dropdown/dialog for category selection
- **Note Field**: Optional text input for transaction notes
- **Date/Time**: Default to current, allow editing
- **Save Functionality**: Validate and save to database
- **Cancel/Back**: Return to previous screen

### 3. Transaction History
- **List Display**: RecyclerView with transaction items
- **Item Layout**: Show amount, category, account, date, note
- **Color Coding**: Green for income, red for expense
- **Edit Transaction**: Tap to edit existing transaction
- **Delete Transaction**: Swipe or long-press to delete with confirmation
- **Empty State**: Display when no transactions found
- **Filter by Month**: Show transactions for selected month

### 4. Accounts Management
- **Account List**: Display all available accounts
- **Account Balance**: Show current balance per account
- **Account Types**: Bank, Card, Cash, Savings, UPI
- **Account Icons**: Material icons for each account type
- **Add Account**: Simple form to add new account (optional)

### 5. Basic Reports/Analysis
- **Monthly Summary**: Income, Expense, Balance for current month
- **Category Breakdown**: List expenses by category with percentages
- **Simple Chart**: Basic pie chart or bar chart (optional)
- **Month Navigation**: View reports for different months

### 6. Navigation
- **Bottom Navigation**: 5 tabs (Records, Analysis, Budgets, Accounts, Categories)
- **FAB**: Floating action button to quickly add transaction
- **Back Navigation**: Proper back button handling
- **Deep Linking**: Navigate directly to specific screens

## Technical Requirements

### 1. Architecture
- **Pattern**: MVVM or Repository pattern
- **Database**: Room Database with SQLite
- **UI**: XML layouts with Material Design Components
- **Language**: Java (No Kotlin)
- **Minimum SDK**: API 21 (Android 5.0)
- **Target SDK**: API 34 (Android 14)

### 2. Database Schema
```java
// Transaction Entity
@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double amount;
    public String type; // "income" or "expense"
    public String category;
    public String account;
    public String note;
    public String date; // ISO 8601 format
    public long createdAt;
}

// Account Entity (Predefined)
@Entity(tableName = "accounts")
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String type;
    public String icon;
}

// Category Entity (Predefined)
@Entity(tableName = "categories") 
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String type; // "income" or "expense"
    public String icon;
    public String color;
}
```

### 3. Core Classes Structure
```
app/
├── database/
│   ├── AppDatabase.java
│   ├── Transaction.java
│   ├── TransactionDao.java
│   ├── Account.java
│   └── Category.java
├── ui/
│   ├── MainActivity.java
│   ├── dashboard/
│   │   └── DashboardFragment.java
│   ├── addtransaction/
│   │   └── AddTransactionFragment.java
│   ├── history/
│   │   └── HistoryFragment.java
│   ├── accounts/
│   │   └── AccountsFragment.java
│   └── reports/
│       └── ReportsFragment.java
├── adapters/
│   ├── TransactionAdapter.java
│   └── AccountAdapter.java
└── utils/
    ├── DateUtils.java
    └── CurrencyUtils.java
```

## UI/UX Requirements

### 1. Design System
- **Theme**: Dark theme matching HTML design
- **Colors**: Use extracted color palette from HTML UI
- **Typography**: Roboto font (Android default) with similar sizing
- **Icons**: Material Design icons
- **Cards**: Rounded corners, elevation, consistent spacing

### 2. Layout Requirements
- **Responsive**: Support phones and tablets
- **Accessibility**: Proper content descriptions, touch targets
- **Loading States**: Show progress indicators during operations
- **Error Handling**: User-friendly error messages
- **Validation**: Input validation with clear feedback

### 3. Animation & Interactions
- **FAB Animation**: Scale and rotation on tap
- **List Animations**: Smooth item insertions/deletions
- **Navigation Transitions**: Standard Android transitions
- **Calculator Feedback**: Visual/haptic feedback on number entry

## Quality Requirements

### 1. Performance
- **Database Operations**: Async operations using Room
- **UI Responsiveness**: No ANR (Application Not Responding)
- **Memory Management**: Proper lifecycle handling
- **Battery Optimization**: Efficient background operations

### 2. Data Integrity
- **Validation**: Input validation before database operations
- **Calculations**: Accurate financial calculations (use BigDecimal)
- **Backup**: Local database backup capability
- **Data Migration**: Handle database schema changes

### 3. User Experience
- **Offline First**: App works without internet connection
- **Data Persistence**: All data saved locally
- **Quick Entry**: Fast transaction entry workflow
- **Visual Feedback**: Clear success/error states

## Constraints

### 1. Technology Constraints
- **No Internet Required**: Fully offline application
- **No Authentication**: No user accounts or login
- **No Cloud Sync**: Local storage only
- **Java Only**: No Kotlin or other languages
- **XML Layouts**: No Jetpack Compose

### 2. Scope Limitations
- **Single User**: App for one user per device
- **Basic Features**: Core functionality only for college project
- **No Advanced Analytics**: Simple reports and charts only
- **No Multi-Currency**: Single currency (INR/USD)
- **No Recurring Transactions**: Manual entry only

## Success Criteria
1. App compiles and runs without errors
2. All core features (Dashboard, Add Transaction, History) work
3. Data persists correctly in Room Database
4. UI matches the original HTML design closely
5. Navigation works smoothly between screens
6. Input validation prevents data corruption
7. Financial calculations are accurate
8. App handles empty states gracefully