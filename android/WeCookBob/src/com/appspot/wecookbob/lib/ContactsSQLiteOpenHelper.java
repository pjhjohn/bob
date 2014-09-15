package com.appspot.wecookbob.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsSQLiteOpenHelper extends SQLiteOpenHelper {

	 public ContactsSQLiteOpenHelper(Context context, String name,
	            CursorFactory factory, int version) {
	        super(context, name, factory, version);
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        String sql = "create table contacts (" +
	                "_id integer primary key autoincrement, " +
	                "userName text, " +
	                "phoneNumber text, " +
	                "isUser boolean, " +
	                "userId string);";
	        db.execSQL(sql);
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        String sql = "drop table if exists contacts";
	        db.execSQL(sql);
	        onCreate(db);
	    }
}
