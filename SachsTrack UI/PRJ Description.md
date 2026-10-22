# Build a Working Personal Finance Tracker Android App Using My Existing HTML UI

I am building a **Personal Finance Tracker Android application** for a college project.

I already have the complete UI designed in **HTML, CSS, and JavaScript**, and it is available in an existing folder in this project/workspace.

## Important Instruction: Use My Existing UI

First, inspect the existing folder containing my HTML UI.

Use the following files as the **design reference**:

- HTML files for screen structure
- CSS files for colors, spacing, typography, cards, and styling
- Images and assets from the existing UI folder
- JavaScript only to understand UI interactions if necessary

Do **not** use the HTML application directly as the final Android application.

Instead, convert/recreate the UI design into Android-compatible XML layouts while preserving the original visual design as closely as possible.

---

# Create a Separate Frontend Folder

Create a separate folder named:

```text id="p6dy9t"
frontend
```

Use this folder to organize the frontend/UI-related source and reference files.

Recommended project organization:

```text id="vjj4lq"
Project Root
│
├── existing-ui
│   ├── index.html
│   ├── dashboard.html
│   ├── css
│   ├── js
│   └── assets
│
├── frontend
│   ├── reference
│   │   └── UI design references or copied source information
│   │
│   ├── layouts
│   │   ├── activity_main.xml
│   │   ├── fragment_dashboard.xml
│   │   ├── fragment_add_transaction.xml
│   │   ├── fragment_history.xml
│   │   └── fragment_reports.xml
│   │
│   └── assets
│
└── Android App
    └── app
        └── src
            └── main
                ├── java
                └── res
                    ├── layout
                    ├── drawable
                    ├── mipmap
                    └── values
```

The `frontend` folder should remain separate and organized.

However, for the Android application to compile and run, the final XML layouts and Android resources must also be placed in the appropriate Android project directories, such as:

```text id="3ccp44"
app/src/main/res/layout/
app/src/main/res/drawable/
app/src/main/res/values/
```

Do not expect Android Studio to automatically use XML files stored only in the root `frontend` folder.

---

# UI Conversion Rules

When converting the HTML UI to Android:

```text id="prpve4"
HTML UI
   │
   ▼
Analyze Structure and Styling
   │
   ▼
Android XML Layout
   │
   ▼
Java Functionality
   │
   ▼
Working Android Application
```

Preserve as much as possible:

- Colors
- Layout structure
- Cards
- Buttons
- Icons
- Typography
- Spacing
- Navigation
- Overall appearance

Map common HTML components to Android components appropriately:

```text id="om1ihy"
HTML Element           Android Equivalent
------------------------------------------------
div                    LinearLayout / ConstraintLayout
button                 Button / MaterialButton
input                  EditText / TextInputEditText
select                 Spinner / AutoCompleteTextView
list                   RecyclerView
img                    ImageView
card                   MaterialCardView
```

Do not unnecessarily redesign the UI.

---

# Technology Stack

Use:

- Android Studio
- Java
- XML
- Room Database
- SQLite through Room
- RecyclerView
- Material Design Components

Do not use:

- Kotlin
- Jetpack Compose
- Firebase
- Authentication
- Cloud database
- Backend server
- REST API
- Payment gateway

The application should work completely offline.

---

# Application Features

## Dashboard

Implement the UI from my existing HTML design.

Display:

- Total Balance
- Total Income
- Total Expense
- Recent Transactions

Calculation:

```text id="2iizk9"
Total Balance = Total Income - Total Expense
```

Automatically refresh the displayed values when transactions change.

---

## Add Transaction

Connect the provided UI to Java functionality.

Fields:

- Amount
- Transaction Type
- Category
- Date
- Note

Transaction types:

```text id="tj6jyd"
Income
Expense
```

When the user saves a transaction:

```text id="m0lv4a"
User Input
     ↓
Java Validation
     ↓
Transaction Object
     ↓
Room Database
     ↓
Refresh Dashboard
     ↓
Refresh Transaction History
```

Use only simple validation.

---

## Transaction History

Use RecyclerView.

Display:

- Category
- Amount
- Transaction Type
- Date
- Note

Implement:

- View transactions
- Edit transactions
- Delete transactions

Show a basic confirmation dialog before deleting.

---

## Edit Transaction

Allow users to:

- Modify amount
- Change transaction type
- Change category
- Change date
- Modify note

Save the changes to the Room database.

---

## Reports

Display:

- Total Income
- Total Expense
- Current Balance

Charts are optional.

Do not spend excessive time implementing advanced analytics.

---

# Database

Use Room Database.

Create a Transaction entity:

```text id="2g50ru"
id
amount
type
category
date
note
```

Create:

```text id="nylvhm"
Transaction.java
TransactionDao.java
AppDatabase.java
```

Implement:

- Insert
- Read
- Update
- Delete

---

# Important Implementation Rules

1. First inspect the existing HTML UI folder.
2. Understand all available UI screens.
3. Use the existing HTML, CSS, and assets as design references.
4. Create and maintain a separate `frontend` folder.
5. Convert the necessary HTML UI designs into Android XML layouts.
6. Keep the visual design as close as possible to the original UI.
7. Implement all application logic using Java.
8. Store financial transactions using Room Database.
9. Ensure all buttons and navigation work.
10. Keep the project simple and suitable for a college project.
11. Do not add unnecessary production-level features.
12. Do not add authentication or cloud services.
13. Make the app fully functional offline.
14. Ensure all Gradle dependencies are included.
15. Verify that the Android project compiles without errors.

---

# Final Priority Order

```text id="3goqfw"
1. Use Existing HTML UI Design
              ↓
2. Create Separate Frontend Folder
              ↓
3. Convert UI to Android XML
              ↓
4. Implement Java Functionality
              ↓
5. Connect Room Database
              ↓
6. Ensure All Features Work
              ↓
7. Keep Everything Simple
```

This is a college project, so prioritize a **working application with clean and understandable code** over advanced architecture, security, or unnecessary technologies.

Before making major UI changes, always inspect the existing HTML UI folder and follow its design.