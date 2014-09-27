package com.appspot.wecookbob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PostRequestForm.OnResponse;




public class SignUpActivity extends Activity implements OnResponse {
	Button btnLogIn, btnSignUp, btnSignUpComplete;
	ViewFlipper vf_login_signup, vf_signup_phonecheck;
	Animation slide_in_left, slide_out_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	
	 
		btnLogIn = (Button) findViewById(R.id.btn_login);
		btnSignUp = (Button) findViewById(R.id.btn_signup);
		btnSignUpComplete = (Button) findViewById(R.id.btn_signup_complete);
		
		vf_login_signup = (ViewFlipper) findViewById(R.id.vf_login_signup);
		vf_signup_phonecheck = (ViewFlipper) findViewById(R.id.vf_signup_phonecheck);
	 
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
	    	String id = signUpId.getText().toString();
	    	String pw = signUpPass.getText().toString();
	    	String pwck = signUpPassAgain.getText().toString();
	        switch (v.getId()) {
	        case R.id.btn_login:
	        	if(v==findViewById(R.id.btn_login))
	        	{
	        		vf_login_signup.setDisplayedChild(vf_login_signup.indexOfChild(findViewById(R.id.vf_login)));
			    }else{
	        		vf_login_signup.stopFlipping();
	        	}
	            break;
	        
	        case R.id.btn_signup:
	        	if(v==findViewById(R.id.btn_signup))
	        	{
	        		vf_login_signup.setDisplayedChild(vf_login_signup.indexOfChild(findViewById(R.id.vf_signup)));
	        	}else{
	        		vf_login_signup.stopFlipping();
	        	}
	            break;

	        case R.id.btn_signup_complete:
	        	if(v==findViewById(R.id.btn_signup_complete))
	        	{
	        		if (pw.equals(pwck)) {
	        			System.out.println(pw);
	        			PostRequestForm form = new PostRequestForm(SignUpActivity.this,"http://wecookbob.appspot.com/contacts");
						form.put("sign-up-id", id);
						form.put("sign-up-pw", pw);
						form.submit();
						vf_signup_phonecheck.setDisplayedChild(vf_signup_phonecheck.indexOfChild(findViewById(R.id.vf_certification)));
					}
	        		else Toast.makeText(SignUpActivity.this, "비밀번호를 확인하십시오",
	                        Toast.LENGTH_SHORT).show();
	        	}else{
	        		vf_signup_phonecheck.stopFlipping();
	        	}
	        default:
	        break;
	        }
	         
	    }
	};
	
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
		
	}
}