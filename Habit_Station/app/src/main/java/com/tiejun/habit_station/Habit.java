/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by tiejun on 2017-10-13.
 */

public class Habit implements Comparable<Habit> {

    private  String title;
    private String reason;
    private Calendar startDate;
    //private ArrayList<HabitEvent> events;

    // "Monday" 2, "Tuesday" 3, "Wednesday" 4, "Thursday" 5, "Friday" 6, "Saturday" 7, "Sunday" 1
    private HashSet<Integer> repeatWeekOfDay;
    SimpleDateFormat formatter = new SimpleDateFormat("M, yyyy-MM-dd");

    public Habit(String title, String reason, Calendar startDate, HashSet<Integer> repeatWeekOfDay) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.repeatWeekOfDay = repeatWeekOfDay;
    }

    public void setTitle(String title) throws InvalidInputException {
        if (title.length()>20 || title.isEmpty() || title.trim().isEmpty()) {
            throw new InvalidInputException();
        }
        this.title = title;
    }

    public void setReason(String reason) throws InvalidInputException {
        if (reason.length()>30) {
            throw new InvalidInputException();
        }
        this.reason = reason;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }                                                                       ///////////////////////////////////////////////

    public void setRepeatWeekOfDay(HashSet<Integer> repeatWeekOfDay) {//throws InvalidInputException {
//        if (repeatWeekOfDay.size() == 0)
//            // the user has to choose one day of week repeat
//            throw new InvalidInputException();
        this.repeatWeekOfDay = repeatWeekOfDay;
    }

    public String getTitle() {
        return title;
    }

    public String getReason() {
        return reason;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public HashSet<Integer> getRepeatWeekOfDay() {
        return repeatWeekOfDay;
    }

    @Override
    public String toString() {
        return title + " starts on " + formatter.format(startDate) + ".";
    }

    public int compareTo(Habit habit) {
        return habit.getStartDate().compareTo(startDate);
    }

}
