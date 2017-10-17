package com.fullsail.franceschinoel_ce06.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.fullsail.franceschinoel_ce06.ConfigActivity;
import com.fullsail.franceschinoel_ce06.ForecastActivity;
import com.fullsail.franceschinoel_ce06.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;

// Noel Franceschi
// MDF3 1610
// WeatherHelper.java

public class WeatherHelper {

    public static final String THEME_PREFERENCE = "com.fullsail.franceschinoel_ce06.THEME_CHOOSER";

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        String themeType = getThemePreference(context);

        RemoteViews remoteViews;

        if (themeType.equals("Light")) {

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.light_theme);

        } else {

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.dark_theme);

        }

        ArrayList<Weather> weathers = WeatherStorage.readWeatherObjects(context);

        if (weathers != null) {

            if (!weathers.isEmpty()) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("US/Eastern"));
                gregorianCalendar.setTimeInMillis((long) weathers.get(0).lastUpdated);

                String description = weathers.get(0).description;

                String city = weathers.get(0).city;

                remoteViews.setTextViewText(R.id.widget_conditions, description);

                remoteViews.setTextViewText(R.id.widget_city, city);

                remoteViews.setTextViewText(R.id.widget_temp, weathers.get(0).temp + " F");

                remoteViews.setTextViewText(R.id.widget_time, simpleDateFormat.format(gregorianCalendar.getTime()));
            }
        }

        Intent intentButtonForecast = new Intent(context, ForecastActivity.class);
        intentButtonForecast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        PendingIntent pendingIntent = getActivity(context, 0, intentButtonForecast, FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_imageButton, pendingIntent);

        Intent intentConfig = new Intent(context, ConfigActivity.class);
        intentConfig.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        PendingIntent configPendingIntent = getActivity(context, 0, intentConfig, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_config, configPendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    public static String getThemePreference(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getString(THEME_PREFERENCE, "NO THEME!");

    }
}

