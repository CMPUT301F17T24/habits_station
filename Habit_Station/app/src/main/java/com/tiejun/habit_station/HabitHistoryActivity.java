/*
 * Copyright (c) 2017 Team 24, CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify this code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Activity to show habit history
 *
 * @author xuanyi
 * @version 1.5
 * @see HabitEvent
 * @see HabitEventList
 * @since 1.0
 *
 */

public class HabitHistoryActivity extends AppCompatActivity {

    private ListView history;
    protected ArrayAdapter<HabitEvent> adapter;
    protected ImageView search;
    private EditText searchKey;

    private  ArrayList<HabitEvent> fillist  = new ArrayList<HabitEvent>();
    private CheckBox comment,type;

    private String query;
    private int  click_item_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        history = (ListView)findViewById(R.id.history);
        type = (CheckBox)findViewById(R.id.type);
        comment = (CheckBox)findViewById(R.id.comment);
        search = (ImageView)findViewById(R.id.search);
        searchKey = (EditText)findViewById(R.id.keyword);

        type.setChecked(false);
        comment.setChecked(false);


        String MYhistory = "{\n" +
                "  \"query\": { \n" +
                " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                " 	}\n" +
                "}";

        ElasticSearchEventController.GetEvents getHistory
                = new  ElasticSearchEventController.GetEvents();
        getHistory.execute(MYhistory);

       try {
            fillist.addAll(getHistory.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchKey.getText().toString();

                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String userName = pref.getString("currentUser", "");

                if (type.isChecked() && comment.isChecked()){
                    Toast.makeText(getApplicationContext(), "Cannot select both", Toast.LENGTH_SHORT).show();
                    query = "{\n" +
                            "  \"query\": { \n" +
                            " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                            " 	}\n" +
                            "}";
                }
                else if(!type.isChecked() && !comment.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please select one", Toast.LENGTH_SHORT).show();
                    query = "{\n" +
                            "  \"query\": { \n" +
                            " \"term\" : { \"uName\" : \"" + userName + "\" }\n" +
                            " 	}\n" +
                            "}";
                }
                else if (type.isChecked()){
                    Log.d("check", "type");

                    query = "{\n" +
                            "  \"query\": { \n" +
                            "\"bool\": {\n"+
                            "\"must\": [\n"+
                            "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                            "{"+ " \"match\" : {  \"eName\" : \"" + keyword +  "\" }}\n" +
                            "]"+
                            "}"+
                            "}"+
                            "}";


                }
                else if (comment.isChecked()){
                    Log.d("check", "comment");
                    query = "{\n" +
                            "  \"query\": { \n" +
                            "\"bool\": {\n"+
                            "\"must\": [\n"+
                            "{"+ " \"term\" : { \"uName\" : \"" + userName +  "\" }},\n" +
                            "{"+ "\"wildcard\" : { \"eComment\" : \"" +"*"+ keyword + "*" +"\"  }}\n" +
                            "]"+
                            "}"+
                            "}"+
                            "}";
                }


                ElasticSearchEventController.GetEvents getHistory
                        = new  ElasticSearchEventController.GetEvents();
                getHistory.execute(query);
                try {

                    fillist.clear();
                    fillist.addAll(getHistory.get());
                    onStart();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });

        final Button map = (Button) findViewById(R.id.historyMap);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setResult(RESULT_OK);
                Intent intent = new Intent(HabitHistoryActivity.this,MyEventMapActivity.class);
                startActivity(intent);
            }
        });


        registerForContextMenu(history);
        history.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                click_item_index=position;
                HabitEvent event = fillist.get(click_item_index);
                String event_id = event.getuName() + event.geteName()
                        + event.geteTime().get(Calendar.YEAR)
                        + String.valueOf(event.geteTime().get(Calendar.MONTH) + 1)
                        + event.geteTime().get(Calendar.DAY_OF_MONTH);
                Intent i = new Intent(HabitHistoryActivity.this, ViewHistoryDetailActivity.class);
                i.putExtra("id", event_id);
                startActivity(i);

            }
        });



    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        HabitEventList results =new  HabitEventList(fillist);
        fillist = results.sortEvents();
        adapter = new ArrayAdapter<HabitEvent>(this, R.layout.list_habits, fillist);
        history.setAdapter(adapter);

    }


}