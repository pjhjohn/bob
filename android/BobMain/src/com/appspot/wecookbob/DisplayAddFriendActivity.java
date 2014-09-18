package com.appspot.wecookbob;

import java.util.*;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.appspot.wecookbob.lib.*;

public class DisplayAddFriendActivity extends Activity {
	
	SQLiteDatabase db;
    MySQLiteOpenHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_add_friend);
		if (checkDataBase()) showList();
		else getContact();
	}
	
	private boolean checkDataBase() {
	    SQLiteDatabase checkDB = null;
	    try {
	        checkDB = SQLiteDatabase.openDatabase("//data/data/com.appspot.wecookbob/databases/contact.db", null,
	                SQLiteDatabase.OPEN_READONLY);
	        checkDB.close();
	    } catch (SQLiteException e) {
	        // database doesn't exist yet.
	    }
	    return checkDB != null ? true : false;
	}
	
    public void showList() {
    	helper = new MySQLiteOpenHelper(DisplayAddFriendActivity.this,
                "contact.db",
                null,
                1);
	    db = helper.getReadableDatabase();
        Cursor c = db.query("contact", null, null, null, null, null, null);
        
        ArrayList<String> arrayList = new ArrayList<String>();
        
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            String username = c.getString(c.getColumnIndex("username"));
            String mobile = c.getString(c.getColumnIndex("mobile"));
            arrayList.add(username);
            Log.i("db", "id: " + _id + ", username : " + username + ", mobile : " + mobile);
        }
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
	    
	    ListView list;
	    list = (ListView)findViewById(R.id.FriendsToInvitelistView);
	    list.setAdapter(adapter);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_add_friend, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			  Toast.makeText(DisplayAddFriendActivity.this, "동기화를 시작합니다",
					  Toast.LENGTH_SHORT).show();
			  getContact();
			  return true;
		}
		else{
                  // if a the new item is clicked show "Toast" message.
		}

		return super.onOptionsItemSelected(item);
	}
    
	public void getContact() {
        helper = new MySQLiteOpenHelper(DisplayAddFriendActivity.this,
            "contact.db",
            null,
            1);
        
        ContentResolver cr = getContentResolver();
	    Cursor cursor = cr. query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
	    
	    int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
	    int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
	    
	    ArrayList<String> result = new ArrayList<String>();
	    
	    while (cursor.moveToNext()) {
	    	result.add(cursor.getString(nameidx) + " :");
	    	
	    	String id = cursor.getString(ididx);
	    	Cursor cursor2 = cr.query(
	    			ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	    			null,
	    			ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
	    			new String[]{id},
	    			null);
	    	
	    	int typeidx = cursor2.getColumnIndex(
	    			ContactsContract.CommonDataKinds.Phone.TYPE);
	    	int numidx = cursor2.getColumnIndex(
	    			ContactsContract.CommonDataKinds.Phone.NUMBER);
	    	
	    	while (cursor2.moveToNext()) {
	    		String num = cursor2.getString(numidx);
	    		switch (cursor2.getInt(typeidx)) {
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
	    			insert(cursor.getString(nameidx), num);
	    			break;
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
	    			insert(cursor.getString(nameidx), num);
	    			break;
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
	    			insert(cursor.getString(nameidx), num);
	    			break;
	    		}
	    	}
	    	cursor2.close();
	    }
	    cursor.close();
	    Toast.makeText(DisplayAddFriendActivity.this, "동기화가 완료되었습니다",
				  Toast.LENGTH_SHORT).show();
        select();
	}
	
    public void insert(String username, String mobile) {
        db = helper.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("mobile", mobile);
        db.insert("contact", null, values);
    }
    
    public void update (String username, String mobile) {
        db = helper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("mobile", mobile);
        db.update("contact", values, "username=?", new String[]{username});
    }
    
    public void delete (String username) {
        db = helper.getWritableDatabase();
        db.delete("contact", "username=?", new String[]{username});
    }

    public void select() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("contact", null, null, null, null, null, null);
 
        while (c.moveToNext()) {
//            int _id = c.getInt(c.getColumnIndex("_id"));
//            String username = c.getString(c.getColumnIndex("username"));
//            String mobile = c.getString(c.getColumnIndex("mobile"));
        }
    }
}

