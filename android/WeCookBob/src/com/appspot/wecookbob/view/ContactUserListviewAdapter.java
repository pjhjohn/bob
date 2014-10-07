package com.appspot.wecookbob.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.data.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.data.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.data.PreferenceUtil;
import com.appspot.wecookbob.data.PreferenceUtil.PROPERTY;
import com.appspot.wecookbob.lib.PostRequestForm;

public class ContactUserListviewAdapter extends ArrayAdapter<ContactUser> implements PostRequestForm.OnResponse{
	private LayoutInflater inflater;
	private final int layoutResourceID;
	private final int textviewResourceID;
	private final int btnResourceID;
	private PostRequestForm.OnResponse responseListener;
	SQLiteDatabase contactsDb;
	SQLiteDatabase bobLogDb;
	ContactsSQLiteOpenHelper contactsHelper;
	BobLogSQLiteOpenHelper bobLogHelper;
	ArrayList<ContactUser> data;
	Context myContext;

	public ContactUserListviewAdapter(Context context, ArrayList<ContactUser> data, int layoutResID, int textViewResId, int btnResId) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResourceID = layoutResID;
		this.textviewResourceID = textViewResId;
		this.btnResourceID = btnResId;
		this.data = data;
		this.myContext = context;
	}
	
	private static class ElementHolder {
		public TextView tvUserName;
	}

	@Override
	public View getView(int position, View oldView, ViewGroup parentView) {
		View view = null;
		if (oldView == null) {
			view = inflater.inflate(this.layoutResourceID, null);
		}
		else view = oldView;
		final ContactUser elementData = this.getItem(position);

		if (elementData != null) {
			TextView textview = (TextView) view.findViewById(this.textviewResourceID);
			textview.setText(elementData.getName());
		}

		final int btnID = this.btnResourceID;

		view.findViewById(btnID).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String[] userName = { elementData.getName() };
				contactsHelper = new ContactsSQLiteOpenHelper(myContext,
						"contacts.db",
						null,
						1);
				contactsDb = contactsHelper.getReadableDatabase();
				Cursor c = contactsDb.rawQuery("SELECT * FROM contacts WHERE userName = ?", userName);
				c.moveToFirst();
				String phoneNumber = c.getString(c.getColumnIndex("phoneNumber"));
				String senderId = PreferenceUtil.getInstance(getContext()).getString(PROPERTY.USER_ID,"");
				String userId = c.getString(c.getColumnIndex("userId"));
				if (btnID == R.id.btn_send_first_bob) {
					PostRequestForm form = new PostRequestForm(ContactUserListviewAdapter.this,"http://wecookbob.appspot.com/bob");
					form.put("sender-user-id", senderId);
					form.put("receiver-user-id", userId);
					form.submit();
					Toast.makeText(myContext, userId + "에게 밥을 보냅니다", Toast.LENGTH_LONG).show();
				}
				else if (btnID == R.id.btn_invite) {
					Toast.makeText(myContext, phoneNumber + "에게 초대 메시지를 보냅니다", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		return view;
	}
	public void onResponse(String responseBody) {
		System.out.println(System.currentTimeMillis());
		System.out.println(responseBody);
		JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(responseBody);
			boolean success = jsonResponse.getBoolean("success");
			String stringDate = jsonResponse.getString("bob-request-time");
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = (Date) f.parse(stringDate);
			long bobRequestTime = d.getTime();
			String bobtnerId = jsonResponse.getString("bobtner-id");
			String bobtnerName = jsonResponse.getString("bobtner-name");
			BobLog.NotificationType notificationType = null;
			try {
				notificationType = BobLog.stringToNotificationType(jsonResponse.getString("notification-type"));
			} catch (Exception e) {
				// Bad Data Response
			}
			if (success) {
				System.out.println("success");
				bobLogHelper = new BobLogSQLiteOpenHelper(this.myContext,
						"boblog.db",
						null,
						1);
				bobLogDb = bobLogHelper.getWritableDatabase();
				ContentValues bobLogValues = new ContentValues();
				try{
					bobLogDb.beginTransaction();
					bobLogValues.put("bobRequestTime", bobRequestTime);
					bobLogValues.put("bobtnerId", bobtnerId);
					bobLogValues.put("bobtnerName", bobtnerName);
					bobLogValues.put("notificationType", BobLog.NotificationType.SENT.toString());
					bobLogDb.insert("boblog", null, bobLogValues);
					bobLogDb.setTransactionSuccessful();
				} catch (SQLException e){
				} finally {
					bobLogDb.endTransaction();
				}
				bobLogDb.close();
				contactsDb = contactsHelper.getWritableDatabase();
				ContentValues updateValues = new ContentValues();
				updateValues.put("hasLog", true);
				contactsDb.update("contacts", updateValues, "userId=?", new String[]{bobtnerId});
				this.notifyDataSetChanged();
				Toast.makeText(this.myContext, "상대방에게 밥을 보냈습니다",
						Toast.LENGTH_SHORT).show();
			}
			else if (success) {
				Toast.makeText(this.myContext, "상대방이 배가 부릅니다",
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}