package com.appspot.wecookbob.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.data.PreferenceUtil;
import com.appspot.wecookbob.data.PreferenceUtil.PROPERTY;
import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PostRequestForm.OnResponse;

public class SignUpActivity extends Activity implements OnResponse {
	ViewFlipper vfSignUp, vfMobile;
	Animation slideInLeft, slideOutRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO : Should Check whether ID/PASSWORD within Preference is correct in Server.
		if(!PreferenceUtil.getInstance(getApplicationContext()).getString(PROPERTY.USER_ID, "").isEmpty()) {
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
		}
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sign_up);
		
		// Register EventListers to Views
		this.findViewById(R.id.btn_login).setOnClickListener(clickListenerForViewFlipper);
		this.findViewById(R.id.btn_signup).setOnClickListener(clickListenerForViewFlipper);
		
		this.findViewById(R.id.btn_signup_complete).setOnClickListener(btnClickListener);
		this.findViewById(R.id.loginBtn).setOnClickListener(btnClickListener);
		this.findViewById(R.id.btn_certificate).setOnClickListener(btnClickListener);
		this.findViewById(R.id.btn_get_certification_number).setOnClickListener(btnClickListener);
		
		// Animations
		slideInLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideOutRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
		
        // To ViewFlipper
		vfSignUp = (ViewFlipper) findViewById(R.id.vf_login_signup);
		vfMobile = (ViewFlipper) findViewById(R.id.vf_signup_phonecheck);
		vfSignUp.setInAnimation(slideInLeft);
        vfSignUp.setOutAnimation(slideOutRight);
        vfMobile.setInAnimation(slideInLeft);
        vfMobile.setOutAnimation(slideOutRight);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);
