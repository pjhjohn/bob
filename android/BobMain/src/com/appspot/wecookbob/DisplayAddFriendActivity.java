package com.appspot.wecookbob;

import java.util.ArrayList;

import trash.InviteFriendAdapter;
import trash.SendFirstBobAdapter;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.wecookbob.custom.ContactUserData;
import com.appspot.wecookbob.custom.ContactUserListviewAdapter;
import com.appspot.wecookbob.lib.MySQLiteOpenHelper;

public class DisplayAddFriendActivity extends Activity {
	private ListView lvInvite, lvFirstBob;
	
	ContactUserListviewAdapter inviteAdapter, sendFirstBobAdapter;
	ArrayList<ContactUserData> inviteArray, sendFirstBobArray;
	SQLiteDatabase db;
    MySQLiteOpenHelper helper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_add_friend);
		if (checkDataBase()) showList();
		else getContact();
		
		// build listview for invitation
		lvInvite = (ListView) findViewById(R.id.FriendsToInvitelistView);
		inviteArray = new ArrayList<ContactUserData>();
		inviteArray.add(new ContactUserData("INAME1", false, 1));
		inviteArray.add(new ContactUserData("INAME2", false, 2));
		inviteArray.add(new ContactUserData("INAME3", false, 3));
		inviteArray.add(new ContactUserData("INAME4", false, 4));
		inviteArray.add(new ContactUserData("INAME5", false, 5));
		inviteAdapter = new ContactUserListviewAdapter(this, inviteArray, R.layout.invite_friend_list_item);
		lvInvite.setAdapter(inviteAdapter);
		
		lvFirstBob = (ListView) findViewById(R.id.FriendsToInvitelistView);
		sendFirstBobArray = new ArrayList<ContactUserData>();
		sendFirstBobArray.add(new ContactUserData("BNAME1", true, 1));
		sendFirstBobArray.add(new ContactUserData("BNAME2", true, 2));
		sendFirstBobArray.add(new ContactUserData("BNAME3", true, 3));
		sendFirstBobArray.add(new ContactUserData("BNAME4", true, 4));
		sendFirstBobArray.add(new ContactUserData("BNAME5", true, 5));
		sendFirstBobAdapter = new ContactUserListviewAdapter(this, sendFirstBobArray, R.layout.send_first_bob_list_item);
		lvFirstBob.setAdapter(sendFirstBobAdapter);
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
			  Toast.makeText(DisplayAddFriendActivity.this, "�숆린�붾� �쒖옉�⑸땲��",
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
	    Toast.makeText(DisplayAddFriendActivity.this, "�숆린�붽� �꾨즺�섏뿀�듬땲��",
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
    
    public void send_invite_message(View view){
    	Toast.makeText(DisplayAddFriendActivity.this, "珥덈� 硫붿꽭吏�� 蹂대깄�덈떎",
				  Toast.LENGTH_SHORT).show();
    }
    
    public void send_first_bob(View view){
    	Toast.makeText(DisplayAddFriendActivity.this, "泥ル갈 �붿껌��蹂대깄�덈떎",
				  Toast.LENGTH_SHORT).show();
    }
    
}

