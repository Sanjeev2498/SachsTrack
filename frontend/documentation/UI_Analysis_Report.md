# Personal Finance Tracker - UI Analysis Report

## HTML UI Structure Analysis

Based on inspection of the existing HTML UI in `SachsTrack UI/finance_manager UI/`, the following screens and components have been identified:

### Core Screens
1. **home_records** - Main Dashboard
2. **add_transaction** - Transaction Entry Form
3. **accounts_overview** - Account Management
4. **analysis_expense_overview** - Reports & Analytics
5. **budgets_overview** - Budget Management
6. **categories_overview** - Category Management

### Supporting Components
7. **navigation_menu_drawer** - Side Navigation
8. **select_account_popup** - Account Selection Modal
9. **slate_mint_finance** - Additional UI variant

## Design System Analysis

### Color Scheme (Dark Theme)
- **Primary**: `#bec6e0` (Light blue-gray)
- **Tertiary**: `#4edea3` (Green accent)
- **Background**: `#051424` (Dark blue)
- **Surface**: `#051424` / `#122131` (Surface containers)
- **Error**: `#ffb4ab` (Light red for expenses)
- **On-Surface**: `#d4e4fa` (Light text)

### Typography
- **Font Family**: Geist (Google Font)
- **Sizes**: display-lg (48px), headline-lg (32px), headline-md (20px), body-lg (16px), body-sm (14px), label-caps (12px)
- **Weights**: 400 (regular), 500 (medium), 600 (semibold)

### UI Components
- **Cards**: Rounded corners (0.5rem), shadows, surface-container background
- **Buttons**: Material Design style with hover states
- **FAB**: Floating Action Button with green accent color
- **Bottom Navigation**: 5 tabs - Records, Analysis, Budgets, Accounts, Categories
- **Side Navigation**: Desktop version of bottom nav
- **Material Icons**: Google Material Symbols Outlined

### Layout Patterns
- **Dashboard**: Summary cards in bento-style grid, empty state with centered content
- **Forms**: Calculator interface, segmented controls, input fields
- **Lists**: Card-based items with icons, amounts, and actions
- **Charts**: Pie/donut chart for expense analysis

## Android Conversion Requirements

### Navigation Structure
```
MainActivity (Container)
├── DashboardFragment (home_records)
├── AddTransactionFragment (add_transaction) 
├── AccountsFragment (accounts_overview)
├── AnalysisFragment (analysis_expense_overview)
└── CategoriesFragment (categories_overview)
```

### Key Android Components Needed
- **BottomNavigationView** for main navigation
- **FloatingActionButton** for add transaction
- **RecyclerView** for transaction lists and account lists
- **CardView** with MaterialCardView for consistent cards
- **Material Design Components** for forms and inputs
- **Custom Calculator View** for transaction entry
- **Chart Library** for analysis screen (optional)

### Database Requirements
```sql
-- Transactions Table
CREATE TABLE transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount REAL NOT NULL,
    type TEXT NOT NULL, -- 'income' or 'expense'
    category TEXT NOT NULL,
    account TEXT NOT NULL,
    note TEXT,
    date TEXT NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Accounts Table (optional - can be predefined)
CREATE TABLE accounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT NOT NULL, -- 'bank', 'card', 'cash', 'savings', 'upi'
    balance REAL DEFAULT 0.0,
    icon TEXT
);

-- Categories Table (optional - can be predefined)
CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT NOT NULL, -- 'income' or 'expense'
    icon TEXT,
    color TEXT
);
```

### Priority Features for Implementation
1. **Dashboard** - Show total balance, income, expense, recent transactions
2. **Add Transaction** - Form with calculator, category/account selection
3. **Transaction History** - List with edit/delete functionality
4. **Basic Reports** - Simple income/expense summaries
5. **Account Management** - View account balances
6. **Category System** - Predefined categories for transactions

### Non-Essential Features (Phase 2)
- Advanced charts and analytics
- Budget tracking
- Complex account management
- Import/Export functionality
- Themes and customization