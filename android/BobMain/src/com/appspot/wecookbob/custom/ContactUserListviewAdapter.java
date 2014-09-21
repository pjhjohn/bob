package com.appspot.wecookbob.custom;

import java.util.ArrayList;
import java.util.List;

import com.appspot.wecookbob.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactUserListviewAdapter extends ArrayAdapter<ContactUserData> {
	private LayoutInflater inflater;
	private final int layoutResourceID;
	
	public ContactUserListviewAdapter(Context context, ArrayList<ContactUserData> data, int resourceID) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResourceID = resourceID;
	}
	
	@Override
	public View getView(int position, View oldView, ViewGroup parentView) {
		View view = null;
		if (oldView == null) {
			view = inflater.inflate(layoutResourceID, null);
		}
		else view = oldView;
		final ContactUserData elementData = this.getItem(position);

		if (elementData != null) {
			TextView textview = (TextView) view.findViewById(R.id.invite_friend_name);
			textview.setText(elementData.getUserName());
		}
		return view;
	}
}