package com.demons.premium;

import com.demons.premium.entity.AppConfig;
import com.demons.premium.entity.RuntimeState;
import com.demons.premium.IIsolationCallback;

interface IIsolation {
    // Lifecycle
    int createSandbox(in AppConfig config);
    boolean startSandbox(int sandboxId);
    boolean stopSandbox(int sandboxId);
    boolean destroySandbox(int sandboxId);
    
    // State management
    RuntimeState getSandboxState(int sandboxId);
    List<RuntimeState> getAllSandboxes();
    
    // App management
    boolean installApp(int sandboxId, String apkPath);
    boolean uninstallApp(int sandboxId, String packageName);
    boolean launchApp(int sandboxId, String packageName);
    
    // Permission management
    boolean grantPermission(int sandboxId, String packageName, String permission);
    boolean revokePermission(int sandboxId, String packageName, String permission);
    List<String> getGrantedPermissions(int sandboxId, String packageName);
    
    // Callback registration
    void registerCallback(IIsolationCallback callback);
    void unregisterCallback(IIsolationCallback callback);
}
