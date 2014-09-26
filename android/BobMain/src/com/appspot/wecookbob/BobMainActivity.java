package com.appspot.wecookbob;

import java.util.*;

import com.appspot.wecookbob.contact.*;
import com.appspot.wecookbob.contact.BobLog.NotificationType;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class BobMainActivity extends ActionBarActivity {
	//declare main listview components
	private ListView BobLogListView;
	private BobLogListviewAdapter BobLogAdapter;
	private ArrayList<BobLog> bobLogArray;
	
	
    Switch sw;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bob_main);
        long bobRequestTime = System.currentTimeMillis();
        
		// build listview for boblog
		BobLogListView = (ListView) findViewById(R.id.lv_bob_log);
		bobLogArray = new ArrayList<BobLog>();
		bobLogArray.add(new BobLog ("alex", "알렉스", NotificationType.RECEIVED, bobRequestTime-1000*60*5));
		bobLogArray.add(new BobLog ("nose", "노승은", NotificationType.RECEIVED, bobRequestTime-1000*60*1));
		bobLogArray.add(new BobLog ("hongJ", "홍지호호할아버지", NotificationType.RECEIVED, bobRequestTime-1000*60*5));
		bobLogArray.add(new BobLog ("namdy", "남디", NotificationType.RECEIVED, bobRequestTime-1000*60*1));
		bobLogArray.add(new BobLog ("parkJ", "박주노주노", NotificationType.RECEIVED, bobRequestTime-1000*60*5));
		BobLogAdapter = new BobLogListviewAdapter(this, bobLogArray, R.layout.bob_log_list_item, R.id.request_time, R.id.btn_bob);
		BobLogListView.setAdapter(BobLogAdapter);
		
        sw = (Switch) findViewById(R.id.alam_switch);
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean ischecked) {
                if (ischecked) {
                    Toast.makeText(getApplicationContext(), "알림",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "알림 ㄴㄴ",
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
                    Toast.makeText(getApplicationContext(), "배곺",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "배불",
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
    		  Toast.makeText(BobMainActivity.this, "�앹궗瑜��쒖옉�대낵�먭�?",
                      Toast.LENGTH_SHORT).show();
          }
    	  else{
                  // if a the new item is clicked show "Toast" message.
          }
   
          return super.onOptionsItemSelected(item);
    }
    
    
    public void showAddFriendTab(View view) {
    	Toast.makeText(BobMainActivity.this, "초대",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, DisplayAddFriendActivity.class);
        startActivity(intent);

        // Do something in response to button
    }
}

