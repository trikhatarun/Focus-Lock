package com.android.trikh.focusLock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by trikh on 02-08-2017.
 */

public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context.getApplicationContext(), BackgroundService.class));
    }
}
