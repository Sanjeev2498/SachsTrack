# Screen Mapping - HTML to Android

## Navigation Structure

### HTML UI Screens → Android Fragments

| HTML Screen | Android Fragment | Purpose | Priority |
|-------------|------------------|---------|----------|
| `home_records` | `DashboardFragment` | Main dashboard with summary and recent transactions | HIGH |
| `add_transaction` | `AddTransactionActivity` | Transaction entry form with calculator | HIGH |
| `accounts_overview` | `AccountsFragment` | Account management and balances | MEDIUM |
| `analysis_expense_overview` | `ReportsFragment` | Financial reports and analysis | MEDIUM |
| `categories_overview` | `CategoriesFragment` | Category management | LOW |
| `budgets_overview` | `BudgetsFragment` | Budget tracking (Phase 2) | LOW |
| `navigation_menu_drawer` | `MainActivity` navigation | Built into main activity | HIGH |
| `select_account_popup` | `AccountSelectionDialog` | Dialog for account selection | HIGH |

## Screen Details

### 1. DashboardFragment (home_records)
**HTML Elements → Android Components:**
- Top bar with menu → `Toolbar` with hamburger menu
- Month selector → Custom month picker or `DatePicker`
- Summary cards (Expense/Income/Total) → `CardView` in `GridLayout`
- Empty state → Custom empty view with illustration
- FAB → `FloatingActionButton`
- Bottom navigation → `BottomNavigationView`

**Key Features:**
- Display financial summary for selected month
- Show recent transactions in RecyclerView
- Navigate between months
- Quick access to add transaction via FAB

### 2. AddTransactionActivity (add_transaction)
**HTML Elements → Android Components:**
- Header with close/save → `Toolbar` with close and save actions
- Segmented control (Income/Expenses/Transfer) → `TabLayout` or custom segmented control
- Amount display → Large `TextView` with custom formatting
- Account selection button → `Button` opening `AccountSelectionDialog`
- Category selection button → `Button` opening category selection
- Note input → `TextInputEditText` with `TextInputLayout`
- Calculator grid → Custom calculator view with `GridLayout`
- Date/time footer → `Button` opening `DatePickerDialog` and `TimePickerDialog`

**Key Features:**
- Calculator interface for amount entry
- Real-time amount display
- Account and category selection
- Date/time picker
- Input validation before saving

### 3. AccountsFragment (accounts_overview)
**HTML Elements → Android Components:**
- Top balance card → `CardView` with total balance
- Income/Expense summary → `CardView` grid
- Account list → `RecyclerView` with custom adapter
- Account items → Custom item layout with icon, name, balance
- Add account button → `Button` at bottom of list
- FAB → `FloatingActionButton`

**Key Features:**
- Display all accounts with balances
- Calculate total balance across accounts
- Show income/expense summary
- Add new account functionality

### 4. ReportsFragment (analysis_expense_overview)
**HTML Elements → Android Components:**
- Month selector → Custom month picker
- Summary cards → `CardView` grid
- Type selector dropdown → `Spinner` or `BottomSheet`
- Chart area → Custom chart view or third-party library (MPAndroidChart)
- Category list → `RecyclerView` with category breakdown

**Key Features:**
- Monthly expense/income analysis
- Category-wise breakdown
- Simple pie/bar chart visualization
- Percentage calculations

### 5. MainActivity (Container Activity)
**HTML Elements → Android Components:**
- Navigation drawer → `NavigationView` with `DrawerLayout`
- Bottom navigation → `BottomNavigationView`
- Fragment container → `FrameLayout` for fragment transactions

**Key Features:**
- Host all fragments
- Handle navigation between screens
- Manage bottom navigation state
- Handle back navigation

## Layout Files Structure

```
res/layout/
├── activity_main.xml                 # Main container with BottomNav
├── activity_add_transaction.xml      # Add transaction screen
├── fragment_dashboard.xml            # Dashboard layout
├── fragment_accounts.xml             # Accounts list layout
├── fragment_reports.xml              # Reports and analysis layout
├── fragment_categories.xml           # Categories management
├── dialog_account_selection.xml      # Account picker dialog
├── dialog_category_selection.xml     # Category picker dialog
├── item_transaction.xml              # Transaction list item
├── item_account.xml                  # Account list item
├── item_category_breakdown.xml       # Category analysis item
├── view_calculator.xml               # Calculator component
├── view_empty_state.xml              # Empty state layout
└── view_summary_cards.xml            # Reusable summary cards
```

## Navigation Flow

```
MainActivity
├── DashboardFragment (Tab 1 - Records) [DEFAULT]
│   ├── → AddTransactionActivity (via FAB)
│   └── → TransactionDetailDialog (via list item tap)
├── ReportsFragment (Tab 2 - Analysis)
│   └── → Month selector
├── BudgetsFragment (Tab 3 - Budgets) [Future]
├── AccountsFragment (Tab 4 - Accounts)
│   ├── → AddAccountDialog (via Add button)
│   └── → AccountDetailDialog (via item tap)
└── CategoriesFragment (Tab 5 - Categories)
    ├── → AddCategoryDialog (via Add button)
    └── → EditCategoryDialog (via item tap)

AddTransactionActivity
├── → AccountSelectionDialog (via Account button)
├── → CategorySelectionDialog (via Category button)
├── → DatePickerDialog (via Date button)
├── → TimePickerDialog (via Time button)
└── → MainActivity (after save or cancel)
```

## Key Differences from HTML

### Responsive Design
- HTML uses responsive grid → Android uses different layouts for phones/tablets
- HTML breakpoints → Android resource qualifiers (sw600dp, sw720dp)

### Interaction Patterns
- HTML hover states → Android touch states with ripple effects
- HTML click handlers → Android OnClickListener implementations
- HTML form validation → Android TextWatcher and validation logic

### Navigation
- HTML single page with JS routing → Android Fragment transactions
- HTML bottom nav → Android BottomNavigationView with FragmentManager
- HTML drawer → Android NavigationView with DrawerLayout

### Data Binding
- HTML direct DOM manipulation → Android View binding or findViewById
- HTML local storage → Android Room Database
- HTML real-time updates → Android LiveData/Observable patterns