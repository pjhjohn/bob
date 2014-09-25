package com.appspot.wecookbob.lib;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.wecookbob.R;

public class SendFirstBobAdapter extends ArrayAdapter<SendFirstBobCData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;
		
		public SendFirstBobAdapter(Context context, ArrayList<SendFirstBobCData> object) {
			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

			if (v == null) {

				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.send_first_bob_list_item, null);
			} else {

				view = v;
			}

			// 자료를 받는다.
			final SendFirstBobCData data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.send_first_bob_friend_name);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getUserName());
			}
			return view;
		}
	}
