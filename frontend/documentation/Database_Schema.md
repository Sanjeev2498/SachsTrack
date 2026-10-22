# Database Schema Documentation

## Room Database Schema for SachsTrack

### Entity Relationship Diagram

```
┌─────────────────────┐
│     Transaction     │
│ ┌─────────────────┐ │
│ │ id (PK)         │ │
│ │ amount          │ │
│ │ type            │ │──┐
│ │ category        │ │  │ References category.name
│ │ account         │ │──┼─ References account.name  
│ │ note            │ │  │
│ │ date            │ │  │
│ │ time            │ │  │
│ │ createdAt       │ │  │
│ └─────────────────┘ │  │
└─────────────────────┘  │
                         │
┌─────────────────────┐  │
│      Account        │  │
│ ┌─────────────────┐ │  │
│ │ id (PK)         │ │  │
│ │ name            │ │←─┘
│ │ type            │ │
│ │ icon            │ │
│ │ balance         │ │
│ │ isActive        │ │
│ └─────────────────┘ │
└─────────────────────┘
                         
┌─────────────────────┐  
│      Category       │  
│ ┌─────────────────┐ │  
│ │ id (PK)         │ │  
│ │ name            │ │←─┘
│ │ type            │ │
│ │ icon            │ │
│ │ color           │ │
│ │ isActive        │ │
│ └─────────────────┘ │
└─────────────────────┘
```

## Table Definitions

### 1. transactions

**Purpose**: Store all financial transactions (income, expense, transfers)

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY, AUTO INCREMENT | Unique transaction identifier |
| amount | REAL | NOT NULL | Transaction amount (always positive) |
| type | TEXT | NOT NULL | Transaction type: 'income', 'expense', 'transfer' |
| category | TEXT | NOT NULL | Category name (references categories table) |
| account | TEXT | NOT NULL | Account name (references accounts table) |
| note | TEXT | NULLABLE | Optional transaction note/description |
| date | TEXT | NOT NULL | Transaction date in ISO format (YYYY-MM-DD) |
| time | TEXT | NOT NULL | Transaction time in HH:MM format |
| created_at | INTEGER | NOT NULL | Unix timestamp of record creation |

**Indexes:**
- Primary index on `id`
- Index on `date` for efficient date-based queries
- Index on `type` for filtering by transaction type
- Index on `category` for category-based reporting
- Index on `account` for account-based filtering

### 2. accounts

**Purpose**: Store account information and balances

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY, AUTO INCREMENT | Unique account identifier |
| name | TEXT | NOT NULL, UNIQUE | Account display name |
| type | TEXT | NOT NULL | Account type: 'bank', 'card', 'cash', 'savings', 'upi' |
| icon | TEXT | NOT NULL | Material icon name for display |
| balance | REAL | NOT NULL, DEFAULT 0.0 | Current account balance |
| is_active | INTEGER | NOT NULL, DEFAULT 1 | Boolean: 1=active, 0=inactive |

**Default Data:**
```sql
INSERT INTO accounts (name, type, icon, balance) VALUES
('Bank', 'bank', 'account_balance', 12000.0),
('Card', 'card', 'credit_card', -1500.0),
('Cash', 'cash', 'payments', 2000.0),
('Savings', 'savings', 'savings', 10000.0),
('UPI', 'upi', 'qr_code_scanner', 732.0);
```

### 3. categories

**Purpose**: Store transaction categories for organization and reporting

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY, AUTO INCREMENT | Unique category identifier |
| name | TEXT | NOT NULL | Category display name |
| type | TEXT | NOT NULL | Category type: 'income' or 'expense' |
| icon | TEXT | NOT NULL | Material icon name for display |
| color | TEXT | NOT NULL | Hex color code for UI theming |
| is_active | INTEGER | NOT NULL, DEFAULT 1 | Boolean: 1=active, 0=inactive |

**Default Income Categories:**
```sql
INSERT INTO categories (name, type, icon, color) VALUES
('Salary', 'income', 'work', '#4edea3'),
('Business', 'income', 'business', '#4edea3'),
('Investment', 'income', 'trending_up', '#4edea3'),
('Gift', 'income', 'card_giftcard', '#4edea3'),
('Other Income', 'income', 'add_circle', '#4edea3');
```

