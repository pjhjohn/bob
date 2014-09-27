package com.appspot.wecookbob.contact;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.contact.BobLog.NotificationType;


public class BobLogListviewAdapter extends ArrayAdapter<BobLog> {
	private LayoutInflater inflater;
	private Context context;
	ArrayList<BobLog> data;
	
	public BobLogListviewAdapter(Context context, ArrayList<BobLog> data, int layoutResID, int textViewResId1) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.context = context;
	}
	private static class ElementHolder {
		public TextView tvBobtnerName;
	}
	
	@Override
	public View getView(int position, View oldView, ViewGroup parentView) {
		View view = null;
		if (oldView == null) {
			view = inflater.inflate(R.layout.bob_log_list_item, null);
		}
		else view = oldView;
		
		final BobLog elementData = this.getItem(position);

		if (elementData != null) {
			TextView textview1 = (TextView) view.findViewById(R.id.bobtner_name);
//			TextView textview2 = (TextView) view.findViewById(R.id.request_time);
			textview1.setText(elementData.getBobtnerName());
//			textview2.setText(elementData.getBobRequestTime()+"");
		}
		

		ImageButton btn_bob = (ImageButton) view.findViewById(R.id.btn_bob);
		ImageButton btn_call = (ImageButton) view.findViewById(R.id.btn_call);

		if (System.currentTimeMillis() - elementData.bobRequestTime < 1000*60*3){
			btn_bob.setBackgroundResource(R.drawable.bob_requested);
			if(elementData.getNotificationType() == NotificationType.RECEIVED){
				btn_call.setBackgroundResource(R.drawable.btn_can_call);
			}else{
				btn_call.setBackgroundResource(R.drawable.btn_empty);
			}
			
		}else{
			btn_bob.setBackgroundResource(R.drawable.send_bob_request);
			btn_call.setBackgroundResource(R.drawable.btn_empty);

		}
		BobLogListviewAdapter.this.sort(BobLog.COMPARE_BY_BOBREQUESTTIME);

		btn_bob.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				String bobtnerId = elementData.getBobtnerId();
				String bobtnerName = elementData.getBobtnerName();
				elementData.bobRequestTime = System.currentTimeMillis();
				elementData.type = NotificationType.SENT;
						
				BobLogListviewAdapter.this.sort(BobLog.COMPARE_BY_BOBREQUESTTIME);
				BobLogListviewAdapter.this.notifyDataSetChanged();
				
				
				Toast.makeText(BobLogListviewAdapter.this.context.getApplicationContext(), bobtnerName + "에게\n" + elementData.getBobRequestTime() + "에 요청 보냄",
                        Toast.LENGTH_LONG).show();
			}
		});
		

		
		
		return view;
	}


}