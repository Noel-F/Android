package com.fullsail.franceschinoel_ce07.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

// Noel Franceschi
// MDF3 1610
// WidgetProvider.java

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {

            WidgetHelper.updateWidget(context, appWidgetManager, appWidgetId);

        }
    }
}

