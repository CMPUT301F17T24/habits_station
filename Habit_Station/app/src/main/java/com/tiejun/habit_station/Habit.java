/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tiejun on 2017-10-13.
 */

public class Habit implements Comparable<Habit> {

    private String title;
    private String reason;
    private Calendar startDate;
    private ArrayList<Calendar> fulfillDates = new ArrayList<Calendar>();
    private ArrayList<String> repeatWeekOfDay = new ArrayList<String>();

    SimpleDateFormat formatter = new SimpleDateFormat("M, yyyy-MM-dd");

    public Habit(String title, String reason, Calendar startDate) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        //startDate.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        //fulfillDates.add(startDate);
    }

    public void setTitle(String title) throws ArgTooLongException{
        if (title.length()>20) {
            throw new ArgTooLongException();
        }
        this.title = title;
    }

    public void setReason(String reason) throws ArgTooLongException{
        if (reason.length()>30) {
            throw new ArgTooLongException();
        }
        this.reason = reason;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setFulfillDates(ArrayList<Calendar> fulfillDates) {
        this.fulfillDates = fulfillDates;
    }

    public void setRepeatWeekOfDay(ArrayList<String> repeatWeekOfDay) {
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

    public ArrayList<Calendar> getFulfillDates() {
        return fulfillDates;
    }

    public ArrayList<String> getRepeatWeekOfDay() {
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
