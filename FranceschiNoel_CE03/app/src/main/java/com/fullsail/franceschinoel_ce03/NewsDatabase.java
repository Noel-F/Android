package com.fullsail.franceschinoel_ce03;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Noel Franceschi
// MDF3 - 1610
// NewsDatabase.java

@SuppressWarnings("WeakerAccess")
public class NewsDatabase {

    private static final String fileStorage = "NewsDB";

    public void saveSerializable(Context context, News news) {

        ArrayList<News> newsDbList = this.readSerializable(context);
        newsDbList.add(news);

        this.insertToDatabase(context, newsDbList);

    }

    public ArrayList<News> readSerializable(Context context) {

        ArrayList<News> newsDbList = new ArrayList<>();

        try {

            FileInputStream fileInputStream = context.openFileInput(fileStorage);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            newsDbList = (ArrayList<News>) objectInputStream.readObject();
            objectInputStream.close();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

        return newsDbList;
    }


    public void insertToDatabase(Context context, ArrayList<News> news) {
        try {

            FileOutputStream fileOutputStream = context.openFileOutput(fileStorage, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(news);
            objectOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}


