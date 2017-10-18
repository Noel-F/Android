package com.fullsail.franceschinoel_ce06.utils;

import android.app.Activity;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// Noel Franceschi
// MDF3 1610
// WeatherService.java

public class WeatherService extends IntentService {

    public static final String LOCATION_PREFERENCE = "com.fullsail.franceschinoel_ce06.LOCATION_CHOOSER";

    public static final String FORECAST_DOWNLOAD = "com.fullsail.franceschinoel_ce06.FORECAST_ACT_DOWNLOAD";

    private static String weatherObjects = "";

    private static String forecastObjects = "";

    private String location;

    public WeatherService() {

        super("WEATHER_SERVICE");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        location = getLocationPreference(getApplicationContext());

        int i = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        if (intent.hasExtra("EXTRA_RECEIVER")) {

            WeatherStorage.saveForecastObjects(getApplicationContext(), getForecastObjects());

            ResultReceiver resultReceiver = intent.getParcelableExtra("EXTRA_RECEIVER");
            resultReceiver.send(Activity.RESULT_OK, null);

        } else {

            WeatherStorage.saveWeatherObjects(getApplicationContext(), getWeatherObjects());

            WeatherHelper.updateWidget(getApplicationContext(), AppWidgetManager.getInstance(getApplicationContext()), i);
        }
    }

    public static String getLocationPreference(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getString(LOCATION_PREFERENCE, "NO LOCATION!");
    }

    public URL urlForWidget() {

        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=imperial&appid=b06fcd8252197768e3749ec5ccd2ae58");

            return url;

        } catch (MalformedURLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public URL urlForForecastActivity() {

        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + location + "&units=imperial&appid=b06fcd8252197768e3749ec5ccd2ae58");

            return url;

        } catch (MalformedURLException e) {

            e.printStackTrace();

            return null;
        }
    }

    public ArrayList<Weather> getWeatherObjects() {

        try {

            URLConnection connector = urlForWidget().openConnection();

            InputStream is = connector.getInputStream();

            weatherObjects = IOUtils.toString(is);
            is.close();

            return weatherJsonToArray();

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }

    public ArrayList<Weather> getForecastObjects() {

        try {

            URLConnection connector = urlForForecastActivity().openConnection();
            InputStream is = connector.getInputStream();

            forecastObjects = IOUtils.toString(is);
            is.close();

            return parseForecastJSONObjects();

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }

    public static ArrayList<Weather> weatherJsonToArray() {

        ArrayList<Weather> weathers = new ArrayList<>();

        if (weatherObjects.equals("")) {

            throw new IllegalArgumentException("weatherObjects is empty");
        }

        try {

            JSONObject fullObject = new JSONObject(weatherObjects);

            String city = (String) fullObject.get("name");

            JSONArray weatherArray = fullObject.getJSONArray("weather");

            JSONObject weatherInfoObject = (JSONObject) weatherArray.get(0);

            String description = (String) weatherInfoObject.get("description");

            JSONObject cityTemperatureObject = fullObject.getJSONObject("main");

            double temperature = cityTemperatureObject.getDouble("temp");

            String timeUpdate = fullObject.get("dt") + "000";

            Weather weather = new Weather(city, description, temperature, Double.parseDouble(timeUpdate), 0, 0);
            weathers.add(weather);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return weathers;
    }

    public static ArrayList<Weather> parseForecastJSONObjects() {

        ArrayList<Weather> weathers = new ArrayList<>();

        if (forecastObjects.equals("")) {

            throw new IllegalArgumentException("forecastObjects is empty");
        }

        try {

            JSONObject object = new JSONObject(forecastObjects);

            JSONArray forecastObjects = object.getJSONArray("list");

            for (int i = 0; i < forecastObjects.length(); i++) {

                JSONObject forecastObjectsChildren = forecastObjects.getJSONObject(i);

                JSONObject cityObject = object.getJSONObject("city");

                String city = (String) cityObject.get("name");

                JSONArray weatherArray = forecastObjectsChildren.getJSONArray("weather");

                JSONObject weatherInfoObject = (JSONObject) weatherArray.get(0);

                String description = (String) weatherInfoObject.get("description");

                JSONObject cityTemperatureObject = forecastObjectsChildren.getJSONObject("temp");

                double minTemp = cityTemperatureObject.getDouble("min");
                double maxTemp = cityTemperatureObject.getDouble("max");

                String timeUpdate = forecastObjectsChildren.get("dt") + "000";

                Weather weather = new Weather(city, description, 0, Double.parseDouble(timeUpdate), minTemp, maxTemp);
                weathers.add(weather);

            }
        } catch (JSONException e) {

            e.printStackTrace();

        }

        return weathers;
    }
}

