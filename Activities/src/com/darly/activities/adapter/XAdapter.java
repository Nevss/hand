package com.darly.activities.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.HomtFragmentBase;
import com.darly.activities.model.HomtFragmentModel;
import com.darly.activities.ui.R;
import com.darly.activities.widget.item.XadapterItem;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class XAdapter extends ParentAdapter<HomtFragmentBase> {

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public XAdapter(List<HomtFragmentBase> data, int resID, Context context,
			ImageLoader imageLoader, DisplayImageOptions options) {
		super(data, resID, context);
		this.data = data;
		this.context = context;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	public void setData(ArrayList<HomtFragmentBase> data) {
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
			final Context context, HomtFragmentBase t) {
		// TODO Auto-generated method stub
		
		if (t.getData() != null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.fragment_index_item, null);
			LinearLayout layout = (LinearLayout) view
					.findViewById(R.id.fragment_index_item_linear);
			LayoutParams lp = new LayoutParams(Literal.width / 4, Literal.width / 4);
			for (final HomtFragmentModel model : t.getData()) {
				RoundedImageView iv = new RoundedImageView(context);
				iv.setLayoutParams(lp);
				iv.setScaleType(ScaleType.FIT_XY);
				lp.setMargins(10, 10, 10, 10);
				iv.setCornerRadius(20f);
				iv.setBorderWidth(2f);
				iv.setBorderColor(context.getResources().getColor(R.color.roundedimageview_color));
				imageLoader.displayImage(model.getUrl(), iv, options);
				iv.setClickable(true);
				
				
				
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ToastApp.showToast(context, model.getName());
					}
				});
				layout.addView(iv);
			}

		} else {
			XadapterItem item = new XadapterItem(context);
			view = item.getView();
			RoundedImageView iv = item.getIv();
			TextView tv = item.getTv();
			imageLoader.displayImage(t.getUrl(), iv,options);
			tv.setText(t.getName());
			
		}

		return view;
	}

}
