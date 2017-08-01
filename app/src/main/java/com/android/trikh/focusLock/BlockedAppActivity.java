package com.android.trikh.focusLock;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class BlockedAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_app);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                BlockedAppActivity.this.finish();
            }
        }, 4000);
    }
}
