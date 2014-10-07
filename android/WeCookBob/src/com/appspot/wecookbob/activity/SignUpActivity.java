package com.appspot.wecookbob.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.R.id;
import com.appspot.wecookbob.R.layout;
import com.appspot.wecookbob.R.menu;
import com.appspot.wecookbob.data.PreferenceUtil;
import com.appspot.wecookbob.data.PreferenceUtil.PROPERTY;
import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PostRequestForm.OnResponse;

public class SignUpActivity extends Activity implements OnResponse {
	Button btnLogIn, btnSignUp, btnSignUpComplete, loginBtn, btnCertificate, btnGetCertificationNumber;
	ViewFlipper vf_login_signup, vf_signup_phonecheck;
	LinearLayout vf_certification, vf_login;
	Animation slide_in_left, slide_out_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(!PreferenceUtil.getInstance(getApplicationContext()).getString(PROPERTY.USER_ID, "").isEmpty()) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	
	 
		btnLogIn = (Button) findViewById(R.id.btn_login);
		btnSignUp = (Button) findViewById(R.id.btn_signup);
		btnSignUpComplete = (Button) findViewById(R.id.btn_signup_complete);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		btnCertificate = (Button) findViewById(R.id.btn_certificate);
		btnGetCertificationNumber = (Button) findViewById(R.id.btn_get_certification_number);
		
		vf_login_signup = (ViewFlipper) findViewById(R.id.vf_login_signup);
		vf_signup_phonecheck = (ViewFlipper) findViewById(R.id.vf_signup_phonecheck);
		
		vf_certification = (LinearLayout) findViewById(R.id.vf_certification);
		vf_login = (LinearLayout) findViewById(R.id.vf_login);
	 
        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
 
        vf_login_signup.setInAnimation(slide_in_left);
        vf_login_signup.setOutAnimation(slide_out_right);
 
        vf_signup_phonecheck.setInAnimation(slide_in_left);
        vf_signup_phonecheck.setOutAnimation(slide_out_right);
 
        
		btnLogIn.setOnClickListener(btnClickListener);
        btnSignUp.setOnClickListener(btnClickListener);
        btnSignUpComplete.setOnClickListener(btnClickListener);
        loginBtn.setOnClickListener(btnClickListener);
        btnCertificate.setOnClickListener(btnClickListener);
        btnGetCertificationNumber.setOnClickListener(btnClickListener);

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

