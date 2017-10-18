package com.fullsail.franceschinoel_ce06;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fullsail.franceschinoel_ce06.utils.WeatherHelper;
import com.fullsail.franceschinoel_ce06.utils.WeatherService;

// Noel Franceschi
// MDF3 1610
// ConfigActivity.java

public class ConfigActivity extends AppCompatActivity {

    private int ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);

        Intent intent = getIntent();
        ID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (ID == AppWidgetManager.INVALID_APPWIDGET_ID) {

            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_config, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.config_done) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

            Intent downloadService = new Intent(this, WeatherService.class);
            downloadService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ID);

            startService(downloadService);

            WeatherHelper.updateWidget(this, appWidgetManager, ID);

            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ID);

            setResult(RESULT_OK, intent);

            finish();
        }

        return super.onOptionsItemSelected(item);

    }

}

