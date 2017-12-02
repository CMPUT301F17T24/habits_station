/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.util.ArrayList;


/**
 *  consturct a user list
 *
 *  @author Xtie
 * @version 1.5
 * @see User
 * @since 1.0
 */
public class UserList {
    private ArrayList<User> users = new ArrayList<>();

    /**
     * construct an empty user list
     */
    public UserList() {
    }

    /**
     *  add a user to the list
     * @param user  user object
     */
    public void addUser(User user) {
        if (hasUser(user))
            throw new IllegalArgumentException("User already existed.");
        users.add(user);
    }

    /**
     * check if the list has the user
     * @param user specific user
     * @return
     */
    public boolean hasUser(User user) {
        return users.contains(user);
    }

    /**
     * delete the user
     * @param user specific user
     */
    public void deleteUser(User user) {
        users.remove(user);
    }

    /**
     * return the user
     * @param user specific user
     * @return
     */
    public User getUser(User user) {
        for (User u : users) {
            if (u.equals(user))
                return u;
        }
        return null;
    }

    /**
     * return the size of the user list
     * @return
     */
    public int getCount() {
        return users.size();
    }

    /**
     * check if the user already exists
     * @param user
     * @return
     */
    public boolean duplicate(User user){

        for (User element: users){
            if (element.getName().equals(user.getName())){
                return true;
            }
        }
        return false;
    }

}
