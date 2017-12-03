/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of lisense in this project. Otherwise please contact xtie@ualberta.ca.
 */

package com.tiejun.habit_station;

/**
 * The class of InvalidInputException extends Exception class
 *
 * @author xtie
 * @version 1.0
 *
 */
public class InvalidInputException extends Exception {

    /**
     * Prompt user invalid habit input
     */
    public InvalidInputException() {
       super("Invalid Habit Input!");
   }
}
