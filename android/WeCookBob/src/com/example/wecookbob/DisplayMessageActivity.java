package com.example.wecookbob;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		
		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		// Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(20);
	    textView.setText(message);
	    
		// Set the text view as the activity layout
//		setContentView(textView);
	    
		ArrayList<String> arrayList = new ArrayList<String>();
	    arrayList.add("listItem1");
	    arrayList.add("listItem2");
	    arrayList.add("listItem3");
	    arrayList.add("listItem4");
	    arrayList.add("listItem5");
	    arrayList.add("listItem6");
	    arrayList.add("listItem7");
	    arrayList.add("listItem8");
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
	    
	    ListView list;
	    list = (ListView)findViewById(R.id.listView);
	    list.setAdapter(adapter);
	    
	    list.setOnItemClickListener(new OnItemClickListener(){
	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    	}
	    });
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
}
