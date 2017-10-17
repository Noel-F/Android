package com.fullsail.franceschinoel_ce06;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.franceschinoel_ce06.utils.Weather;
import com.fullsail.franceschinoel_ce06.utils.WeatherService;
import com.fullsail.franceschinoel_ce06.utils.WeatherStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

// Noel Franceschi
// MDF3 1610
// ForecastActivity.java

public class ForecastActivity extends AppCompatActivity {

    ArrayList<Weather> weatherArrayList = new ArrayList<>();

    final Handler handler = new Handler();

    CustomReceiver resultReceiver;

    ForecastFragment forecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wforecast);

        resultReceiver = new CustomReceiver(handler);

        Intent downloadService = new Intent(this, WeatherService.class);
        downloadService.putExtra(WeatherService.FORECAST_DOWNLOAD, "FORECAST");
        downloadService.putExtra("EXTRA_RECEIVER", resultReceiver);

        startService(downloadService);

        forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.forecast_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        forecastFragment.setListAdapter(baseAdapter);
    }

    BaseAdapter baseAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return weatherArrayList.size();
        }

        @Override
        public Weather getItem(int position) {
            return weatherArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return weatherArrayList.indexOf(weatherArrayList.get(position));
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Weather weather = getItem(position);

            if (convertView == null) {

                LayoutInflater mInflater = (LayoutInflater) ForecastActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_item, null);

                TextView cityName = (TextView) convertView.findViewById(R.id.list_city);

                TextView description = (TextView) convertView.findViewById(R.id.list_description);

                TextView minMax = (TextView) convertView.findViewById(R.id.list_height_and_low);

                TextView timeUpdate = (TextView) convertView.findViewById(R.id.list_updated);

                cityName.setText(weather.city);

                description.setText(weather.description);

                String minMaxString = "Min: " + weather.minTemp + " - Max:" + weather.maxTemp;
                minMax.setText(minMaxString);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));

                calendar.setTimeInMillis((long) weather.lastUpdated);
                String time = "Updated: " + sdf.format(calendar.getTime());
                timeUpdate.setText(time);
            }

            return convertView;
        }
    };

    @SuppressLint("ParcelCreator")
    public class CustomReceiver extends ResultReceiver {

        public CustomReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            if (resultCode == RESULT_OK) {

                weatherArrayList = WeatherStorage.readForecastObjects(ForecastActivity.this);

                if (weatherArrayList == null) {

                    weatherArrayList = new ArrayList<>();
                }

                BaseAdapter baseAdapter = (BaseAdapter) forecastFragment.getListAdapter();
                baseAdapter.notifyDataSetChanged();
            }
        }
    }
}


