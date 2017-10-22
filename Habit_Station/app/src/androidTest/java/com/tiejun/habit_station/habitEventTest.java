/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import android.location.Location;
import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;

import java.util.Date;

/**
 * Created by yfeng3 2017-10-21.
 */

public class habitEventTest extends ActivityInstrumentationTestCase2 {

    public habitEventTest() {
        super(com.tiejun.habit_station.MainActivity.class);
    }

    public void testConstructor() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );
    }

    public void testGetName() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertEquals("test1", habitevent.geteName());
    }

    public void testGetTime() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertEquals(date, habitevent.geteTime());
    }

    public void testGetReason() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertEquals("test reason", habitevent.geteReason());
    }

    public void testGetPhoto() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Image image = null;

        assertEquals(image,  habitevent.getePhoto());
    }

    public void testGetLocation() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Location location = null;

        assertEquals(location, habitevent.geteLocation());
    }

    public void testGetStatus() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertEquals(false, habitevent.getStatus());
    }

    public void testGetComment() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertEquals(null, habitevent.geteComment());
    }

    public void testSetName() {
        Date date = new Date();

        String str = "setname";

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.seteName(str);

        assertEquals(str, habitevent.geteName());
    }

    public void testSetTime() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.seteTime(date);

        assertEquals(date, habitevent.geteTime());
    }

    public void testSetReason() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.seteReason("new test reason");

        assertEquals("new test reason", habitevent.geteReason());
    }

    public void testSetComment() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        try{
            habitevent.seteComment("new test comment");
        } catch (ArgTooLongException e) {
            assertTrue(true);
        }

        assertEquals("new test comment", habitevent.geteComment());
    }

    public void testSetPhoto() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Image photo = null;

        habitevent.setePhoto(photo);

        assertEquals(photo, habitevent.getePhoto());
    }

    public void testSetLocation() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        Location location = null;

        habitevent.seteLocation(location);

        assertEquals(location, habitevent.geteLocation());
    }

    public void testSetStatus() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        habitevent.setStatus(true);

        assertTrue(habitevent.getStatus());
    }

    public void testToString() {
        Date date = new Date();

        habitEvent habitevent = new habitEvent("test1", date, "test reason" );

        assertEquals("to string method", habitevent.toString());
    }
}
