package com.appspot.wecookbob.contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.contact.BobLog.NotificationType;
import com.appspot.wecookbob.lib.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.lib.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PreferenceUtil;


public class BobLogListviewAdapter extends ArrayAdapter<BobLog> implements PostRequestForm.OnResponse {
	private LayoutInflater inflater;
	private Context context;
	ArrayList<BobLog> data;
	SQLiteDatabase bobLogDb;
	ContactsSQLiteOpenHelper contactsHelper;
	BobLogSQLiteOpenHelper bobLogHelper;
	Context myContext;

	public BobLogListviewAdapter(Context context, ArrayList<BobLog> data, int layoutResID, int textViewResId1) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.context = context;
		this.myContext = context;
	}
	private static class ElementHolder {
		public TextView tvBobtnerName;
	}

	@Override
	public View getView(int position, View oldView, ViewGroup parentView) {
		View view = null;
		if (oldView == null) {
			view = inflater.inflate(R.layout.bob_log_list_item, null);
		}
		else view = oldView;

		final BobLog elementData = this.getItem(position);

		if (elementData != null) {
			TextView textview1 = (TextView) view.findViewById(R.id.bobtner_name);
			//			TextView textview2 = (TextView) view.findViewById(R.id.request_time);
			textview1.setText(elementData.getBobtnerName());
			//			textview2.setText(elementData.getBobRequestTime()+"");
		}


		ImageButton btn_bob = (ImageButton) view.findViewById(R.id.btn_bob);
		ImageButton btn_call = (ImageButton) view.findViewById(R.id.btn_call);

		if (System.currentTimeMillis() - elementData.bobRequestTime < 1000*60*3){
			btn_bob.setBackgroundResource(R.drawable.bob_requested);
			if(elementData.getNotificationType() == NotificationType.RECEIVED){
				btn_call.setBackgroundResource(R.drawable.btn_can_call);
			}else{
				btn_call.setBackgroundResource(R.drawable.btn_empty);
			}

		}else{
			btn_bob.setBackgroundResource(R.drawable.send_bob_request);
			btn_call.setBackgroundResource(R.drawable.btn_empty);

		}
		BobLogListviewAdapter.this.sort(BobLog.COMPARE_BY_BOBREQUESTTIME);

		btn_bob.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				String bobtnerId = elementData.getBobtnerId();
				String bobtnerName = elementData.getBobtnerName();
				elementData.bobRequestTime = System.currentTimeMillis();
				elementData.type = NotificationType.SENT;

				PostRequestForm form = new PostRequestForm(BobLogListviewAdapter.this,"http://wecookbob.appspot.com/bob");
				form.put("sender-user-id", PreferenceUtil.instance(myContext).userId());
				form.put("receiver-user-id", bobtnerId);
				form.submit();
				Toast.makeText(myContext, bobtnerId + "에게 밥을 보냅니다", Toast.LENGTH_LONG).show();
		}
	});

		return view;
}

	@Override
	public void onResponse(String responseBody) {
		JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(responseBody);
			boolean success = jsonResponse.getBoolean("success");
			BobLog.NotificationType notificationType = null;
			try {
				notificationType = BobLog.stringToNotificationType(jsonResponse.getString("notification-type"));
			} catch (Exception e) {
				// Bad Data Response
			}
			if (success) {
				System.out.println("success");
				String stringDate = jsonResponse.getString("bob-request-time");
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = (Date) f.parse(stringDate);
				long bobRequestTime = d.getTime();
				String bobtnerId = jsonResponse.getString("bobtner-id");
				String bobtnerName = jsonResponse.getString("bobtner-name");
				bobLogHelper = new BobLogSQLiteOpenHelper(this.myContext,
						"boblog.db",
						null,
						1);
				bobLogDb = bobLogHelper.getWritableDatabase();
				ContentValues updateValues = new ContentValues();
				updateValues.put("bobRequestTime", bobRequestTime);
				bobLogDb.update("boblog", updateValues, "bobtnerId=?", new String[]{bobtnerId});
				BobLogListviewAdapter.this.sort(BobLog.COMPARE_BY_BOBREQUESTTIME);
				BobLogListviewAdapter.this.notifyDataSetChanged();
				Toast.makeText(this.myContext, "상대방에게 밥을 보냈습니다",
						Toast.LENGTH_SHORT).show();
			}
			else {
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