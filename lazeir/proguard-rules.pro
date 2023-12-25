# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes *Annotation*

-keep class com.velox.lazeir.utils.*
#-dontshrink class com.velox.lazeir.utils.*
#-dontoptimize class com.velox.lazeir.utils.*

-dontobfuscate class com.velox.lazeir.utils.outlet.*


#-dontwarn com.squareup.retrofit2.*
#-dontwarn com.squareup.okhttp3.*
#-dontwarn com.jakewharton.retrofit.*
#-dontwarn org.conscrypt.*
#-dontwarn com.squareup.retrofit2.*
#-dontwarn com.google.code.gson.*
#-dontwarn com.squareup.moshi.*
#-dontwarn com.google.android.gms.*
#-dontwarn com.google.dagger.*
#-dontwarn androidx.hilt.*
#-dontwarn com.google.dagger.*
#-dontwarn androidx.hilt.*
#-dontwarn androidx.hilt.*