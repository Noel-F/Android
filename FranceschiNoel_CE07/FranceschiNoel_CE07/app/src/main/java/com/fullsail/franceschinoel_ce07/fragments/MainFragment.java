package com.fullsail.franceschinoel_ce07.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

// Noel Franceschi
// MDF3 1610
// MainFragment.java

public class MainFragment extends ListFragment {

    //public static final String TAG = "MainFragment";

    MainActivityFragmentInterface mainActivityFragmentInterface;

    public MainFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No data posted yet");

    }

    public interface MainActivityFragmentInterface {

        void launchDetailActivity(String id);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivityFragmentInterface) {

            mainActivityFragmentInterface = (MainActivityFragmentInterface) context;

        } else throw new RuntimeException("MainActivityFragmentInterface not implemented");

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mainActivityFragmentInterface.launchDetailActivity(String.valueOf(position));

    }

}
