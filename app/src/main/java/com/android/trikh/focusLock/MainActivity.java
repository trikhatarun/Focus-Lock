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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.trikh.focusLock.ui.ApplicationListFragment;
import com.android.trikh.focusLock.ui.FreeVideosFragment;
import com.android.trikh.focusLock.ui.HomeFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    private int oldTabId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("first_time", true)) {
            startActivity(new Intent(this, MainIntroActivity.class));

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("first_time", false);
            editor.commit();
        }

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

        MobileAds.initialize(this, getString(R.string.app_id_admob));

        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra("fromService", false)) {
                bottomBar.selectTabAtPosition(2, true);
            }
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
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
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

}
