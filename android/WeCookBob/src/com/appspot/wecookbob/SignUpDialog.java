package com.appspot.wecookbob;

import com.appspot.wecookbob.lib.PostRequestForm;
import com.appspot.wecookbob.lib.PostRequestForm.OnResponse;
import com.appspot.wecookbob.lib.PreferenceUtil;

import android.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

// 다이얼로그 만들기
public class SignUpDialog extends DialogFragment implements OnResponse{
	View dialogView;
	EditText userNameText;
	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState){
		if(!PreferenceUtil.instance(this.getActivity().getApplicationContext()).userName().isEmpty())
		{
			SignUpDialog.this.dismiss();
		}
		
		AlertDialog.Builder SignUpDialogBuilder = new AlertDialog.Builder(
				getActivity());
		LayoutInflater SignUpDialogInflater = getActivity().getLayoutInflater();
		View dialogView = SignUpDialogInflater.inflate(R.layout.enter_user_name, null);
		SignUpDialogBuilder.setView(dialogView);
		// 다이얼로그 타이틀이 될 것
		SignUpDialogBuilder.setTitle("이름을 입력해주세요");
		
		this.userNameText = (EditText) dialogView.findViewById(R.id.enter_user_name);
		
		Button btn_submit_user_name = (Button) dialogView.findViewById(R.id.btn_sumit_user_name);
    	btn_submit_user_name.setOnClickListener(new View.OnClickListener(){
    		@Override
    		
			// 다이얼로그에 유저네임을 집어넣고 클릭했을 때 이벤트를 처리
    		public void onClick(View v) {
    			String get_user_name = userNameText.getText().toString();
    			String userId = PreferenceUtil.instance(SignUpDialog.this.getActivity().getApplicationContext()).userId();
    			
    			// 입력없이 등록버튼을 눌렀을 경우와 제대로 입력햇을때를 구분해주기.
    				if (get_user_name.equals("")){
    	    			Toast.makeText(SignUpDialog.this.getActivity().getApplicationContext(), "다시 ㄱㄱ", Toast.LENGTH_SHORT).show();
    	    		}else{
    	    			PreferenceUtil.instance(SignUpDialog.this.getActivity().getApplicationContext()).putUserName(get_user_name);
    	    			PostRequestForm form = new PostRequestForm(SignUpDialog.this, "http://wecookbob.appspot.com/register_name");
    	        		form.put("user-id", userId);
    	        		form.put("user-name", get_user_name);
    	        		form.submit();
    	    			Toast.makeText(SignUpDialog.this.getActivity().getApplicationContext(), get_user_name + "로 등록되었습니다", Toast.LENGTH_SHORT).show();
        	    		SignUpDialog.this.dismiss();	
    	    		}
    		}
    	});
    	
    return SignUpDialogBuilder.create();
	}
	
	
	@Override
	public void onStop(){
		super.onStop();
		
	}


	@Override
	public void onResponse(String responseBody) {
		// TODO Auto-generated method stub
		
	}
}
