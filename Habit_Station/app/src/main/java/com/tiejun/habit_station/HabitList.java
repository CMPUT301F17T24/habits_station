/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by tiejun on 2017-10-13.
 */

public class HabitList{
    // list of all habits created
    public ArrayList<Habit> habits;

    public HabitList() {
        this.habits = new ArrayList<Habit>();
    }

    public HabitList(ArrayList<Habit> habitList) {
        this.habits = habitList;
    }

    public void add(Habit habit) {
        if (this.hasHabit(habit)) {
            throw new IllegalArgumentException("Duplicate habits.");
        }
        this.habits.add(habit);
    }

    public boolean hasHabit(Habit habit) {
        return habits.contains(habit);
    }

    public void delete(Habit habit) {
        habits.remove(habit);
    }

    public int getCount() {
        return habits.size();
    }

    public Habit getHabit(int index) {
        return habits.get(index);
    }

    class DateCompare implements Comparator<Habit> {
        public int compare(Habit habit, Habit t1) {
            if (habit.getStartDate().before(t1.getStartDate()))
                return 1;
            if (habit.getStartDate().after(t1.getStartDate()))
                return -1;
            return 0;
        }
    }

    public ArrayList<Habit> getHabits() {
        DateCompare compare = new DateCompare();
        Collections.sort(habits, compare);
        return habits;
    }

    public ArrayList<Habit> getTodayHabits() {
        ArrayList<Habit> todayList = new ArrayList<Habit>();
        Calendar today = Calendar.getInstance();
        int weekDay = today.get(Calendar.DAY_OF_WEEK);

        for (Habit h : habits) {
            if (h.getRepeatWeekOfDay().contains(weekDay))
                todayList.add(h);
        }
        return todayList;
    }


    public boolean check_dup (Habit habit){
        for (Habit element: habits){
            if (element.getTitle().toUpperCase().equals(habit.getTitle().toUpperCase())){
                return true ;
            }
        }
        return false;
    }





}
