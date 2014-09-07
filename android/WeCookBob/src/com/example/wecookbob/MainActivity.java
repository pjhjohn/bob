package com.example.wecookbob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.wecookbob.request.OnResponse;
import com.example.wecookbob.request.RequestForm;


public class MainActivity extends ActionBarActivity implements OnResponse {
	public final static String EXTRA_MESSAGE = "com.example.wecookbob.MESSAGE";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	String msgUsername = ((EditText) findViewById(R.id.editText2)).getText().toString();
    	String msgMobile = ((EditText) findViewById(R.id.editText1)).getText().toString();
    	RequestForm form = new RequestForm(MainActivity.this);
		form.add("username", msgUsername);
		form.add("mobile", msgMobile);
		form.sendTo("http://wecookbob.appspot.com/");
    }
    
	@Override
	public void onResponse(String response) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, response);
		startActivity(intent);
	}
}