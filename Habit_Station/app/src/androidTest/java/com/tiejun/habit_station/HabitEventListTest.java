/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import android.location.Location;
import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yfeng3 2017-10-21.
 */

public class HabitEventListTest extends ActivityInstrumentationTestCase2 {

    public HabitEventListTest() {
        super(SignInActivity.class);
    }


    public void testAddEvent() {
        HabitEventList eventList = new HabitEventList();
        Calendar date = Calendar.getInstance();
        date.set(2017,10,11);
        HabitEvent habitevent = new HabitEvent("test1", date, "test reason" );
        eventList.add(habitevent);
        assertTrue(eventList.hasHabit(habitevent));
    }


    public void testDeleteEvent() {
        HabitEventList eventList = new HabitEventList();
        Calendar date = Calendar.getInstance();
        date.set(2017,10,11);
        HabitEvent habitevent = new HabitEvent("test1", date, "test reason" );
        eventList.add(habitevent);
        assertTrue(eventList.hasHabit(habitevent));

        eventList.delete(habitevent);
        assertFalse(eventList.hasHabit(habitevent));

    }


    public void testHasHabitEvent() {
        HabitEventList eventList = new HabitEventList();
        Calendar date = Calendar.getInstance();
        date.set(2017,10,11);
        HabitEvent habitevent = new HabitEvent("test1", date, "test reason" );
        eventList.add(habitevent);
        assertTrue(eventList.hasHabit(habitevent));
    }



    public void testGetCount() {
        HabitEventList eventList = new HabitEventList();
        Calendar date1 = Calendar.getInstance();
        date1.set(2017,10,11);
        HabitEvent habitevent1 = new HabitEvent("test1", date1, "test reason" );
        eventList.add(habitevent1);
        HabitEvent habitevent2 = new HabitEvent("test2", date1, "test reason2" );
        eventList.add(habitevent2);
        assertEquals(eventList.getCount(), 2);
    }


    public void testGetHabitEvent() {
        HabitEventList eventList = new HabitEventList();
        Calendar date = Calendar.getInstance();
        date.set(2017,10,11);
        HabitEvent habitevent1 = new HabitEvent("test1", date, "test reason" );
        eventList.add(habitevent1);
        HabitEvent returnEvent = eventList.getEvent(0);

        assertEquals(habitevent1, returnEvent);

    }

    public void testSortEvent(){
        HabitEventList eventList = new HabitEventList();
        Calendar date1 = Calendar.getInstance();
        date1.set(2017,10,11);
        HabitEvent habitevent1 = new HabitEvent("test1", date1, "test reason" );
        eventList.add(habitevent1);

        Calendar date2 = Calendar.getInstance();
        date2.set(2017,10,9);
        HabitEvent habitevent2 = new HabitEvent("test1", date2, "test reason2" );
        eventList.add(habitevent2);

        Calendar date3 = Calendar.getInstance();
        date3.set(2017,10,17);
        HabitEvent habitevent3 = new HabitEvent("test3", date3, "test reason3" );
        eventList.add(habitevent3);

        ArrayList<HabitEvent> sortedEventList = eventList.sortEvents();

        assertEquals(sortedEventList.get(0).geteName(),habitevent3.geteName());
        assertEquals(sortedEventList.get(2).geteName(),habitevent2.geteName());




    }


}
