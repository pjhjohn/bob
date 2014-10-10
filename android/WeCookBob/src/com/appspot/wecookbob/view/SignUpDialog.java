package com.appspot.wecookbob.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.data.PreferenceUtil;
import com.appspot.wecookbob.data.PreferenceUtil.PROPERTY;
import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PostRequestForm.OnResponse;

public class SignUpDialog extends DialogFragment implements OnResponse{
	private EditText userNameEditText;
	private Context context;

	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState){
		this.context = this.getActivity().getApplicationContext();
		if(!PreferenceUtil.getInstance(this.context).getString(PROPERTY.USER_NAME, "").isEmpty()) SignUpDialog.this.dismiss();

		// Inflate View for Dialog
		View dialogView = this.getActivity().getLayoutInflater().inflate(R.layout.enter_user_name, null);
		
		// Build Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity())
			.setView(dialogView)
			.setTitle("이름을 입력해주세요");
		
		// get username
		this.userNameEditText = (EditText) dialogView.findViewById(R.id.enter_user_name);
		dialogView.findViewById(R.id.btn_sumit_user_name).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String userName = userNameEditText.getText().toString();
				String userId = PreferenceUtil.getInstance(SignUpDialog.this.context).getString(PROPERTY.USER_ID, "");
				if(userName.isEmpty()) {
					Toast.makeText(SignUpDialog.this.context, "다시 ㄱㄱ", Toast.LENGTH_SHORT).show();
					return;
				}
				PreferenceUtil.getInstance(SignUpDialog.this.context).putString(userName, PROPERTY.USER_NAME); 
				new PostRequestForm(SignUpDialog.this, "http://wecookbob.appspot.com/register_name")
					.put("user-id", userId)
					.put("user-name", userName)
					.submit();
				Toast.makeText(SignUpDialog.this.context, userName + "로 등록되었습니다", Toast.LENGTH_SHORT).show();
				SignUpDialog.this.dismiss();	
			}
		});
		return builder.create();
	}

	@Override
	public void onStop(){
		super.onStop();
	}
	@Override
	public void onResponse(String responseBody) {
	}
}
