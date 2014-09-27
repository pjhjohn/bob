package com.appspot.wecookbob.contact;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.wecookbob.ContactsActivity;
import com.appspot.wecookbob.R;
import com.appspot.wecookbob.lib.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.lib.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PreferenceUtil;

public class ContactUserListviewAdapter extends ArrayAdapter<ContactUser> {
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
		this.responseListener = null;
	}
	public ContactUserListviewAdapter(Context context, ArrayList<ContactUser> data, int layoutResID, int textViewResId, int btnResId, PostRequestForm.OnResponse responselistener) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResourceID = layoutResID;
		this.textviewResourceID = textViewResId;
		this.btnResourceID = btnResId;
		this.data = data;
		this.myContext = context;
		this.responseListener = responselistener;
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
				String senderId = PreferenceUtil.instance(getContext()).userId();
				String userId = c.getString(c.getColumnIndex("userId"));
				if (btnID == R.id.btn_send_first_bob) {
					PostRequestForm form = new PostRequestForm(ContactUserListviewAdapter.this.responseListener,"http://wecookbob.appspot.com/bob");
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
}