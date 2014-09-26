package com.appspot.wecookbob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.wecookbob.lib.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.lib.Contact;
import com.appspot.wecookbob.lib.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.lib.InviteFriendCData;
import com.appspot.wecookbob.lib.InviteFriendDataAdapter;
import com.appspot.wecookbob.lib.SendFirstBobAdapter;
import com.appspot.wecookbob.lib.SendFirstBobCData;
import com.appspot.wecookbob.request.OnResponse;
import com.appspot.wecookbob.request.RequestForm;

public class ContactsActivity extends Activity implements OnResponse {
	public ArrayList<Contact> contactlist;
	SQLiteDatabase contactsDb;
	SQLiteDatabase bobLogDb;
	ContactsSQLiteOpenHelper contactsHelper;
	BobLogSQLiteOpenHelper bobLogHelper;
	// 리스트 뷰 선언
	private ListView inviteFriendlistview, sendFirstBoblistview;
	// 데이터를 연결할 어댑터
	InviteFriendDataAdapter invite_adapter;
	SendFirstBobAdapter send_first_bob_adapter;

	// 데이터를 담을 자료구조. 씨데이터.
	ArrayList<InviteFriendCData> inviteFriendlist;
	ArrayList<SendFirstBobCData> sendFirstBoblist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		if (checkDataBase("contacts.db")) showList();
		else
			try {
				getContact();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private boolean checkDataBase(String dbName) {
		SQLiteDatabase checkcontactsDb = null;
		try {
			checkcontactsDb = SQLiteDatabase.openDatabase("//data/data/com.appspot.wecookbob/databases/" + dbName, null,
					SQLiteDatabase.OPEN_READONLY);
			checkcontactsDb.close();
		} catch (SQLiteException e) {
			// database doesn't exist yet.
		}
		return checkcontactsDb != null ? true : false;
	}

	public void showList() {
		System.out.println("showlist!");
		contactsHelper = new ContactsSQLiteOpenHelper(ContactsActivity.this,
				"contacts.db",
				null,
				1);
		contactsDb = contactsHelper.getReadableDatabase();
		Cursor contactsCursor = contactsDb.rawQuery("SELECT * FROM contacts WHERE userId IS NULL", null);
		Cursor firstBobCursor = contactsDb.rawQuery("SELECT * FROM contacts WHERE userId IS NOT NULL", null);	
		ArrayList<String> contactsList = new ArrayList<String>();
		ArrayList<String> firstBobList = new ArrayList<String>();

		// 선언한 리스트뷰에 사용할 리스트뷰를 연결
		inviteFriendlistview = (ListView) findViewById(R.id.FriendsToInvitelistView);
		sendFirstBoblistview = (ListView) findViewById(R.id.sendFirstBobList);

		// 객체를 생성
		inviteFriendlist = new ArrayList<InviteFriendCData>();
		sendFirstBoblist = new ArrayList<SendFirstBobCData>(); 

		// 데이터 받기위한 데이터어댑터 객체 선언
		invite_adapter = new InviteFriendDataAdapter(this, inviteFriendlist);
		send_first_bob_adapter = new SendFirstBobAdapter(this, sendFirstBoblist);

		// 리스트뷰에 어댑터 연결
		inviteFriendlistview.setAdapter(invite_adapter);
		sendFirstBoblistview.setAdapter(send_first_bob_adapter);

		// ArrayAdapter를 통해서 ArrayList에 자료 저장
		// 하나의 String값을 저장하던 것을 CData클래스의 객체를 저장하던것으로 변경
		// CData 객체는 생성자에 리스트 표시 텍스트뷰1의 내용과 텍스트뷰2의 내용 그리고 사진이미지값을 어댑터에 연결

		// CData클래스를 만들때의 순서대로 해당 인수값을 입력
		// 한줄 한줄이 리스트뷰에 뿌려질 한줄 한줄이라고 생각하면 됩니다.

		while (contactsCursor.moveToNext()) {
			String userName = contactsCursor.getString(contactsCursor.getColumnIndex("userName"));
			contactsList.add(userName);
			invite_adapter.add(new InviteFriendCData(getApplicationContext(), userName));
		}
		while (firstBobCursor.moveToNext()) {
			String userName = firstBobCursor.getString(firstBobCursor.getColumnIndex("userName"));
			firstBobList.add(userName);
			send_first_bob_adapter.add(new SendFirstBobCData(getApplicationContext(), userName));
		}

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, firstBobList);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);

		ListView List1;
		ListView List2;
//		List1 = (ListView)findViewById(R.id.firstBobListView);
//		List2 = (ListView)findViewById(R.id.friendsToInviteListView);
//		List1.setAdapter(adapter1);
//		List2.setAdapter(adapter2);
//		List1.setChoiceMode(List1.CHOICE_MODE_SINGLE);
//		List2.setChoiceMode(List2.CHOICE_MODE_SINGLE);
//		List1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				ListView list = (ListView) parent;
//				// TODO 아이템 클릭시에 구현할 내용은 여기에.
//				String[] userName = { (String)list.getItemAtPosition(position) };
//				contactsHelper = new ContactsSQLiteOpenHelper(ContactsActivity.this,
//						"contacts.db",
//						null,
//						1);
//				contactsDb = contactsHelper.getReadableDatabase();
//				Cursor c = contactsDb.rawQuery("SELECT * FROM contacts WHERE userName = ?", userName);
//				c.moveToFirst();
//				String phoneNumber = c.getString(c.getColumnIndex("phoneNumber"));
//				Toast.makeText(ContactsActivity.this, phoneNumber, Toast.LENGTH_LONG).show();
//			}
//		});
//		List2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				ListView list = (ListView) parent;
//				// TODO 아이템 클릭시에 구현할 내용은 여기에.
//				String[] userName = { (String)list.getItemAtPosition(position) };
//				contactsHelper = new ContactsSQLiteOpenHelper(ContactsActivity.this,
//						"contacts.db",
//						null,
//						1);
//				contactsDb = contactsHelper.getReadableDatabase();
//				Cursor c = contactsDb.rawQuery("SELECT * FROM contacts WHERE userName = ?", userName);
//				c.moveToFirst();
//				String phoneNumber = c.getString(c.getColumnIndex("phoneNumber"));
//				Toast.makeText(ContactsActivity.this, phoneNumber, Toast.LENGTH_LONG).show();
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(ContactsActivity.this, "동기화를 시작합니다",
					Toast.LENGTH_SHORT).show();
			try {
				getContact();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		else{
			// if a the new item is clicked show "Toast" message.
		}
		return super.onOptionsItemSelected(item);
	}

