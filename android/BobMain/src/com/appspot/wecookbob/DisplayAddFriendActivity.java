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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.wecookbob.contact.ContactUser;
import com.appspot.wecookbob.contact.ContactUserListviewAdapter;
import com.appspot.wecookbob.model.MySQLiteOpenHelper;

public class DisplayAddFriendActivity extends Activity {
	private ListView inviteListView, firstBobListView;
	private ContactUserListviewAdapter inviteAdapter, firstBobAdapter;
	private ArrayList<ContactUser> inviteArray, firstBobArray;
	SQLiteDatabase db;
	MySQLiteOpenHelper helper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_display_add_friend);

		// build listview for firstbob
		firstBobListView = (ListView) findViewById(R.id.lv_first_bob);
		firstBobArray = new ArrayList<ContactUser>();
		firstBobArray.add(new ContactUser("BNAME1", 1));
		firstBobArray.add(new ContactUser("BNAME2", 2));
		firstBobArray.add(new ContactUser("BNAME3", 3));
		firstBobArray.add(new ContactUser("BNAME4", 4));
		firstBobArray.add(new ContactUser("BNAME5", 5));
		firstBobAdapter = new ContactUserListviewAdapter(this, firstBobArray, R.layout.send_first_bob_list_item, R.id.send_first_bob_friend_name);
		firstBobListView.setAdapter(firstBobAdapter);

		
		// build listview for invitation
		inviteListView = (ListView) findViewById(R.id.lv_invite);
		inviteArray = new ArrayList<ContactUser>();
		inviteArray.add(new ContactUser("INAME1", 1));
		inviteArray.add(new ContactUser("INAME2", 2));
		inviteArray.add(new ContactUser("INAME3", 3));
		inviteArray.add(new ContactUser("INAME4", 4));
		inviteArray.add(new ContactUser("INAME5", 5));
		inviteAdapter = new ContactUserListviewAdapter(this, inviteArray, R.layout.invite_friend_list_item, R.id.invite_friend_name);
		inviteListView.setAdapter(inviteAdapter);		
	}
	
	private boolean checkDatabase() {
		SQLiteDatabase dbForCheck = null;
		try {
			dbForCheck = SQLiteDatabase.openDatabase("//data/data/com.appspot.wecookbob/databases/contact.db", null, SQLiteDatabase.OPEN_READONLY);
			dbForCheck.close();
		} catch (SQLiteException e) {
		}
		return dbForCheck != null;
	}
	private void showList() {
		helper = new MySQLiteOpenHelper(DisplayAddFriendActivity.this, "contact.db", null, 1);
		db = helper.getReadableDatabase();
		Cursor cursor = db.query("contact", null, null, null, null, null, null);
		ArrayList<String> userNameArray = new ArrayList<String>();
		while (cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String userName = cursor.getString(cursor.getColumnIndex("username"));
			String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
			userNameArray.add(userName);
			Log.i("db", "id: " + _id + ", username : " + userName + ", mobile : " + mobile);
		}
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNameArray);
	    inviteListView.setAdapter(adapter);
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

