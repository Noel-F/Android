package com.fullsail.franceschinoel_ce04;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Noel Franceschi
// MDF 3 Term 1610
// FileStorage.java

@SuppressWarnings("WeakerAccess")
public class FileStorage {

    private final String MARKER_ID = "id";
    private final String MARKER_NAME = "name";
    private final String MARKER_DETAIL = "detail";
    private final String MARKER_PATH = "path";
    private final String MARKER_LATITUDE = "latitude";
    private final String MARKER_LONGITUDE = "longitude";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void writeToFile(ArrayList<MyMarker> markers) {

        try {

            File folder = new File(Environment.getExternalStorageDirectory(), "MappingPhotosProject");

            if (!folder.exists()) {

                folder.mkdirs();

            }

            File file = new File(folder, "MyDataFile.txt");

            if (file.exists()) {

                file.delete();

            }
            if (!file.exists()) {

                file.createNewFile();

                JSONArray array = new JSONArray();

                JSONObject obj = new JSONObject();

                try {

                    obj.put("markers", array);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

            JSONArray jsonArray = new JSONArray();

            JSONObject obj = new JSONObject();

            for (int i = 0; i < markers.size(); i++) {

                MyMarker marker = markers.get(i);

                JSONObject json_marker = new JSONObject();
                json_marker.put(MARKER_ID, (i + 1));
                json_marker.put(MARKER_NAME, marker.getName());
                json_marker.put(MARKER_DETAIL, marker.getDetail());
                json_marker.put(MARKER_PATH, marker.getPhotoPath());
                json_marker.put(MARKER_LATITUDE, marker.getLatitude());
                json_marker.put(MARKER_LONGITUDE, marker.getLongitude());
                jsonArray.put(json_marker);

            }

            obj.put("markers", jsonArray);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(obj.toString());
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }

    }

    public String fileReadAllText() {

        StringBuilder text = new StringBuilder();

        try {

            File folder = new File(Environment.getExternalStorageDirectory(), "MappingPhotosProject");

            File file = new File(folder, "MyDataFile.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {

                text.append(line);

            }

            br.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return text.toString();

    }

    public ArrayList<MyMarker> readFromFile() {

        ArrayList<MyMarker> myMarkers = new ArrayList<>();

        String fileText = fileReadAllText();

        try {

            JSONObject jsonObject = new JSONObject(fileText);

            JSONArray json_markers = jsonObject.getJSONArray("markers");

            for (int i = 0; i < json_markers.length(); i++) {

                JSONObject json_marker = json_markers.getJSONObject(i);

                MyMarker marker = new MyMarker();
                marker.setId(json_marker.getString(MARKER_ID));
                marker.setName(json_marker.getString(MARKER_NAME));
                marker.setDetail(json_marker.getString(MARKER_DETAIL));
                marker.setPhotoPath(json_marker.getString(MARKER_PATH));
                marker.setLatitude(json_marker.getString(MARKER_LATITUDE));
                marker.setLongitude(json_marker.getString(MARKER_LONGITUDE));

                myMarkers.add(marker);

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return myMarkers;
    }

    public void deleteMarkerFromFile(MyMarker marker) {

        ArrayList<MyMarker> myMarkers = readFromFile();

        boolean change = false;

        for (MyMarker myMarker : myMarkers) {

            if (myMarker.getId().equals(marker.getId())) {

                change = true;

                myMarkers.remove(myMarker);

            }
        }
        if (change) {

            writeToFile(myMarkers);

        }
    }

    public void updateMarkerFromFile(MyMarker marker) {

        ArrayList<MyMarker> myMarkers = readFromFile();

        boolean change = false;

        for (MyMarker myMarker : myMarkers) {

            if (myMarker.getId().equals(marker.getId())) {

                change = true;

                myMarker.setName(marker.getName());
                myMarker.setDetail(marker.getDetail());
                myMarker.setPhotoPath(marker.getPhotoPath());
                myMarker.setLatitude(marker.getLatitude());
                myMarker.setLongitude(marker.getLongitude());
            }
        }

        if (change) {

            writeToFile(myMarkers);

        }
    }

    public void addMarkerToFile(MyMarker marker) {

        ArrayList<MyMarker> myMarkers = readFromFile();

        myMarkers.add(marker);

        writeToFile(myMarkers);
    }

}
