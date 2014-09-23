package com.appspot.wecookbob.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Triggered by the first run of SQLiteOpenHelper
		String query = 
			"create table contact (" +
			"_id integer primary key autoincrement, " +
			"username text, " +
			"mobile text);";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db = ������ db, old/new �� ����/�Ź���
		/*
		 * db ������ ���׷��̵� �Ǿ��� �� ����Ǵ� �޼ҵ�
		 * �� �κ��� ��뿡 �����ؾ� �ϴ� ���� ���� �ִ�. ������ 1�� ����ڰ� 2�� �ٲ��
		 * �ѹ��� ������ �ϸ� ������ ������ 3���� �Ǹ� 1�� ����ڰ� 2, 3�� ���ľ� �ϰ�
		 * 2�� ����ڴ� 3 ������ ��ġ�� �ȴ�. �̷��� ������ ���� ������ ���� �������Ƿ�
		 * ������ ����ؾ� �ϸ� �����ϸ� ���� ���� �ÿ� �Ϻ��� ���ϴ� ���� ���� ���� ���̴�.
		 * �׽�Ʈ������ ������ �����͸� ��� ����� �ٽ� ����� ���·� �ϰڴ�.
		 */

		String query = "drop table if exists student";
		db.execSQL(query);
		this.onCreate(db); // ���̺��� �������Ƿ� �ٽ� ���̺��� ������ִ� ����
	}
}
