package com.fullsail.franceschinoel_ce05;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

// Noel Franceschi
// MDF3 1610
// MainActivity.java

public class MainActivity extends AppCompatActivity implements MainFragment.MainActivityFragmentInterface {

    public static final String TAG = "MainActivity";

    MusicPlayerService musicPlayerService;

    public boolean isServiceBound = false;

    MainFragment mainFragment;

    int mediaPosition = 0;

    Intent audioIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter broadcastUpdaterFilter = new IntentFilter();

        broadcastUpdaterFilter.addAction(getString(R.string.SEEKER_UPDATE));

        broadcastUpdaterFilter.addAction(getResources().getString(R.string.NOTIFICATION_UPDATE));

        registerReceiver(broadcastReceiver, broadcastUpdaterFilter);

        audioIntent = new Intent(this, MusicPlayerService.class);

        bindService(audioIntent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(broadcastReceiver);

        unbindService(serviceConnection);

    }

    @Override
    public void shuffle() {

        if (isServiceBound) {

            musicPlayerService.shuffle();

        }
    }

    @Override
    public void backward() {

        if (isServiceBound) {

            mediaPosition = musicPlayerService.previous();

            mainFragment.updateUI();

            musicPlayerService.buildNotification();

            musicPlayerService.notificationManager.notify(R.integer.NOTIFICATION_ID, musicPlayerService.notification);
        }
    }

    @Override
    public void play() {

        if (isServiceBound) {

            mediaPosition = musicPlayerService.play();

            mediaPosition = musicPlayerService.mediaPosition;

            musicPlayerService.buildNotification();

            musicPlayerService.startForeground(R.integer.NOTIFICATION_ID, musicPlayerService.notification);

            mainFragment.updateUI();

            musicPlayerService.buildNotification();

            musicPlayerService.notificationManager.notify(R.integer.NOTIFICATION_ID, musicPlayerService.notification);

        }
    }

    @Override
    public void pause() {

        if (isServiceBound) {

            musicPlayerService.pause();

        }
    }

    @Override
    public void stop() {

        if (isServiceBound) {

            musicPlayerService.stop();

            mediaPosition = 0;

            musicPlayerService.buildNotification();

            musicPlayerService.stopForeground(true);

            mainFragment.seekBar.setMax(0);

            mainFragment.seekBar.setProgress(0);

        }
    }

    @Override
    public void next() {

        if (isServiceBound) {

            mediaPosition = musicPlayerService.next();

            mainFragment.updateUI();

            musicPlayerService.buildNotification();

            musicPlayerService.notificationManager.notify(R.integer.NOTIFICATION_ID, musicPlayerService.notification);

        }
    }

    @Override
    public void loop() {

        if (isServiceBound) {

            musicPlayerService.loop();

        }
    }

    @Override
    public int getCurrentMediaPosition() {

        return mediaPosition;

    }

    @Override
    public void seekTo(int i) {

        if (isServiceBound) {

            musicPlayerService.seekTo(i);

        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String intentAction = intent.getAction();

            if (intentAction.equals(getString(R.string.SEEKER_UPDATE))) {

                int length = intent.getIntExtra(getString(R.string.MEDIA_LENGTH), -1);

                int progress = intent.getIntExtra(getString(R.string.SEEKER_PROGRESS), -1);

                if (length < 0 || progress < 0) {

                    throw new IllegalStateException("MainActivityFragmentInterface not implemented...");
                }


                mainFragment.seekBar.setMax(length / 1000);
                mainFragment.seekBar.setProgress(progress / 1000);
            }

            if (intentAction.equals(getString(R.string.NOTIFICATION_UPDATE))) {

                mediaPosition = intent.getIntExtra(getString(R.string.MEDIA_POSITION), -1);
                mainFragment.updateUI();

            }
        }
    };

    public ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder baBinder) {

            Log.e(TAG, "onServiceConnected: EXECUTED");

            MusicPlayerService.PlayerServiceBinder binder = (MusicPlayerService.PlayerServiceBinder) baBinder;

            musicPlayerService = binder.getService();

            mediaPosition = musicPlayerService.mediaPosition;

            mainFragment.shuffle.setChecked(musicPlayerService.musicPlayerStatesManager.shuffling);

            mainFragment.repeat.setChecked(musicPlayerService.musicPlayerStatesManager.looping);

            startService(audioIntent);

            mainFragment.updateUI();

            isServiceBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {

            Log.e(TAG, "onServiceDisconnected: EXECUTED");

            musicPlayerService = null;
        }
    };
}
