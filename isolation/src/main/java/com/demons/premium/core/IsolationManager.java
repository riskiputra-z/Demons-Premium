package com.demons.premium.core;

import android.content.Context;

import com.demons.premium.utils.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Mengelola isolasi sandbox dari sistem host
 */
public class IsolationManager {
    private static final String TAG = "IsolationManager";
    private Context context;
    private Map<Integer, IsolationContext> isolationContexts = new HashMap<>();
    
    private static class IsolationContext {
        int sandboxId;
        File sandboxDir;
        File filesystemRoot;
        File dataDir;
        boolean fileSystemIsolationEnabled;
        boolean networkIsolationEnabled;
        boolean processIsolationEnabled;
    }
    
    public IsolationManager(Context context) {
        this.context = context;
    }
    
    /**
     * Setup isolasi untuk sandbox
     */
    public boolean setupIsolation(int sandboxId, String sandboxName) {
        try {
            Logger.d("Setting up isolation for sandbox: " + sandboxName);
            
            IsolationContext isolationContext = new IsolationContext();
            isolationContext.sandboxId = sandboxId;
            
            File sandboxDir = new File(context.getFilesDir(), "sandbox/" + sandboxName);
            if (!sandboxDir.exists()) {
                sandboxDir.mkdirs();
            }
            isolationContext.sandboxDir = sandboxDir;
            
            File filesystemRoot = new File(sandboxDir, "fs");
            if (!filesystemRoot.exists()) {
                filesystemRoot.mkdirs();
            }
            isolationContext.filesystemRoot = filesystemRoot;
            
            File dataDir = new File(sandboxDir, "data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            isolationContext.dataDir = dataDir;
            
            isolationContext.fileSystemIsolationEnabled = true;
            isolationContext.networkIsolationEnabled = true;
            isolationContext.processIsolationEnabled = true;
            
            isolationContexts.put(sandboxId, isolationContext);
            Logger.d("Isolation setup completed for sandbox: " + sandboxName);
            
            return true;
        } catch (Exception e) {
            Logger.e("Failed to setup isolation", e);
            return false;
        }
    }
    
    /**
     * Get isolated filesystem root untuk sandbox
     */
    public File getIsolatedFilesystemRoot(int sandboxId) {
        IsolationContext context = isolationContexts.get(sandboxId);
        if (context != null) {
            return context.filesystemRoot;
        }
        return null;
    }
    
    /**
     * Get isolated data directory untuk sandbox
     */
    public File getIsolatedDataDir(int sandboxId) {
        IsolationContext context = isolationContexts.get(sandboxId);
        if (context != null) {
            return context.dataDir;
        }
        return null;
    }
    
    /**
     * Check apakah file system isolation enabled
     */
    public boolean isFileSystemIsolationEnabled(int sandboxId) {
        IsolationContext context = isolationContexts.get(sandboxId);
        if (context != null) {
            return context.fileSystemIsolationEnabled;
        }
        return false;
    }
    
    /**
     * Check apakah network isolation enabled
     */
    public boolean isNetworkIsolationEnabled(int sandboxId) {
        IsolationContext context = isolationContexts.get(sandboxId);
        if (context != null) {
            return context.networkIsolationEnabled;
        }
        return false;
    }
    
    /**
     * Check apakah process isolation enabled
     */
    public boolean isProcessIsolationEnabled(int sandboxId) {
        IsolationContext context = isolationContexts.get(sandboxId);
        if (context != null) {
            return context.processIsolationEnabled;
        }
        return false;
    }
    
    /**
     * Cleanup isolasi untuk sandbox
     */
    public boolean cleanupIsolation(int sandboxId) {
        try {
            Logger.d("Cleaning up isolation for sandbox: " + sandboxId);
            
            IsolationContext context = isolationContexts.get(sandboxId);
            if (context != null) {
                deleteDirectory(context.sandboxDir);
                isolationContexts.remove(sandboxId);
                Logger.d("Isolation cleanup completed");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            Logger.e("Failed to cleanup isolation", e);
            return false;
        }
    }
    
    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        return dir.delete();
    }
}
