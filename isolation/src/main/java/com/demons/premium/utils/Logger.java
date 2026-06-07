package com.demons.premium.utils;

import android.util.Log;

/**
 * Utility class untuk logging
 */
public class Logger {
    private static String TAG = "DemonsIsolation";
    private static boolean debugEnabled = true;
    
    public static void init(String tag) {
        TAG = tag;
    }
    
    public static void d(String message) {
        if (debugEnabled) {
            Log.d(TAG, message);
        }
    }
    
    public static void d(String message, Throwable throwable) {
        if (debugEnabled) {
            Log.d(TAG, message, throwable);
        }
    }
    
    public static void i(String message) {
        Log.i(TAG, message);
    }
    
    public static void i(String message, Throwable throwable) {
        Log.i(TAG, message, throwable);
    }
    
    public static void w(String message) {
        Log.w(TAG, message);
    }
    
    public static void w(String message, Throwable throwable) {
        Log.w(TAG, message, throwable);
    }
    
    public static void e(String message) {
        Log.e(TAG, message);
    }
    
    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }
    
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }
}
