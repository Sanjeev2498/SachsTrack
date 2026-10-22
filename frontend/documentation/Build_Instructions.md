# Build Instructions - SachsTrack Android App

## Project Status
✅ **READY TO BUILD** - All core components implemented and integrated

## Prerequisites

### Development Environment
- **Android Studio**: Latest stable version (2023.3.1 or newer)
- **JDK**: Java 11 or newer
- **Android SDK**: API Level 24-37 (Android 7.0 to Android 14)
- **Gradle**: 9.5.0 (handled by wrapper)

### Project Configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 37 (Android 14) 
- **Compile SDK**: 37
- **Language**: Java (100%)
- **Architecture**: MVVM with Repository Pattern
- **Database**: Room (SQLite)

## Build Process

### 1. Clone/Open Project
```bash
# If cloning from repository
git clone <repository-url>
cd SachsTrack

# Open in Android Studio
# File > Open > Select SachsTrack folder
```

### 2. Gradle Sync
- Android Studio will automatically prompt to sync Gradle
- If not, use: **File > Sync Project with Gradle Files**
- All dependencies are configured in `gradle/libs.versions.toml`

### 3. Build Options

#### Debug Build (Recommended for Testing)
```bash
# Command line
./gradlew assembleDebug

# Android Studio
Build > Make Project (Ctrl+F9)
```

#### Release Build
```bash
# Command line  
./gradlew assembleRelease

# Android Studio
Build > Generate Signed Bundle/APK
```

### 4. Run on Device/Emulator
```bash
# Install and run on connected device
./gradlew installDebug

# Android Studio
Run > Run 'app' (Shift+F10)
```

## Project Architecture

### Core Components
```
SachsTrack/
├── app/src/main/java/com/example/sachstrack/
│   ├── SachsTrackApplication.java          # Application class
│   ├── MainActivity.java                   # Main container
│   ├── database/                          # Room Database
│   │   ├── AppDatabase.java
│   │   ├── entities/                      # Data models
│   │   └── dao/                          # Data access
│   ├── repository/                        # Repository pattern
│   ├── ui/                               # UI components
│   │   ├── dashboard/                     # Dashboard feature
│   │   ├── accounts/                      # Accounts feature  
│   │   ├── addtransaction/               # Transaction entry
│   │   └── adapters/                     # RecyclerView adapters
│   └── utils/                            # Utility classes
└── app/src/main/res/                      # Android resources
    ├── layout/                           # XML layouts
    ├── values/                           # Themes, colors, strings
    └── drawable/                         # Vector icons
```

### Dependencies (Managed via Version Catalog)
- **Room Database**: 2.6.1
- **Lifecycle Components**: 2.8.6  
- **Navigation**: 2.8.4
- **Material Design**: 1.10.0
- **RecyclerView**: 1.3.2
- **CardView**: 1.0.0

## Features Implemented

### ✅ Core Features
- **Dashboard**: Month navigation, financial summary, transaction list
- **Add Transaction**: Calculator interface, form validation, category/account selection
- **Accounts Management**: Account list, balance overview
- **Database Integration**: Full CRUD operations with Room
- **Dark Theme UI**: Material Design 3 with custom color scheme

### ✅ Technical Features  
- **MVVM Architecture**: Proper separation of concerns
- **LiveData**: Reactive UI updates
- **Repository Pattern**: Clean data access layer
- **View Binding**: Type-safe view references
- **Background Threading**: Proper database operations
- **Form Validation**: Input validation with error messaging

### 🚧 Planned Enhancements (Future Phases)
- Transaction editing functionality
- Advanced reporting with charts
- Budget tracking
- Category management UI
- Search and filtering
- Data export functionality

## Build Configuration

### Gradle Files Structure
```
gradle/
├── libs.versions.toml                    # Version catalog
├── gradle-daemon-jvm.properties         # JVM settings
└── wrapper/                             # Gradle wrapper

app/
└── build.gradle.kts                     # App-level build config

build.gradle.kts                         # Project-level build config
```

### Key Build Settings
```kotlin
// app/build.gradle.kts
android {
    namespace = "com.example.sachstrack"
    compileSdk = 37
    
    defaultConfig {
        applicationId = "com.example.sachstrack"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        viewBinding = true
    }
}
```

## Troubleshooting

### Common Build Issues

#### 1. Gradle Sync Failure
```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

#### 2. Room Database Issues
- Database schema is automatically generated
- Default data populated on first run via `roomDatabaseCallback`
- Clear app data if database schema changes during development

#### 3. Dependency Conflicts
- All dependencies managed via version catalog
- Update `gradle/libs.versions.toml` if version conflicts occur

#### 4. Memory Issues During Build
```bash
# Increase Gradle daemon memory
echo "org.gradle.jvmargs=-Xmx4096m" >> gradle.properties
```

### Device/Emulator Requirements
- **Android Version**: 7.0 (API 24) or higher
- **RAM**: 2GB minimum (4GB recommended)
- **Storage**: 100MB for app installation
- **Permissions**: None required (offline app)

## Testing Strategy

### Manual Testing Checklist
1. **App Launch**: Verify app starts and shows dashboard
2. **Navigation**: Test bottom navigation between screens  
3. **Add Transaction**: Test calculator and form submission
4. **Data Persistence**: Add transaction and verify it appears in dashboard
5. **Month Navigation**: Test previous/next month functionality
6. **Account Selection**: Test account picker dialog
7. **Category Selection**: Test category picker dialog
8. **Form Validation**: Test empty fields and invalid inputs

### Database Testing
```java
// Sample test data is automatically inserted
// Check AppDatabase.insertSampleData() method
// Verify default accounts and categories are created
```

## Deployment Notes

### Debug APK
- Generated at: `app/build/outputs/apk/debug/app-debug.apk`
- Suitable for testing and development
- No signing required for installation via ADB

### Release APK (Future)
- Requires code signing with release keystore
- Enable ProGuard/R8 for code obfuscation
- Test thoroughly before distribution

## Code Quality

### Standards Implemented
- **Clean Architecture**: Separation of concerns
- **SOLID Principles**: Single responsibility, dependency inversion
- **Error Handling**: Comprehensive validation and error messaging  
- **Memory Management**: Proper lifecycle awareness
- **Threading**: Background database operations
- **Accessibility**: Content descriptions and touch targets

### Performance Considerations
- **Database**: Efficient queries with proper indexing
- **UI**: RecyclerView with DiffUtil for efficient list updates
- **Memory**: LiveData prevents memory leaks
- **Background Work**: ExecutorService for database operations

## Final Notes

The app is architecturally complete with a solid foundation for future enhancements. All core personal finance tracking features are implemented and functional. The modular architecture allows for easy extension with additional features.

**Ready for college project submission with full documentation and working implementation.**