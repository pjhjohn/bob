package com.appspot.wecookbob.gcm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.*;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;


import com.appspot.wecookbob.R;
import com.appspot.wecookbob.activity.MainActivity;
import com.appspot.wecookbob.data.BobLogSQLiteOpenHelper;
import com.appspot.wecookbob.data.ContactsSQLiteOpenHelper;
import com.appspot.wecookbob.data.PreferenceUtil;
import com.appspot.wecookbob.data.PreferenceUtil.PROPERTY;
import com.appspot.wecookbob.view.*;
import com.google.android.gms.gcm.*;

public class GcmIntentService extends IntentService
{
	SQLiteDatabase contactsDb;
	SQLiteDatabase bobLogDb;
	ContactsSQLiteOpenHelper contactsHelper;
	BobLogSQLiteOpenHelper bobLogHelper;
	
	public static final int NOTIFICATION_ID = 1;

	public GcmIntentService()
	{
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty())
		{ // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
			{
				sendNotification("Send error: " + extras.toString());
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
			{
				sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
			{
				String bobtnerId = intent.getStringExtra("sender-user-id");
				String bobtnerName = intent.getStringExtra("sender-user-name");
				String stringDate = intent.getStringExtra("bob-request-time");
				// Post notification of received message.
				//            sendNotification("Received: " + extras.toString());
				sendNotification(bobtnerId);
				
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d;
				try {
					d = (Date) f.parse(stringDate);
					long bobRequestTime = d.getTime();
					bobLogHelper = new BobLogSQLiteOpenHelper(this.getBaseContext(),
							"boblog.db",
							null,
							1);
					bobLogDb = bobLogHelper.getWritableDatabase();
					ContentValues bobLogValues = new ContentValues();
					bobLogValues.put("bobRequestTime", bobRequestTime);
					bobLogValues.put("bobtnerId", bobtnerId);
					bobLogValues.put("bobtnerName", bobtnerName);
					bobLogValues.put("notificationType", BobLog.NotificationType.RECEIVED.toString());
					if(bobLogDb.update("boblog", bobLogValues, "bobtnerId=?", new String[]{bobtnerId})==0)
					{
						contactsHelper = new ContactsSQLiteOpenHelper(this.getBaseContext(), "contacts.db", null, 1);
						contactsDb = contactsHelper.getWritableDatabase();
						ContentValues updateValues = new ContentValues();
						updateValues.put("hasLog", true);
						contactsDb.update("contacts", updateValues, "userId=?", new String[]{bobtnerId});
						bobLogDb.insert("boblog", null, bobLogValues);
					}
					bobLogDb.close();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.i("GcmIntentService.java | onHandleIntent", "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg)
	{
		boolean getAlarm = PreferenceUtil.getInstance(getApplicationContext()).getBoolean(PROPERTY.ALARM, true);
		if(getAlarm){
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("msg", msg);

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("밥")
					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
					.setContentText(msg)
					.setAutoCancel(true)
					.setVibrate(new long[] { 0, 500 });

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}
}