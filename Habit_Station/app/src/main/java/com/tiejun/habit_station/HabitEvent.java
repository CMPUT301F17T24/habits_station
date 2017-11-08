/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

/**
 * Created by yfeng3 on 2017-10-21.
 */

import android.location.Location;
import android.media.Image;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * a class for habit events
 */
public class HabitEvent {

    public String eName;

    public Calendar eTime;

    public String eReason;

    public String eComment;

    public Image ePhoto;

    public Location eLocation;

    public boolean status;

    public HabitEvent(String name, Calendar time, String comment){

        this.eName = name;
        this.eTime = time;
        this.eComment = comment;

        this.status = false;


    }


    public String geteName(){
        return this.eName;
    }

    public Calendar geteTime(){

        return eTime;

    }

    public String geteReason(){

        return this.eReason;

    }

    public String geteComment(){

        return this.eComment;

    }

    public Image getePhoto(){

        return this.ePhoto;

    }

    public Location geteLocation(){
        return this.eLocation;
    }


    public boolean getStatus(){

        return this.status;
    }


    public void seteName(String name){
        this.eName = name;

    }

    public void seteTime(Calendar time){
        this.eTime = time;
    }

    public void seteReason(String reason){
        this.eReason = reason;

    }

    public void setePhoto(Image photo){
        this.ePhoto = photo;

    }

    public void seteLocation(Location location){

    }

    public void setStatus(boolean status){
        this.status = status;

    }

    public void seteComment(String comment){
        this.eComment = comment;
    }

    @Override
    public String toString(){
        return this.eName +":"+ eTime.get(Calendar.YEAR)+"/" + String.valueOf(eTime.get(Calendar.MONTH)+1) + "/" + eTime.get(Calendar.DAY_OF_MONTH);
    }

    ///


}
