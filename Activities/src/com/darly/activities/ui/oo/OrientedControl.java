/**
 * 上午10:02:18
 * @author zhangyh2
 * $
 * OrientedControl.java
 * TODO
 */
package com.darly.activities.ui.oo;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.darly.activities.R;

/**
 * @author zhangyh2 OrientedControl $ 上午10:02:18 TODO
 */
public class OrientedControl implements OnClickListener, TextWatcher {

	private OrientedAcitvity base;

	private boolean isNext;


	public OrientedControl(OrientedAcitvity base) {
		this.base = base;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.oo.OrientedInterFace#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.oriented_btn:
			if (isNext) {
				isNext = false;
				base.bt.setText("版本测试");
			} else {
				isNext = true;
				base.bt.setText("反向测试");
			}
			break;

		case R.id.oriented_share:
			postShare();
			break;

		default:
			break;
		}
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		base.shareBoard.showAtLocation(base.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.oo.OrientedInterFace#afterTextChanged(android
	 * .text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Log.i(getClass().getSimpleName(), "afterTextChanged");
		base.bt.setText("afterTextChanged");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.oo.OrientedInterFace#beforeTextChanged(java.lang
	 * .CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		Log.i(getClass().getSimpleName(), "beforeTextChanged");
		base.bt.setText("beforeTextChanged");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.oo.OrientedInterFace#onTextChanged(java.lang.
	 * CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		Log.i(getClass().getSimpleName(), "onTextChanged");
		base.bt.setText("onTextChanged");
	}
}
