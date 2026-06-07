package com.demons.premium.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * State dari Sandbox Runtime
 */
public class RuntimeState implements Parcelable {
    public static final int STATE_CREATED = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_PAUSED = 3;
    public static final int STATE_STOPPED = 4;
    public static final int STATE_ERROR = 5;
    
    private int sandboxId;
    private String sandboxName;
    private int state;
    private long timestamp;
    private long uptime = 0;
    private int runningAppsCount = 0;
    private long currentMemoryUsage = 0;
    
    public RuntimeState() {
    }
    
    public RuntimeState(int sandboxId, String sandboxName, int state, long timestamp) {
        this.sandboxId = sandboxId;
        this.sandboxName = sandboxName;
        this.state = state;
        this.timestamp = timestamp;
    }
    
    protected RuntimeState(Parcel in) {
        sandboxId = in.readInt();
        sandboxName = in.readString();
        state = in.readInt();
        timestamp = in.readLong();
        uptime = in.readLong();
        runningAppsCount = in.readInt();
        currentMemoryUsage = in.readLong();
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sandboxId);
        dest.writeString(sandboxName);
        dest.writeInt(state);
        dest.writeLong(timestamp);
        dest.writeLong(uptime);
        dest.writeInt(runningAppsCount);
        dest.writeLong(currentMemoryUsage);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<RuntimeState> CREATOR = new Creator<RuntimeState>() {
        @Override
        public RuntimeState createFromParcel(Parcel in) {
            return new RuntimeState(in);
        }
        
        @Override
        public RuntimeState[] newArray(int size) {
            return new RuntimeState[size];
        }
    };
    
    // Getters and Setters
    public int getSandboxId() {
        return sandboxId;
    }
    
    public void setSandboxId(int sandboxId) {
        this.sandboxId = sandboxId;
    }
    
    public String getSandboxName() {
        return sandboxName;
    }
    
    public void setSandboxName(String sandboxName) {
        this.sandboxName = sandboxName;
    }
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public String getStateString() {
        switch (state) {
            case STATE_CREATED:
                return "CREATED";
            case STATE_RUNNING:
                return "RUNNING";
            case STATE_PAUSED:
                return "PAUSED";
            case STATE_STOPPED:
                return "STOPPED";
            case STATE_ERROR:
                return "ERROR";
            default:
                return "UNKNOWN";
        }
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public long getUptime() {
        return uptime;
    }
    
    public void setUptime(long uptime) {
        this.uptime = uptime;
    }
    
    public int getRunningAppsCount() {
        return runningAppsCount;
    }
    
    public void setRunningAppsCount(int runningAppsCount) {
        this.runningAppsCount = runningAppsCount;
    }
    
    public long getCurrentMemoryUsage() {
        return currentMemoryUsage;
    }
    
    public void setCurrentMemoryUsage(long currentMemoryUsage) {
        this.currentMemoryUsage = currentMemoryUsage;
    }
    
    @Override
    public String toString() {
        return "RuntimeState{" +
                "sandboxId=" + sandboxId +
                ", sandboxName='" + sandboxName + '\'' +
                ", state=" + getStateString() +
                ", timestamp=" + timestamp +
                ", uptime=" + uptime +
                ", runningAppsCount=" + runningAppsCount +
                ", currentMemoryUsage=" + currentMemoryUsage +
                "}";
    }
}
