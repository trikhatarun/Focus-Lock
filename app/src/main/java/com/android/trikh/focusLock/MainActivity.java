package com.android.trikh.focusLock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

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

        startService(new Intent(this, BackgroundService.class));

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                if (tabId == R.id.home) {
                    fragment = new HomeFragment();
                    setTitle(getString(R.string.item1_title));
                } else if (tabId == R.id.applications) {
                    fragment = new ApplicationListFragment();
                    setTitle(getString(R.string.item3_title));
                }
                if (oldTabId != -1) {
                    Log.d("First Tab: ", " This is first time tab");
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
    }

}
