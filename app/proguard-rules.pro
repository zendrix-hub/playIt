# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Vosk Speech Recognition JNI bridge & JNA
-keep class org.vosk.** { *; }
-dontwarn org.vosk.**
-keep class com.sun.jna.** { *; }
-dontwarn com.sun.jna.**

# Room Database & Local Entities
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.**
-keep class com.playit.app.data.local.entity.** { *; }
-keepclassmembers class com.playit.app.data.local.entity.** { *; }