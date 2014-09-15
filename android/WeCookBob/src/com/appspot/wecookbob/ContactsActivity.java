package com.appspot.wecookbob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
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
import com.appspot.wecookbob.request.OnResponse;
import com.appspot.wecookbob.request.RequestForm;

public class ContactsActivity extends Activity implements OnResponse {
	public ArrayList<Contact> contactlist;
	SQLiteDatabase contactsDb;
	SQLiteDatabase bobLogDb;
	ContactsSQLiteOpenHelper contactsHelper;
	BobLogSQLiteOpenHelper bobLogHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		if (checkDataBase()) showList();
		else
			try {
				getContact();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkcontactsDb = null;
		try {
			checkcontactsDb = SQLiteDatabase.openDatabase("//data/data/com.appspot.wecookbob/databases/contacts.db", null,
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
		while (contactsCursor.moveToNext()) {
			System.out.println("no userId");
			String userName = contactsCursor.getString(contactsCursor.getColumnIndex("userName"));
			contactsList.add(userName);
		}
		while (firstBobCursor.moveToNext()) {
			String userName = firstBobCursor.getString(firstBobCursor.getColumnIndex("userName"));
			firstBobList.add(userName);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);

		ListView list;
		list = (ListView)findViewById(R.id.FriendsToInvitelistView);
		// TODO add first bob list here
		list.setAdapter(adapter);
		list.setChoiceMode(list.CHOICE_MODE_SINGLE);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				// TODO 아이템 클릭시에 구현할 내용은 여기에.
				String[] userName = { (String)list.getItemAtPosition(position) };
				contactsHelper = new ContactsSQLiteOpenHelper(ContactsActivity.this,
						"contacts.db",
						null,
						1);
				contactsDb = contactsHelper.getReadableDatabase();
				Cursor c = contactsDb.rawQuery("SELECT * FROM contacts WHERE userName = ?", userName);
				c.moveToFirst();
				String phoneNumber = c.getString(c.getColumnIndex("phoneNumber"));
				Toast.makeText(ContactsActivity.this, phoneNumber, Toast.LENGTH_LONG).show();
			}
		});
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
		if (checkDataBase()) deleteDatabase("contacts.contactsDb");
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
				contactlist.add(acontact);
			} while (contactCursor.moveToNext());
		}

		JSONArray dataCollection = new JSONArray();
		for(int i = 0; i < contactlist.size(); i++) {
			JSONObject dataToAppend = new JSONObject();
			dataToAppend.put("phone-number", contactlist.get(i).getPhoneNumber());
			dataCollection.put(dataToAppend);
		}
		String stringToSend = dataCollection.toString();

		RequestForm form = new RequestForm(ContactsActivity.this);
		form.add("phone-number-list", stringToSend);
		form.sendTo("http://wecookbob.appspot.com/contacts");
	}

	public void insert(ArrayList<Contact> listToInsert) {
		contactsDb = contactsHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0; i < listToInsert.size(); i++) {
			values.clear();
			values.put("userName", listToInsert.get(i).getUserName());
			values.put("phoneNumber", listToInsert.get(i).getPhoneNumber());
			values.put("isUser", false);
			if (listToInsert.get(i).getUserId() != null) values.put("userId",
					listToInsert.get(i).getUserId());
			contactsDb.insert("contacts", null, values);
		}
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

	@Override
	public void onResponse(String responseBody) {
		// TODO Auto-generated method stub
		JSONObject jsonResponse;
		System.out.println(responseBody);
		try {
			jsonResponse = new JSONObject(responseBody);
			JSONArray dataCollection = jsonResponse.getJSONArray("user-id-list");
			System.out.println(dataCollection);
			for (int i = 0; i < dataCollection.length(); i++) {
				Object userId = dataCollection.getJSONObject(i).get("user-id");
				if (!userId.equals(null))
				{
					contactlist.get(i).setUserId(userId.toString());
					System.out.println(contactlist.get(i).getUserId());
					System.out.println("daehongjam!!");
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

