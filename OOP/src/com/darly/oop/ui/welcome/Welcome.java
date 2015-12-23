/**
 * 上午11:20:38
 * @author zhangyh2
 * $
 * Welcome.java
 * TODO
 */
package com.darly.oop.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.common.SharePreferHelp;
import com.darly.oop.ui.MainActivity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zhangyh2 Welcome $ 上午11:20:38 TODO
 */
@ContentView(R.layout.activity_welcome)
public class Welcome extends BaseActivity implements AnimationListener {

	@ViewInject(R.id.welcome_iv)
	private View view;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Animation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(3000);
		animation.setFillAfter(true);
		view.setAnimation(animation);
		animation.setAnimationListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#loadData()
	 */
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * 下午3:37:03
	 * 
	 * @author zhangyh2 GuideAnim.java TODO
	 */
	private void intoMain() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	/**
	 * 
	 * 上午11:23:10
	 * 
	 * @author zhangyh2 Welcome.java TODO
	 */
	private void intoGuide() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, GuideAnim.class));
		finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.animation.Animation.AnimationListener#onAnimationStart(android
	 * .view.animation.Animation)
	 */
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.animation.Animation.AnimationListener#onAnimationEnd(android
	 * .view.animation.Animation)
	 */
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		boolean isFirstCome = SharePreferHelp.getValue(
				APPEnum.ISFIRSTCOME.getDec(), true);
		if (isFirstCome) {
			// 第一次使用
			intoGuide();
		} else {
			// 直接进入MainActivity
			intoMain();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.animation.Animation.AnimationListener#onAnimationRepeat(
	 * android.view.animation.Animation)
	 */
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}
