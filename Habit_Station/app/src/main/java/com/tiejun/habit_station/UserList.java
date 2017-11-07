/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import java.util.ArrayList;

/**
 * Created by tiejun on 2017-10-21.
 */

public class UserList {
    private ArrayList<User> users = new ArrayList<>();

    public UserList (){}

    public void addUser(User user) {
        if (hasUser(user))
            throw new IllegalArgumentException("User already existed.");
        users.add(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }

    public void deleteUser(User user) {
        users.remove(user);
    }

    public User getUser(User user) {
        for (User u : users) {
            if (u.equals(user))
                return u;
        }
        return null;
    }

    public int getCount() {
        return users.size();
    }

    public boolean duplicate(User user){

        for (User element: users){
            if (element.getName().equals(user.getName())){
                return true;
            }
        }
        return false;
    }

}
