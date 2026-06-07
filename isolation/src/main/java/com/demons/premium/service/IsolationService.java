package com.demons.premium.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.demons.premium.IIsolation;
import com.demons.premium.IIsolationCallback;
import com.demons.premium.entity.AppConfig;
import com.demons.premium.entity.RuntimeState;
import com.demons.premium.core.SandboxRuntime;
import com.demons.premium.core.IsolationManager;
import com.demons.premium.core.PermissionManager;
import com.demons.premium.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service untuk App Isolation
 */
public class IsolationService extends Service {
    private static final String TAG = "IsolationService";
    
    private SandboxRuntime sandboxRuntime;
    private IsolationManager isolationManager;
    private PermissionManager permissionManager;
    private Map<Integer, AppConfig> sandboxConfigs = new HashMap<>();
    private Map<Integer, RuntimeState> sandboxStates = new HashMap<>();
    private List<IIsolationCallback> callbacks = new ArrayList<>();
    private int nextSandboxId = 1;
    
    private final IIsolation.Stub binder = new IIsolation.Stub() {
        @Override
        public int createSandbox(AppConfig config) throws RemoteException {
            return IsolationService.this.createSandbox(config);
        }
        
        @Override
        public boolean startSandbox(int sandboxId) throws RemoteException {
            return IsolationService.this.startSandbox(sandboxId);
        }
        
        @Override
        public boolean stopSandbox(int sandboxId) throws RemoteException {
            return IsolationService.this.stopSandbox(sandboxId);
        }
        
        @Override
        public boolean destroySandbox(int sandboxId) throws RemoteException {
            return IsolationService.this.destroySandbox(sandboxId);
        }
        
        @Override
        public RuntimeState getSandboxState(int sandboxId) throws RemoteException {
            return IsolationService.this.getSandboxState(sandboxId);
        }
        
        @Override
        public List<RuntimeState> getAllSandboxes() throws RemoteException {
            return new ArrayList<>(sandboxStates.values());
        }
        
        @Override
        public boolean installApp(int sandboxId, String apkPath) throws RemoteException {
            return IsolationService.this.installApp(sandboxId, apkPath);
        }
        
        @Override
        public boolean uninstallApp(int sandboxId, String packageName) throws RemoteException {
            return IsolationService.this.uninstallApp(sandboxId, packageName);
        }
        
        @Override
        public boolean launchApp(int sandboxId, String packageName) throws RemoteException {
            return IsolationService.this.launchApp(sandboxId, packageName);
        }
        
        @Override
        public boolean grantPermission(int sandboxId, String packageName, String permission) throws RemoteException {
            return IsolationService.this.grantPermission(sandboxId, packageName, permission);
        }
        
        @Override
        public boolean revokePermission(int sandboxId, String packageName, String permission) throws RemoteException {
            return IsolationService.this.revokePermission(sandboxId, packageName, permission);
        }
        
        @Override
        public List<String> getGrantedPermissions(int sandboxId, String packageName) throws RemoteException {
            return IsolationService.this.getGrantedPermissions(packageName);
        }
        
        @Override
        public void registerCallback(IIsolationCallback callback) throws RemoteException {
            if (!callbacks.contains(callback)) {
                callbacks.add(callback);
            }
        }
        
        @Override
        public void unregisterCallback(IIsolationCallback callback) throws RemoteException {
            callbacks.remove(callback);
        }
    };
    
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(TAG);
        
        sandboxRuntime = new SandboxRuntime(this);
        isolationManager = new IsolationManager(this);
        permissionManager = new PermissionManager(this);
        
        Logger.d("IsolationService created");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("IsolationService started");
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("IsolationService bound");
        return binder;
    }
    
    private int createSandbox(AppConfig config) {
        try {
            Logger.d("Creating sandbox: " + config.getName());
            
            int sandboxId = nextSandboxId++;
            
            if (!isolationManager.setupIsolation(sandboxId, config.getName())) {
                return -1;
            }
            
            if (!sandboxRuntime.initializeRuntime(config, sandboxId)) {
                return -1;
            }
            
            sandboxConfigs.put(sandboxId, config);
            
            RuntimeState state = new RuntimeState(
                    sandboxId,
                    config.getName(),
                    RuntimeState.STATE_CREATED,
                    System.currentTimeMillis()
            );
            sandboxStates.put(sandboxId, state);
            
            notifySandboxStateChanged(state);
            
            Logger.d("Sandbox created with ID: " + sandboxId);
            return sandboxId;
        } catch (Exception e) {
            Logger.e("Failed to create sandbox", e);
            return -1;
        }
    }
    
    private boolean startSandbox(int sandboxId) {
        try {
            Logger.d("Starting sandbox: " + sandboxId);
            
            if (!sandboxRuntime.startRuntime(sandboxId)) {
                return false;
            }
            
            RuntimeState state = sandboxStates.get(sandboxId);
            if (state != null) {
                state.setState(RuntimeState.STATE_RUNNING);
                notifySandboxStateChanged(state);
            }
            
            return true;
        } catch (Exception e) {
            Logger.e("Failed to start sandbox", e);
            return false;
        }
    }
    
    private boolean stopSandbox(int sandboxId) {
        try {
            Logger.d("Stopping sandbox: " + sandboxId);
            
            if (!sandboxRuntime.stopRuntime(sandboxId)) {
                return false;
            }
            
            RuntimeState state = sandboxStates.get(sandboxId);
            if (state != null) {
                state.setState(RuntimeState.STATE_STOPPED);
                notifySandboxStateChanged(state);
            }
            
            return true;
        } catch (Exception e) {
            Logger.e("Failed to stop sandbox", e);
            return false;
        }
    }
    
    private boolean destroySandbox(int sandboxId) {
        try {
            Logger.d("Destroying sandbox: " + sandboxId);
            
            sandboxRuntime.destroyRuntime(sandboxId);
            isolationManager.cleanupIsolation(sandboxId);
            
            sandboxConfigs.remove(sandboxId);
            sandboxStates.remove(sandboxId);
            
            return true;
        } catch (Exception e) {
            Logger.e("Failed to destroy sandbox", e);
            return false;
        }
    }
    
    private RuntimeState getSandboxState(int sandboxId) {
        return sandboxStates.get(sandboxId);
    }
    
    private boolean installApp(int sandboxId, String apkPath) {
        Logger.d("Installing app in sandbox " + sandboxId + ": " + apkPath);
        return true;
    }
    
    private boolean uninstallApp(int sandboxId, String packageName) {
        Logger.d("Uninstalling app in sandbox " + sandboxId + ": " + packageName);
        return true;
    }
    
    private boolean launchApp(int sandboxId, String packageName) {
        Logger.d("Launching app in sandbox " + sandboxId + ": " + packageName);
        return true;
    }
    
    private boolean grantPermission(int sandboxId, String packageName, String permission) {
        Logger.d("Granting permission: " + permission);
        return permissionManager.grantPermission(packageName, permission);
    }
    
    private boolean revokePermission(int sandboxId, String packageName, String permission) {
        Logger.d("Revoking permission: " + permission);
        return permissionManager.revokePermission(packageName, permission);
    }
    
    private List<String> getGrantedPermissions(String packageName) {
        return permissionManager.getGrantedPermissions(packageName);
    }
    
    private void notifySandboxStateChanged(RuntimeState state) {
        for (IIsolationCallback callback : callbacks) {
            try {
                callback.onSandboxStateChanged(state.getSandboxId(), state);
            } catch (RemoteException e) {
                Logger.e("Failed to notify callback", e);
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("IsolationService destroyed");
    }
}
