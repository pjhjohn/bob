package com.appspot.wecookbob;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.animation.*;
import android.widget.*;

public class SignUp extends Activity {
	ViewFlipper vf_login_signup, vf_signup_phonecheck;
	Animation slide_in_left, slide_out_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		vf_login_signup = (ViewFlipper) findViewById(R.id.vf_login_signup);
		vf_signup_phonecheck = (ViewFlipper) findViewById(R.id.vf_signup_phonecheck);
	 
        slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
 
        vf_login_signup.setInAnimation(slide_in_left);
        vf_login_signup.setOutAnimation(slide_out_right);
 
        vf_signup_phonecheck.setInAnimation(slide_in_left);
        vf_signup_phonecheck.setOutAnimation(slide_out_right);
 
        
        findViewById(R.id.btn_login).setOnClickListener(btnClickListener);
        findViewById(R.id.btn_signup).setOnClickListener(btnClickListener);
        findViewById(R.id.btn_signup_complete).setOnClickListener(btnClickListener);
        findViewById(R.id.btn_certificate).setOnClickListener(btnClickListener);
        findViewById(R.id.btn_get_certification_number).setOnClickListener(get_certificate_phone);
    }
	
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
	        switch (v.getId()) {
	        case R.id.btn_login:
	        	if(v==findViewById(R.id.btn_login)) vf_login_signup.setDisplayedChild(vf_login_signup.indexOfChild(findViewById(R.id.vf_login)));
	        	else vf_login_signup.stopFlipping();
	            break;
	        case R.id.btn_signup:
	        	
	        	if(v==findViewById(R.id.btn_signup)) vf_login_signup.setDisplayedChild(vf_login_signup.indexOfChild(findViewById(R.id.vf_signup)));
	        	else vf_login_signup.stopFlipping();
	            break;
	        case R.id.btn_signup_complete:
	        	if(v==findViewById(R.id.btn_signup_complete)) vf_signup_phonecheck.setDisplayedChild(vf_signup_phonecheck.indexOfChild(findViewById(R.id.vf_certification)));
	        	else vf_signup_phonecheck.stopFlipping();
	        case R.id.btn_certificate : 
	        	Toast.makeText(SignUp.this, "�몄쬆���꾨즺�섏뿀�듬땲���뱀� �ㅼ떆 �낅젰�댁＜�몄슂", Toast.LENGTH_SHORT).show();
		    	showBobMain(v);
		    	break;
	        default:
	        break;
	        }
	    }
	};
	
	OnClickListener get_certificate_phone = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Toast.makeText(SignUp.this, "臾몄옄濡�諛쏆� �몄쬆踰덊샇瑜��낅젰�섏떗�쒖삤", Toast.LENGTH_SHORT).show();
		}
    };
	
    public void showBobMain(View view) {
    	Toast.makeText(SignUp.this, "諛�硫붿씤�섏씠吏�줈", Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, BobMainActivity.class);
        startActivity(intent);
    }
}