**Default Expense Categories:**
```sql
INSERT INTO categories (name, type, icon, color) VALUES
('Food', 'expense', 'restaurant', '#ffb4ab'),
('Transport', 'expense', 'directions_car', '#ffb4ab'),
('Rent', 'expense', 'home', '#ffb4ab'),
('Shopping', 'expense', 'shopping_bag', '#ffb4ab'),
('Entertainment', 'expense', 'movie', '#ffb4ab'),
('Healthcare', 'expense', 'local_hospital', '#ffb4ab'),
('Education', 'expense', 'school', '#ffb4ab'),
('Utilities', 'expense', 'electrical_services', '#ffb4ab'),
('Insurance', 'expense', 'security', '#ffb4ab'),
('Other Expense', 'expense', 'remove_circle', '#ffb4ab');
```

## Key DAO Operations

### TransactionDao Key Queries

```sql
-- Get monthly summary
SELECT SUM(amount) FROM transactions 
WHERE type = 'income' AND date LIKE '2026-09%';

SELECT SUM(amount) FROM transactions 
WHERE type = 'expense' AND date LIKE '2026-09%';

-- Category breakdown for reports
SELECT category, SUM(amount) as total, COUNT(*) as count 
FROM transactions 
WHERE type = 'expense' 
GROUP BY category 
ORDER BY total DESC;

-- Recent transactions for dashboard
SELECT * FROM transactions 
ORDER BY date DESC, created_at DESC 
LIMIT 10;
```

### AccountDao Key Operations

```sql
-- Calculate total balance across all accounts
SELECT SUM(balance) FROM accounts WHERE is_active = 1;

-- Update account balance after transaction
UPDATE accounts SET balance = balance + :amount WHERE name = :accountName;
UPDATE accounts SET balance = balance - :amount WHERE name = :accountName;
```

### CategoryDao Key Operations

```sql
-- Get categories by type for transaction entry
SELECT * FROM categories 
WHERE type = 'expense' AND is_active = 1 
ORDER BY name ASC;

-- Category usage statistics
SELECT c.*, COUNT(t.id) as usage_count 
FROM categories c 
LEFT JOIN transactions t ON c.name = t.category 
WHERE c.is_active = 1 
GROUP BY c.id 
ORDER BY usage_count DESC;
```

## Data Validation Rules

### Transaction Validation
- Amount must be positive (> 0)
- Type must be one of: 'income', 'expense', 'transfer'
- Category must exist in categories table
- Account must exist in accounts table
- Date must be valid ISO format (YYYY-MM-DD)
- Time must be valid HH:MM format

### Account Validation
- Name must be unique
- Type must be one of: 'bank', 'card', 'cash', 'savings', 'upi'
- Balance can be negative (for credit accounts)
- Icon must be valid Material icon name

### Category Validation
- Name must be unique within same type
- Type must be one of: 'income', 'expense'
- Color must be valid hex color code
- Icon must be valid Material icon name

## Performance Considerations

### Indexes
```sql
CREATE INDEX idx_transactions_date ON transactions(date);
CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_transactions_category ON transactions(category);
CREATE INDEX idx_transactions_account ON transactions(account);
CREATE INDEX idx_accounts_name ON accounts(name);
CREATE INDEX idx_categories_type ON categories(type);
```

### Query Optimization
- Use LIMIT for pagination
- Use date ranges for monthly queries
- Use prepared statements for parameterized queries
- Cache frequently accessed data in ViewModels

## Database Migrations

### Future Migration Strategy
```java
// Example migration from version 1 to 2
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE transactions ADD COLUMN description TEXT");
    }
};

// Example migration from version 2 to 3
static final Migration MIGRATION_2_3 = new Migration(2, 3) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE budgets (id INTEGER PRIMARY KEY, category TEXT, amount REAL, month TEXT)");
    }
};
```

## Backup and Restore Strategy

### Data Export Format (JSON)
```json
{
  "version": 1,
  "exported_at": "2026-09-03T12:00:00Z",
  "transactions": [...],
  "accounts": [...],
  "categories": [...]
}
```

### Restore Validation
- Validate JSON structure
- Check for data consistency
- Handle conflicts with existing data
- Maintain referential integrity

## Testing Considerations

### In-Memory Database for Tests
```java
@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class TestDatabase extends RoomDatabase {
    public static TestDatabase create() {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TestDatabase.class
        ).allowMainThreadQueries().build();
    }
}
```

### Sample Test Data
```java
public static Transaction[] getSampleTransactions() {
    return new Transaction[]{
        new Transaction(1000.0, "income", "Salary", "Bank", "Test income", "2026-09-01", "09:00"),
        new Transaction(500.0, "expense", "Food", "Cash", "Test expense", "2026-09-02", "18:30")
    };
}
```