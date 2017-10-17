package com.fullsail.franceschinoel_ce03;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

// Noel Franceschi
// MDF3 - 1610
// MainActivity.java

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_UPDATE_UI = "com.fullsail.franceschinoel_ce03.ACTION_UPDATE_UI";

    public static final String API_URL = "http://api.nytimes.com/svc/topstories/v1/world.json?api-key=ca30f3335ef44d50ab0af492c2004c75";

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, NewsService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 6000, 60000, pendingIntent);

        Intent serviceIntent = new Intent(this, NewsService.class);
        startService(serviceIntent);

        ArrayList<News> newsArrayList = new NewsDatabase().readSerializable(this);

        NewsAdapter customAdapter = new NewsAdapter(newsArrayList, this);
        mainFragment.setListAdapter(customAdapter);

    }

    @Override
    protected void onResume() {

        super.onResume();

        registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_UI));

    }

    @Override
    protected void onPause() {

        super.onPause();

        unregisterReceiver(mReceiver);

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_UPDATE_UI)) {

                ArrayList<News> newsArrayList = new NewsDatabase().readSerializable(context);

                NewsAdapter customAdapter = (NewsAdapter) mainFragment.getListAdapter();
                customAdapter.updateData(newsArrayList);

            }
        }
    };
}

