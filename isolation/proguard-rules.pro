# Demons Premium - App Isolation ProGuard rules

# Keep AIDL interfaces
-keep interface com.demons.premium.IIsolation { *; }
-keep interface com.demons.premium.IIsolationCallback { *; }

# Keep entity classes
-keep class com.demons.premium.entity.** { *; }

# Keep service
-keep class com.demons.premium.service.IsolationService { *; }

# Keep manager
-keep class com.demons.premium.IsolationManager { *; }

# Keep core classes
-keep class com.demons.premium.core.** { *; }

# Keep utils
-keep class com.demons.premium.utils.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve line numbers
-keepattributes SourceFile,LineNumberTable

# Obfuscation
-renamesourcefileattribute SourceFile
