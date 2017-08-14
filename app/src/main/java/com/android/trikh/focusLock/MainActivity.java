package com.android.trikh.focusLock;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.android.trikh.focusLock.data.JsonHelper;
import com.android.trikh.focusLock.ui.ApplicationListFragment;
import com.android.trikh.focusLock.ui.FreeVideosFragment;
import com.android.trikh.focusLock.ui.HomeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    private int oldTabId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        ButterKnife.bind(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission required ")
                    .setMessage("This permission is required to stop distractions!");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                }
            });

            builder.show();
        }

        startService(new Intent(this, BackgroundService.class));

        /*MobileAds.initialize(this, getString(R.string.app_id_admob));

        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);*/

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment;
                if (tabId == R.id.home) {
                    fragment = new HomeFragment();
                    setTitle(getString(R.string.item1_title));
                } else if (tabId == R.id.applications) {
                    fragment = new ApplicationListFragment();
                    setTitle(getString(R.string.item3_title));
                } else {
                    fragment = new FreeVideosFragment();
                    setTitle(getString(R.string.item2_title));
                }
                if (oldTabId != -1) {
                    ft.addToBackStack(null);
                }
                if (oldTabId < tabId) {
                    ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    oldTabId = tabId;
                } else {
                    ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                    oldTabId = tabId;
                }

                ft.replace(R.id.contentContainer, fragment);
                ft.commit();
            }
        });

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("my_first_time", true)) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String country = tm.getSimCountryIso().toLowerCase();
            JSONArray data = null;
            try {
                data = new JSONArray(new JsonHelper(this).loadJSONFromAsset());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject jsonobject = data.getJSONObject(i);
                        String countryJson = jsonobject.getString("country");
                        if (countryJson.toLowerCase().equals(country)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                            //Todo[1]: subtract age of person
                            editor.putInt(getString(R.string.life_days_left_key), Integer.valueOf(jsonobject.getString("2015")) * 365);
                            editor.putString(getString(R.string.country_key), countryJson);
                            editor.apply();
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            settings.edit().putBoolean("my_first_time", false).commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

}
