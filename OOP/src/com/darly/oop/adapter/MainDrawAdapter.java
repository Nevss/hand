/**
 * 下午2:51:49
 * @author zhangyh2
 * $
 * MainAdapter.java
 * TODO
 */
package com.darly.oop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.darly.oop.model.Menu;

/**
 * @author zhangyh2 MainAdapter $ 下午2:51:49 TODO
 */
public class MainDrawAdapter extends ParentAdapter<Menu> {

	/**
	 * @param data
	 * @param resID
	 * @param context
	 *            下午2:52:10
	 * @author zhangyh2 MainAdapter.java TODO
	 */
	public MainDrawAdapter(List<Menu> data, int resID, Context context) {
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
			Context context, Menu t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker = new ViewHocker();

			hocker.titles = (LinearLayout) view
					.findViewById(R.id.item_draw_title);
			hocker.titlename = (TextView) view
					.findViewById(R.id.item_draw_titlename);
			hocker.views = (LinearLayout) view
					.findViewById(R.id.item_draw_view);
			hocker.viewiv = (ImageView) view
					.findViewById(R.id.item_draw_viewiv);
			hocker.viewname = (TextView) view
					.findViewById(R.id.item_draw_viewname);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		switch (t.type) {
		case APPEnum.ITEMTITLE:
			hocker.titles.setVisibility(View.VISIBLE);
			hocker.viewiv.setVisibility(View.GONE);
			hocker.titlename.setText(t.title);
			break;
		case APPEnum.ITEMVIEW:
			hocker.titles.setVisibility(View.GONE);
			hocker.viewiv.setVisibility(View.VISIBLE);
			hocker.viewiv.setImageResource(t.tops.imageRes);
			hocker.viewname.setText(t.tops.name);
			if (t.isSelect) {
				hocker.views.setBackgroundColor(context.getResources()
						.getColor(R.color.back_color));
			} else {
				hocker.views.setBackgroundColor(context.getResources()
						.getColor(R.color.white));
			}
			break;
		default:
			break;
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		if (getItem(position).type == APPEnum.ITEMTITLE) {
			return false;
		}
		return super.isEnabled(position);
	}

	class ViewHocker {
		LinearLayout titles;
		TextView titlename;
		LinearLayout views;
		ImageView viewiv;
		TextView viewname;
	}

}
