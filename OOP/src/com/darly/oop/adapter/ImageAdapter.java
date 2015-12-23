package com.darly.oop.adapter;

import java.util.List;

import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;

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
		showAnim.setDuration(30000);
		showAnim.setFillAfter(true);
		iv.setAnimation(showAnim);

		LinearLayout big = (LinearLayout) view.findViewById(R.id.anim_big);
		Animation bigAnim = new RotateAnimation(0f, 359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		bigAnim.setRepeatMode(Animation.RESTART);
		bigAnim.setRepeatCount(Animation.INFINITE);
		bigAnim.setInterpolator(new LinearInterpolator());
		bigAnim.setDuration(60000);
		bigAnim.setFillAfter(true);
		big.setAnimation(bigAnim);
		LinearLayout small = (LinearLayout) view.findViewById(R.id.anim_small);
		Animation smallAnim = new RotateAnimation(359f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		smallAnim.setRepeatMode(Animation.RESTART);
		smallAnim.setRepeatCount(Animation.INFINITE);
		smallAnim.setInterpolator(new LinearInterpolator());
		smallAnim.setDuration(15000);
		smallAnim.setFillAfter(true);
		small.setAnimation(smallAnim);

		TextView v1 = (TextView) view.findViewById(R.id.anim_v1_text);
		Animation animation = new AlphaAnimation(0f, 1f);
		animation.setDuration(5000);
		animation.setFillAfter(true);
		v1.setAnimation(animation);
	}

	/**
	 * @param view
	 *            下午5:52:04
	 * @author zhangyh2 ImageAdapter.java TODO 第二张动画效果
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
	 *            下午5:52:14
	 * @author zhangyh2 ImageAdapter.java TODO 第三张动画效果
	 */
	private void ViewDownAnim(View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.anim_ship);
		Animation downAnims = new TranslateAnimation(0, APPEnum.WIDTH.getLen(),
				0, 0);
		downAnims.setRepeatCount(Animation.INFINITE);
		downAnims.setRepeatMode(Animation.REVERSE);
		downAnims.setFillAfter(true);
		downAnims.setDuration(15000);
		iv.setAnimation(downAnims);
		ImageView wave = (ImageView) view.findViewById(R.id.anim_wave);
		AnimationDrawable drawable = (AnimationDrawable) wave.getBackground();
		drawable.start();
		
		TextView v3 = (TextView) view.findViewById(R.id.anim_v3_text);
		Animation animation = new AlphaAnimation(0f, 1f);
		animation.setDuration(5000);
		animation.setFillAfter(true);
		v3.setAnimation(animation);

	}

}