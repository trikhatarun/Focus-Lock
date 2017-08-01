package com.android.trikh.focusLock.data;

import android.graphics.drawable.Drawable;

/**
 * Created by trikh on 01-07-2017.
 */

public class AppInfo {
    private String appName;
    private Drawable appIcon;
    private Boolean appBlockedstatus;
    private String appPackageName;

    public AppInfo(String name, Drawable icon, Boolean blocked, String packageName) {
        appName = name;
        appIcon = icon;
        appBlockedstatus = blocked;
        appPackageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppBlockedstatus(Boolean status) {
        appBlockedstatus = status;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public Boolean getAppBlocked() {
        return appBlockedstatus;
    }

    public String getPackageName() {
        return appPackageName;
    }
}
