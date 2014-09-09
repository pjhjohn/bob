package com.example.bobmain;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.content.Intent;



import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.content.Intent;
import android.content.Intent;

import android.content.Intent;

public class BobMainActivity extends ActionBarActivity {
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bob_main);

        sw = (Switch) findViewById(R.id.alam_switch);
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean ischecked) {
                if (ischecked) {
                    Toast.makeText(getApplicationContext(), "알림을 받습니다",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "알림을 거부합니다",
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
                    Toast.makeText(getApplicationContext(), "배고파 디질듯",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "배가 불렀네 불렀어",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
 
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
    		  Toast.makeText(BobMainActivity.this, "식사를 시작해볼텐가?",
                      Toast.LENGTH_SHORT).show();
          }
    	  else{
                  // if a the new item is clicked show "Toast" message.
          }
   
          return super.onOptionsItemSelected(item);
    }
    
    
    public void showAddFriendTab(View view) {
    	Toast.makeText(BobMainActivity.this, "애드 프렌드",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, DisplayAddFriendActivity.class);
        startActivity(intent);

        // Do something in response to button
    }
}

