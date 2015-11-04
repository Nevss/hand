/**
 * 下午3:57:43
 * @author Zhangyuhui
 * ChatAdapter.java
 * TODO
 */
package com.darly.activities.adapter;

import java.util.List;

import com.darly.activities.app.Constract;
import com.darly.activities.model.GirlBase;
import com.darly.activities.ui.R;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author Zhangyuhui ChatAdapter 下午3:57:43 TODO
 */
public class ChatAdapter extends ParentAdapter<GirlBase> {

	private ImageLoader imageLoader;

	private DisplayImageOptions options;

	/**
	 * @param data
	 * @param resID
	 * @param context
	 * @param imageLoader
	 * @param options
	 *            下午4:04:13
	 * @author Zhangyuhui ChatAdapter.java TODO
	 */
	public ChatAdapter(List<GirlBase> data, int resID, Context context,
			ImageLoader imageLoader, DisplayImageOptions options) {
		super(data, resID, context);
		this.imageLoader = imageLoader;
		this.options = options;
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
			Context context, GirlBase t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker = new ViewHocker();
			hocker.iv = (RoundedImageView) view
					.findViewById(R.id.chat_fragment_iv);
			hocker.iv.setLayoutParams(new LayoutParams(Constract.width / 4,
					Constract.width / 4));
			hocker.name = (TextView) view.findViewById(R.id.chat_fragment_name);
			hocker.descrp = (TextView) view
					.findViewById(R.id.chat_fragment_descrp);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}

		if (t.getPicUrl() != null) {
			imageLoader.displayImage(t.getPicUrl(), hocker.iv, options);
		}
		hocker.name.setText(t.getTitle());
		hocker.descrp.setText(t.getDescription());

		return view;
	}

	class ViewHocker {
		RoundedImageView iv;
		TextView name;
		TextView descrp;
	}

}
