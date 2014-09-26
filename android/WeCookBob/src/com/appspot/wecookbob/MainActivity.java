package com.appspot.wecookbob;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.appspot.wecookbob.lib.PreferenceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class MainActivity extends ActionBarActivity {
    Switch sw;
	SQLiteDatabase bobLogDb;
	BobLogSQLiteOpenHelper bobLogHelper;
	String[] data = {"이동우", "김대홍", "선재원 연세대 의학", "조경욱"};
	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "71421696637";
  
    private GoogleCloudMessaging _gcm;
    private String _regId;
    
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
		
      // google play service가 사용가능한가
      if (checkPlayServices())
      {
         _gcm = GoogleCloudMessaging.getInstance(this);
         _regId = getRegistrationId();
         System.out.println(_regId);
         if (TextUtils.isEmpty(_regId))
            registerInBackground();
      }
      else
      {
         Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");
      }
 
      // display received msg
      String msg = getIntent().getStringExtra("msg");
      
      TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
      String deviceid = tm.getDeviceId();
      
      System.out.println(deviceid);
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
    
    
    @Override
    protected void onNewIntent(Intent intent)
    {
       super.onNewIntent(intent);
  
       // display received msg
       String msg = intent.getStringExtra("msg");
       Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");
    }
  
    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
       int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
       if (resultCode != ConnectionResult.SUCCESS)
       {
          if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
          {
             GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
          }
          else
          {
             Log.i("MainActivity.java | checkPlayService", "|This device is not supported.|");
             finish();
          }
          return false;
       }
       return true;
    }
  
    // registration  id를 가져온다.
    private String getRegistrationId()
    {
       String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
       if (TextUtils.isEmpty(registrationId))
       {
          Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");
          return "";
       }
       int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
       int currentVersion = getAppVersion();
       if (registeredVersion != currentVersion)
       {
          Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");
          return "";
       }
       return registrationId;
    }
  
    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
       try
       {
          PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
          return packageInfo.versionCode;
       }
       catch (NameNotFoundException e)
       {
          // should never happen
          throw new RuntimeException("Could not get package name: " + e);
       }
    }
  
    // gcm 서버에 접속해서 registration id를 발급받는다.
    private void registerInBackground()
    {
       new AsyncTask<Void, Void, String>()
       {
          @Override
          protected String doInBackground(Void... params)
          {
             String msg = "";
             try
             {
                if (_gcm == null)
                {
                   _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                _regId = _gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + _regId;
  
                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.
  
                // Persist the regID - no need to register again.
                storeRegistrationId(_regId);
             }
             catch (IOException ex)
             {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
             }
  
             return msg;
          }
  
          @Override
          protected void onPostExecute(String msg)
          {
             Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");
          }
       }.execute(null, null, null);
    }
  
    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
       int appVersion = getAppVersion();
       Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
       PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
       PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }
}