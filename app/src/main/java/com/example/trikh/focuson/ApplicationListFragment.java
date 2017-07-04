package com.example.trikh.focuson;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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

import com.example.trikh.focuson.data.AppInfo;
import com.example.trikh.focuson.recyclerView.AppListAdapter;

import java.util.ArrayList;
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

        adapter = new AppListAdapter(getContext(), this);

        recyclerView.setAdapter(adapter);
        new LoadApplications().execute();

        return rootview;

    }

    @Override
    public void onClick(AppInfo app) {
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
        editor.commit();
        adapter.notifyDataSetChanged();
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                if ((!isSystemPackage(p))) {
                    String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                    String packageName = p.packageName;
                    Drawable icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                    if (blockAppsSet.contains(packageName)) {
                        appInfoList.add(new AppInfo(appName, icon, true, packageName));
                    } else {
                        appInfoList.add(new AppInfo(appName, icon, false, packageName));
                    }
                }
            }

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
