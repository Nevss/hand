package com.darly.activities.widget.load;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.darly.activities.ui.R;

public class ProgressDialogUtil extends Dialog {
	protected Context context;

	public ProgressDialogUtil(Context context) {
		super(context, R.style.Custom_Progress);
		this.context = context;
		init();
	}

	public ProgressDialogUtil(Context context, int theme) {
		super(context, theme);
		this.context = context;
		init();
	}

	/**
	 * 当窗口焦点改变时调用
	 * 
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
		// 获取ImageView上的动画背景
		AnimationDrawable spinner = (AnimationDrawable) imageView
				.getBackground();
		// 开始动画
		spinner.start();
	}

	/**
	 * 给Dialog设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(CharSequence message) {
		if (message != null && message.length() > 0) {
			TextView txt = (TextView) findViewById(R.id.message);
			txt.setVisibility(View.VISIBLE);
			txt.setText(message);
			txt.invalidate();
		}
	}

	private void init() {
		setContentView(R.layout.progress_custom);
		// 按返回键是否取消
		setCanceledOnTouchOutside(false);
		// setCancelable(false);
		// 监听返回键处理
		// setOnCancelListener(cancelListener);
		// 设置居中
		getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.2f;
		getWindow().setAttributes(lp);
	}

}
