package com.fullsail.franceschinoel_ce07.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Noel Franceschi
// MDF3 1610
// StorageDB.java

public class StorageDB {

    private static final String TAG = "StorageDB";

    private static final String fileStorage = "StorageDB";

    public static void savePerson(Context context, People person) {

        ArrayList<People> tempList = StorageDB.readPersons(context);

        boolean objectExist = false;

        for (People p : tempList) {

            if (p.id.equals(person.id)) {

                objectExist = true;

                Toast.makeText(context, "Person already exist...", Toast.LENGTH_SHORT).show();

                Log.e(TAG, "savePerson: Person already exist...");

                break;
            }
        }


        if (!objectExist) {

            tempList.add(person);

            Toast.makeText(context, "Person saved...", Toast.LENGTH_SHORT).show();

            Log.e(TAG, "savePerson: Person saved...");
        }

        StorageDB.replacePersons(context, tempList);

    }

    public static ArrayList<People> readPersons(Context context) {

        ArrayList<People> tempList = new ArrayList<>();


        try {

            FileInputStream fileInputStream = context.openFileInput(fileStorage);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            tempList = (ArrayList<People>) objectInputStream.readObject();

            objectInputStream.close();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
        }

        return tempList;

    }

    public static void deletePerson(Context context, String id) {

        ArrayList<People> tempList = StorageDB.readPersons(context);

        for (int i = 0; i < tempList.size(); ++i) {

            if (tempList.get(i).id.equals(id)) {

                tempList.remove(i);

                Toast.makeText(context, "Person deleted...", Toast.LENGTH_SHORT).show();
            }
        }

        StorageDB.replacePersons(context, tempList);

        StorageDB.updatePersonsIds(context);
    }

    public static
    @Nullable
    People getPersonFromId(Context context, String id) {

        ArrayList<People> tempList = StorageDB.readPersons(context);

        for (People p : tempList) {

            if (p.id.equals(id)) {

                Log.e(TAG, "savePerson: Person extracted from database...");

                return p;
            }
        }

        return null;
    }

    public static void replacePersons(Context context, ArrayList<People> persons) {

        try {

            FileOutputStream fileOutputStream = context.openFileOutput(fileStorage, Context.MODE_PRIVATE);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(persons);
            objectOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public static void updatePersonsIds(Context context) {

        ArrayList<People> internalDataStored = readPersons(context);

        for (int i = 0; i < internalDataStored.size(); i++) {

            internalDataStored.get(i).id = String.valueOf(i);

        }

        replacePersons(context, internalDataStored);
    }
}

