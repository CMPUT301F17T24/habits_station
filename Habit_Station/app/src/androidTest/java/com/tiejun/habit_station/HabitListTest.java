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
import java.util.HashSet;

/**
 * Created by tiejun on 2017-10-13.
 */

public class HabitListTest extends ActivityInstrumentationTestCase2 {

    public HabitListTest() {
        super(SignInActivity.class);
    }

    public void testAdd() {
        HabitList habitList = new HabitList();

        HashSet<Integer> weekDay = new HashSet<Integer>();
        weekDay.add(6);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("uu","TEST","",startDate, weekDay);
        habitList.add(habit);
        assertTrue(habitList.hasHabit(habit));

        Habit habit2 = new Habit("uu","Test","",startDate, weekDay);
        if (habitList.check_dup(habit2)) {
            boolean thrown  = false;
            try {
                Log.d("DD", "duplicate");
                throw new IllegalArgumentException("Duplicate Habit");
            } catch(IllegalArgumentException e) {
                thrown = true;
                assertTrue(thrown);
            }
        }
        else{
            assertTrue(habitList.hasHabit(habit2));
        }


        Habit habit3 = new Habit("uu","TEST3","",startDate, weekDay);
        if (habitList.check_dup(habit3)) {
            boolean thrown  = false;
            try {
                throw new IllegalArgumentException("Duplicate Habit");
            } catch(IllegalArgumentException e) {
                thrown = true;
                assertTrue(thrown);
            }
        }
        else{
            habitList.add(habit3);
            assertTrue(habitList.hasHabit(habit3));
        }



    }

    public void testDelete() {
        HabitList habitList = new HabitList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        HashSet<Integer> weekDay = new HashSet<Integer>();
        weekDay.add(6);
        Habit habit = new Habit("uu","TEST","",startDate, weekDay);
        habitList.add(habit);
        habitList.delete(habit);
        assertFalse(habitList.hasHabit(habit));
    }

    public void testHasHabit() {
        HabitList habitList = new HabitList();
        HashSet<Integer> weekDay = new HashSet<Integer>();
        weekDay.add(6);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("uu","TEST","",startDate, weekDay);
        habitList.add(habit);
        assertTrue(habitList.hasHabit(habit));
    }

    public void testGetHabit() {
        HabitList habitList = new HabitList();
        HashSet<Integer> weekDay = new HashSet<Integer>();
        weekDay.add(6);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("uu","TEST","",startDate, weekDay);
        habitList.add(habit);
        Habit returnHabit = habitList.getHabit(0);
        assertEquals(habit.getTitle(), returnHabit.getTitle());
        assertEquals(habit.getReason(), returnHabit.getReason());
        assertEquals(habit.getStartDate(), returnHabit.getStartDate());
        assertEquals(habit.getRepeatWeekOfDay(), weekDay);
    }

    public void testGetHabits() {
        HabitList habitList = new HabitList();
        HashSet<Integer> weekDay = new HashSet<Integer>();
        weekDay.add(6);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("uu","TEST0","",startDate, weekDay);
        habitList.add(habit);

        Calendar startDate1 = Calendar.getInstance();
        startDate1.set(2017,10,11);
        Habit habit1 = new Habit("uu","TEST1","",startDate1, weekDay);
        habitList.add(habit1);

        Calendar startDate2 = Calendar.getInstance();
        startDate2.set(2016,1,2);
        Habit habit2 = new Habit("uu","TEST2","",startDate2, weekDay);
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
        HashSet<Integer> weekDay = new HashSet<Integer>();
        weekDay.add(6);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017,9,28);
        Habit habit = new Habit("uu","TEST","",startDate, weekDay);
        habitList.add(habit);

        Calendar startDate1 = Calendar.getInstance();
        startDate1.set(2017,10,11);
        Habit habit1 = new Habit("uu","TEST1","",startDate1, weekDay);
        habitList.add(habit1);


        assertEquals(habitList.getCount(), 2);
    }

}

