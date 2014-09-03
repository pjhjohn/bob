package com.example.httprequesttest.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;

public class RequestAsyncTask extends AsyncTask<String, Void, String> {
	private TextView textBody;
	
	public void setTextView(TextView body) {
		this.textBody = body;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://pjhjohn.appspot.com");
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
		nameValuePair.add(new BasicNameValuePair("test-name", "test-value"));
		// Encode Form in POST shape
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Receive Response
		try {
		    HttpResponse response = httpClient.execute(httpPost);
		    return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
		    return "ClientProtocolException\n" + e;
		} catch (IOException e) {
			Log.e(this.getClass().toString(), e.getLocalizedMessage());
			return "IOException\n" + e;
		}
	}
	
	@Override
	protected void onPostExecute(String response) {
		this.textBody.setText(response);
	}
}
