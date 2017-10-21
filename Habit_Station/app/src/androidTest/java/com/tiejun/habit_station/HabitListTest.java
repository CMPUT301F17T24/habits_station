/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tiejun on 2017-10-13.
 */

public class HabitListTest extends ActivityInstrumentationTestCase2 {

    public HabitListTest() {
        super(com.tiejun.habit_station.MainActivity.class);
    }

    public void testAdd() {
        HabitList habitList = new HabitList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("TEST","",startDate);
        habitList.add(habit);
        assertTrue(habitList.hasHabit(habit));

        try {
            habitList.add(habit);
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public void testDelete() {
        HabitList habitList = new HabitList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("TEST","",startDate);
        habitList.add(habit);
        habitList.delete(habit);
        assertFalse(habitList.hasHabit(habit));
    }

    public void testHasHabit() {
        HabitList habitList = new HabitList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("TEST","",startDate);
        habitList.add(habit);
        assertTrue(habitList.hasHabit(habit));
    }

    public void testGetHabit() {
        HabitList habitList = new HabitList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("TEST","",startDate);
        habitList.add(habit);
        Habit returnHabit = habitList.getHabit(0);
        assertEquals(habit.getTitle(), returnHabit.getTitle());
        assertEquals(habit.getReason(), returnHabit.getReason());
        assertEquals(habit.getStartDate(), returnHabit.getStartDate());
    }

    public void testGetHabits() {
        HabitList habitList = new HabitList();

        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("TEST0","",startDate);
        habitList.add(habit);

        Calendar startDate1 = Calendar.getInstance();
        startDate1.set(2017,10,11);
        Habit habit1 = new Habit("TEST1","",startDate1);
        habitList.add(habit1);

        Calendar startDate2 = Calendar.getInstance();
        startDate2.set(2016,1,2);
        Habit habit2 = new Habit("TEST2","",startDate2);
        habitList.add(habit2);

        ArrayList<Habit> test = habitList.getHabits();

        if (test.get(0).getStartDate().after(test.get(1).getStartDate())) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }

        if (test.get(1).getStartDate().after(test.get(2).getStartDate())) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }

    public void testGetCount() {
        HabitList habitList = new HabitList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("TEST","",startDate);
        habitList.add(habit);
        assertEquals(habitList.getCount(), 1);
    }

}

