
/*
 * Copyright (c) 2017 Team24, CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify this code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
/**
 * Activity to show the photo taken by the user using camera
 *
 * @author xtie
 * @version 1.0
 *
 */
public class PhotoDisplayActivity extends AppCompatActivity {

    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        photo = (ImageView) findViewById(R.id.photo);
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image");
        photo.setImageBitmap(bitmap);

    }
}