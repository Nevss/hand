package com.darly.oop.adapter;

import java.util.List;

import com.darly.oop.R;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {
	private List<View> list;

	public ImageAdapter(List<View> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list.size() != 0) {
			return list.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		switch (position) {
		case 0:
			ViewShowAnim(list.get(position));
			break;
		case 1:
			ViewSecAnim(list.get(position));
			break;
		case 2:
			ViewDownAnim(list.get(position));
			break;

		default:
			break;
		}

		container.addView(list.get(position));
		return list.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(list.get(position));
	}

	/**
	 * 
	 * 下午4:35:36
	 * 
	 * @author zhangyh2 GuideAnim.java TODO 第一张动画效果
	 * @param view
	 */
	private void ViewShowAnim(View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.anim_iv);
		Animation showAnim = new RotateAnimation(0f, 359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		showAnim.setRepeatMode(Animation.RESTART);
		showAnim.setRepeatCount(Animation.INFINITE);
		showAnim.setInterpolator(new LinearInterpolator());
		showAnim.setDuration(5000);
		showAnim.setFillAfter(true);
		iv.setAnimation(showAnim);
	}

	/**
	 * @param view
	 * 下午5:52:04
	 * @author zhangyh2
	 * ImageAdapter.java
	 * TODO 第二张动画效果
	 */
	private void ViewSecAnim(View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.anim_sev);
		Animation secAnim = new RotateAnimation(359f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		secAnim.setRepeatMode(Animation.RESTART);
		secAnim.setRepeatCount(Animation.INFINITE);
		secAnim.setInterpolator(new LinearInterpolator());
		secAnim.setFillAfter(true);
		secAnim.setDuration(5000);
		iv.setAnimation(secAnim);
	}

	/**
	 * @param view
	 * 下午5:52:14
	 * @author zhangyh2
	 * ImageAdapter.java
	 * TODO 第三张动画效果
	 */
	private void ViewDownAnim(View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.anim_down);
		Animation downAnim = new RotateAnimation(359f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		downAnim.setRepeatMode(Animation.RESTART);
		downAnim.setRepeatCount(Animation.INFINITE);
		downAnim.setInterpolator(new LinearInterpolator());
		downAnim.setFillAfter(true);
		downAnim.setDuration(2000);
		iv.setAnimation(downAnim);
	}

}