package com.darly.oop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.darly.oop.ui.plug.PlugsModel;

/**
 * 显示已安装插件Adapter
 * 
 * @author 梁前武 www.apkplug.com
 */
public class PlugBundleAdapter extends ParentAdapter<PlugsModel> {

	/**
	 * @param data
	 * @param resID
	 * @param context
	 *            下午2:23:46
	 * @author zhangyh2 ListBundleAdapter.java TODO
	 */
	public PlugBundleAdapter(List<PlugsModel> data, int resID, Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	private final class ListViewHolder {
		/**
		 * 下午2:31:05 TODO 插件图标
		 */
		public ImageView icon;
		/**
		 * 下午2:31:35 TODO 插件名
		 */
		public TextView plugName;
		/**
		 * 下午2:32:16 TODO 插件信息
		 */
		public TextView plugInfo;
		/**
		 * 下午2:32:42 TODO 下载安装打开插件
		 */
		public TextView plugStart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.adapter.ParentAdapter#HockView(int, android.view.View,
	 * android.view.ViewGroup, int, android.content.Context, java.lang.Object)
	 */
	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			final Context context, final PlugsModel t) {
		// TODO Auto-generated method stub
		final ListViewHolder viewHolder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(resID, null);
			viewHolder = new ListViewHolder();
			viewHolder.icon = (ImageView) view.findViewById(R.id.image_item_1);
			viewHolder.icon.setLayoutParams(new LayoutParams(APPEnum.WIDTH
					.getLen() / 6, APPEnum.WIDTH.getLen() / 6));
			viewHolder.plugName = (TextView) view
					.findViewById(R.id.text_item_1);
			viewHolder.plugInfo = (TextView) view
					.findViewById(R.id.text_item_2);
			viewHolder.plugStart = (TextView) view
					.findViewById(R.id.text_item_4);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ListViewHolder) view.getTag();
		}

		if (t.install && t.bundle != null) {
			if (t.bundle.getBundle_icon() != null) {
				viewHolder.icon.setImageBitmap(t.bundle.getBundle_icon());
			}
			viewHolder.plugName.setText(t.bundle.getName());
			viewHolder.plugInfo.setText(t.bundle.getVersion()
					+ t.bundle.getLocation());
			viewHolder.plugStart.setText("运行");
		} else {
			viewHolder.icon.setImageBitmap(null);
			viewHolder.plugName.setText(t.name);
			viewHolder.plugInfo.setText("新功能点击安装");
		}
		return view;
	}

}
