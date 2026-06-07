package com.demons.premium.core;

import android.content.Context;

import com.demons.premium.entity.ProcessInfo;
import com.demons.premium.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mengelola processes dalam sandbox
 */
public class ProcessManager {
    private static final String TAG = "ProcessManager";
    private Context context;
    private Map<Integer, ProcessInfo> processes = new HashMap<>();
    
    public ProcessManager(Context context) {
        this.context = context;
    }
    
    /**
     * Register process baru
     */
    public int registerProcess(ProcessInfo processInfo) {
        try {
            Logger.d("Registering process: " + processInfo.getProcessName());
            
            int pid = processInfo.getPid();
            processes.put(pid, processInfo);
            
            Logger.d("Process registered with PID: " + pid);
            return pid;
        } catch (Exception e) {
            Logger.e("Failed to register process", e);
            return -1;
        }
    }
    
    /**
     * Unregister process
     */
    public boolean unregisterProcess(int pid) {
        try {
            Logger.d("Unregistering process: " + pid);
            
            ProcessInfo processInfo = processes.remove(pid);
            if (processInfo != null) {
                Logger.d("Process unregistered");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            Logger.e("Failed to unregister process", e);
            return false;
        }
    }
    
    /**
     * Get process info
     */
    public ProcessInfo getProcessInfo(int pid) {
        return processes.get(pid);
    }
    
    /**
     * Get all processes
     */
    public List<ProcessInfo> getAllProcesses() {
        return new ArrayList<>(processes.values());
    }
    
    /**
     * Get processes by package name
     */
    public List<ProcessInfo> getProcessesByPackage(String packageName) {
        List<ProcessInfo> result = new ArrayList<>();
        for (ProcessInfo process : processes.values()) {
            if (process.getPackageName().equals(packageName)) {
                result.add(process);
            }
        }
        return result;
    }
    
    /**
     * Update process memory usage
     */
    public boolean updateProcessMemoryUsage(int pid, long memoryUsage) {
        try {
            ProcessInfo processInfo = processes.get(pid);
            if (processInfo != null) {
                processInfo.setMemoryUsage(memoryUsage);
                return true;
            }
            return false;
        } catch (Exception e) {
            Logger.e("Failed to update process memory", e);
            return false;
        }
    }
    
    /**
     * Update process CPU time
     */
    public boolean updateProcessCpuTime(int pid, long cpuTime) {
        try {
            ProcessInfo processInfo = processes.get(pid);
            if (processInfo != null) {
                processInfo.setCpuTime(cpuTime);
                return true;
            }
            return false;
        } catch (Exception e) {
            Logger.e("Failed to update process CPU time", e);
            return false;
        }
    }
    
    /**
     * Get total memory usage dari semua processes
     */
    public long getTotalMemoryUsage() {
        long total = 0;
        for (ProcessInfo process : processes.values()) {
            total += process.getMemoryUsage();
        }
        return total;
    }
    
    /**
     * Get process count
     */
    public int getProcessCount() {
        return processes.size();
    }
    
    /**
     * Terminate all processes
     */
    public boolean terminateAllProcesses() {
        try {
            Logger.d("Terminating all processes");
            processes.clear();
            return true;
        } catch (Exception e) {
            Logger.e("Failed to terminate all processes", e);
            return false;
        }
    }
}
