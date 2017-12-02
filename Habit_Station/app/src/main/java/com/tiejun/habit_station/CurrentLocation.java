/*
 * Copyright (c) 2017 Team 24, CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify this code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import org.osmdroid.util.GeoPoint;

/**
 * The class used to get the current location
 *
 * @author xuanyi
 * @version 1.0
 *
 */

public class CurrentLocation implements LocationListener {

    public GeoPoint mcurrentLocation;

    @Override
    public void onLocationChanged(Location location) {
        int latitude = (int) (location.getLatitude() * 1E6);
        int longitude = (int) (location.getLongitude() * 1E6);
        mcurrentLocation = new GeoPoint(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}