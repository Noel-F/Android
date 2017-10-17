package com.fullsail.franceschinoel_ce07.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

// Noel Franceschi
// MDF3 1610
// WidgetService.java

public class WidgetService extends RemoteViewsService {

    public WidgetService() {

    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new WidgetFactory(getApplicationContext());

    }
}

