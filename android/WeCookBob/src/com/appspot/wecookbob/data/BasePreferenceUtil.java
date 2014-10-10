package com.appspot.wecookbob.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BasePreferenceUtil {
	private SharedPreferences pref;
	protected BasePreferenceUtil(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	protected void put(String key, String value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	protected void put(String key, boolean value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	protected void put(String key, int value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	protected String get(String key, String _default) {
		return pref.getString(key, _default);
	}  

	protected boolean get(String key, boolean _default) {
		return pref.getBoolean(key, _default);
	}

	protected int get(String key, int _default) {
		return pref.getInt(key, _default);
	}
}