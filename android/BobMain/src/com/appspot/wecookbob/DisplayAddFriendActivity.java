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
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import com.appspot.wecookbob.lib.*;

public class DisplayAddFriendActivity extends Activity {
	// 리스트 뷰 선
	private ListView listview;
	// 데이터를 연결할 어댑
	InviteFriendDataAdapter adapter;
	// 데이터를 담을 자료구조. 씨데이터.
	ArrayList<InviteFriendCData> alist;

	SQLiteDatabase db;
    MySQLiteOpenHelper helper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_add_friend);
		
		if (checkDataBase()) showList();
		else getContact();
		
		// 선언한 리스트뷰에 사용할 리스트뷰를 연
		listview = (ListView) findViewById(R.id.sendFirstBobList);
		// 객체를 생성
		alist = new ArrayList<InviteFriendCData>();
		// 데이터 받기위한 데이터어댑터 객체 선언
		adapter = new InviteFriendDataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);
		// ArrayAdapter를 통해서 ArrayList에 자료 저장
		// 하나의 String값을 저장하던 것을 CData클래스의 객체를 저장하던것으로 변경
		// CData 객체는 생성자에 리스트 표시 텍스트뷰1의 내용과 텍스트뷰2의 내용 그리고 사진이미지값을 어댑터에 연결
	
		// CData클래스를 만들때의 순서대로 해당 인수값을 입력
		// 한줄 한줄이 리스트뷰에 뿌려질 한줄 한줄이라고 생각하면 됩니다.
		adapter.add(new InviteFriendCData(getApplicationContext(), "친구1"));
		adapter.add(new InviteFriendCData(getApplicationContext(), "친구2"));
		adapter.add(new InviteFriendCData(getApplicationContext(), "친구3"));
		
	}
	private class InviteFriendDataAdapter extends ArrayAdapter<InviteFriendCData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;
		
		public InviteFriendDataAdapter(Context context, ArrayList<InviteFriendCData> object) {
			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}
		
		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

			if (v == null) {

				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.invite_friend_list_item, null);
			} else {

				view = v;
			}

			// 자료를 받는다.
			final InviteFriendCData data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.invite_friend_name);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getUserName());
			}
			return view;
		}
	}
	
	// CData안에 받은 값을 직접 할당
	class InviteFriendCData {
		private String invite_user_name;
		
		public InviteFriendCData(Context context, String invite_user_name) {
			this.invite_user_name = invite_user_name;
		}

		public String getUserName() {
			return invite_user_name;
		}
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
    
    public void send_invite_message(View view){
    	Toast.makeText(DisplayAddFriendActivity.this, "초대 메세지를 보냅니다",
				  Toast.LENGTH_SHORT).show();
    }
}

