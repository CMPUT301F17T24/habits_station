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
import java.util.HashSet;

/**
 * Created by tiejun on 2017-10-13.
 */

/**
 *  This class is used to create a Habit object
 *
 * @author Xtie
 * @version 1.5
 * @see HabitList
 * @since 1.0
 *
 */
public class Habit implements Comparable<Habit> {
    private String uName;
    private  String title;
    private String reason;
    private Calendar startDate;
    // "Monday" 1, "Tuesday" 2, "Wednesday" 3, "Thursday" 4, "Friday" 5, "Saturday" 6, "Sunday" 0
    private HashSet<Integer> repeatWeekOfDay;
    private HabitEventList events;



    /**
     *  Create Habit object with no initialization
     */
    public Habit(){}

    /**
     *
     *   Create Habit object with information
     *
     * @param uname   useName
     * @param title     Habit title
     * @param reason    Habit Reason
     * @param startDate Habit StartDate
     * @param repeatWeekOfDay   Habit frequency( what days in a week plan to do)
     */
    public Habit(String uname, String title, String reason, Calendar startDate, HashSet<Integer> repeatWeekOfDay) {
        this.uName = uname;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.repeatWeekOfDay = repeatWeekOfDay;
    }

    /**
     *  return userName
     *
     * @return
     */
    public String getuName() {
        return uName;
    }

    /**
     *  Set title for the Habit
     *
     * @param title    Habit Title
     * @throws InvalidInputException
     */
    public void setTitle(String title) throws InvalidInputException {
        if (title.length()>20 || title.isEmpty() || title.trim().isEmpty()) {
            throw new InvalidInputException();
        }
        this.title = title;
    }

    /**
     *  set reason for the Habit
     *
     * @param reason  Habit reason
     * @throws InvalidInputException
     */
    public void setReason(String reason) throws InvalidInputException {
        if (reason.length()>30) {
            throw new InvalidInputException();
        }
        this.reason = reason;
    }

    /**
     * Set Date for the Habit
     * @param startDate  Habit startDate
     */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    /**
     *  Set  Repeat days of week
     *
     * @param repeatWeekOfDay Habit frequency( what days in a week plan to do)
     */
    public void setRepeatWeekOfDay(HashSet<Integer> repeatWeekOfDay) {
        this.repeatWeekOfDay = repeatWeekOfDay;
    }

    /**
     *  return Habit Title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *  return habit reason
     * @return
     */
    public String getReason() {
        return reason;
    }

    /**
     * return Habit start date
     *
     * @return
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * return Habit frequency( what days in a week plan to do)
     * @return
     */
    public HashSet<Integer> getRepeatWeekOfDay() {
        return repeatWeekOfDay;
    }

    /**
     * to String method
     * @return
     */
    @Override
    public String toString() {
        return this.uName +"  >>>  "+ this.title + " \nstarts " + startDate.get(Calendar.YEAR)+"/" + String.valueOf(startDate.get(Calendar.MONTH)+1) + "/" + startDate.get(Calendar.DAY_OF_MONTH); //;formatter.format(startDate) + ".";
    }

    /**
     * used to compare the start date of the two habits
     *
     * @param habit Habit object
     *
     * @return
     */
    public int compareTo(Habit habit) {
        return habit.getStartDate().compareTo(startDate);
    }

    /**
     *  set the corresponding Habit Event List
     * @param events  corresponding Habit EventList
     */
    public void setHabitEventList(HabitEventList events){
        this.events = events;
    }

    /**
     * return the corresponding Habit Event List
     * @return
     */
    public HabitEventList getHabitEventList(){
        return this.events;
    }


}
