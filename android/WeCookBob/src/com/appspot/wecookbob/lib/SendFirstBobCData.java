package com.appspot.wecookbob.lib;

import android.content.*;


public class SendFirstBobCData {
	private String send_first_bob_user_name;

	public SendFirstBobCData(Context context, String send_first_bob_user_name) {
		this.send_first_bob_user_name = send_first_bob_user_name;
	}

	public String getUserName() {
		return send_first_bob_user_name;
	}
}