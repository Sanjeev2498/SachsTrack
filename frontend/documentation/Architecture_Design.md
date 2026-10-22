# Android App Architecture Design

## Architecture Pattern: Repository Pattern with MVVM

### Overview
The app follows the Repository pattern with MVVM (Model-View-ViewModel) components to separate concerns and ensure maintainable code for the college project.

```
┌─────────────────┐
│       View      │ ← Activities & Fragments
│   (XML Layouts) │
└─────────────────┘
         ↓
┌─────────────────┐
│   ViewModel     │ ← Business Logic & UI State
│  (LiveData)     │
└─────────────────┘
         ↓
┌─────────────────┐
│   Repository    │ ← Data Access Logic
│                 │
└─────────────────┘
         ↓
┌─────────────────┐
│  Room Database  │ ← Local SQLite Database
│   (DAO/Entity)  │
└─────────────────┘
```

## Package Structure

```
com.example.sachstrack/
├── database/
│   ├── entities/
│   │   ├── Transaction.java
│   │   ├── Account.java
│   │   └── Category.java
│   ├── dao/
│   │   ├── TransactionDao.java
│   │   ├── AccountDao.java
│   │   └── CategoryDao.java
│   └── AppDatabase.java
├── repository/
│   ├── TransactionRepository.java
│   ├── AccountRepository.java
│   └── CategoryRepository.java
├── ui/
│   ├── MainActivity.java
│   ├── dashboard/
│   │   ├── DashboardFragment.java
│   │   └── DashboardViewModel.java
│   ├── addtransaction/
│   │   ├── AddTransactionActivity.java
│   │   └── AddTransactionViewModel.java
│   ├── accounts/
│   │   ├── AccountsFragment.java
│   │   └── AccountsViewModel.java
│   ├── reports/
│   │   ├── ReportsFragment.java
│   │   └── ReportsViewModel.java
│   └── adapters/
│       ├── TransactionAdapter.java
│       ├── AccountAdapter.java
│       └── CategoryAdapter.java
├── utils/
│   ├── DateUtils.java
│   ├── CurrencyUtils.java
│   └── Constants.java
└── SachsTrackApplication.java
```

## Database Design (Room)

### Entity Relationships
```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│   Transaction   │      │     Account     │      │    Category     │
├─────────────────┤      ├─────────────────┤      ├─────────────────┤
│ id (PK)         │  ┌───│ id (PK)         │      │ id (PK)         │
│ amount          │  │   │ name            │      │ name            │
│ type            │  │   │ type            │      │ type            │
│ category        │──┼───│ icon            │  ┌───│ icon            │
│ account         │──┘   │ balance         │  │   │ color           │
│ note            │      └─────────────────┘  │   └─────────────────┘
│ date            │                           │
│ createdAt       │───────────────────────────┘
└─────────────────┘
```

### Core Entities

#### Transaction Entity
- Primary entity for all financial transactions
- Links to Account and Category via foreign keys
- Supports income, expense, and transfer types

#### Account Entity
- Predefined account types (Bank, Card, Cash, Savings, UPI)
- Tracks balance for each account
- Used for account selection in transactions

#### Category Entity
- Predefined categories for income and expenses
- Color-coded for visual representation
- Used for transaction categorization and reporting

## Key Components

### 1. Repository Layer
**Purpose**: Centralized data access and business logic
```java
public class TransactionRepository {
    // Single source of truth for transaction data
    // Handles database operations
    // Provides LiveData for UI observation
    // Implements caching if needed
}
```

### 2. ViewModel Layer
**Purpose**: UI-related data holder and business logic
```java
public class DashboardViewModel extends ViewModel {
    // Holds UI state and data
    // Survives configuration changes
    // Communicates with Repository
    // Exposes LiveData to UI
}
```

### 3. Database Layer (Room)
**Purpose**: Local SQLite database abstraction
```java
@Database(entities = {Transaction.class, Account.class, Category.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // Singleton database instance
    // Provides DAO access
    // Handles migrations
}
```

## Data Flow Patterns

### 1. Read Data Flow
```
UI Request → ViewModel → Repository → DAO → Database → LiveData → UI Update
```

### 2. Write Data Flow
```
User Input → UI → ViewModel → Repository → DAO → Database → LiveData Notification
```

### 3. Navigation Flow
```
MainActivity (Container)
├── Bottom Navigation handles fragment switching
├── FAB triggers AddTransactionActivity
└── Fragment transactions managed by Navigation Component
```

## Key Design Decisions

### 1. Database Choice: Room
- **Reason**: Recommended by Google, compile-time verification
- **Benefits**: Type safety, LiveData integration, easier testing
- **Trade-offs**: Slightly more setup than raw SQLite

### 2. Architecture: Repository + MVVM
- **Reason**: Separation of concerns, testability, maintainability
- **Benefits**: Clear data flow, lifecycle-aware, configuration changes
- **Trade-offs**: More classes, but better organization

### 3. Navigation: Fragment-based with Activity for Add Transaction
- **Reason**: Bottom navigation works well with fragments
- **Benefits**: Shared state, better memory management
- **Trade-offs**: Add Transaction as separate Activity for full-screen experience

### 4. Data Binding: View Binding (not Data Binding)
- **Reason**: Simpler setup, better performance, null safety
- **Benefits**: Type safety, null safety, no reflection
- **Trade-offs**: More boilerplate than Data Binding

## Threading Strategy

### Database Operations
```java
// All database operations on background thread
// Repository handles thread switching
// LiveData automatically switches to main thread for UI updates

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();
    
    @Insert
    void insertTransaction(Transaction transaction); // Runs on background
}
```

### UI Updates
```java
// ViewModels observe Repository LiveData
// Fragments observe ViewModel LiveData
// Automatic UI updates when data changes

public class DashboardViewModel extends ViewModel {
    private LiveData<List<Transaction>> transactions;
    
    public LiveData<List<Transaction>> getTransactions() {
        return repository.getAllTransactions(); // LiveData from Repository
    }
}
```

## Error Handling Strategy

### Database Errors
- Validation at ViewModel level before database operations
- Try-catch blocks in Repository for database exceptions
- User-friendly error messages in UI

### Input Validation
- Real-time validation in UI (TextWatcher)
- Business logic validation in ViewModel
- Database constraints as final validation

### State Management
- Loading states for async operations
- Error states with retry mechanisms
- Empty states for no data scenarios

## Testing Strategy

### Unit Tests
- Repository logic testing
- ViewModel business logic testing
- Database operations testing (with in-memory database)

### Integration Tests
- Full data flow testing
- UI navigation testing
- Database migration testing

## Performance Considerations

### Database Optimization
- Proper indexing on frequently queried columns
- Efficient queries with appropriate LIMIT clauses
- Lazy loading for large datasets

### Memory Management
- ViewModels survive configuration changes
- LiveData prevents memory leaks
- Proper lifecycle management in fragments

### UI Performance
- RecyclerView with ViewHolder pattern
- Image loading optimization (if needed)
- Efficient layout hierarchies