	public void getContact() throws JSONException {
		if (checkDataBase("contacts.db")) deleteDatabase("contacts.db");
		bobLogHelper = new BobLogSQLiteOpenHelper(ContactsActivity.this,
				"boblog.db",
				null,
				1);
		contactsHelper = new ContactsSQLiteOpenHelper(ContactsActivity.this,
				"contacts.db",
				null,
				1);
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
				ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름.

		String[] selectionArgs = null;

		String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		@SuppressWarnings("deprecation")
		Cursor contactCursor = managedQuery(uri, projection, null,
				selectionArgs, sortOrder);

		contactlist = new ArrayList<Contact>();

		if (contactCursor.moveToFirst()) {
			do {
				String phoneNumber = contactCursor.getString(1).replaceAll("-",
						"");
				Contact acontact = new Contact();
				acontact.setPhoneNumber(phoneNumber);
				acontact.setUserName(contactCursor.getString(2));
				acontact.setUserId(null);
				acontact.setHasLog(false);
				contactlist.add(acontact);
			} while (contactCursor.moveToNext());
		}

		JSONArray dataCollection = new JSONArray();
		for(int i = 0; i < contactlist.size(); i++) {
			dataCollection.put(contactlist.get(i).getPhoneNumber());
		}
		String stringToSend = dataCollection.toString();
		RequestForm form = new RequestForm(ContactsActivity.this);
		form.add("phone-number-list", stringToSend);
		form.sendTo("http://wecookbob.appspot.com/contacts");
	}

	public void insert(ArrayList<Contact> listToInsert) {
		contactsDb = contactsHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		try{
			contactsDb.beginTransaction();
			for (int i = 0; i < listToInsert.size(); i++) {
				values.clear();
				values.put("userName", listToInsert.get(i).getUserName());
				values.put("phoneNumber", listToInsert.get(i).getPhoneNumber());
				values.put("hasLog", listToInsert.get(i).getHasLog());
				if (listToInsert.get(i).getUserId() != null) values.put("userId",
						listToInsert.get(i).getUserId());
				contactsDb.insert("contacts", null, values);
			}
			contactsDb.setTransactionSuccessful();
		} catch (SQLException e){
		} finally {
			contactsDb.endTransaction();
		}
		contactsDb.close();
	}

	public void update (String userName, String phoneNumber, boolean isUser, String userId) {
		contactsDb = contactsHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("phoneNumber", phoneNumber);
		contactsDb.update("contacts", values, "userName=?", new String[]{userName});
	}

	public void delete (String userName) {
		contactsDb = contactsHelper.getWritableDatabase();
		contactsDb.delete("contacts", "userName=?", new String[]{userName});
	}

	public void select() {
		contactsDb = contactsHelper.getReadableDatabase();
		Cursor c = contactsDb.query("contacts", null, null, null, null, null, null);

		while (c.moveToNext()) {
			//            int _id = c.getInt(c.getColumnIndex("_id"));
			//            String username = c.getString(c.getColumnIndex("username"));
			//            String mobile = c.getString(c.getColumnIndex("mobile"));
		}
	}

	public void addFriend(String userName) {

	}

	public ArrayList<String> getBobtnerIdList() {
		bobLogHelper = new BobLogSQLiteOpenHelper(ContactsActivity.this,
				"boblog.db",
				null,
				1);
		bobLogDb = bobLogHelper.getReadableDatabase();
		Cursor bobCursor = bobLogDb.rawQuery("SELECT * FROM boblog", null);
		ArrayList<String> bobtnerList = new ArrayList<String>();
		while (bobCursor.moveToNext()) {
			String bobtnerId = bobCursor.getString(bobCursor.getColumnIndex("bobtnerId"));
			bobtnerList.add(bobtnerId);
		}
		return bobtnerList;
	}

	@Override
	public void onResponse(String responseBody) {
		// TODO Auto-generated method stub
		JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(responseBody);
			JSONArray dataCollection = jsonResponse.getJSONArray("user-id-list");
			System.out.println(dataCollection);
			for (int i = 0; i < dataCollection.length(); i++) {
				Object userId = dataCollection.get(i);
				System.out.println(userId);
				if (!userId.equals(null)) {
					contactlist.get(i).setUserId(userId.toString());
					if (checkDataBase("boblog.db")) {
						ArrayList<String> bobtnerList = getBobtnerIdList();
						if (bobtnerList.contains(userId.toString())) contactlist.get(i).setHasLog(true);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		insert(contactlist);
		Toast.makeText(ContactsActivity.this, "동기화가 완료되었습니다",
				Toast.LENGTH_SHORT).show();
	}
}

