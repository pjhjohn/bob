package com.appspot.wecookbob.lib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class PostRequestForm extends AsyncTask<String, Void, String> {
	private final OnResponse receiver;
	private final String endpoint;
	private ArrayList<NameValuePair> nameValueList;
	private HttpClient client;
	private HttpPost request;
	
	public static interface OnResponse {
		void onResponse(String responseBody);
	}

	public PostRequestForm(OnResponse receiver, String endpoint) {
		this.nameValueList = new ArrayList<NameValuePair>();
		this.receiver = receiver;
		this.endpoint = endpoint;
		this.clear();
	}

	public void clear() {
		this.nameValueList = new ArrayList<NameValuePair>();
	}
	
	public void put(String name, String value) {
		int indexToRemove = -1;
		for(int i = 0; i < nameValueList.size(); i++) {
			if(name == nameValueList.get(i).getName()) {
				indexToRemove = i;
				break;
			}
		}
		if(indexToRemove >= 0) this.nameValueList.remove(indexToRemove);
		this.nameValueList.add(new BasicNameValuePair(name, value));
	}

	public void submit() {
		this.build();
		this.execute();
	}
	
	private void build() {
		this.client = new DefaultHttpClient();
		this.request = new HttpPost(this.endpoint);
		// Encode for POST Request
		try {
			this.request.setEntity(new UrlEncodedFormEntity(nameValueList));
		} catch(UnsupportedEncodingException e) {
			Log.e(getClass().toString(), e.getMessage());
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			HttpResponse response = this.client.execute(this.request);
			return EntityUtils.toString(response.getEntity());
		} catch(ClientProtocolException e) {
			Log.e(getClass().toString(), e.getMessage());
			return getClass().toString() +'\n'+  e.getMessage();
		} catch(IOException e) {
			Log.e(getClass().toString(), e.getMessage());
			return getClass().toString() +'\n'+  e.getMessage();
		}
	}
	
	@Override
	protected void onPostExecute(String response) {
		this.receiver.onResponse(response);
	}
}
