package com.appspot.wecookbob;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.appspot.wecookbob.lib.BobLogSQLiteOpenHelper;


public class MainActivity extends ActionBarActivity {
    Switch sw;
	SQLiteDatabase bobLogDb;
	BobLogSQLiteOpenHelper bobLogHelper;
	String[] data = {"이동우", "김대홍", "선재원 연세대 의학", "조경욱"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bob_main);

        sw = (Switch) findViewById(R.id.alarm_switch);
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
//      if (checkDataBase()) showList();
        ListView list = (ListView) findViewById(R.id.mainFriendsListView);
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
    	Toast.makeText(MainActivity.this, "애드 프렌드",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);

        // Do something in response to button
    }
    

    private boolean checkDataBase() {
		SQLiteDatabase checkBobLogDb = null;
		try {
			checkBobLogDb = SQLiteDatabase.openDatabase("//data/data/com.appspot.wecookbob/databases/boblog.db", null,
					SQLiteDatabase.OPEN_READONLY);
			checkBobLogDb.close();
		} catch (SQLiteException e) {
			// database doesn't exist yet.
		}
		return checkBobLogDb != null ? true : false;
	}

    
    public void showList() {
		bobLogHelper = new BobLogSQLiteOpenHelper(MainActivity.this,
				"boblog.db",
				null,
				1);
		bobLogDb = bobLogHelper.getReadableDatabase();
		Cursor contactsCursor = bobLogDb.query("boblog", null, null, null, null, null, null);

		// put first bob list here

		ArrayList<String> arrayList = new ArrayList<String>();

		while (contactsCursor.moveToNext()) {
			String userName = contactsCursor.getString(contactsCursor.getColumnIndex("userName"));
			arrayList.add(userName);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

		ListView list;
		list = (ListView)findViewById(R.id.mainFriendsListView);
		list.setAdapter(adapter);
		list.setChoiceMode(list.CHOICE_MODE_SINGLE);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				// TODO 아이템 클릭시에 구현할 내용은 여기에.
				String[] userName = { (String)list.getItemAtPosition(position) };
				bobLogHelper = new BobLogSQLiteOpenHelper(MainActivity.this,
						"contacts.db",
						null,
						1);
				bobLogDb = bobLogHelper.getReadableDatabase();
				Cursor c = bobLogDb.rawQuery("SELECT * FROM contacts WHERE userName = ?", userName);
				c.moveToFirst();
				String bobtnerPhoneNumber = c.getString(c.getColumnIndex("bobtnerPhoneNumber"));
				Toast.makeText(MainActivity.this, bobtnerPhoneNumber, Toast.LENGTH_LONG).show();
			}
		});
	}
}

