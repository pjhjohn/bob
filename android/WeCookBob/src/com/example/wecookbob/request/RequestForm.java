package com.example.wecookbob.request;

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

public class RequestForm extends AsyncTask<String, Void, String> {
	private final OnResponse caller;
	private String endpoint;
	private ArrayList<NameValuePair> pairOfNameValue;
	private HttpClient client;
	private HttpPost request;
	
	public RequestForm(OnResponse caller) {
		this.caller = caller;
		this.pairOfNameValue = new ArrayList<NameValuePair>();
		this.endpoint = "";
		build();
	}
	
	public void add(String name, String value) {
		this.pairOfNameValue.add(new BasicNameValuePair(name, value));
	}
	
	public void sendTo(String endpoint) {
		this.endpoint = endpoint;
		build();
		this.execute();
	}
	
	private void build() {
		this.client = new DefaultHttpClient();
		this.request = new HttpPost(this.endpoint);
		// Encode for POST Request
		try {
			this.request.setEntity(new UrlEncodedFormEntity(pairOfNameValue));
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
		this.caller.onResponse(response);
	}
}
