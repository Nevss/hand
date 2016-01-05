/**
 * 上午10:55:59
 * @author zhangyh2
 * $
 * LuckDrawAdapter.java
 * TODO
 */
package com.darly.oop.adapter;

import java.util.List;

import com.darly.oop.R;
import com.darly.oop.model.LuckDraw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author zhangyh2 LuckDrawAdapter $ 上午10:55:59 TODO
 */
public class LuckDrawAdapter extends ParentAdapter<LuckDraw> {

	/**
	 * @param data
	 * @param resID
	 * @param context
	 *            上午10:56:27
	 * @author zhangyh2 LuckDrawAdapter.java TODO
	 */
	public LuckDrawAdapter(List<LuckDraw> data, int resID, Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.adapter.ParentAdapter#HockView(int, android.view.View,
	 * android.view.ViewGroup, int, android.content.Context, java.lang.Object)
	 */
	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			Context context, LuckDraw t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker = new ViewHocker();
			hocker.tv = (TextView) view.findViewById(R.id.item_luck_text);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		hocker.tv.setText(t.getName());
		return view;
	}

	class ViewHocker {
		TextView tv;
	}
}
