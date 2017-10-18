package com.fullsail.franceschinoel_ce06;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

// Noel Franceschi
// MDF3 1610
// ForecastFragment.java

public class ForecastFragment extends ListFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No weather data");

    }
}
