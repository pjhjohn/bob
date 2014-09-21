package trash;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.wecookbob.R;
import com.appspot.wecookbob.custom.ContactUserData;


public class SendFirstBobAdapter extends ArrayAdapter<ContactUserData> {
	private LayoutInflater inflater;
	
	public SendFirstBobAdapter(Context context, ArrayList<ContactUserData> data) {
		super(context, 0, data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View oldView, ViewGroup parentView) {
		View view = null;
		if (oldView == null) view = inflater.inflate(R.layout.send_first_bob_list_item, null);
		else view = oldView;
		final ContactUserData elementData = this.getItem(position);

		if (elementData != null) {
			TextView textview = (TextView) view.findViewById(R.id.invite_friend_name);
			textview.setText(elementData.getUserName());
		}
		return view;
	}
}