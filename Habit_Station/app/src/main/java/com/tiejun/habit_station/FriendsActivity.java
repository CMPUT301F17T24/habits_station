/*
 * Copyright (c) 2017 Team 24,CMPUT301, University of Alberta - All Rights Reserved.
 * You mayuse,distribute, or modify thid code under terms and condition of the Code of Student Behavior at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact xuanyi@ualberta.ca.
 *
 */

package com.tiejun.habit_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private ArrayList<String> uNames  = new ArrayList<String>();
    protected ArrayAdapter<String> adapter;
    private ListView nameList;
    private User user;
    private int click_item_index;
    private int inPending = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        nameList = (ListView)findViewById(R.id.names);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");

        ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
        getUserTask.execute(userName);
        try{
            user = getUserTask.get();
        }
        catch (Exception e) {
            Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
        }

        uNames = user.getFollowee();           // by default show the friends

        Button friendButton = (Button) findViewById(R.id.friends);
        friendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                uNames = user.getFollowee();           // get the friends (allowed to follow)
                inPending = 0;
                onStart();
            }
        });

        Button pendingButton = (Button) findViewById(R.id.pending);
        pendingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                uNames = user.getPendingPermission();
                inPending = 1;
                onStart();
            }
        });


        registerForContextMenu(nameList);
        nameList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                click_item_index=position;
            }
        });
        
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.setHeaderTitle("Context Menu");
        if (inPending == 1){
            menu.add(0, v.getId(), 0, "Accept");
            menu.add(0, v.getId(), 0, "Ignore");
        }

    }



    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String userName = pref.getString("currentUser", "");
        ArrayList<String> pendings  = user.getPendingPermission();
        ArrayList<String> followers  = user.getFollower();


        if (item.getTitle().equals("Accept")) {
            String pendingUser = pendings.get(position);
            // remove from pending list
            // update current user's follower list
            followers.add(pendingUser);
            pendings.remove(position);
            ElasticSearchUserController.AddUserTask addUserTask
                    = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            // update the accepted user's followee list
            User pending_user = new User();
            ElasticSearchUserController.GetUserTask getUserTask = new ElasticSearchUserController.GetUserTask();
            getUserTask.execute(pendingUser);
            try{
               pending_user = getUserTask.get();
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            ArrayList<String> pendingFollowees = pending_user.getFollowee();
            pendingFollowees.add(userName);
            ElasticSearchUserController.AddUserTask addUserTask2
                    = new ElasticSearchUserController.AddUserTask();
            addUserTask2.execute(pending_user);
            Toast.makeText(this, "You accepted this request!", Toast.LENGTH_SHORT).show();
            onStart();
        }
        else if (item.getTitle().equals("Ignore")){
            //remove from pending list
            pendings.remove(position);
            ElasticSearchUserController.AddUserTask addUserTask
                    = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            Toast.makeText(this, "You ignored this user! ", Toast.LENGTH_SHORT).show();
            onStart();
        }
        else {
            return false;
        }
        return true;
    }




    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


        adapter = new ArrayAdapter<String>(this, R.layout.list_habits, uNames);
        nameList.setAdapter(adapter);


    }
}
