/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

/**
 * Created by yfeng3 on 2017-10-21.
 */

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * a class for habit events
 *
 * @author Xtie
 * @version 1.5
 * @see HabitEventList
 * @since 1.0
 *
 */
public class HabitEvent {
    public String uName;

    public Calendar sTime;
    public String eReason;
    public ArrayList<String> Plan = new ArrayList<String>();

    public String eName;
    public Calendar eTime;
    public String eComment;
    public Image ePhoto;
    public Location eLocation;


    /**
     * construct a habit event
     */
    public HabitEvent(){}

    /**
     * construct a habit event with given information
     *
     * @param name Event name
     * @param time      event finish Time
     * @param comment   event comment
     *
     */
    public HabitEvent(String name, Calendar time, String comment){

        this.eName = name;
        this.eTime = time;
        this.eComment = comment;

    }

    /**
     *
     * @param uName user name
     * @param eName event name
     * @param time event finish time
     * @param comment event comment
     */
    public HabitEvent(String uName,String eName, Calendar time, String comment){
        this.uName = uName;
        this.eName = eName;
        this.eTime = time;
        this.eComment = comment;
    }

    /**
     * return the event's user name
     * @return
     */
    public String getuName(){
        return  uName;
    }


    /**
     * return the event's name
     * @return
     */
    public String geteName(){
        return this.eName;
    }

    /**
     * return the event's finish time
     * @return
     */
    public Calendar geteTime(){
        return eTime;
    }

    /**
     * return event's reason
     * @return
     */
    public String geteReason(){
        return this.eReason;
    }

    /**
     * return the event's comment
     * @return
     */
    public String geteComment(){
        return this.eComment;
    }

    /**
     * return the event's image
     * @return
     */
    public Image getePhoto(){
        return this.ePhoto;
    }

    /**
     * return the event's location
     * @return
     */
    public Location geteLocation(){
        return this.eLocation;
    }


    /**
     *  set the event's name
     * @param name event's name
     */
    public void seteName(String name){
        this.eName = name;
    }

    /**
     * set the event's finish time
     * @param time
     */
    public void seteTime(Calendar time){
        this.eTime = time;
    }

    /**
     * set the event's reason
     * @param reason event's reason
     */
    public void seteReason(String reason){
        this.eReason = reason;

    }

    /**
     * set the event's photo
     * @param photo event's photo
     */
    public void setePhoto(Image photo){
        this.ePhoto = photo;
    }

    /**
     * set the event's location
     * @param location event's location
     */
    public void seteLocation(Location location){
        this.eLocation = location;
    }


    /**
     * set the event's comment
     * @param comment event's location
     */
    public void seteComment(String comment){
        this.eComment = comment;
    }

    /**
     * to string method
     * @return
     */
    @Override
    public String toString(){
        return this.eName +":"+ eTime.get(Calendar.YEAR)+"/" + String.valueOf(eTime.get(Calendar.MONTH)+1) + "/" + eTime.get(Calendar.DAY_OF_MONTH);
    }




}
