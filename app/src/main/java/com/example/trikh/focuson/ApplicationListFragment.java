package com.example.trikh.focuson;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trikh.focuson.data.AppInfo;
import com.example.trikh.focuson.recyclerView.AppListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trikh on 24-06-2017.
 */

public class ApplicationListFragment extends Fragment {

    @BindView(R.id.applicationList)
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.application_list, container, false);

        ButterKnife.bind(this, rootview);

        ArrayList<AppInfo> appInfoList = new ArrayList<>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p))) {
                String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                appInfoList.add(new AppInfo(appName, icon));
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        AppListAdapter adapter = new AppListAdapter(getContext());
        adapter.setAppList(appInfoList);

        recyclerView.setAdapter(adapter);

        return rootview;

    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
