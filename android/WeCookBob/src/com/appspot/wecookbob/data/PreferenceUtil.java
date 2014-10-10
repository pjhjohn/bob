package com.appspot.wecookbob.data;

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil {
	public static enum PROPERTY {
		// String
		REG_ID, DEVICE_ID, USER_ID, USER_NAME, SIGNUP_ID, SIGNUP_PW, SIGNUP_MOBILE,  
		// boolean
		ALARM, REGISTERED,
		// int
		APP_VERSION 
	}
	private static PreferenceUtil instance = null;
	protected PreferenceUtil(Context context) {
		super(context);
	}
	public static synchronized PreferenceUtil getInstance(Context context) {
		if (instance == null) instance = new PreferenceUtil(context);
		return instance;
	}
	
	// Setter
	public void putString(String value, PROPERTY property) {
		if(property != PROPERTY.APP_VERSION && property != PROPERTY.REGISTERED && property != PROPERTY.ALARM)
			super.put(property.toString(), value);
	}
	public void putInteger(int value, PROPERTY property) {
		if(property == PROPERTY.APP_VERSION) super.put(property.toString(), value);
	}
	public void putBoolean(boolean value, PROPERTY property) {
		if(property == PROPERTY.REGISTERED || property == PROPERTY.ALARM) super.put(property.toString(), value);
	}
	
	// Getter : null if the PROPERTY type mismatches
	public String getString(PROPERTY property, String _default) {
		if(property != PROPERTY.APP_VERSION && property != PROPERTY.REGISTERED && property != PROPERTY.ALARM)
			return super.get(property.toString(), _default);
		else return null;
	}
	public Integer getInteger(PROPERTY property, int _default) {
		if(property == PROPERTY.APP_VERSION) return Integer.valueOf(super.get(property.toString(), Integer.MIN_VALUE));
		else return null;
	}
	public Boolean getBoolean(PROPERTY property, boolean _default) {
		if(property == PROPERTY.REGISTERED || property == PROPERTY.ALARM) return Boolean.valueOf(super.get(property.toString(), _default));
		else return null;
	}
}