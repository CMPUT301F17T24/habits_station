/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class NearbyActivity extends AppCompatActivity {

    private GeoPoint currentLocation;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 301;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        title = (TextView)findViewById(R.id.title);

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
            }else {
                Toast.makeText(getApplicationContext(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            }
        }


        // get current location
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
            title.setText(currentLocation.toString());
        }
        else{
            title.setText("empty");
        }




    }
}
