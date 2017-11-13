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
import android.media.Image;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;


public class EditHabitEventActivity extends AppCompatActivity {

    private TextView info;
    private EditText comment;
    private DatePicker simpleDatePicker;
    private Button image;
    private Bitmap photo;

    protected HabitEventList habitEventList = new HabitEventList();
    protected HabitEvent habitEvent;

    private String habit_name;
    private Habit habit;

    private ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();


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
        //final int habitIndex = intent.getIntExtra("habit index", 0);

        final int eventIndex = intent.getIntExtra("select", 0);              // !!!!!!!!!!!!
        habit_name = intent.getStringExtra("habit name");


        comment = (EditText)findViewById(R.id.comment);
        simpleDatePicker = (DatePicker)findViewById(R.id.datePicker);

        image = (Button) findViewById(R.id.image);

        if (!hasCamera())
            image.setEnabled(false);

        String habit_id = userName + habit_name.toUpperCase();
        Log.d("habitid",habit_id);

        //final Habit habit = new Habit();

        ElasticSearchHabitController.GetHabitTask getHabit
                = new  ElasticSearchHabitController.GetHabitTask();
        getHabit.execute(habit_id);

        try {
            habit = getHabit.get();  //other way later
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("habitinfo","???");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("habitinfo","???");
        }


        Log.d("habitinfo",habit.getTitle());

        HashSet<Integer> days = habit.getRepeatWeekOfDay();
        ArrayList<String> sdays = getPlans(days);


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
                boolean added = true;   // variable to indicate if event added successfully
                String sComment = comment.getText().toString();

////////////////////////////////////// fix time comparison bug /////////////////

                doDate.set(do_year, do_month, do_day);

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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

                if (sComment.length() > 20){
                    comment.setError("Title should not be empty and should be at most 20 words");
                    added = false;
                }

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

                if(added){

                    HabitEvent event = new HabitEvent(userName,habit.getTitle(), doDate, sComment);

                    //added = setEvent(userName,habit.getTitle(),sComment,doDate, habitIndex, eventIndex);

                    added = setEvent(userName, event, eventIndex);
                }

