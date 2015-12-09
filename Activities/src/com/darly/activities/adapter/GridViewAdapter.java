package com.darly.activities.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.R;
import com.darly.activities.app.Constract;
import com.darly.activities.model.GridViewData;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GridViewAdapter extends ParentAdapter<GridViewData> {

	private ImageLoader imageLoader = ImageLoader.getInstance();

	public GridViewAdapter(List<GridViewData> data, int resID, Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			Context context, GridViewData t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker = new ViewHocker();
			LayoutParams params = new LayoutParams(Constract.width / 2,
					Constract.width / 2);
			params.setMargins(2, 2, 2, 2);
			hocker.iv = (RoundedImageView) view.findViewById(R.id.item_grid_iv);
			hocker.iv.setLayoutParams(params);
			hocker.tv = (TextView) view.findViewById(R.id.item_grid_tv);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		imageLoader.displayImage(t.url, hocker.iv);
		hocker.tv.setText(t.url);
		return view;
	}

	class ViewHocker {
		RoundedImageView iv;
		TextView tv;
	}
}
