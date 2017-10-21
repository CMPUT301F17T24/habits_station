/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import android.location.Location;
import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yfeng3 2017-10-21.
 */

public class habitEventTest extends ActivityInstrumentationTestCase2 {

    public habitEventTest() {
        super(com.tiejun.habit_station.MainActivity.class);
    }

    public void testConstructer() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

    }

    public void testgetname() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertTrue("test1" == habitevent.geteName());
    }

    public void testgettime() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertTrue(date == habitevent.geteTime());
    }

    public void testgetReason() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertTrue("test reason" == habitevent.geteReason());
    }

    public void testgetPhoto() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Image image = null;

        assertTrue( image == habitevent.getePhoto());
    }

    public void testgetlocation() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Location location = null;

        assertTrue( location == habitevent.geteLocation());
    }

    public void testgetStatus() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertTrue( false == habitevent.getStatus());
    }

    public void testsetName() {
        Date date = new Date();

        String str = "setname";

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.seteName(str);

        assertTrue(str == habitevent.geteName());
    }

    public void testsetTime() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.seteTime(date);

        assertTrue(date == habitevent.geteTime());
    }

    public void testsetReason() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.seteReason("new test reason");

        assertTrue("new test reason" == habitevent.geteReason());
    }

    public void testsetPhoto() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Image photo = null;

        habitevent.setePhoto(photo);

        assertTrue(photo == habitevent.getePhoto());
    }

    public void testsetStatus() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.setStatus(true);

        assertTrue(true == habitevent.getStatus());
    }

    public void testTostring() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertTrue("to string method" == habitevent.toString());
    }

}
