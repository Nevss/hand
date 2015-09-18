package com.darly.activities.widget.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darly.activities.ui.R;
import com.darly.activities.widget.roundedimage.RoundedImageView;

/**
 * @author zhangyuhui
 * 	自定义的控件。实现了item实例。
 *	使用代码块进行对应的XML编写。使用代码更为优化。
 */
public class XadapterItem extends LinearLayout {

	public XadapterItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public XadapterItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private LinearLayout view;
	private RoundedImageView iv;
	private TextView tv;

	private void init(Context context) {
		view = new LinearLayout(context);
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.setGravity(Gravity.CENTER_VERTICAL);
		iv = new RoundedImageView(context);
		iv.setPadding(10, 10, 10, 10);

		LinearLayout secView = new LinearLayout(context);
		secView.setOrientation(LinearLayout.VERTICAL);
		secView.setGravity(Gravity.CENTER);

		ImageView seciv = new ImageView(context);
		seciv.setImageResource(R.drawable.xlistview_arrow);
		tv = new TextView(context);
		tv.setPadding(10, 10, 10, 10);
		tv.setSingleLine(true);

		secView.addView(tv);
		secView.addView(seciv);

		view.addView(iv);
		view.addView(secView);
	}

	public LinearLayout getView() {
		return view;
	}

	public RoundedImageView getIv() {
		return iv;
	}

	public TextView getTv() {
		return tv;
	}

}
