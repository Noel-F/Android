package com.fullsail.franceschinoel_ce07.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fullsail.franceschinoel_ce07.R;
import com.fullsail.franceschinoel_ce07.activities.DetailActivity;
import com.fullsail.franceschinoel_ce07.activities.FormActivity;

// Noel Franceschi
// MDF3 1610
// WidgetHelper.java

public class WidgetHelper {

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {

        appWidgetManager.notifyAppWidgetViewDataChanged(getWidgetIds(context), R.id.widget_list);

        RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Intent intent = new Intent(context, WidgetService.class);
        widgetViews.setRemoteAdapter(R.id.widget_list, intent);
        widgetViews.setEmptyView(R.id.widget_list, R.id.empty_text);

        intent = new Intent(context, DetailActivity.class);
        PendingIntent detailPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        widgetViews.setPendingIntentTemplate(R.id.widget_list, detailPendingIntent);

        intent = new Intent(context, FormActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent formPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        widgetViews.setOnClickPendingIntent(R.id.widget_list_add, formPendingIntent);

        appWidgetManager.updateAppWidget(widgetId, widgetViews);

    }

    @SuppressLint("NewApi")
    private static int[] getWidgetIds(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        ComponentName componentName = ComponentName.createRelative(context.getPackageName(), WidgetProvider.class.getName());

        return appWidgetManager.getAppWidgetIds(componentName);
    }

    public static void updateWidget(Context context) {
        int[] ids = getWidgetIds(context);

        for (int id : ids) {

            updateWidget(context, AppWidgetManager.getInstance(context), id);
        }
    }
}

