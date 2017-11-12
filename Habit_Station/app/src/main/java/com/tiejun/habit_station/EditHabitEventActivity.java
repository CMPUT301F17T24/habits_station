/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class EditHabitEventActivity extends AppCompatActivity {

    private TextView info;
    private EditText comment;
    private DatePicker simpleDatePicker;
    private CheckBox location;
    private Button image;
    private Bitmap photo;

    protected HabitEventList habitEventList = new HabitEventList();
    protected HabitEvent habitEvent;


    public  int do_year = 0,
            do_month = 0,
            do_day = 0;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit_event);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();
        final int habitIndex = intent.getIntExtra("habit index", 0);
        final int eventIndex = intent.getIntExtra("select",0);
        image = (Button) findViewById(R.id.image);

        if (!hasCamera()) {
            image.setEnabled(false);
        }


        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }


        final Habit habit = user.getHabitList().getHabit(habitIndex);
        comment = (EditText)findViewById(R.id.comment);
        simpleDatePicker = (DatePicker)findViewById(R.id.datePicker);

        final Calendar startDate = habit.getStartDate();
        final HashSet<Integer> weekDay = habit.getRepeatWeekOfDay();

        final Calendar doDate = Calendar.getInstance();
        if (do_year == 0 && do_month == 0 && do_day == 0){
            //
            do_year = doDate.get(Calendar.YEAR);
            do_month = doDate.get(Calendar.MONTH);
            do_day = doDate.get(Calendar.DAY_OF_MONTH);
        }
//        doDate.set(do_year, do_month, do_day);


        final Button confirmBtn = (Button) findViewById(R.id.save);                        //  click the button to save the information
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                boolean added = true;
                String sComment = comment.getText().toString();

////////////////////////////////////// fix time comparison bug /////////////////
                if(  (startDate.get(Calendar.YEAR) == doDate.get(Calendar.YEAR) )
                        && ( (startDate.get(Calendar.MONTH) == doDate.get(Calendar.MONTH)))
                        && ( (startDate.get(Calendar.DAY_OF_MONTH) == doDate.get(Calendar.DAY_OF_MONTH))))
                {
                    added = true;
                }
                else{
                    if (startDate.after(doDate)){
                        Toast.makeText(getApplicationContext(), "Should after startDate.", Toast.LENGTH_SHORT).show();
                        added = false;
                    }
                }
////////////////////////////////////////////////////

                if (sComment.length() > 20){
                    comment.setError("Title should not be empty and should be at most 20 words");
                    added = false;
                }


                doDate.set(do_year, do_month, do_day);

                Calendar today = Calendar.getInstance();
                if (doDate.after(today)) {
                    Toast.makeText(getApplicationContext(), "Are you sure you have done this event?", Toast.LENGTH_SHORT).show();
                    added = false;
                }


                Log.d("TTT","Week day is"+String.valueOf(doDate.get(Calendar.DAY_OF_WEEK)-1));
                int day_of_week = doDate.get(Calendar.DAY_OF_WEEK)-1;
                if (! weekDay.contains(day_of_week)){
                    Toast.makeText(getApplicationContext(), "Not in the plan.", Toast.LENGTH_SHORT).show();
                    added = false;
                }

        /*        if (startDate.after(doDate)){
                    Toast.makeText(getApplicationContext(), "Should after startDate.", Toast.LENGTH_SHORT).show();
                    added = false;
                }
*/

                if(added){
                    added = setEvent(userName,habit.getTitle(),sComment,doDate, habitIndex, eventIndex);
                }
                if (added) {
                    Intent intent = new Intent(EditHabitEventActivity.this, HabitEventLibraryActivity.class);
                    intent.putExtra("habit index", habitIndex);

                    startActivity(intent);
                }

            }
        });

///////////////////////////////////////////////////////

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        simpleDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        do_year = simpleDatePicker.getYear();
                        do_month = simpleDatePicker.getMonth();
                        do_day =  simpleDatePicker.getDayOfMonth();

                    }
                });




    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        Log.d("TTT","start");

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        Intent intent = getIntent();
        int habitIndex = intent.getIntExtra("habit index", 0);
        int eventIndex = intent.getIntExtra("select",0);


        User user = new User();
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }



        habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();


        info = (TextView) findViewById(R.id.info);
        Habit habit = user.getHabitList().getHabit(habitIndex);
        //String habit_name  = habit.getTitle();
        HashSet<Integer> days = habit.getRepeatWeekOfDay();
        ArrayList<String> sdays = new ArrayList<String>();

        if (days.contains(1)){
            sdays.add("M");
        }
        if (days.contains(2)){
            sdays.add("T");
        }
        if (days.contains(3)){
            sdays.add("W");
        }
        if (days.contains(4)){
            sdays.add("R");
        }
        if (days.contains(5)){
            sdays.add("F");
        }
        if (days.contains(6)){
            sdays.add("SAT");
        }
        if (days.contains(0)){
            sdays.add("SUN");
        }

        info.setText(habit.toString() +"\nReason: "+habit.getReason()+"\nPlan: "+sdays);

        if (eventIndex >=0){

            Log.d("TTT",String.valueOf(eventIndex));
// if the event already exists, show its old info
            habitEventList = user.getHabitList().getHabit(habitIndex).getHabitEventList();
            HabitEvent event = habitEventList.getEvent(eventIndex);
            comment = (EditText)findViewById(R.id.comment);
            comment.setText(event.geteComment());

            simpleDatePicker = (DatePicker)findViewById(R.id.datePicker);
            simpleDatePicker.updateDate(event.geteTime().get(Calendar.YEAR),event.geteTime().get(Calendar.MONTH),event.geteTime().get(Calendar.DAY_OF_MONTH));

        }
        else{
            Log.d("TTT","event haven't been created");
        }


    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean setEvent(String current_user,String sName,String sComment,Calendar doDate, int habitIndex,int eventIndex )
    {

        User user = new User();
        //String query = current_user;
        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(current_user);
        try {
            user = getUserTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }

        //Log.d("CCC",habit.getTitle());

        Habit habit = user.getHabitList().getHabit(habitIndex);
        HabitEventList events = habit.getHabitEventList();




        HabitEvent event1 = new HabitEvent(sName, doDate, sComment);

        if (events.check_duplicate(event1)){
            Toast.makeText(this, "The event for that day already exists!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{

            if (eventIndex >=0 ){
                HabitEvent old_event = events.getEvent(eventIndex);
                old_event.seteComment(sComment);
                old_event.seteTime(doDate);
                //old_event.setePhoto();
                //old_event.seteLocation();

                ElasticSearchUserController.AddUserTask addUserTask
                        = new ElasticSearchUserController.AddUserTask();
                addUserTask.execute(user);
                return true;

            }


            else {
                events.add(event1);
            }
            HabitEventList events2 = new HabitEventList(events.sortEvents());
            habit.setHabitEventList(events2);
            //user.setHabitList(list2);



            ElasticSearchUserController.AddUserTask addUserTask
                    = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            return true;
        }

    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // take a picture and pass result along to onActivityResult
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    // IF YOU WANT TO RETURN THE IMAGE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // GET THE PHOTO
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            //habitEvent.setePhoto(photo);
            Intent intent = new Intent(EditHabitEventActivity.this, PhotoDisplayActivity.class);
            intent.putExtra("image", photo);
            startActivity(intent);

        }
    }

}
