package com.appspot.wecookbob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    Switch sw;
	String[] data = {"A", "B", "C", "D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bob_main);

        sw = (Switch) findViewById(R.id.notification_switch);
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean ischecked) {
                if (ischecked) {
                    Toast.makeText(getApplicationContext(), "on",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "off",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        
        
        sw = (Switch) findViewById(R.id.status_switch);
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean ischecked) {
                if (ischecked) {
                    Toast.makeText(getApplicationContext(), "on",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "off",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        
        ListView list = (ListView) findViewById(R.id.FriendsAlreadyAddedlistView);
		ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
		list.setAdapter(adapter);
 
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_action, menu);
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
    	  if(item.getItemId() == R.id.action_settings){
              // ( 1 ) add a new item 
                  // ( 2 ) change add to remove
    		  Toast.makeText(MainActivity.this, "식사를 시작해볼텐가?",
                      Toast.LENGTH_SHORT).show();
          }
    	  else{
                  // if a the new item is clicked show "Toast" message.
          }
   
          return super.onOptionsItemSelected(item);
    }
    
    
    public void showAddFriendTab(View view) {
    	Toast.makeText(MainActivity.this, "친구 페이지",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, DisplayAddFriendActivity.class);
        startActivity(intent);

        // Do something in response to button
    }
}

