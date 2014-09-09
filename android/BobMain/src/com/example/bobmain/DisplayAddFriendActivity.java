package com.example.bobmain;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

public class DisplayAddFriendActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_add_friend);
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
			  return true;
		}
		else{
                  // if a the new item is clicked show "Toast" message.
		}

		return super.onOptionsItemSelected(item);
	}
}
