package com.demons.premium.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Konfigurasi untuk App Isolation Sandbox
 */
public class AppConfig implements Parcelable {
    private String name;
    private String label;
    private String description;
    private int userId;
    private List<String> permissions = new ArrayList<>();
    private boolean enableFileSystemIsolation = true;
    private boolean enableNetworkIsolation = true;
    private boolean enableProcessIsolation = true;
    private long maxMemory = 512 * 1024 * 1024; // 512MB default
    private int maxProcesses = 100;
    private long createdAt;
    
    public AppConfig() {
        this.createdAt = System.currentTimeMillis();
    }
    
    public AppConfig(String name, String label) {
        this.name = name;
        this.label = label;
        this.createdAt = System.currentTimeMillis();
    }
    
    protected AppConfig(Parcel in) {
        name = in.readString();
        label = in.readString();
        description = in.readString();
        userId = in.readInt();
        permissions = in.createStringArrayList();
        enableFileSystemIsolation = in.readByte() != 0;
        enableNetworkIsolation = in.readByte() != 0;
        enableProcessIsolation = in.readByte() != 0;
        maxMemory = in.readLong();
        maxProcesses = in.readInt();
        createdAt = in.readLong();
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(label);
        dest.writeString(description);
        dest.writeInt(userId);
        dest.writeStringList(permissions);
        dest.writeByte((byte) (enableFileSystemIsolation ? 1 : 0));
        dest.writeByte((byte) (enableNetworkIsolation ? 1 : 0));
        dest.writeByte((byte) (enableProcessIsolation ? 1 : 0));
        dest.writeLong(maxMemory);
        dest.writeInt(maxProcesses);
        dest.writeLong(createdAt);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<AppConfig> CREATOR = new Creator<AppConfig>() {
        @Override
        public AppConfig createFromParcel(Parcel in) {
            return new AppConfig(in);
        }
        
        @Override
        public AppConfig[] newArray(int size) {
            return new AppConfig[size];
        }
    };
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public void addPermission(String permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }
    
    public boolean isEnableFileSystemIsolation() {
        return enableFileSystemIsolation;
    }
    
    public void setEnableFileSystemIsolation(boolean enableFileSystemIsolation) {
        this.enableFileSystemIsolation = enableFileSystemIsolation;
    }
    
    public boolean isEnableNetworkIsolation() {
        return enableNetworkIsolation;
    }
    
    public void setEnableNetworkIsolation(boolean enableNetworkIsolation) {
        this.enableNetworkIsolation = enableNetworkIsolation;
    }
    
    public boolean isEnableProcessIsolation() {
        return enableProcessIsolation;
    }
    
    public void setEnableProcessIsolation(boolean enableProcessIsolation) {
        this.enableProcessIsolation = enableProcessIsolation;
    }
    
    public long getMaxMemory() {
        return maxMemory;
    }
    
    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }
    
    public int getMaxProcesses() {
        return maxProcesses;
    }
    
    public void setMaxProcesses(int maxProcesses) {
        this.maxProcesses = maxProcesses;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public String toString() {
        return "AppConfig{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", enableFileSystemIsolation=" + enableFileSystemIsolation +
                ", enableNetworkIsolation=" + enableNetworkIsolation +
                ", enableProcessIsolation=" + enableProcessIsolation +
                ", maxMemory=" + maxMemory +
                ", maxProcesses=" + maxProcesses +
                "}";
    }
}
