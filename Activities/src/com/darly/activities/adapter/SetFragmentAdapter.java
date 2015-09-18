package com.darly.activities.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.darly.activities.model.SetFragmentModel;
import com.darly.activities.ui.R;

/**
 * 2015年9月17日 SetFragmentAdapter.java com.darly.activities.adapter
 * 
 * @auther Darly Fronch 上午10:21:39 SetFragmentAdapter TODO
 */
public class SetFragmentAdapter extends ParentAdapter<SetFragmentModel> {

	/**
	 * @param data
	 * @param resID
	 * @param context
	 */
	public SetFragmentAdapter(List<SetFragmentModel> data, int resID,
			Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.adapter.ParentAdapter#HockView(int,
	 * android.view.View, android.view.ViewGroup, int, android.content.Context,
	 * java.lang.Object)
	 */
	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			Context context, SetFragmentModel t) {
		// TODO Auto-generated method stub
		ViewHocker hocker;
		if (view == null) {
			hocker = new ViewHocker();
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker.iv = (ImageView) view.findViewById(R.id.item_image);
			hocker.tv = (TextView) view.findViewById(R.id.item_text);
			view.setTag(hocker);
		}else {
			hocker = (ViewHocker) view.getTag();
		}
		
		hocker.iv.setImageResource(t.getResid());
		hocker.tv.setText(t.getName());
		
		return view;
	}
	
	
	class ViewHocker{
		ImageView iv;
		TextView tv;
	}
}