		return super.onOptionsItemSelected(item);
	}

	OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText signUpId = (EditText) findViewById(R.id.signUpenterId);
	    	EditText signUpPass = (EditText) findViewById(R.id.signUpEnterPass);
	    	EditText signUpPassAgain = (EditText) findViewById(R.id.signUpEnterPassAgain);
	    	EditText enterId = (EditText) findViewById(R.id.enterId);
	    	EditText enterPass = (EditText) findViewById(R.id.enterPass);
	    	EditText enterPhonenumber = (EditText) findViewById(R.id.enter_phonenumber);
	    	String id = signUpId.getText().toString();
	    	String pw = signUpPass.getText().toString();
	    	String pwck = signUpPassAgain.getText().toString();
	    	String loginId = enterId.getText().toString();
	    	String loginPw = enterPass.getText().toString();
	    	String mobile = enterPhonenumber.getText().toString();
	        switch (v.getId()) {
	        case R.id.btn_login:
	        	if(v==findViewById(R.id.btn_login))
	        		vf_login_signup.setDisplayedChild(vf_login_signup.indexOfChild(findViewById(R.id.vf_login)));
	        	else
	        		vf_login_signup.stopFlipping();
	            break;
	        
	        case R.id.btn_signup:
	        	if(v==findViewById(R.id.btn_signup))
	        		vf_login_signup.setDisplayedChild(vf_login_signup.indexOfChild(findViewById(R.id.vf_signup)));
	        	else
	        		vf_login_signup.stopFlipping();
	            break;

	        case R.id.btn_signup_complete:
	        	if(v==findViewById(R.id.btn_signup_complete))
	        	{
	        		if (pw.equals(pwck)&!pw.equals(null)&!pw.equals("")) {
						PreferenceUtil.getInstance(getApplicationContext()).putString(id, PROPERTY.SIGNUP_ID);
						PreferenceUtil.getInstance(getApplicationContext()).putString(pw, PROPERTY.SIGNUP_PW);
	        			new PostRequestForm(SignUpActivity.this,"http://wecookbob.appspot.com/user_id_check")
	        				.put("signup-id", id)
	        				.submit();
					}
	        		else Toast.makeText(SignUpActivity.this, "비밀번호를 확인하십시오", Toast.LENGTH_SHORT).show();
	        	}else{
	        		vf_signup_phonecheck.stopFlipping();
	        	}
	        case R.id.loginBtn:
	        	if(v==findViewById(R.id.loginBtn)) {
	        		PostRequestForm form = new PostRequestForm(SignUpActivity.this, "http://wecookbob.appspot.com/login");
	        		form.put("user-id",loginId);
	        		form.put("password", loginPw);
	        		form.submit();
	        	}
	        case R.id.btn_get_certification_number:
	        	if(v==findViewById(R.id.btn_get_certification_number)) {
	        		PostRequestForm form = new PostRequestForm(SignUpActivity.this, "http://wecookbob.appspot.com/mobile_check");
	        		form.put("mobile", mobile);
	        		form.submit();
	        	}
	        case R.id.btn_certificate:
	        	if(v==findViewById(R.id.btn_certificate)) {
	        		PreferenceUtil pref = PreferenceUtil.getInstance(getApplicationContext());
	        		if(pref.getString(PROPERTY.SIGNUP_MOBILE, null) != null) {
	        			System.out.println("GO");
	        			PostRequestForm form = new PostRequestForm(SignUpActivity.this, "http://wecookbob.appspot.com/signup");
						form.put("signup-id", pref.getString(PROPERTY.SIGNUP_ID, ""));
						form.put("signup-pw", pref.getString(PROPERTY.SIGNUP_PW, ""));
						form.put("signup-mobile", pref.getString(PROPERTY.SIGNUP_MOBILE, ""));
						System.out.println(form);
						form.submit();
	        		}
	        		else {
	        			Toast.makeText(SignUpActivity.this, "전화번호를 인증받지 않았습니다", Toast.LENGTH_SHORT).show();
	        		}
	        	}
	        }
	    }
	};
	
	private void storeUserId(String userId) {
		Log.i("MainActivity.java | storeUserId","| userId: " + userId + "|");
		PreferenceUtil.getInstance(getApplicationContext()).putString(userId, PROPERTY.USER_ID);
	}
	
	public void get_certificate_number(View view) {
    	Toast.makeText(SignUpActivity.this, "문자로 받은 인증번호를 입력하십시오",
                Toast.LENGTH_SHORT).show();
    }

	
	public void certificate_phonenumber(View view) {
    	Toast.makeText(SignUpActivity.this, "인증이 완료되었습니다 혹은 다시 입력해주세요",
                Toast.LENGTH_SHORT).show();
    	
    	showBobMain(view);
    }
	
	
    public void showBobMain(View view) {
    	Toast.makeText(SignUpActivity.this, "밥 메인페이지로",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



	@Override
	public void onResponse(String responseBody) {
		// TODO Auto-generated method stub
		JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(responseBody);
			String responseType = jsonResponse.getString("type");
			if(responseType.equals("user-id-check"))
			{
				System.out.println(jsonResponse);
				if(jsonResponse.getBoolean("available")) 
					vf_signup_phonecheck.setDisplayedChild(vf_signup_phonecheck.indexOfChild(findViewById(R.id.vf_certification)));
				else
					Toast.makeText(SignUpActivity.this, "사용중인 아이디입니다", Toast.LENGTH_SHORT).show();
			}
			else if(responseType.equals("mobile-check"))
			{
				System.out.println(jsonResponse);
				if(jsonResponse.getBoolean("available"))
				{
					PreferenceUtil.getInstance(getApplicationContext()).putString(jsonResponse.getString("mobile"), PROPERTY.SIGNUP_MOBILE);
					Toast.makeText(SignUpActivity.this, "사용 가능한 전화번호입니다", Toast.LENGTH_SHORT).show();
				} else Toast.makeText(SignUpActivity.this, "등록된 전화번호입니다", Toast.LENGTH_SHORT).show();
			}
			else if(responseType.equals("signup-check"))
			{
				System.out.println(jsonResponse);
				if(jsonResponse.getBoolean("success")) {
					storeUserId(jsonResponse.getString("signup-id"));
					showBobMain(vf_certification);
					finish();
				}
				else Toast.makeText(SignUpActivity.this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
			}
			else if(responseType.equals("login-check"))
			{
				System.out.println(jsonResponse);
				if(jsonResponse.getBoolean("success")) {
					storeUserId(jsonResponse.getString("user-id"));
					showBobMain(vf_login);
					finish();
				}
				else Toast.makeText(SignUpActivity.this, "잘못된 아이디 또는 비밀번호입니다", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}