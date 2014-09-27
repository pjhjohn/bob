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
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.wecookbob.contact.ContactUser;
import com.appspot.wecookbob.contact.ContactUserListviewAdapter;
import com.appspot.wecookbob.lib.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.lib.Contact;
import com.appspot.wecookbob.lib.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.lib.PostRequestForm;

public class ContactsActivity extends Activity {
	public ArrayList<Contact> contactlist;
	SQLiteDatabase contactsDb;
	SQLiteDatabase bobLogDb;
	ContactsSQLiteOpenHelper contactsHelper;
	BobLogSQLiteOpenHelper bobLogHelper;

	private ListView inviteListView, firstBobListView;
	private ContactUserListviewAdapter inviteAdapter, firstBobAdapter;
	private ArrayList<ContactUser> inviteArray, firstBobArray;

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

		// build listview for firstbob
		firstBobListView = (ListView) findViewById(R.id.lv_first_bob);
		firstBobArray = new ArrayList<ContactUser>();

		// build listview for invitation
		inviteListView = (ListView) findViewById(R.id.lv_invite);
		inviteArray = new ArrayList<ContactUser>();

		while (contactsCursor.moveToNext()) {
			String userName = contactsCursor.getString(contactsCursor.getColumnIndex("userName"));
			inviteArray.add(new ContactUser(userName, false));
		}
		while (firstBobCursor.moveToNext()) {
			String userName = firstBobCursor.getString(firstBobCursor.getColumnIndex("userName"));
			firstBobArray.add(new ContactUser(userName, true));
		}

		firstBobAdapter = new ContactUserListviewAdapter(this, firstBobArray, R.layout.send_first_bob_list_item, R.id.send_first_bob_friend_name, R.id.btn_send_first_bob, this.sendRequestReceiver);
		firstBobListView.setAdapter(firstBobAdapter);
		inviteAdapter = new ContactUserListviewAdapter(this, inviteArray, R.layout.invite_friend_list_item, R.id.invite_friend_name, R.id.btn_invite, this.sendRequestReceiver);
		inviteListView.setAdapter(inviteAdapter);
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
		return super.onOptionsItemSelected(item);
	}

	public void getContact() throws JSONException {
		if (checkDataBase("contacts.db")) deleteDatabase("contacts.db");
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
		PostRequestForm form = new PostRequestForm(syncRequestReceiver,"http://wecookbob.appspot.com/contacts");
		form.put("phone-number-list", stringToSend);
		form.submit();
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

	public PostRequestForm.OnResponse syncRequestReceiver = new PostRequestForm.OnResponse() {
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
	};

	public PostRequestForm.OnResponse sendRequestReceiver = new PostRequestForm.OnResponse() {
		@Override
		public void onResponse(String responseBody) {
			if (responseBody.equals("Success")) {
				bobLogHelper = new BobLogSQLiteOpenHelper(ContactsActivity.this,
						"boblog.db",
						null,
						1);
				bobLogDb = bobLogHelper.getWritableDatabase();
				ContentValues bobLogValues = new ContentValues();
				bobLogValues.put("bobtnerId","asdf");
				Toast.makeText(ContactsActivity.this, "상대방에게 밥을 보냈습니다",
						Toast.LENGTH_SHORT).show();
			}
			else if (responseBody.equals("Fail")) {
				Toast.makeText(ContactsActivity.this, "상대방이 배가 부릅니다",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
}

