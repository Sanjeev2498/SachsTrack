# Add project specific ProGuard rules here.

# Keep all Room database classes
-keep class com.example.sachstrack.database.** { *; }
-keep class com.example.sachstrack.database.entities.** { *; }
-keep class com.example.sachstrack.database.dao.** { *; }

# Keep all data classes and entities
-keep class com.example.sachstrack.ui.** { *; }
-keep class com.example.sachstrack.repository.** { *; }

# Room specific rules
-dontwarn androidx.room.paging.**

# Keep data binding classes
-keep class androidx.databinding.** { *; }

# Keep view binding classes
-keep class com.example.sachstrack.databinding.** { *; }

# Keep application class
-keep class com.example.sachstrack.SachsTrackApplication { *; }

# Material Design components
-keep class com.google.android.material.** { *; }

# RecyclerView
-keep class androidx.recyclerview.widget.** { *; }

# Lifecycle components
-keep class androidx.lifecycle.** { *; }

# Navigation components
-keep class androidx.navigation.** { *; }

# General Android optimizations
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}