                if (added) {

                   /* Intent intent = new Intent(EditHabitEventActivity.this, HabitEventLibraryActivity.class);
                   // intent.putExtra("habit index", habitIndex);   // later will be useless

                    intent.putExtra("habit name",habit_name);

                    startActivity(intent);
                    */

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
        int habitIndex = intent.getIntExtra("habit index", 0);  // later be useless
        int eventIndex = intent.getIntExtra("select",0);
        habit_name = intent.getStringExtra("habit name");
        String event_query = intent.getStringExtra("query");


///////////////////////


        info = (TextView) findViewById(R.id.info);


        String habit_id = userName +habit_name.toUpperCase();
        Habit habit = new Habit();

        ElasticSearchHabitController.GetHabitTask getHabit
                = new  ElasticSearchHabitController.GetHabitTask();
        getHabit.execute(habit_id);

        try {
            habit = getHabit.get();  //other way later
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        HashSet<Integer> days = habit.getRepeatWeekOfDay();
        ArrayList<String> sdays = getPlans(days);

        info.setText(habit.toString() +"\nReason: "+habit.getReason()+"\nPlan: "+sdays);


/////////////////////////

        if (eventIndex >=0){    // if the event already exists, show its old info         edit, need info directly from event

            //////  used to find the events /////////
            /*String event_query = "{\n" +
                    "  \"query\": { \n" +
                    "\"bool\": {\n"+
                    "\"must\": [\n"+
                    "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                    "{"+ " \"term\" : {  \"eName\" : \"" + habit_name +  "\" }}\n" +
                    "]"+
                    "}"+
                    "}"+
                    "}";
*/
            ElasticSearchEventController.GetEvents getEvents
                    = new  ElasticSearchEventController.GetEvents();
            getEvents.execute(event_query);
            try {
                fillist.clear();
                fillist.addAll(getEvents.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            HabitEvent event = fillist.get(eventIndex);
        /*    info.setText(event.geteName() + " \nstarts " + event.getsTime().get(Calendar.YEAR)+"/"
                    + String.valueOf(event.getsTime().get(Calendar.MONTH)+1) + "/" + event.getsTime().get(Calendar.DAY_OF_MONTH)
                    +"\nReason: "+event.geteReason()+"\nPlan: "+event.getPlan());
*/
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

    public boolean setEvent(String current_user, HabitEvent new_event, int eventIndex) {

        Boolean isOnline = InternetChecker.isOnline(this);

        Log.d("event",String.valueOf(eventIndex));


        String new_id = current_user + new_event.geteName()
                + new_event.geteTime().get(Calendar.YEAR)
                + String.valueOf(new_event.geteTime().get(Calendar.MONTH) + 1)
                + new_event.geteTime().get(Calendar.DAY_OF_MONTH);

        if (eventIndex >= 0 && isOnline) {              //  used for edit when CONNECTED TO INTERNET

                //////  used to find the events /////////
                String event_query = "{\n" +
                        "  \"query\": { \n" +
                        "\"bool\": {\n" +
                        "\"must\": [\n" +
                        "{" + " \"term\" : { \"uName\" : \"" + current_user + "\" }},\n" +
                        "{" + " \"match\" : {  \"eName\" : \"" + habit_name + "\" }}\n" +
                        "]" +
                        "}" +
                        "}" +
                        "}";

                ElasticSearchEventController.GetEvents getEvents
                        = new ElasticSearchEventController.GetEvents();
                getEvents.execute(event_query);
                try {
                    fillist.clear();
                    fillist.addAll(getEvents.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                HabitEvent old_event = fillist.get(eventIndex);

                ////////////////////

                String old_id = current_user + old_event.geteName()
                        + old_event.geteTime().get(Calendar.YEAR)
                        + String.valueOf(old_event.geteTime().get(Calendar.MONTH) + 1)
                        + old_event.geteTime().get(Calendar.DAY_OF_MONTH);


                if ((existedEvent(new_id)) && !(new_id.equals(old_id))  ){
                    Toast.makeText(getApplicationContext(), "This event for today already exists !", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    ///////  delete old event /////
                    ElasticSearchEventController.DeleteEventTask deleteEventTask
                            = new ElasticSearchEventController.DeleteEventTask();
                    deleteEventTask.execute(old_event);

                    ////// add new event ///////
                    ElasticSearchEventController.AddEventTask addEventTask
                            = new ElasticSearchEventController.AddEventTask();
                    addEventTask.execute(new_event);

                    Toast.makeText(getApplicationContext(), "Successfully updated the event.", Toast.LENGTH_SHORT).show();

                    return true;
                }
            }

            else { ////// add new event !! ///////

                OfflineEventController offlineEventController = new OfflineEventController(new_id,this);

                if (!isOnline) {
                    OfflineEventModule offlineEventModule = new OfflineEventModule();
                    offlineEventModule.action = "add";
                    offlineEventModule.event = new_event;
                    offlineEventController.addOfflineAction(offlineEventModule.action,offlineEventModule.event);
                    Toast.makeText(this, "Event added offline!",Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (existedEvent(new_id)){
                    offlineEventController.clearFile(new_id);
                    Toast.makeText(getApplicationContext(), "This event for today already exists !", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Boolean sync = offlineEventController.syncOfflineEvent();
                ElasticSearchEventController.AddEventTask addEventTask
                        = new ElasticSearchEventController.AddEventTask();
                addEventTask.execute(new_event);
                Toast.makeText(getApplicationContext(), "Successfully added to history.", Toast.LENGTH_SHORT).show();
                return true;
            }

    }

     public ArrayList<String> getPlans (HashSet<Integer> days){
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

         return sdays;

     }

    private boolean existedEvent (String id) {
        ElasticSearchEventController.IsExist isExist = new ElasticSearchEventController.IsExist();
        isExist.execute(id);

        try {
            if (isExist.get()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // Launch the camrea
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


