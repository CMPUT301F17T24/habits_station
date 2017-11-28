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
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class NearbyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GeoPoint currentLocation;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 301;
    private User user;
    private ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();

    GoogleMap mgoogleMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.nearby_map);
        mapFragment.getMapAsync(this);

        //the dialog for checking the permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(NearbyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // explanation
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(NearbyActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mgoogleMap = googleMap;
            //LatLng Edmonton = new LatLng(53.5444, -113.4909);
            getcLocation();
            double la = currentLocation.getLatitude();
            double ll = currentLocation.getLongitude();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(la,ll)), 10));

            setMarker();
        }

    public void getcLocation(){
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
    }


    public void setMarker(){
        // get current location
        getcLocation();

        if (currentLocation != null){
            //title.setText(currentLocation.toString());
            addMarker();
            }
        else{
            //title.setText("empty");
            Toast.makeText(this, "Cannot access current location, check you GPS.", Toast.LENGTH_SHORT).show();
            // just used to test
            currentLocation = new GeoPoint(53.537519,-113.497412,0.0);
            addMarker();
        }
    }


    public void addMarker() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try{
            user = getUserTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
        }

        ArrayList<String> friends = user.getFollowee();     // get the users followed by the current user
        for (String element: friends){
            String query = "{\n" +
                    "  \"query\": { \n" +
                    " \"term\" : { \"uName\" : \"" + element + "\" }\n" +
                    " 	}\n" +
                    "}";

            ElasticSearchEventController.GetEvents getEvents
                    = new  ElasticSearchEventController.GetEvents();
            getEvents.execute(query);
            try {
                events.addAll(getEvents.get());                 // get all the events of the friends
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        // check 5 km
        int len = events.size();
        for (int i = 0; i<len; i++) {
            HabitEvent habitEvent = events.get(i);
            GeoPoint geoPoint = habitEvent.geteLocation();

            if (geoPoint != null) {
                Location current = new Location("Current Location");
                current.setLatitude(currentLocation.getLatitudeE6() / 1E6);
                current.setLongitude(currentLocation.getLongitudeE6() / 1E6);

                Location eventLocation = new Location("Mood's location");
                eventLocation.setLatitude(geoPoint.getLatitudeE6() / 1E6);
                eventLocation.setLongitude(geoPoint.getLongitudeE6() / 1E6);
                double distance = current.distanceTo(eventLocation);
                double disKM = distance/1000;
                Log.d("dis", String.valueOf(disKM));
                if (disKM <=5) {
                    double lat = geoPoint.getLatitude();
                    double lon = geoPoint.getLongitude();
                    mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(habitEvent.geteName()));

                }
                else{
                    double lat = geoPoint.getLatitude();
                    double lon = geoPoint.getLongitude();
                    mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(habitEvent.geteName()));
                }
            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
}

