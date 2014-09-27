package com.appspot.wecookbob.contact;

import java.util.ArrayList;
import java.util.List;

import com.appspot.wecookbob.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactUserListviewAdapter extends ArrayAdapter<ContactUser> {
	private LayoutInflater inflater;
	private final int layoutResourceID;
	private final int textviewResourceID;
	ArrayList<ContactUser> data;
	
	public ContactUserListviewAdapter(Context context, ArrayList<ContactUser> data, int layoutResID, int textViewResId) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResourceID = layoutResID;
		this.textviewResourceID = textViewResId;
		this.data = data;
	}
	private static class ElementHolder {
		public TextView tvUserName;
	}
	
	@Override
	public View getView(int position, View oldView, ViewGroup parentView) {
		View view = null;
		if (oldView == null) {
			view = inflater.inflate(this.layoutResourceID, null);
		}
		else view = oldView;
		final ContactUser elementData = this.getItem(position);

		if (elementData != null) {
			TextView textview = (TextView) view.findViewById(this.textviewResourceID);
			textview.setText(elementData.getName());
		}
		return view;
	}
}