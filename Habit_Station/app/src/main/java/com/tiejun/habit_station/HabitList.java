/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * a class for habit list
 *
 * @author xtie
 * @version 1.5
 * @see Habit
 * @since 1.0
 */
public class HabitList{
    // list of all habits created
    public ArrayList<Habit> habits;

    /**
     *  construct an empty habit list
     */
    public HabitList() {
        this.habits = new ArrayList<Habit>();
    }

    /**
     * construct a habit list
     *
     * @param habitList
     */
    public HabitList(ArrayList<Habit> habitList) {
        this.habits = habitList;
    }

    /**
     * Add a habit for the habit list
     * @param habit habit added
     */
    public void add(Habit habit) {
        if (this.hasHabit(habit)) {
            throw new IllegalArgumentException("Duplicate habits.");
        }
        this.habits.add(habit);
    }

    /**
     * check if twe have the habit
     * @param habit habit
     * @return
     */
    public boolean hasHabit(Habit habit) {
        return habits.contains(habit);
    }

    /**
     * delete a habit
     * @param habit habit deleted
     */
    public void delete(Habit habit) {
        habits.remove(habit);
    }

    /**
     * get the size of the habit list
     * @return
     */
    public int getCount() {
        return habits.size();
    }

    /**
     * get the index of the habit
     * @param index  the position of the habit in the list
     * @return
     */
    public Habit getHabit(int index) {
        return habits.get(index);
    }

    /**
     *  compare the start day of the habits
     */
    class DateCompare implements Comparator<Habit> {
        public int compare(Habit habit, Habit t1) {
            if (habit.getStartDate().before(t1.getStartDate()))
                return 1;
            if (habit.getStartDate().after(t1.getStartDate()))
                return -1;
            return 0;
        }
    }

    /**
     * return the sorted habit list
     * @return
     */
    public ArrayList<Habit> getHabits() {
        DateCompare compare = new DateCompare();
        Collections.sort(habits, compare);
        return habits;
    }

    /**
     * return today's habits
     * @return
     */
    public ArrayList<Habit> getTodayHabits() {
        ArrayList<Habit> todayList = new ArrayList<Habit>();
        Calendar today = Calendar.getInstance();
        int weekDay = today.get(Calendar.DAY_OF_WEEK)-1;

        for (Habit h : habits) {
            if ( (h.getRepeatWeekOfDay().contains(weekDay))  &&( h.getStartDate().before(today))  ){
                todayList.add(h);
            }
        }
        return todayList;
    }

    /**
     * check if the habit already exists in the list
     * @param habit
     * @return
     */
    public boolean check_dup (Habit habit){
        for (Habit element: habits){
            if (element.getTitle().toUpperCase().equals(habit.getTitle().toUpperCase())){
                return true ;
            }
        }
        return false;
    }





}
