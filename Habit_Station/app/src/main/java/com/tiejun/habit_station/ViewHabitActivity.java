/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ObjectUtils;
import org.w3c.dom.Text;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tiejun.habit_station.R.id.date;
import static com.tiejun.habit_station.R.id.habits;
import static java.sql.Types.NULL;

/**
 *
 * this activity shows details about a habit
 * with button to start new intent to edit or show the status
 * fulfilled by using start for result
 */
public class ViewHabitActivity extends AppCompatActivity {
    
    protected HabitList habitsList =new HabitList();
    private TextView theTitle;
    private TextView theName;
    private TextView theReason;
    private TextView theDate;
    private TextView showRepeat;
    private Habit habit;
    private int index;
    private User user = new User();

    private ArrayList<Habit> fillist  = new ArrayList<Habit>();
    private String event_query;
    private ArrayList<HabitEvent> events  = new ArrayList<HabitEvent>();


    /**
     * online resource from stack overflow of pass variable back to main activity
     *
     * modified to start a new intent of edit habit
     *
     * requestcode 1 for edit and delete
     * if delete go back to the library
     *  if edit, update the content
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");


        // find  the corresponding events:
/*        ElasticSearchEventController.GetEvents getHEvent
                = new  ElasticSearchEventController.GetEvents();
        getHEvent.execute(event_query);

        try {

            events.addAll(getHEvent.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

*/
        if (requestCode == 1) { // edit or delete habit
            if(resultCode == RESULT_OK) {
                // set variable of edited data here
                int delSig =data.getIntExtra("delSig",0); //get signal of delete
                if( delSig == 1 ){ // fix a issue of delete

                    ///// delete corresponding events? ///////////////////


/////// find events
                    String event_query = "{\n" +
                            "  \"query\": { \n" +
                            "\"bool\": {\n"+
                            "\"must\": [\n"+
                            "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                            "{"+ " \"match\" : {  \"eName\" : \"" + habit.getTitle() +  "\" }}\n" +
                            "]"+
                            "}"+
                            "}"+
                            "}";


                    ElasticSearchEventController.GetEvents getHEvent
                            = new  ElasticSearchEventController.GetEvents();
                    getHEvent.execute(event_query);

                    try {

                        events.addAll(getHEvent.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    ////////// delete events //////
                    for (HabitEvent element: events){
                        ElasticSearchEventController.DeleteEventTask deleteEventTask
                                = new ElasticSearchEventController.DeleteEventTask();
                        deleteEventTask.execute(element);

                    }
/////////////////// block moved to here
                    ElasticSearchHabitController.DeleteHabitTask deleteHabitTask
                            = new ElasticSearchHabitController.DeleteHabitTask();
                    deleteHabitTask.execute(habit);

                    Intent deleteBackIntent = new Intent(getApplicationContext(), HabitLibraryActivity.class);
                    startActivity(deleteBackIntent);
                }


                if( delSig == 0){// edit
//////////// added for edit repeat
                    if(data.getIntegerArrayListExtra("newRepeat").isEmpty() == false){
                        HashSet<Integer> newRepeat = new HashSet<Integer>(data.getIntegerArrayListExtra("newRepeat"));
                        habit.setRepeatWeekOfDay(newRepeat);
                    }
////////////end of block
                    Calendar calendar = Calendar.getInstance();

                    try{
                        String newDate = data.getStringExtra("newDate");
                        if( newDate.equals("no change") == false){ //fix error report, cannot use == to compare
                            try {
                                String pattern = "yyyyMMdd";
                                Date date = new SimpleDateFormat(pattern).parse(newDate);
                                calendar.setTime(date);
                                habit.setStartDate(calendar);
                                Log.i("Error", calendar.toString());
                            }catch (Exception e){
                                Log.i("Error", "failed to set date");
                                Toast.makeText(getApplicationContext(), "failed to set date.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        // need to compare to the oldest date of corresponding events

                        /////// find events
                        String event_query = "{\n" +
                                "  \"query\": { \n" +
                                "\"bool\": {\n"+
                                "\"must\": [\n"+
                                "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                                "{"+ " \"match\" : {  \"eName\" : \"" + habit.getTitle() +  "\" }}\n" +
                                "]"+
                                "}"+
                                "}"+
                                "}";


                        ElasticSearchEventController.GetEvents getHEvent
                                = new  ElasticSearchEventController.GetEvents();
                        getHEvent.execute(event_query);

                        try {

                            events.addAll(getHEvent.get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        HabitEventList corres_events = new HabitEventList(events);
                        events = corres_events.sortEvents();
                        
                       /* HabitEvent oldest_event = events.get(events.size());
                        Calendar oldest_date =oldest_event.geteTime();
                        if (calendar.after(oldest_date)) {
                            Toast.makeText(getApplicationContext(), "Failed to set date, should before the oldest event.", Toast.LENGTH_SHORT).show();

                        }*/


                        // update date here, no suitable method
                        habit.setReason(data.getStringExtra("newReason"));
                        habit.setTitle(data.getStringExtra("newTitle"));

                        if (habit.getTitle().equals(data.getStringExtra("newTitle"))){      // haven't changed the title


                            // update habit
                            ElasticSearchHabitController.AddHabitTask addHabitTask
                                    = new ElasticSearchHabitController.AddHabitTask();
                            addHabitTask.execute(habit);

                           ///// no need to change corresponding events

                        }
                 ///////////////////////////// the title is changed ////////////
                        else if (existedHabit(userName+habit.getTitle().toUpperCase())){
                            Toast.makeText(this, "This habit already exists!!!", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            // update habit
                            ElasticSearchHabitController.AddHabitTask addHabitTask
                                    = new ElasticSearchHabitController.AddHabitTask();
                            addHabitTask.execute(habit);


                            // upddate corresponding events
                           for (HabitEvent element: events){
                               element.seteName(data.getStringExtra("newTitle"));

                               ElasticSearchEventController.AddEventTask AddEventTask
                                        = new ElasticSearchEventController.AddEventTask();
                                AddEventTask.execute(element);

                            }
                        }


                    }catch (Exception e){
                        Log.i("Error", "failed to change");
                    }

                }

            }
            onStart(); // fix not update view issue by call onstart
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        
        // my code start here
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String tempName = pref.getString("currentUser", "");
        
        theName = (TextView) findViewById(R.id.name);
        theName.setText(tempName);

    //////////////////////////
        Intent i = getIntent();
        index = i.getIntExtra("habit index", 0); // get index of specific habit
        String habit_query = i.getStringExtra("habit query");
        //String habit_name = i.getStringExtra("habit name");

        ElasticSearchHabitController.GetHabits getHabits
                = new  ElasticSearchHabitController.GetHabits();
        getHabits.execute(habit_query);

        try {
            fillist.clear();
            fillist.addAll(getHabits.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        habit = fillist.get(index); // get the specific habit

/////////////////////////////////////////////////
        
        theTitle = (TextView) findViewById(R.id.showTitle);//title
        theTitle.setText(habit.getTitle());
        
        theReason = (TextView) findViewById(R.id.showReason);// reason
        theReason.setText(habit.getReason());

         /**
         * 　a show repeat method if need to change, change the block or extract
         */
        showRepeat = (TextView) findViewById(R.id.showRepeat);
        ArrayList<Integer> frequency = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
        String daysInWeek = "";
        if(frequency.contains(1) == true){
            daysInWeek += " Monday ";
        }
        if(frequency.contains(2) == true){
            daysInWeek += " Tuesday ";
        }
        if(frequency.contains(3)){
            daysInWeek += " Wednesday ";
        }
        if(frequency.contains(4)){
            daysInWeek += " Thursday ";
        }
        if(frequency.contains(5)){
            daysInWeek += " Friday ";
        }
        if(frequency.contains(6)){
            daysInWeek += " Saturday ";
        }
        if(frequency.contains(7)){
            daysInWeek += " Sunday ";
        }
        showRepeat.setText(daysInWeek);
////////// the block ends here
        
        /**
         * a tostring method to show date
         */
        theDate = (TextView) findViewById(R.id.showDate);// reason
        int year = habit.getStartDate().get(Calendar.YEAR);
        int month = habit.getStartDate().get(Calendar.MONTH);
        int day = habit.getStartDate().get(Calendar.DAY_OF_MONTH);
        
        theDate.setText(Integer.toString(year)+"/"+ Integer.toString(month)+1 +"/"+Integer.toString(day));
        
        /**
         * a listener when edit button clicked,
         *
         * implemented by using strat intent for result
         */
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                
                Intent onClickIntent = new Intent(getApplicationContext(), EditHabitActivity.class);
                
                onClickIntent.putExtra("habitTitle", habit.getTitle()); //title
                onClickIntent.putExtra("habitReason", habit.getReason()); //reason
                onClickIntent.putExtra("year", habit.getStartDate().get(Calendar.YEAR)); //year
                onClickIntent.putExtra("month", habit.getStartDate().get(Calendar.MONTH)+1); //month
                onClickIntent.putExtra("day", habit.getStartDate().get(Calendar.DAY_OF_MONTH)); //day
                
                startActivityForResult(onClickIntent,1);
                
                
            }
        });
        
        /**
         * a listener when states button clicked,
         *
         * implemented by using strat intent for result
         */
        Button state = (Button) findViewById(R.id.status);
        state.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                // online resource from stack overflow to get a
                // count of given day:https://stackoverflow.com/questions/10428798/how-to-calculate-the-number-of-tuesday-in-one-month
                
                Calendar start = habit.getStartDate();
                Calendar today = Calendar.getInstance();

                Log.d("start", String.valueOf(start.get(Calendar.YEAR)));
                Log.d("start", String.valueOf(start.get(Calendar.MONTH)));
                Log.d("start", String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
                Log.d("to", String.valueOf(today.get(Calendar.YEAR)));
                Log.d("to", String.valueOf(today.get(Calendar.MONTH)));
                Log.d("to", String.valueOf(today.get(Calendar.DAY_OF_MONTH)));





                ArrayList<Integer> frequency = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
                int complete=0;
                int total = 0;
                if(start.after(today)){ // not started
                    complete=0;
                    total = 0;
                    Log.d("days","haven't started");

                }
                else { //started

/////////////////////// used to get complete days

                    ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();
                    event_query = "{\n" +
                            "  \"query\": { \n" +
                            "\"bool\": {\n"+
                            "\"must\": [\n"+
                            "{"+ " \"term\" : { \"uName\" : \"" + tempName +  "\" }},\n" +
                            "{"+ " \"match\" : {  \"eName\" : \"" + habit.getTitle() +  "\" }}\n" +
                            "]"+
                            "}"+
                            "}"+
                            "}";

                    ElasticSearchEventController.GetEvents getHEvent
                            = new  ElasticSearchEventController.GetEvents();
                    getHEvent.execute(event_query);

                    try {
                        fillist.clear();
                        fillist.addAll(getHEvent.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    complete = fillist.size();
///////////////////////////////////////////


                    long millsec = today.getTimeInMillis()- start.getTimeInMillis();
                    total = (int)millsec;///1000/60/60/24;


                    //th = today.get(Calendar.DATE) - start.get(Calendar.DATE);
                    Log.d("days",String.valueOf(total));

                    /*maxDays = today.get(Calendar.DATE) - c.get(Calendar.DATE);


                    for (int d = 1; d <= maxDays; d++) {
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        if (frequency.contains(dayOfWeek)) {
                            th++;
                        }
                        c.add(Calendar.DATE, 1); //next day
                    }*/
                }

                
                Intent onClickIntent = new Intent(getApplicationContext(), StatusActivity.class);
                onClickIntent.putExtra("total",total);
                onClickIntent.putExtra("complete",complete);
                
                startActivity(onClickIntent);
            }
        });
    }
    
    @Override
    /**
     * update the content
     */
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        final String tempName = pref.getString("currentUser", "");
        
        theName = (TextView) findViewById(R.id.name);
        theName.setText(tempName);


/////////////////
        Intent i = getIntent();
        index = i.getIntExtra("habit index", 0); // get index of specific habit
        String habit_query = i.getStringExtra("habit query");
        //String habit_name = i.getStringExtra("habit name");

        ElasticSearchHabitController.GetHabits getHabits
                = new  ElasticSearchHabitController.GetHabits();
        getHabits.execute(habit_query);

        try {
            fillist.clear();
            fillist.addAll(getHabits.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        habit = fillist.get(index); // get the specific habit


///////////////////
        
        theTitle = (TextView) findViewById(R.id.showTitle);//title
        theTitle.setText(habit.getTitle());
        
        theReason = (TextView) findViewById(R.id.showReason);// reason
        theReason.setText(habit.getReason());

        /**
         * 　a show repeat method if need to change, change the block or extract
         */
        showRepeat = (TextView) findViewById(R.id.showRepeat);
        ArrayList<Integer> frequency = new ArrayList<>(habit.getRepeatWeekOfDay());//get repeat date
        String daysInWeek = "";
        if(frequency.contains(1) == true){
            daysInWeek += " Monday ";
        }
        if(frequency.contains(2) == true){
            daysInWeek += " Tuesday ";
        }
        if(frequency.contains(3)){
            daysInWeek += " Wednesday ";
        }
        if(frequency.contains(4)){
            daysInWeek += " Thursday ";
        }
        if(frequency.contains(5)){
            daysInWeek += " Friday ";
        }
        if(frequency.contains(6)){
            daysInWeek += " Saturday ";
        }
        if(frequency.contains(7)){
            daysInWeek += " Sunday ";
        }
        showRepeat.setText(daysInWeek);
        // the block ends here
        
        /**
         * a tostring method to show date
         */
        theDate = (TextView) findViewById(R.id.showDate);// reason
        int year = habit.getStartDate().get(Calendar.YEAR);
        int month = habit.getStartDate().get(Calendar.MONTH)+1;
        int day = habit.getStartDate().get(Calendar.DAY_OF_MONTH);
        
        theDate.setText(Integer.toString(year)+"/"+ Integer.toString(month) +"/"+Integer.toString(day));
        
        
    }



        private boolean existedHabit (String id) {
            ElasticSearchHabitController.IsExist isExist = new ElasticSearchHabitController.IsExist();
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


    }