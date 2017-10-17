package com.fullsail.franceschinoel_ce05;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CheckableImageButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

// Noel Franceschi
// MDF3 1610
// MainFragment.java

public class MainFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "MainFragment";

    public SeekBar seekBar;

    public CheckableImageButton shuffle, repeat;

    FloatingActionButton previous, play, stop, next;

    ImageView albumArt;

    TextView songName;

    MainActivityFragmentInterface mainActivityFragmentInterface;

    public MainFragment() {

    }

    public interface MainActivityFragmentInterface {

        void shuffle();

        void backward();

        void play();

        void pause();

        void stop();

        void next();

        void loop();

        int getCurrentMediaPosition();

        void seekTo(int i);

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof MainActivityFragmentInterface) {

            mainActivityFragmentInterface = (MainActivityFragmentInterface) context;

        } else {

            throw new IllegalStateException("MainActivityFragmentInterface not implemented...");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        seekBar = (SeekBar) getActivity().findViewById(R.id.fab_seekBar);

        shuffle = (CheckableImageButton) getActivity().findViewById(R.id.fab_shuffle);

        repeat = ((CheckableImageButton) getActivity().findViewById(R.id.fab_repeat));

        previous = (FloatingActionButton) getActivity().findViewById(R.id.fab_previous);

        play = (FloatingActionButton) getActivity().findViewById(R.id.fab_play_pause);

        stop = (FloatingActionButton) getActivity().findViewById(R.id.fab_stop);

        next = (FloatingActionButton) getActivity().findViewById(R.id.fab_forward);

        albumArt = (ImageView) getActivity().findViewById(R.id.player_album_art);

        songName = (TextView) getActivity().findViewById(R.id.player_album_sogn_title);

        stop.hide();

        ((CoordinatorLayout.LayoutParams) play.getLayoutParams()).gravity = Gravity.CENTER;

    }

    @Override
    public void onResume() {

        super.onResume();

        seekBar.setOnSeekBarChangeListener(this);

        shuffle.setOnClickListener(this);

        repeat.setOnClickListener(this);

        previous.setOnClickListener(this);

        play.setOnClickListener(this);

        stop.setOnClickListener(this);

        next.setOnClickListener(this);


    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPause() {

        super.onPause();

        seekBar.setOnSeekBarChangeListener(null);

        shuffle.setOnClickListener(null);

        repeat.setOnClickListener(null);

        previous.setOnClickListener(null);

        play.setOnClickListener(null);

        stop.setOnClickListener(null);

        next.setOnClickListener(null);

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {

        Log.e(TAG, "onClick: EXECUTED");

        int id = v.getId();

        if (id == R.id.fab_previous) {

            mainActivityFragmentInterface.backward();

        }

        if (id == R.id.fab_play_pause) {

            String playingState = (String) play.getTag();

            if (String.valueOf(playingState).equals("PLAY_DISPLAY") || playingState == null) {

                play.setTag("PAUSE_DISPLAY");

                play.setImageDrawable(getResources().getDrawable(R.drawable.player_pause, null));

                stop.show();

                ((CoordinatorLayout.LayoutParams) play.getLayoutParams()).gravity = Gravity.START;

                mainActivityFragmentInterface.play();

            } else {

                play.setTag("PLAY_DISPLAY");

                play.setImageDrawable(getResources().getDrawable(R.drawable.player_play, null));

                mainActivityFragmentInterface.pause();

            }

        }

        if (id == R.id.fab_stop) {

            play.setTag("PLAY_DISPLAY");

            play.setImageDrawable(getResources().getDrawable(R.drawable.player_play, null));

            ((CoordinatorLayout.LayoutParams) play.getLayoutParams()).gravity = Gravity.CENTER;

            stop.hide();

            mainActivityFragmentInterface.stop();

        }

        if (id == R.id.fab_forward) {

            mainActivityFragmentInterface.next();

        }

        if (id == shuffle.getId()) {

            ColorStateList toggleColor;

            shuffle.toggle();

            if (shuffle.isChecked()) {

                toggleColor = getResources().getColorStateList(R.color.colorAccent, null);

            } else {

                toggleColor = getResources().getColorStateList(R.color.colorPrimary, null);
            }

            shuffle.setBackgroundTintList(toggleColor);

            mainActivityFragmentInterface.shuffle();
        }

        if (id == repeat.getId()) {

            ColorStateList toggleColor;

            repeat.toggle();

            if (repeat.isChecked()) {

                toggleColor = getResources().getColorStateList(R.color.colorAccent, null);

            } else {

                toggleColor = getResources().getColorStateList(R.color.colorPrimary, null);

            }

            repeat.setBackgroundTintList(toggleColor);

            mainActivityFragmentInterface.loop();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mainActivityFragmentInterface.seekTo(seekBar.getProgress());

    }


    public void updateUI() {

        int index = mainActivityFragmentInterface.getCurrentMediaPosition();

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(getActivity(), Uri.parse(MusicPlayerService.songs.get(index)));

        byte[] bytes = mmr.getEmbeddedPicture();
        Bitmap artBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        albumArt.setImageBitmap(artBitmap);

        songName.setText(String.valueOf(songTitle));

    }
}
