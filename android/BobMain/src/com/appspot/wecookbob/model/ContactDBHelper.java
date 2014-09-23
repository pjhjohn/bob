package com.appspot.wecookbob.model;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract;
import android.widget.Toast;


public class ContactDBHelper {
	private Context context;
	private SQLiteDatabase db;
	private MySQLiteOpenHelper helper;

	public ContactDBHelper(Context context, String name, CursorFactory factory, int version) {
		this.helper = new MySQLiteOpenHelper(context, name, factory, version);
		this.context = context;
	}
	
	public static boolean isDatabaseAvailiable(Context context, String dbName) {
		String dbPath = "//data/data/" + context.getPackageName()+"/databases/" + dbName;
		try {
			SQLiteDatabase dbForCheck = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
			dbForCheck.close();
			return true;
		} catch(SQLiteException e) {
			return false;
		}
		
	}
	
	public void insert(String userName, String mobileNumber) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", userName);
		values.put("mobile", mobileNumber);
		db.insert("contact", null, values);
	}
	
	public void update(String userName, String mobileNumber) {
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mobile", mobileNumber);
		db.update("contact", values, "username=?", new String[]{userName});
	}
	
	public void delete(String userName) {
		db = helper.getWritableDatabase();
		db.delete("contact", "username=?", new String[]{userName});
	}
	public void select() {
		db = helper.getReadableDatabase();
		Cursor cursor = db.query("contact", null, null, null, null, null, null);
		while(cursor.moveToNext()) {
//			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
//			String userName = cursor.getString(cursor.getColumnIndex("username"));
//			String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
		}
	}
	public void getContact() {
		ContentResolver resolver = this.context.getContentResolver();
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		ArrayList<String> result = new ArrayList<String>();
		
		while(cursor.moveToNext()) {
			result.add(cursor.getString(nameIndex) + " :");
			String id = cursor.getString(idIndex);
			Cursor innerCursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[]{id},
				null
			);
			int typeIndex = innerCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
			int numberIndex = innerCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			while(innerCursor.moveToNext()) {
				String num = innerCursor.getString(numberIndex);
				switch(innerCursor.getInt(typeIndex)) {
				case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
	    			insert(cursor.getString(nameIndex), num);
	    			break;
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
	    			insert(cursor.getString(nameIndex), num);
	    			break;
	    		case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
	    			insert(cursor.getString(nameIndex), num);
	    			break;
				}
			} innerCursor.close();
		} cursor.close();
		Toast.makeText(this.context, "¾îÂ¼±¸ÀúÂ¼±¸", Toast.LENGTH_SHORT).show();
		this.select();
	}
	
	public ArrayList<String> getUserNames() {
		this.db = helper.getReadableDatabase();
		Cursor cursor = db.query("contact", null, null, null, null, null, null);
		ArrayList<String> userNameArray = new ArrayList<String>();
		while(cursor.moveToNext()) userNameArray.add(cursor.getString(cursor.getColumnIndex("username")));
		return userNameArray;
	}
}
