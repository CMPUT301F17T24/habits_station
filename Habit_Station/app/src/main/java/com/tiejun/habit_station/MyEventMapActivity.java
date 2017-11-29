/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.tiejun.habit_station.R.id.events;

public class MyEventMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mgoogleMap;
    private GeoPoint currentLocation;
    private ArrayList<HabitEvent> fillist = new ArrayList<HabitEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.history_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        LatLng Edmonton = new LatLng(53.5444, -113.4909);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Edmonton, 10));

        setMarker();
    }


    public void setMarker() {
        try {
            CurrentLocation locationListener = new CurrentLocation();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if( location != null ) {
                int latitude = (int) (location.getLatitude() * 1E6);
                int longitude = (int) (location.getLongitude() * 1E6);
                currentLocation = new GeoPoint(latitude, longitude);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (currentLocation != null){
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            String userName = pref.getString("currentUser", "");
            String MYhistory = "{\n" +
                    "  \"query\": { \n" +
                    " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                    " 	}\n" +
                    "}";

            ElasticSearchEventController.GetEvents getHistory
                    = new ElasticSearchEventController.GetEvents();
            getHistory.execute(MYhistory);

            try {
                fillist.addAll(getHistory.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            int len = fillist.size();
            for (int i = 0; i < len; i++) {
                HabitEvent habitEvent = fillist.get(i);
                GeoPoint geoPoint = habitEvent.geteLocation();

                if (geoPoint != null) {
                    Location current = new Location("Current Location");
                    current.setLatitude(currentLocation.getLatitudeE6() / 1E6);
                    current.setLongitude(currentLocation.getLongitudeE6() / 1E6);

                    Location eventLocation = new Location("Mood's location");
                    eventLocation.setLatitude(geoPoint.getLatitudeE6() / 1E6);
                    eventLocation.setLongitude(geoPoint.getLongitudeE6() / 1E6);
                    double distance = current.distanceTo(eventLocation);
                    double disKM = distance / 1000;
                    Log.d("dis", String.valueOf(disKM));
                    if (disKM <= 5) {
                        double lat = geoPoint.getLatitude();
                        double lon = geoPoint.getLongitude();
                        mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(habitEvent.geteName()));

                    } else {
                        double lat = geoPoint.getLatitude();
                        double lon = geoPoint.getLongitude();
                        mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(habitEvent.geteName()));
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Cannot access current location, check you GPS.", Toast.LENGTH_SHORT).show();
            currentLocation = new GeoPoint(53.537519,-113.497412,0.0);
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            String userName = pref.getString("currentUser", "");
            String MYhistory = "{\n" +
                    "  \"query\": { \n" +
                    " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                    " 	}\n" +
                    "}";

            ElasticSearchEventController.GetEvents getHistory
                    = new ElasticSearchEventController.GetEvents();
            getHistory.execute(MYhistory);

            try {
                fillist.addAll(getHistory.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            int len = fillist.size();
            for (int i = 0; i < len; i++) {
                HabitEvent habitEvent = fillist.get(i);
                GeoPoint geoPoint = habitEvent.geteLocation();

                if (geoPoint != null) {
                    Location current = new Location("Current Location");
                    current.setLatitude(currentLocation.getLatitudeE6() / 1E6);
                    current.setLongitude(currentLocation.getLongitudeE6() / 1E6);

                    Location eventLocation = new Location("Mood's location");
                    eventLocation.setLatitude(geoPoint.getLatitudeE6() / 1E6);
                    eventLocation.setLongitude(geoPoint.getLongitudeE6() / 1E6);
                    double distance = current.distanceTo(eventLocation);
                    double disKM = distance / 1000;
                    Log.d("dis", String.valueOf(disKM));
                    if (disKM <= 5) {
                        double lat = geoPoint.getLatitude();
                        double lon = geoPoint.getLongitude();
                        mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(habitEvent.geteName()));

                    } else {
                        double lat = geoPoint.getLatitude();
                        double lon = geoPoint.getLongitude();
                        mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(habitEvent.geteName()));
                    }
                }
            }
        }

    }
}