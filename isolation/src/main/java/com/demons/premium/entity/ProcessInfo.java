package com.demons.premium.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Informasi tentang Process dalam Sandbox
 */
public class ProcessInfo {
    private int pid;
    private int uid;
    private String processName;
    private String packageName;
    private long startTime;
    private long cpuTime;
    private long memoryUsage;
    private int priority;
    private int state;
    private List<String> permissions = new ArrayList<>();
    
    public ProcessInfo(int pid, String processName, String packageName) {
        this.pid = pid;
        this.processName = processName;
        this.packageName = packageName;
        this.startTime = System.currentTimeMillis();
        this.state = 1; // RUNNING
    }
    
    // Getters and Setters
    public int getPid() {
        return pid;
    }
    
    public void setPid(int pid) {
        this.pid = pid;
    }
    
    public int getUid() {
        return uid;
    }
    
    public void setUid(int uid) {
        this.uid = uid;
    }
    
    public String getProcessName() {
        return processName;
    }
    
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getCpuTime() {
        return cpuTime;
    }
    
    public void setCpuTime(long cpuTime) {
        this.cpuTime = cpuTime;
    }
    
    public long getMemoryUsage() {
        return memoryUsage;
    }
    
    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public void addPermission(String permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }
    
    @Override
    public String toString() {
        return "ProcessInfo{" +
                "pid=" + pid +
                ", uid=" + uid +
                ", processName='" + processName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", memoryUsage=" + memoryUsage +
                "}";
    }
}
