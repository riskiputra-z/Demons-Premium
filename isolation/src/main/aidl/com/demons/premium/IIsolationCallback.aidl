package com.demons.premium;

import com.demons.premium.entity.RuntimeState;

interface IIsolationCallback {
    void onSandboxStateChanged(int sandboxId, in RuntimeState state);
    void onAppInstalled(int sandboxId, String packageName);
    void onAppUninstalled(int sandboxId, String packageName);
    void onError(int sandboxId, String errorMessage);
}
