/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.util.ArrayList;
import java.util.Objects;
import io.searchbox.annotations.JestId;

/**
 * Created by tiejun on 2017-10-21.
 */

/**
 *
 *  a class for user
 *
 *  @author Xtie
 * @version 1.5
 * @see UserList
 * @since 1.0
 *
 */
public class User {

    @JestId
    private int uid;
    private String name;
    private ArrayList<String> follower = new ArrayList<String>();
    private ArrayList<String> followee = new ArrayList<String>();


    /**
     *  construct a user
     *
     * @param uid the user id
     * @param name  the user name
     */
    public User(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    /**
     * construct an empty user
     */public User() {}

    /**
     * return the user id
     * @return
     */
   public int getUid() {
        return uid;
    }

    /**
     * set the user id
     * @param uid user id
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * return the user name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set the user name
     * @param name user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * to string method
     * @return
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * check if the user already exists
     * @param o object
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid;
    }

    /**
     *  return a list of followers
     * @return
     */
    public ArrayList<String> getFollower() {
        return follower;
    }

    /**
     * set the follower list
     * @param follower a list of updated followers
     */
    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    /**
     * return a list of followees
     * @return
     */
    public ArrayList<String> getFollowee() {
        return followee;
    }

    /**
     * set a list of followees
     * @param followee a list of updated followees
     */
    public void setFollowee(ArrayList<String> followee) {
        this.followee = followee;
    }


}
