package com.darly.activities.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.common.Literal;
import com.darly.activities.model.HomtFragmentModel;
import com.darly.activities.ui.R;
import com.darly.activities.widget.item.XadapterItem;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class XAdapter extends ParentAdapter<HomtFragmentModel> {

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public XAdapter(List<HomtFragmentModel> data, int resID, Context context,
			ImageLoader imageLoader, DisplayImageOptions options) {
		super(data, resID, context);
		this.data = data;
		this.context = context;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	public void setData(ArrayList<HomtFragmentModel> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	class ViewHocker {
		TextView tv;
		RoundedImageView iv;
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
			Context context, HomtFragmentModel t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			hocker = new ViewHocker();
			// 方式二：使用对象思想进行编写。
			XadapterItem item = new XadapterItem(context);
			view = item.getView();
			hocker.tv = item.getTv();
			hocker.iv = item.getIv();
			hocker.iv.setLayoutParams(new LayoutParams(Literal.width / 5,
					Literal.width / 5));
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		if (position % 2 == 0) {
			hocker.iv.setBorderWidth(R.dimen.roundedimageview_border);
			hocker.iv.setBorderColor(context.getResources().getColor(
					R.color.roundedimageview_no_color));
			hocker.iv.setCornerRadius(100);
			hocker.iv.setScaleType(ScaleType.CENTER);
		} else {
			hocker.iv.setBorderWidth(R.dimen.roundedimageview_border);
			hocker.iv.setBorderColor(context.getResources().getColor(
					R.color.roundedimageview_color));
			hocker.iv.setCornerRadius(10);
			hocker.iv.setScaleType(ScaleType.CENTER_CROP);
		}
		hocker.tv.setText(t.getName());
		imageLoader.displayImage(t.getUrl(), hocker.iv, options);
		return view;
	}

}
