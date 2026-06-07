package com.demons.premium.utils;

import java.io.File;
import java.io.IOException;

/**
 * Utility class untuk file operations
 */
public class FileUtils {
    
    /**
     * Create directory recursively
     */
    public static boolean createDirectory(String path) {
        try {
            File dir = new File(path);
            return dir.mkdirs();
        } catch (Exception e) {
            Logger.e("Failed to create directory: " + path, e);
            return false;
        }
    }
    
    /**
     * Delete file
     */
    public static boolean deleteFile(String path) {
        try {
            File file = new File(path);
            return file.delete();
        } catch (Exception e) {
            Logger.e("Failed to delete file: " + path, e);
            return false;
        }
    }
    
    /**
     * Delete directory recursively
     */
    public static boolean deleteDirectory(String path) {
        try {
            File dir = new File(path);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDirectory(file.getAbsolutePath());
                        } else {
                            file.delete();
                        }
                    }
                }
            }
            return dir.delete();
        } catch (Exception e) {
            Logger.e("Failed to delete directory: " + path, e);
            return false;
        }
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String path) {
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception e) {
            Logger.e("Failed to check if file exists: " + path, e);
            return false;
        }
    }
    
    /**
     * Get file size
     */
    public static long getFileSize(String path) {
        try {
            File file = new File(path);
            return file.length();
        } catch (Exception e) {
            Logger.e("Failed to get file size: " + path, e);
            return -1;
        }
    }
    
    /**
     * Get canonical path
     */
    public static String getCanonicalPath(String path) {
        try {
            File file = new File(path);
            return file.getCanonicalPath();
        } catch (IOException e) {
            Logger.e("Failed to get canonical path: " + path, e);
            return null;
        }
    }
}
