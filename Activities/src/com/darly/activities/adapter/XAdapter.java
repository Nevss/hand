package com.darly.activities.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.darly.activities.common.Literal;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.HomeFragmentModel;
import com.darly.activities.model.HomeSingleOne;
import com.darly.activities.ui.R;
import com.darly.activities.widget.item.XadapterItem;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class XAdapter extends ParentAdapter<HomeFragmentModel> {

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public XAdapter(List<HomeFragmentModel> data, int resID, Context context,
			ImageLoader imageLoader, DisplayImageOptions options) {
		super(data, resID, context);
		this.data = data;
		this.context = context;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	public void setData(ArrayList<HomeFragmentModel> data) {
		this.data = data;
		notifyDataSetChanged();
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
			final Context context, final HomeFragmentModel t) {
		// TODO Auto-generated method stub

		// 在这里实现分组功能模块。
		if (t.getSingle() != null) {
			// 单列
			XadapterItem item = new XadapterItem(context);
			view = item.getView();
			item.getIv().setLayoutParams(
					new LayoutParams(Literal.width / 5, Literal.width / 5));
			item.getIv().setBorderWidth(R.dimen.roundedimageview_border);
			item.getIv().setBorderColor(
					context.getResources().getColor(
							R.color.roundedimageview_no_color));
			item.getIv().setCornerRadius(100);
			item.getIv().setScaleType(ScaleType.CENTER);
			item.getTv().setText(t.getSingle().getName());
			imageLoader.displayImage(t.getSingle().getUrl(), item.getIv(),
					options);
		}
		if (t.getmore() != null) {
			// 多列
			view = LayoutInflater.from(context).inflate(R.layout.xlist_item,
					null);
			LinearLayout linear = (LinearLayout) view
					.findViewById(R.id.xlist_item_linear);
			for (int i = 0, len = t.getmore().size(); i < len; i++) {
				RoundedImageView iv = new RoundedImageView(context);
				iv.setLayoutParams(new LayoutParams(Literal.width / 5,
						Literal.width / 5));
				iv.setBorderWidth(R.dimen.roundedimageview_border);
				iv.setBorderColor(context.getResources().getColor(
						R.color.roundedimageview_color));
				iv.setCornerRadius(10);
				iv.setScaleType(ScaleType.CENTER_CROP);

				final HomeSingleOne one = t.getmore().get(i);
				imageLoader.displayImage(one.getUrl(), iv, options);
				iv.setClickable(true);
				linear.addView(iv);

				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ToastApp.showToast(context, one.getName());
					}
				});

			}
		}
		return view;
	}

}
