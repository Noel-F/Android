package com.fullsail.franceschinoel_ce03;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

// Noel Franceschi
// MDF3 - 1610
// MainFragment.java

public class MainFragment extends ListFragment {

    public MainFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        ArrayList<News> newsArrayList = new NewsDatabase().readSerializable(getContext());

        Intent contentIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsArrayList.get(position).getUrl()));

        startActivity(contentIntent);

    }
}

