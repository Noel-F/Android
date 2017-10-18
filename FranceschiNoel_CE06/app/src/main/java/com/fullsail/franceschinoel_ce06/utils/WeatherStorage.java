package com.fullsail.franceschinoel_ce06.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Noel Franceschi
// MDF3 1610
// WeatherStorage.java

public class WeatherStorage {

    private static final String WEATHER_DB = "weather_db";

    private static final String FORECAST_DB = "forecast_db";

    public static void saveWeatherObjects(Context context, ArrayList<Weather> weatherList) {

        try {

            FileOutputStream fos = context.openFileOutput(WEATHER_DB, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(weatherList);
            oos.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Weather> readWeatherObjects(Context context) {

        ArrayList<Weather> weatherList = new ArrayList<>();

        try {

            FileInputStream fis = context.openFileInput(WEATHER_DB);

            ObjectInputStream ois = new ObjectInputStream(fis);
            weatherList = (ArrayList<Weather>) ois.readObject();
            ois.close();

        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
        }

        return weatherList;
    }

    public static void saveForecastObjects(Context context, ArrayList<Weather> weatherList) {

        try {

            FileOutputStream fos = context.openFileOutput(FORECAST_DB, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(weatherList);
            oos.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Weather> readForecastObjects(Context context) {

        ArrayList<Weather> weatherList = new ArrayList<>();

        try {

            FileInputStream fis = context.openFileInput(FORECAST_DB);

            ObjectInputStream ois = new ObjectInputStream(fis);
            weatherList = (ArrayList<Weather>) ois.readObject();
            ois.close();

        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
        }

        return weatherList;
    }
}

