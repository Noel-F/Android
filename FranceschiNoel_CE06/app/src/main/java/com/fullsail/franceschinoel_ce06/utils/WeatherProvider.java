package com.fullsail.franceschinoel_ce06.utils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

// Noel Franceschi
// MDF3 1610
// WeatherProvider.java

public class WeatherProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {

            WeatherHelper.updateWidget(context, appWidgetManager, appWidgetId);

        }
    }
}
