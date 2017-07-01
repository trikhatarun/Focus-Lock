package com.example.trikh.focuson.data;

import android.graphics.drawable.Drawable;

/**
 * Created by trikh on 01-07-2017.
 */

public class AppInfo {
    private String appName;
    private Drawable appIcon;

    public AppInfo(String name, Drawable icon) {
        appName = name;
        appIcon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }
}
