package com.android.trikh.focusLock;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.android.trikh.focusLock.alarmPackage.AlarmHelper;
import com.android.trikh.focusLock.alarmPackage.AlarmService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.android.trikh.focusLock.PreferenceHelper.getDayToday;
import static com.android.trikh.focusLock.PreferenceHelper.getDaysLeft;
import static com.android.trikh.focusLock.PreferenceHelper.getFirstDay;
import static com.android.trikh.focusLock.PreferenceHelper.reduceDaysLeft;
import static com.android.trikh.focusLock.PreferenceHelper.updateDayToday;
import static com.android.trikh.focusLock.ui.FreeVideosFragment.VIDEO_ID;
import static java.lang.Integer.valueOf;

public class BackgroundService extends Service {
    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.focus_block_2)
                .setContentTitle("Focus Lock")
                .setPriority(Notification.PRIORITY_MIN)
                .setContentText("Ensuring additional security measures and failure prevention")
                .build();

        startForeground(1337, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                doOnceAdayTask(getApplicationContext());

                boolean alarmUp = (PendingIntent.getService(BackgroundService.this, 110,
                        new Intent(BackgroundService.this, AlarmService.class),
                        PendingIntent.FLAG_NO_CREATE) != null);

                if (!alarmUp) {
                    setAlarmsNow(BackgroundService.this);
                }
                if (isServiceRunningInForeground(BackgroundService.this, AlarmService.class)) {
                    stopService();
                } else {
                    sendBroadcast(new Intent("RestartAppPlease"));
                }
            }
        }, 90000);
        return START_STICKY;
    }

    private void doOnceAdayTask(final Context context) {
        Calendar calendar = Calendar.getInstance();
        final int dayToday = calendar.get(Calendar.DAY_OF_YEAR);
        if (getDayToday(context) < dayToday) {
            reduceDaysLeft(context);
            updateDayToday(context);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.days_left);
            ComponentName thisWidget = new ComponentName(context, DaysLeftWidget.class);
            remoteViews.setTextViewText(R.id.appwidget_text, String.valueOf(getDaysLeft(context)));
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("fromService", true);

            PendingIntent intentClicked = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Bitmap videoThumbnail = null;

            try {
                videoThumbnail = new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        try {
                            return Picasso.with(context).load("http://img.youtube.com/vi/" + VIDEO_ID[dayToday - getFirstDay(context)] + "/default.jpg")
                                    .get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Daily Motivation ready")
                    .setSmallIcon(R.drawable.focus_block_2_icon)
                    .setLargeIcon(videoThumbnail)
                    .setContentText("New motivational video ready")
                    .setContentIntent(intentClicked)
                    .build();

            notificationManager.notify(101, notification);

        }
    }

    private void setAlarmsNow(Context context) {
        AlarmHelper ah = new AlarmHelper(context);
        ah.setAlarm(PreferenceHelper.getMorningTimeString(context), valueOf(context.getString(R.string.request_code_for_morning)));
        ah.setAlarm(PreferenceHelper.getSleepTimeString(context), valueOf(context.getString(R.string.request_code_for_night)));
    }

    private void stopService() {
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
