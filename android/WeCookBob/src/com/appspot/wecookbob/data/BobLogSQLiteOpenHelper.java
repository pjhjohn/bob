package com.appspot.wecookbob.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BobLogSQLiteOpenHelper extends SQLiteOpenHelper {

	 public BobLogSQLiteOpenHelper(Context context, String name,
	            CursorFactory factory, int version) {
	        super(context, name, factory, version);
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        String sql = "create table boblog (" +
	                "_id integer primary key autoincrement, " +
	                "bobtnerId text, " +
	                "bobtnerName text, " +
	                "bobtnerPhoneNumber text, " +
	                "notificationType string, " +
	                "bobRequestTime text);";
	        db.execSQL(sql);
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        String sql = "drop table if exists boblog";
	        db.execSQL(sql);
	        onCreate(db);
	    }
}