//	}
	
	OnClickListener clickListenerForViewFlipper = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
	        case R.id.btn_login:
	        	if(v==findViewById(R.id.btn_login)) vfSignUp.setDisplayedChild(vfSignUp.indexOfChild(findViewById(R.id.vf_login)));
	        	else vfSignUp.stopFlipping();
	            break;
	        
	        case R.id.btn_signup:
	        	if(v==findViewById(R.id.btn_signup)) vfSignUp.setDisplayedChild(vfSignUp.indexOfChild(findViewById(R.id.vf_signup)));
	        	else vfSignUp.stopFlipping();
	            break;
			}
		}
	};
	
	private String getStringById(int resourceID) {
		return ((EditText) this.findViewById(resourceID)).getText().toString();
	}
	OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
	        switch (view.getId()) {
	        case R.id.btn_signup_complete:
	        	// TODO : is if-statement below useless???
	        	if(view==findViewById(R.id.btn_signup_complete)) {
	        		String id = getStringById(R.id.signUpenterId), pw = getStringById(R.id.signUpEnterPass), pwCheck = getStringById(R.id.signUpEnterPassAgain);
	    	    	if(pw.equals(null) || pw.equals("")) {
	    	    		Toast.makeText(SignUpActivity.this, "비밀번호를 입력해 주십시오", Toast.LENGTH_SHORT).show();
	    	    		return;
	    	    	}
	    	    	if(pw.equals(pwCheck)) {
						PreferenceUtil.getInstance(getApplicationContext()).putString(id, PROPERTY.SIGNUP_ID);
						PreferenceUtil.getInstance(getApplicationContext()).putString(pw, PROPERTY.SIGNUP_PW);
	        			new PostRequestForm(SignUpActivity.this,"http://wecookbob.appspot.com/user_id_check")
	        				.put("signup-id", id)
	        				.submit();
					} else Toast.makeText(SignUpActivity.this, "비밀번호를 확인하십시오", Toast.LENGTH_SHORT).show();
	        	} else vfMobile.stopFlipping();
	        case R.id.loginBtn:
	        	if(view==findViewById(R.id.loginBtn)) {
	        		new PostRequestForm(SignUpActivity.this, "http://wecookbob.appspot.com/login")
	        			.put("user-id", getStringById(R.id.enterId))
	        			.put("password", getStringById(R.id.enterPass))
	        			.submit();
	        	}
	        case R.id.btn_get_certification_number:
	        	if(view==findViewById(R.id.btn_get_certification_number)) {
	        		new PostRequestForm(SignUpActivity.this, "http://wecookbob.appspot.com/mobile_check")
	        			.put("mobile", getStringById(R.id.enter_phonenumber))
	        			.submit();
	        	}
	        case R.id.btn_certificate:
	        	if(view==findViewById(R.id.btn_certificate)) {
	        		PreferenceUtil pref = PreferenceUtil.getInstance(getApplicationContext());
	        		if(pref.getString(PROPERTY.SIGNUP_MOBILE, null) != null) {
	        			new PostRequestForm(SignUpActivity.this, "http://wecookbob.appspot.com/signup")
							.put("signup-id", pref.getString(PROPERTY.SIGNUP_ID, ""))
							.put("signup-pw", pref.getString(PROPERTY.SIGNUP_PW, ""))
							.put("signup-mobile", pref.getString(PROPERTY.SIGNUP_MOBILE, ""))
							.submit();
	        		}
	        		else Toast.makeText(SignUpActivity.this, "전화번호를 인증받지 않았습니다", Toast.LENGTH_SHORT).show();
	        	}
	        }
	    }
	};
	
	
	
	public void certificate_phonenumber(View view) {
    	Toast.makeText(SignUpActivity.this, "인증이 완료되었습니다 혹은 다시 입력해주세요", Toast.LENGTH_SHORT).show();
    	startActivity(new Intent(this, MainActivity.class));
    }
    
	@Override
	public void onResponse(String responseBody) {
		// Initialize Variables
		JSONObject respJSON;
		String respType;
		try {
			respJSON = new JSONObject(responseBody);
			respType = respJSON.getString("type");
		} catch (JSONException e) {
			Toast.makeText(this, "JSONException Occured while initializing variables.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Values Availiable
		try {
			if(respType.equals("user-id-check")) {
				if(respJSON.getBoolean("available")) vfMobile.setDisplayedChild(vfMobile.indexOfChild(findViewById(R.id.vf_certification)));
				else Toast.makeText(this, "ID already in use", Toast.LENGTH_SHORT).show();
			} else if(respType.equals("mobile-check")) {
				if(respJSON.getBoolean("available")) {
					PreferenceUtil.getInstance(this).putString(respJSON.getString("mobile"), PROPERTY.SIGNUP_MOBILE);
					Toast.makeText(this, "Mobile# availiable", Toast.LENGTH_SHORT).show();
				} else Toast.makeText(this, "Mobile# already taken", Toast.LENGTH_SHORT).show();
			} else if(respType.equals("signup-check")) {
				if(respJSON.getBoolean("success")) {
					Log.i("MainActivity.java | storeUserId","| userId: " + respJSON.getString("signup-id") + "|");
					PreferenceUtil.getInstance(getApplicationContext()).putString(respJSON.getString("signup-id"), PROPERTY.USER_ID);
					startActivity(new Intent(this, MainActivity.class));
					finish();
				} else Toast.makeText(this, "Failed to Sign-up", Toast.LENGTH_SHORT).show();
			} else if(respType.equals("login-check")) {
				if(respJSON.getBoolean("success")) {
					Log.i("MainActivity.java | storeUserId","| userId: " + respJSON.getString("user-id") + "|");
					PreferenceUtil.getInstance(getApplicationContext()).putString(respJSON.getString("user-id"), PROPERTY.USER_ID);
					startActivity(new Intent(this, MainActivity.class));
					finish();
				} else Toast.makeText(this, "Invalid ID or Password", Toast.LENGTH_SHORT).show();
			} else Toast.makeText(this, "Invalid Response Type", Toast.LENGTH_SHORT).show();
		} catch(JSONException e) {
			Toast.makeText(this, "JSONException Occured while Executing.", Toast.LENGTH_SHORT).show();
		}
	}
}