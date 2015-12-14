/**
 * 下午4:42:01
 * @author Zhangyuhui
 * BottomGridView.java
 * TODO
 */
package com.darly.activities.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darly.activities.R;
import com.darly.activities.model.BottomModel;
import com.darly.activities.widget.roundedimage.RoundedImageView;

/**
 * @author Zhangyuhui BottomGridView 下午4:42:01 TODO
 */
public class BottomGridViewAdapter extends ParentAdapter<BottomModel> {

	/**
	 * @param data
	 * @param resID
	 * @param context
	 *            下午4:42:54
	 * @author Zhangyuhui BottomGridViewAdapter.java TODO
	 */
	public BottomGridViewAdapter(List<BottomModel> data, int resID,
			Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.adapter.ParentAdapter#HockView(int,
	 * android.view.View, android.view.ViewGroup, BottomModel,
	 * android.content.Context, java.lang.Object)
	 */
	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			final Context context, final BottomModel t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker = new ViewHocker();
			hocker.iv = (RoundedImageView) view
					.findViewById(R.id.item_bottom_iv_x);

			hocker.tv = (TextView) view.findViewById(R.id.item_bottom_tv_x);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		hocker.iv.setImageResource(t.getResid());
		if (t.getName() == null) {
			hocker.tv.setVisibility(View.GONE);
		}else {
			hocker.tv.setVisibility(View.VISIBLE);
		}
		hocker.tv.setText(t.getName());
		return view;
	}

	class ViewHocker {
		RoundedImageView iv;
		TextView tv;
	}
}
