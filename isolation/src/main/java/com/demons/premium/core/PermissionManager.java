package com.demons.premium.core;

import android.content.Context;

import com.demons.premium.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mengelola permissions untuk aplikasi dalam sandbox
 */
public class PermissionManager {
    private static final String TAG = "PermissionManager";
    private Context context;
    private Map<String, List<String>> appPermissions = new HashMap<>();
    
    public PermissionManager(Context context) {
        this.context = context;
    }
    
    /**
     * Grant permission ke app
     */
    public boolean grantPermission(String packageName, String permission) {
        try {
            Logger.d("Granting permission: " + permission + " to " + packageName);
            
            if (!appPermissions.containsKey(packageName)) {
                appPermissions.put(packageName, new ArrayList<>());
            }
            
            List<String> permissions = appPermissions.get(packageName);
            if (!permissions.contains(permission)) {
                permissions.add(permission);
                Logger.d("Permission granted successfully");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            Logger.e("Failed to grant permission", e);
            return false;
        }
    }
    
    /**
     * Revoke permission dari app
     */
    public boolean revokePermission(String packageName, String permission) {
        try {
            Logger.d("Revoking permission: " + permission + " from " + packageName);
            
            if (appPermissions.containsKey(packageName)) {
                List<String> permissions = appPermissions.get(packageName);
                if (permissions.remove(permission)) {
                    Logger.d("Permission revoked successfully");
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            Logger.e("Failed to revoke permission", e);
            return false;
        }
    }
    
    /**
     * Check apakah app memiliki permission
     */
    public boolean hasPermission(String packageName, String permission) {
        List<String> permissions = appPermissions.get(packageName);
        if (permissions != null) {
            return permissions.contains(permission);
        }
        return false;
    }
    
    /**
     * Get all granted permissions untuk app
     */
    public List<String> getGrantedPermissions(String packageName) {
        List<String> permissions = appPermissions.get(packageName);
        if (permissions != null) {
            return new ArrayList<>(permissions);
        }
        return new ArrayList<>();
    }
    
    /**
     * Grant multiple permissions
     */
    public boolean grantPermissions(String packageName, List<String> permissions) {
        try {
            Logger.d("Granting " + permissions.size() + " permissions to " + packageName);
            
            for (String permission : permissions) {
                grantPermission(packageName, permission);
            }
            
            return true;
        } catch (Exception e) {
            Logger.e("Failed to grant permissions", e);
            return false;
        }
    }
    
    /**
     * Reset semua permissions untuk app
     */
    public boolean resetPermissions(String packageName) {
        try {
            Logger.d("Resetting permissions for " + packageName);
            appPermissions.put(packageName, new ArrayList<>());
            return true;
        } catch (Exception e) {
            Logger.e("Failed to reset permissions", e);
            return false;
        }
    }
    
    /**
     * Get default permissions untuk sandbox
     */
    public List<String> getDefaultPermissions() {
        List<String> defaultPerms = new ArrayList<>();
        defaultPerms.add("android.permission.INTERNET");
        defaultPerms.add("android.permission.ACCESS_NETWORK_STATE");
        return defaultPerms;
    }
}
