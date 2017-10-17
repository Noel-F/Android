package com.fullsail.franceschinoel_ce05;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// Noel Franceschi
// MDF3 1610
// MusicPlayerService.java

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final String TAG = "MusicPlayerService";

    MusicPlayerStatesManager musicPlayerStatesManager;

    public static final ArrayList<String> songs = new ArrayList<>(Arrays.asList(

            String.valueOf("android.resource://com.fullsail.franceschinoel_ce05/" + R.raw.the_only_thing),
            String.valueOf("android.resource://com.fullsail.franceschinoel_ce05/" + R.raw.it_was_written),
            String.valueOf("android.resource://com.fullsail.franceschinoel_ce05/" + R.raw.the_real_is_back)

    ));

    MediaPlayer mediaPlayer;

    Notification notification;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;

    int seekerPosition = 0;
    int mediaPosition = 0;
    int mediaLength = 0;

    private final Handler handler = new Handler();

    public class PlayerServiceBinder extends Binder {

        MusicPlayerService getService() {

            return MusicPlayerService.this;

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        Log.e(TAG, "onStartCommand: " + action);

        Log.e(TAG, "onStartCommand: EXECUTED");

        if (action != null) {

            if (action.equals(getString(R.string.PREVIOUS_CLICKED))) {

                previous();

                buildNotification();

                notificationManager.notify(R.integer.NOTIFICATION_ID, notification);

            }

            if (action.equals(getString(R.string.NEXT_CLICKED))) {

                next();

                buildNotification();

                notificationManager.notify(R.integer.NOTIFICATION_ID, notification);

            }

            Intent updateIntent = new Intent(getResources().getString(R.string.NOTIFICATION_UPDATE));
            updateIntent.putExtra(getString(R.string.MEDIA_POSITION), mediaPosition);

            sendBroadcast(updateIntent);

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.e(TAG, "onBind: EXECUTED");

        return new PlayerServiceBinder();

    }

    @Override
    public void onCreate() {

        super.onCreate();

        Log.e(TAG, "onCreate: EXECUTED");

        musicPlayerStatesManager = new MusicPlayerStatesManager(R.integer.STATE_IDLE);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.e(TAG, "onPrepared: EXECUTED");

        musicPlayerStatesManager.playerState = R.integer.STATE_PREPARED;

        mediaPlayer.start();

        musicPlayerStatesManager.playerState = R.integer.STATE_STARTED;

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        Log.e(TAG, "onCompletion: EXECUTED");

        if (musicPlayerStatesManager.shuffling) {

            int temp = mediaPosition;

            while (temp == mediaPosition) {

                temp = new Random().nextInt(songs.size());

            }

            mediaPosition = temp;

            play();
        }

        musicPlayerStatesManager.playerState = R.integer.STATE_COMPLETED;

    }


    public int previous() {

        Log.e(TAG, "previous: EXECUTED");

        if (musicPlayerStatesManager.looping) {

            play();

            return mediaPosition;

        }

        if (musicPlayerStatesManager.shuffling) {

            int temp = mediaPosition;

            while (temp == mediaPosition) {

                temp = new Random().nextInt(songs.size());

            }

            mediaPosition = temp;

            play();

            return mediaPosition;

        }

        if (mediaPosition > 0) mediaPosition--;

        else mediaPosition = songs.size() - 1;

        play();

        return mediaPosition;
    }

    public int play() {

        Log.e(TAG, "play: EXECUTED");

        boolean isPaused = musicPlayerStatesManager.playerState == R.integer.STATE_PAUSED;

        boolean isCOMPLETED = musicPlayerStatesManager.playerState == R.integer.STATE_COMPLETED;

        if (isPaused || isCOMPLETED) {

            mediaPlayer.start();

            musicPlayerStatesManager.playerState = R.integer.STATE_STARTED;

        } else {

            mediaPlayer.reset();

            musicPlayerStatesManager.playerState = R.integer.STATE_IDLE;

            try {

                mediaPlayer.setDataSource(this, Uri.parse(songs.get(mediaPosition)));

                musicPlayerStatesManager.playerState = R.integer.STATE_INITIALIZE;

            } catch (Exception e) {

                e.printStackTrace();

            }

            mediaPlayer.prepareAsync();

            musicPlayerStatesManager.playerState = R.integer.STATE_PREPARING;

        }

        setUpHandler();

        return mediaPosition;

    }

    private void setUpHandler() {

        handler.removeCallbacks(updateUI);

        handler.postDelayed(updateUI, 1000);

    }

    private Runnable updateUI = new Runnable() {

        @Override
        public void run() {

            if (mediaPlayer != null) {

                seekerPosition = mediaPlayer.getCurrentPosition();

                mediaLength = mediaPlayer.getDuration();
            }

            Intent seekerIntent = new Intent(getString(R.string.SEEKER_UPDATE));
            seekerIntent.putExtra(getResources().getString(R.string.SEEKER_PROGRESS), seekerPosition);
            seekerIntent.putExtra(getResources().getString(R.string.MEDIA_LENGTH), mediaLength);

            getApplicationContext().sendBroadcast(seekerIntent);

            handler.postDelayed(this, 1000);

        }

    };

    public void pause() {

        Log.e(TAG, "pause: EXECUTED");

        boolean isStarted = musicPlayerStatesManager.playerState == R.integer.STATE_STARTED;

        if (isStarted) {

            mediaPlayer.pause();

            musicPlayerStatesManager.playerState = R.integer.STATE_PAUSED;

        }
    }

    public void stop() {

        Log.e(TAG, "stop: EXECUTED");

        boolean isPaused = musicPlayerStatesManager.playerState == R.integer.STATE_PAUSED;

        boolean isStarted = musicPlayerStatesManager.playerState == R.integer.STATE_STARTED;

        if (isPaused || isStarted) {

            mediaPlayer.stop();

            mediaPosition = 0;

            musicPlayerStatesManager.playerState = R.integer.STATE_STOPPED;

            notificationManager.cancelAll();

            handler.removeCallbacks(updateUI);

        }
    }

    public int next() {

        Log.e(TAG, "next: EXECUTED");

        if (musicPlayerStatesManager.looping) {

            play();

            return mediaPosition;
        }

        if (musicPlayerStatesManager.shuffling) {

            int temp = mediaPosition;

            while (temp == mediaPosition) {

                temp = new Random().nextInt(songs.size());

            }

            mediaPosition = temp;

            play();

            return mediaPosition;
        }

        if (mediaPosition < songs.size() - 1) {

            mediaPosition++;

        } else {

            mediaPosition = 0;

        }

        play();

        return mediaPosition;
    }

    public void seekTo(int i) {

        mediaPlayer.seekTo((i * mediaPlayer.getDuration()) / (mediaPlayer.getDuration() / 1000));

    }

    public void shuffle() {

        Log.e(TAG, "shuffle: EXECUTED");

        musicPlayerStatesManager.shuffling = !musicPlayerStatesManager.shuffling;

    }

    public void loop() {

        Log.e(TAG, "loop: EXECUTED");

        if (musicPlayerStatesManager.looping) {

            mediaPlayer.setLooping(false);

            musicPlayerStatesManager.looping = false;

        } else {

            mediaPlayer.setLooping(true);

            musicPlayerStatesManager.looping = true;

        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (mediaPlayer != null) {

            mediaPlayer.release();

            mediaPlayer = null;

        }
    }

    public void buildNotification() {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, Uri.parse(MusicPlayerService.songs.get(mediaPosition)));

        byte[] bytes = mmr.getEmbeddedPicture();
        Bitmap artBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        String authorName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        bigPictureStyle.bigPicture(artBitmap);
        bigPictureStyle.setBigContentTitle(authorName);
        bigPictureStyle.setSummaryText(songTitle);

        notificationBuilder.setSmallIcon(R.drawable.icon_music);
        notificationBuilder.setContentTitle(authorName);
        notificationBuilder.setContentText(songTitle);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_music));

        notificationBuilder.setOngoing(true);
        notificationBuilder.setStyle(bigPictureStyle);

        Intent notiIntent = new Intent(getString(R.string.NOTIFICATION_CLICKED));
        notiIntent.setClass(this, MainActivity.class);

        PendingIntent notiPendingIntent = PendingIntent.getActivity(this, 0, notiIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setContentIntent(notiPendingIntent);

        Intent previousIntent = new Intent(getString(R.string.PREVIOUS_CLICKED));
        previousIntent.setClass(this, MusicPlayerService.class);

        PendingIntent previousPendingIntent = PendingIntent.getService(this, 1, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.player_previous, null, previousPendingIntent);

        Intent nextIntent = new Intent(getString(R.string.NEXT_CLICKED));
        nextIntent.setClass(this, MusicPlayerService.class);

        PendingIntent nextPendingIntent = PendingIntent.getService(this, 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.player_next, null, nextPendingIntent);

        notificationBuilder.addAction(R.drawable.icon_speaker, null, null).setVisibility(View.GONE);

        notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

    }

}
