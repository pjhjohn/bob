package com.appspot.wecookbob;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.animation.*;
import android.widget.*;


public class SignUpActivity extends Activity {
	Button btnLogIn, btnSignUp;
	ViewFlipper vf;
	Animation slide_in_left, slide_out_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	
	 
		btnLogIn = (Button) findViewById(R.id.btn_login);
		btnSignUp = (Button) findViewById(R.id.btn_signup);
		vf = (ViewFlipper) findViewById(R.id.viewflipper);
	 
        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
 
        vf.setInAnimation(slide_in_left);
        vf.setOutAnimation(slide_out_right);
 
		btnLogIn.setOnClickListener(btnClickListener);
        btnSignUp.setOnClickListener(btnClickListener);
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
	        switch (v.getId()) {
	        case R.id.btn_login:
	        	if(v==findViewById(R.id.btn_login))
	        	{
	        		vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.vf_login)));
	        	}else{
	        		vf.stopFlipping();
	        	}
	        		
	            break;
	        case R.id.btn_signup:
	        	
	        	if(v==findViewById(R.id.btn_signup))
	        	{
	        		vf.setDisplayedChild(vf.indexOfChild(findViewById(R.id.vf_signup)));
	        	}else{
	        		vf.stopFlipping();
	        	}
	            break;
	        default:
	            break;
	        }
	         
	    }
	};
	
    public void signUp(View view) {
    	Toast.makeText(SignUpActivity.this, "가입완료되었습니다",
                Toast.LENGTH_SHORT).show();
    }
	
	
    public void showBobMain(View view) {
    	Toast.makeText(SignUpActivity.this, "밥 메인페이지로",
                Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
