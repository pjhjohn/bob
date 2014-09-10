package com.appspot.wecookbob;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class SignUp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.startBobBtn) {			
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void showBobMain(View view) {
    	Toast.makeText(SignUp.this, "밥 메인페이지로",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
