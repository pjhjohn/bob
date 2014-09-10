package com.appspot.wecookbob;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.wecookbob.lib.MySQLiteOpenHelper;

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
        Cursor c = db.query("contact", null, null, null, null, null, "username asc");
        
        ArrayList<String> arrayList = new ArrayList<String>();
        
        while (c.moveToNext()) {
            String username = c.getString(c.getColumnIndex("username"));
            arrayList.add(username);
        }
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
	    
	    ListView list;
	    list = (ListView)findViewById(R.id.FriendsToInvitelistView);
	    list.setAdapter(adapter);
	    list.setChoiceMode(list.CHOICE_MODE_SINGLE);
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list = (ListView) parent;
                // TODO 아이템 클릭시에 구현할 내용은 여기에.
                String[] username = { (String)list.getItemAtPosition(position) };
                helper = new MySQLiteOpenHelper(DisplayAddFriendActivity.this,
                        "contact.db",
                        null,
                        1);
        	    db = helper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM contact WHERE username = ?", username);
                c.moveToFirst();
                String mobile = c.getString(c.getColumnIndex("mobile"));
                Toast.makeText(DisplayAddFriendActivity.this, mobile, Toast.LENGTH_LONG).show();
	    	}
	    });
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
		if (checkDataBase()) deleteDatabase("contact.db");
		
        helper = new MySQLiteOpenHelper(DisplayAddFriendActivity.this,
            "contact.db",
            null,
            1);
        
        ContentResolver cr = getContentResolver();
	    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
	    
	    int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
	    int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
	     
	    while (cursor.moveToNext()) {
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
	    			insert(cursor.getString(nameidx), num, false);
	    			break;
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
	    			insert(cursor.getString(nameidx), num, false);
	    			break;
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
	    			insert(cursor.getString(nameidx), num, false);
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
	
    public void insert(String username, String mobile, boolean isFriend) {
        db = helper.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("mobile", mobile);
        values.put("isFriend", isFriend);
        db.insert("contact", null, values);
    }
    
    public void update (String username, String mobile, boolean isFriend) {
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
    
    public void addFriend(String username) {
    	
    }
}

