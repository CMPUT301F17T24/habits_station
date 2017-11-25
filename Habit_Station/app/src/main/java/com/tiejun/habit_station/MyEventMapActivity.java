/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.tiejun.habit_station.R.id.events;

public class MyEventMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mgoogleMap;
    private ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.history_map);
        mapFragment.getMapAsync(this);



        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        String MYhistory = "{\n" +
            "  \"query\": { \n" +
            " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
            " 	}\n" +
            "}";

            ElasticSearchEventController.GetEvents getHistory
            = new  ElasticSearchEventController.GetEvents();
        getHistory.execute(MYhistory);

                try {
                fillist.addAll(getHistory.get());
                } catch (InterruptedException e) {
                e.printStackTrace();
                } catch (ExecutionException e) {
                e.printStackTrace();
                }

                int len = fillist.size();
                for (int i = 0; i<len; i++) {
                    HabitEvent habitEvent = fillist.get(i);
                    GeoPoint geoPoint = habitEvent.geteLocation();


                    if (geoPoint != null) {
                        double lat = geoPoint.getLatitude();
                        double lon = geoPoint.getLongitude();
                        mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(""));
                     }
                }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        LatLng Edmonton = new LatLng(53.5444, -113.4909);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Edmonton, 10));

    }

}