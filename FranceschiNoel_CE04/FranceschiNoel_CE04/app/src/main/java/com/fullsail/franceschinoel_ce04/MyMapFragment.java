package com.fullsail.franceschinoel_ce04;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// Noel Franceschi
// Term 1610
// MyMapFragment.java

public class MyMapFragment extends MapFragment implements OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {

    GoogleMap myMap;

    @SuppressWarnings("FieldCanBeLocal")
    private LocationManager myLocationManager;

    ArrayList<MyMarker> mapMarkers;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        myMap = getMap();

        FileStorage fileStorage = new FileStorage();

        mapMarkers = fileStorage.readFromFile();

        for (MyMarker marker : mapMarkers) {

            myMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(marker.getLatitude()),
                            Double.valueOf(marker.getLongitude())))
                            .title(String.valueOf(marker.getId()) + "," + marker.getName())
                            .snippet(marker.getDetail())
            );

        }

        myMap.setInfoWindowAdapter(new MyMarkerAdapter(getActivity()));

        myMap.setOnInfoWindowClickListener(this);

        myMap.setOnMapLongClickListener(this);


        myMap.setMyLocationEnabled(true);

        myLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        @SuppressWarnings("MissingPermission")
        Location lastKnownLocation = myLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (lastKnownLocation != null) {

            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude()), 16));

        } else {

            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.593770, -81.303797 ), 16));

        }

        myMap.setMyLocationEnabled(false);

    }

    @Override
    public void onInfoWindowClick(final Marker marker) {

        Intent myIntent = new Intent(getActivity(), FileReadAndWrite.class);

        String titleWithId = marker.getTitle();

        String markerId = titleWithId.split(",")[0];

        myIntent.putExtra("marker", mapMarkers.get(Integer.parseInt(markerId) - 1));

        startActivity(myIntent);

    }

    @Override
    public void onMapLongClick(final LatLng location) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Map Clicked")
                .setMessage("Do you want to add new marker here?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(getActivity(), FormActivity.class);
                        myIntent.putExtra("Latitude:", location.latitude);
                        myIntent.putExtra("Longitude:", location.longitude);
                        startActivity(myIntent);
                    }
                })
                .show();

    }

    private class MyMarkerAdapter implements InfoWindowAdapter {

        private Context myContext;

        @SuppressWarnings("WeakerAccess")
        public MyMarkerAdapter(Context context) {
            this.myContext = context;
        }

        @Override
        public View getInfoContents(Marker marker) {

            LayoutInflater myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @SuppressLint("InflateParams") View myView = myInflater.inflate(R.layout.windowlayout, null);

            String titleWithId = marker.getTitle();

            String name = titleWithId.split(",")[1];

            String detail = marker.getSnippet();

            TextView textViewName = (TextView) myView.findViewById(R.id.tv_name);

            TextView textViewDetail = (TextView) myView.findViewById(R.id.tv_detail);

            textViewName.setText(name);

            textViewDetail.setText(detail);

            return myView;

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }
}




