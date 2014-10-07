package com.appspot.wecookbob.activity;

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

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.R.id;
import com.appspot.wecookbob.R.layout;
import com.appspot.wecookbob.R.menu;
import com.appspot.wecookbob.data.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.data.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.view.Contact;
import com.appspot.wecookbob.view.ContactUser;
import com.appspot.wecookbob.view.ContactUserListviewAdapter;

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
		
		String[] var = { "0" };
		Cursor firstBobCursor = contactsDb.rawQuery("SELECT * FROM contacts WHERE userId IS NOT NULL AND hasLog = ?", var);	

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

		firstBobAdapter = new ContactUserListviewAdapter(this, firstBobArray, R.layout.send_first_bob_list_item, R.id.send_first_bob_friend_name, R.id.btn_send_first_bob);
		firstBobAdapter.notifyDataSetChanged();
		firstBobListView.setAdapter(firstBobAdapter);
		inviteAdapter = new ContactUserListviewAdapter(this, inviteArray, R.layout.invite_friend_list_item, R.id.invite_friend_name, R.id.btn_invite);
		inviteAdapter.notifyDataSetChanged();
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

	public void update (String userName, String userId, String phoneNumber, boolean hasLog) {
		contactsDb = contactsHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("hasLog", true);
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
}

