package com.appspot.wecookbob.data;

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil {
	public static enum PROPERTY {
		REG_ID, DEVICE_ID, APP_VERSION, USER_ID, USER_NAME, SIGNUP_ID, SIGNUP_PW, SIGNUP_MOBILE, ALARM, REGISTERED
	}
	private static PreferenceUtil instance = null;
	protected PreferenceUtil(Context context) {
		super(context);
	}
	public static synchronized PreferenceUtil getInstance(Context context) {
		if (instance == null) instance = new PreferenceUtil(context);
		return instance;
	}
	
	public void putString(String value, PROPERTY property) {
		if(property != PROPERTY.APP_VERSION) super.put(property.toString(), value);
	}
	public String getString(PROPERTY property, String _default) {
		if(property != PROPERTY.APP_VERSION) return super.get(property.toString(), _default);
		else return null;
	}
	public void putInteger(int value, PROPERTY property) {
		if(property == PROPERTY.APP_VERSION) super.put(property.toString(), value);
	}
	public Integer getInteger(PROPERTY property, int _default) {
		if(property == PROPERTY.APP_VERSION) return super.get(property.toString(), Integer.MIN_VALUE);
		else return (Integer) null;
	}
}