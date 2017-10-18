package com.fullsail.franceschinoel_ce03;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

// Noel Franceschi
// MDF3 - 1610
// NewsService.java

public class NewsService extends IntentService {

    public static final int NOTIFICATION_ID = 0x10001;

    public static News currentNews;

    private NotificationManager mNotificationManager;

    int randomNum;

    public NewsService() {

        super("NewsService");

    }

    @Override
    public void onCreate() {

        super.onCreate();

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Random mRandomInt = new Random(System.currentTimeMillis());
        randomNum = mRandomInt.nextInt(28);

        try {

            URL url = new URL(MainActivity.API_URL);
            URLConnection connection = url.openConnection();

            InputStream inputStream = connection.getInputStream();
            String response = IOUtils.toString(inputStream);

            News news = new News();
            JSONObject jsonRoot = new JSONObject(response);

            if (jsonRoot.has("results")) {

                JSONArray jsnArr = jsonRoot.getJSONArray("results");
                JSONObject jsonNews = jsnArr.getJSONObject(randomNum);

                if (jsonNews.has("section")) {

                    news.setSection(jsonNews.getString("section"));

                }

                if (jsonNews.has("subsection")) {

                    news.setSubSection(jsonNews.getString("subsection"));

                }

                if (jsonNews.has("title")) {

                    news.setTitle(jsonNews.getString("title"));

                }

                if (jsonNews.has("abstract")) {

                    news.setDescription(jsonNews.getString("abstract"));

                }

                if (jsonNews.has("url")) {

                    news.setUrl(jsonNews.getString("url"));

                }

            }

            startNotification(news);

            currentNews = news;

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }
    }

    private void startNotification(News news) {

        mNotificationManager.cancelAll();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("MDF3 Lab 3");
        mBuilder.setContentText(news.getTitle());
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(news.getDescription()).setBigContentTitle(news.getTitle()));

        String url = news.getUrl();

        if (!url.startsWith("http://") && !url.startsWith("https://")) {

            url = "http://" + url;
        }

        Intent contentIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, contentIntent, 0);

        mBuilder.setContentIntent(pendingIntent1);

        Intent buttonIntent = new Intent(NewsReceiver.ACTION_SAVE_DATA);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, buttonIntent, 0);

        mBuilder.addAction(R.drawable.save_img, "Save", pendingIntent2);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

}