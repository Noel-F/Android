package com.fullsail.franceschinoel_ce03;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

// Noel Franceschi
// MDF3 - 1610
// NewsReceiver.java

public class NewsReceiver extends BroadcastReceiver {

    public static final String ACTION_SAVE_DATA = "com.fullsail.franceschinoel_ce03.ACTION_SAVE_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_SAVE_DATA)) {

            new NewsDatabase().saveSerializable(context, NewsService.currentNews);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(NewsService.NOTIFICATION_ID);

            Intent updateIntent = new Intent(MainActivity.ACTION_UPDATE_UI);

            context.sendBroadcast(updateIntent);

        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, NewsService.class);

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 6000, 60000, pendingIntent);

        }
    }
}

