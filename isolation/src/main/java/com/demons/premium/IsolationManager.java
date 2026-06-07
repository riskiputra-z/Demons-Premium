package com.demons.premium;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.demons.premium.core.SandboxRuntime;
import com.demons.premium.core.IsolationManager;
import com.demons.premium.core.PermissionManager;
import com.demons.premium.core.ProcessManager;
import com.demons.premium.entity.AppConfig;
import com.demons.premium.entity.RuntimeState;
import com.demons.premium.utils.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry point untuk App Isolation Manager
 * Mengelola lifecycle, isolasi, dan komunikasi antar sandbox
 * 
 * @author Demons Premium
 * @version 1.0.0
 */
public class IsolationManager {
    private static final String TAG = "IsolationManager";
    private static volatile IsolationManager instance;
    
    private Context context;
    private SandboxRuntime sandboxRuntime;
    private IsolationManager isolationManager;
    private PermissionManager permissionManager;
    private ProcessManager processManager;
    private IIsolation remoteService;
    private ServiceConnection serviceConnection;
    
    private Map<Integer, AppConfig> sandboxConfigs = new HashMap<>();
    private Map<Integer, RuntimeState> sandboxStates = new HashMap<>();
    
    private IsolationManager() {
    }
    
    /**
     * Singleton instance
     */
    public static IsolationManager getInstance(Context context) {
        if (instance == null) {
            synchronized (IsolationManager.class) {
                if (instance == null) {
                    instance = new IsolationManager();
                    instance.init(context);
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize manager dengan context
     */
    private void init(Context context) {
        this.context = context.getApplicationContext();
        Logger.init(TAG);
        
        sandboxRuntime = new SandboxRuntime(this.context);
        isolationManager = new com.demons.premium.core.IsolationManager(this.context);
        permissionManager = new PermissionManager(this.context);
        processManager = new ProcessManager(this.context);
        
        connectToService();
        Logger.d("IsolationManager initialized");
    }
    
    /**
     * Connect ke IsolationService
     */
    private void connectToService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(android.content.ComponentName name, IBinder service) {
                remoteService = IIsolation.Stub.asInterface(service);
                Logger.d("Connected to IsolationService");
            }
            
            @Override
            public void onServiceDisconnected(android.content.ComponentName name) {
                remoteService = null;
                Logger.w("Disconnected from IsolationService");
            }
        };
        
        Intent intent = new Intent(context, com.demons.premium.service.IsolationService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    /**
     * Buat sandbox baru
     */
    public int createSandbox(AppConfig config) {
        try {
            Logger.d("Creating sandbox: " + config.getName());
            
            if (remoteService != null) {
                int sandboxId = remoteService.createSandbox(config);
                if (sandboxId > 0) {
                    sandboxConfigs.put(sandboxId, config);
                    RuntimeState state = new RuntimeState(
                            sandboxId,
                            config.getName(),
                            RuntimeState.STATE_CREATED,
                            System.currentTimeMillis()
                    );
                    sandboxStates.put(sandboxId, state);
                    Logger.d("Sandbox created with ID: " + sandboxId);
                }
                return sandboxId;
            } else {
                Logger.e("Remote service not available");
                return -1;
            }
        } catch (RemoteException e) {
            Logger.e("Failed to create sandbox", e);
            return -1;
        }
    }
    
    /**
     * Start sandbox
     */
    public boolean startSandbox(int sandboxId) {
        try {
            Logger.d("Starting sandbox: " + sandboxId);
            if (remoteService != null) {
                boolean success = remoteService.startSandbox(sandboxId);
                if (success) {
                    RuntimeState state = sandboxStates.get(sandboxId);
                    if (state != null) {
                        state.setState(RuntimeState.STATE_RUNNING);
                    }
                    Logger.d("Sandbox started");
                }
                return success;
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to start sandbox", e);
            return false;
        }
    }
    
    /**
     * Stop sandbox
     */
    public boolean stopSandbox(int sandboxId) {
        try {
            Logger.d("Stopping sandbox: " + sandboxId);
            if (remoteService != null) {
                boolean success = remoteService.stopSandbox(sandboxId);
                if (success) {
                    RuntimeState state = sandboxStates.get(sandboxId);
                    if (state != null) {
                        state.setState(RuntimeState.STATE_STOPPED);
                    }
                    Logger.d("Sandbox stopped");
                }
                return success;
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to stop sandbox", e);
            return false;
        }
    }
    
    /**
     * Destroy sandbox
     */
    public boolean destroySandbox(int sandboxId) {
        try {
            Logger.d("Destroying sandbox: " + sandboxId);
            if (remoteService != null) {
                boolean success = remoteService.destroySandbox(sandboxId);
                if (success) {
                    sandboxConfigs.remove(sandboxId);
                    sandboxStates.remove(sandboxId);
                    Logger.d("Sandbox destroyed");
                }
                return success;
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to destroy sandbox", e);
            return false;
        }
    }
    
    /**
     * Get sandbox state
     */
    public RuntimeState getSandboxState(int sandboxId) {
        return sandboxStates.get(sandboxId);
    }
    
    /**
     * Install aplikasi di sandbox
     */
    public boolean installApp(int sandboxId, String apkPath) {
        try {
            Logger.d("Installing app in sandbox " + sandboxId + ": " + apkPath);
            if (remoteService != null) {
                return remoteService.installApp(sandboxId, apkPath);
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to install app", e);
            return false;
        }
    }
    
    /**
     * Launch aplikasi di sandbox
     */
    public boolean launchApp(int sandboxId, String packageName) {
        try {
            Logger.d("Launching app in sandbox " + sandboxId + ": " + packageName);
            if (remoteService != null) {
                return remoteService.launchApp(sandboxId, packageName);
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to launch app", e);
            return false;
        }
    }
    
    /**
     * Grant permission ke app di sandbox
     */
    public boolean grantPermission(int sandboxId, String packageName, String permission) {
        try {
            Logger.d("Granting permission: " + permission + " to " + packageName);
            if (remoteService != null) {
                return remoteService.grantPermission(sandboxId, packageName, permission);
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to grant permission", e);
            return false;
        }
    }
    
    /**
     * Revoke permission dari app di sandbox
     */
    public boolean revokePermission(int sandboxId, String packageName, String permission) {
        try {
            Logger.d("Revoking permission: " + permission + " from " + packageName);
            if (remoteService != null) {
                return remoteService.revokePermission(sandboxId, packageName, permission);
            }
            return false;
        } catch (RemoteException e) {
            Logger.e("Failed to revoke permission", e);
            return false;
        }
    }
    
    /**
     * Get granted permissions
     */
    public List<String> getGrantedPermissions(int sandboxId, String packageName) {
        try {
            if (remoteService != null) {
                return remoteService.getGrantedPermissions(sandboxId, packageName);
            }
            return null;
        } catch (RemoteException e) {
            Logger.e("Failed to get granted permissions", e);
            return null;
        }
    }
    
    /**
     * Cleanup resources
     */
    public void destroy() {
        if (serviceConnection != null) {
            context.unbindService(serviceConnection);
        }
        sandboxConfigs.clear();
        sandboxStates.clear();
        Logger.d("IsolationManager destroyed");
    }
    
    public Context getContext() {
        return context;
    }
    
    public SandboxRuntime getSandboxRuntime() {
        return sandboxRuntime;
    }
    
    public com.demons.premium.core.IsolationManager getIsolationCore() {
        return isolationManager;
    }
    
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
    
    public ProcessManager getProcessManager() {
        return processManager;
    }
}
