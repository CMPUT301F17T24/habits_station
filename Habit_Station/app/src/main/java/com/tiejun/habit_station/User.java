/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.util.ArrayList;
import java.util.Objects;
<<<<<<< HEAD
import io.searchbox.annotations.JestId;
=======
>>>>>>> origin

/**
 * Created by tiejun on 2017-10-21.
 */

public class User {

<<<<<<< HEAD
    @JestId
    private String Aid;

=======
>>>>>>> origin
    private int uid;
    private String name;
    private ArrayList<Integer> follower;
    private ArrayList<Integer> followee;
    private HabitList habits;

    public User(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

<<<<<<< HEAD
    public String getAid() {
        return Aid;
    }

    public void setAid(String aid) {
        Aid = aid;
    }

    public User() {}

=======
>>>>>>> origin
   public int getUid() {
        return uid;
    }

<<<<<<< HEAD
    public void setUid(int uid) {
        this.uid = uid;
    }
=======
    /*public void setUid(int uid) {
        this.uid = uid;
    }*/
>>>>>>> origin


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid;
    }

    public ArrayList<Integer> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<Integer> follower) {
        this.follower = follower;
    }

    public ArrayList<Integer> getFollowee() {
        return followee;
    }

    public void setFollowee(ArrayList<Integer> followee) {
        this.followee = followee;
    }


    public void setHabitList(HabitList habits){
        this.habits = habits;
    }

    public HabitList getHabitList(){
        return this.habits;
    }


    //    @Override
//    public int hashCode() {
//        return Objects.hash(uid);
//    }
}
