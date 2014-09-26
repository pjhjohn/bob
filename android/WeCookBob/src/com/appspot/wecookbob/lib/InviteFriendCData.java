package com.appspot.wecookbob.lib;

import android.content.Context;

public class InviteFriendCData {
	private String invite_user_name;

	public InviteFriendCData(Context context, String invite_user_name) {
		this.invite_user_name = invite_user_name;
	}

	public String getUserName() {
		return invite_user_name;
	}
}