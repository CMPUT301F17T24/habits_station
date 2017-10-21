/*
 * Copyright (c) 2017 TeamX, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact contact@abc.ca.
 */

package com.tiejun.habit_station;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by tiejun on 2017-10-21.
 */

public class UserListTest extends ActivityInstrumentationTestCase2 {

    public UserListTest() {
        super(MainActivity.class);
    }

    public void testAddUser() {
        UserList users = new UserList();
        User user = new User(1, "test1");
        users.addUser(user);
        User user1 = new User(2, "test1");
        users.addUser(user1);
        if (users.hasUser(user1)) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }

        try {
            users.addUser(user);
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public void testGetUser() {
        UserList users = new UserList();
        User user = new User(1, "test1");
        users.addUser(user);

        User expected = users.getUser(user);
        if (user.equals(expected))
            assertTrue(true);
        else
            assertTrue(false);
    }

    public void testHasUser() {
        UserList users = new UserList();
        User user = new User(1, "test1");
        users.addUser(user);
        if (users.hasUser(user)) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }

    public void testDeleteUser() {
        UserList users = new UserList();
        User user = new User(1, "test1");
        users.addUser(user);
        users.deleteUser(user);
        if (users.hasUser(user))
            assertTrue(false);
        else
            assertTrue(true);
    }

    public void testGetCount() {
        UserList users = new UserList();
        User user = new User(1, "test1");
        users.addUser(user);
        if (users.getCount() == 1)
            assertTrue(true);
        else
            assertTrue(false);
    }

    public void testGetFollowee() {
        User user = new User(1, "test1");
        User user1 = new User(2, "test1");
        User user2 = new User(3, "test1");

        ArrayList<Integer> followees = new ArrayList<>();
        followees.add(user1.getUid());
        followees.add(user2.getUid());
        user.setFollowee(followees);
        assertEquals(followees, user.getFollowee());
    }

    public void testGetFollower() {
        User user = new User(1, "test1");
        User user1 = new User(2, "test1");
        User user2 = new User(3, "test1");

        ArrayList<Integer> followers = new ArrayList<>();
        followers.add(user1.getUid());
        followers.add(user2.getUid());
        user.setFollower(followers);
        assertEquals(followers, user.getFollower());

    }
}
