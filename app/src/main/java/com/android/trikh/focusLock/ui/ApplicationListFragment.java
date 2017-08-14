package com.android.trikh.focusLock.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.trikh.focusLock.BackgroundService;
import com.android.trikh.focusLock.R;
import com.android.trikh.focusLock.alarmPackage.AlarmService;
import com.android.trikh.focusLock.data.AppInfo;
import com.android.trikh.focusLock.recyclerView.AppListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 24-06-2017.
 */

public class ApplicationListFragment extends Fragment implements AppListAdapter.OnAppClickListener {

    @BindView(R.id.applicationList)
    RecyclerView recyclerView;

    private ArrayList<AppInfo> appInfoList;
    private AppListAdapter adapter;
    private SharedPreferences preferences;
    private Set<String> blockAppsSet;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.application_list, container, false);

        ButterKnife.bind(this, rootview);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        blockAppsSet = preferences.getStringSet(getString(R.string.block_app_set_key), new HashSet<String>());

        appInfoList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AppListAdapter(getContext(), this);

        recyclerView.setAdapter(adapter);
        new LoadApplications().execute();

        return rootview;

    }

    @Override
    public void onClick(AppInfo app) {
        if (!BackgroundService.isServiceRunningInForeground(getContext(), AlarmService.class)) {
            SharedPreferences.Editor editor = preferences.edit();
            String packageName = app.getPackageName();
            if (blockAppsSet.contains(packageName)) {
                blockAppsSet.remove(packageName);
                app.setAppBlockedstatus(false);
            } else {
                blockAppsSet.add(packageName);
                app.setAppBlockedstatus(true);
            }
            editor.putStringSet(getString(R.string.block_app_set_key), blockAppsSet);
            editor.apply();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "The blocked list cannot be edited during FOCUS period.", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            PackageManager packageManager = getActivity().getPackageManager();

            List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
            for (ApplicationInfo app : apps) {
                String appName, packageName;
                Drawable icon;
                //checks for flags; if flagged, check if updated system app
                if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    appName = (String) packageManager.getApplicationLabel(app);
                    packageName = app.packageName;
                    icon = app.loadIcon(packageManager);
                    if (blockAppsSet.contains(packageName)) {
                        appInfoList.add(new AppInfo(appName, icon, true, packageName));
                    } else {
                        appInfoList.add(new AppInfo(appName, icon, false, packageName));
                    }
                }
                //it's a system app, not interested
                else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }
                //in this case, it should be a user-installed app
                else {
                    appName = (String) packageManager.getApplicationLabel(app);
                    packageName = app.packageName;
                    icon = app.loadIcon(packageManager);
                    if (blockAppsSet.contains(packageName)) {
                        appInfoList.add(new AppInfo(appName, icon, true, packageName));
                    } else {
                        appInfoList.add(new AppInfo(appName, icon, false, packageName));
                    }
                }
            }
            Collections.sort(appInfoList, new Comparator<AppInfo>() {
                public int compare(AppInfo obj1, AppInfo obj2) {
                    return obj1.getAppName().compareToIgnoreCase(obj2.getAppName()); // To compare string values
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            adapter.setAppList(appInfoList);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null, "Loading apps info...");
            super.onPreExecute();
        }
    }
}
