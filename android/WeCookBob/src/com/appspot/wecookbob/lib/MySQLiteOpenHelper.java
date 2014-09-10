package com.appspot.wecookbob.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	 public MySQLiteOpenHelper(Context context, String name,
	            CursorFactory factory, int version) {
	        super(context, name, factory, version);
	        // TODO Auto-generated constructor stub
	 
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        // TODO Auto-generated method stub 
	 
	  // SQL �������� ������ ���� ���·ε� ���� �� ���� �ִ�. 
	 
	        // SQLiteOpenHelper �� ���� ���� �Ǿ��� ��
	        String sql = "create table contact (" +
	                "_id integer primary key autoincrement, " +
	                "username text, " +
	                "mobile text, " +
	                "isFriend boolean);";
	        db.execSQL(sql);
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // db = ����� db, old/new �� ����/�Ź���
	        // TODO Auto-generated method stub
	        /*
	         * db ������ ���׷��̵� �Ǿ��� �� ����Ǵ� �޼ҵ�
	         * �� �κ��� ��뿡 �����ؾ� �ϴ� ���� ���� �ִ�. ������ 1�� ����ڰ� 2�� �ٲ��
	         * �ѹ��� ������ �ϸ� ������ ������ 3���� �Ǹ� 1�� ����ڰ� 2, 3�� ���ľ� �ϰ�
	         * 2�� ����ڴ� 3 ������ ��ġ�� �ȴ�. �̷��� ������ ���� ������ ���� �������Ƿ�
	         * ������ ����ؾ� �ϸ� �����ϸ� ���� ���� �ÿ� �Ϻ��� ���ϴ� ���� ���� ���� ���̴�.
	         * �׽�Ʈ������ ������ �����͸� ��� ����� �ٽ� ����� ���·� �ϰڴ�.
	         */
	        
	        String sql = "drop table if exists contact";
	        db.execSQL(sql);
	        
	        onCreate(db); // ���̺��� �������Ƿ� �ٽ� ���̺��� ������ִ� ����
	    }
}
