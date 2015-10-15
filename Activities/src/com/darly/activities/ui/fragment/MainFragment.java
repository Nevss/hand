/**
 * 2015年9月16日
 * IndexFragment.java
 * com.darly.activities.ui.fragment
 * @auther Darly Fronch
 * 下午4:59:37
 * IndexFragment
 * TODO
 */
package com.darly.activities.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.darly.activities.adapter.FragmentAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.Literal;
import com.darly.activities.ui.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 IndexFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午4:59:37 IndexFragment TODO娱乐首页下层框架
 */
public class MainFragment extends BaseFragment implements
		OnCheckedChangeListener, OnPageChangeListener {
	/**
	 * TODO根View
	 */
	private View rootView;
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	@ViewInject(R.id.fragment_header_horizontal)
	private HorizontalScrollView scroll;

	@ViewInject(R.id.fragment_main_virepager)
	private ViewPager viewpager;
	@ViewInject(R.id.fragment_header_radio)
	private RadioGroup radio;
	@ViewInject(R.id.fragment_header_showone)
	private RadioButton one;
	@ViewInject(R.id.fragment_header_showtwo)
	private RadioButton two;
	@ViewInject(R.id.fragment_header_showthree)
	private RadioButton thr;
	@ViewInject(R.id.fragment_header_showfour)
	private RadioButton fou;
	@ViewInject(R.id.fragment_header_showfive)
	private RadioButton fiv;

	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	private boolean isScroll;
	private boolean left;
	private int lastValue = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main, container, false);// 关联布局文件
		ViewUtils.inject(this, rootView); // 注入view和事件
		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		radio.setOnCheckedChangeListener(this);
		fragmentList.add(new IndexFragment());
		fragmentList.add(new FriendFragment());
		fragmentList.add(new ContactsFragment());
		fragmentList.add(new ChatFragment());
		fragmentList.add(new IndexFragment());
		mFragmentAdapter = new FragmentAdapter(getActivity()
				.getSupportFragmentManager(), fragmentList);
		viewpager.setAdapter(mFragmentAdapter);
		viewpager.setCurrentItem(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		viewpager.setOnPageChangeListener(this);
		one.setLayoutParams(new RadioGroup.LayoutParams(Literal.width / 4,
				LayoutParams.MATCH_PARENT));
		two.setLayoutParams(new RadioGroup.LayoutParams(Literal.width / 4,
				LayoutParams.MATCH_PARENT));
		thr.setLayoutParams(new RadioGroup.LayoutParams(Literal.width / 4,
				LayoutParams.MATCH_PARENT));
		fou.setLayoutParams(new RadioGroup.LayoutParams(Literal.width / 4,
				LayoutParams.MATCH_PARENT));
		fiv.setLayoutParams(new RadioGroup.LayoutParams(Literal.width / 4,
				LayoutParams.MATCH_PARENT));
		one.setChecked(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		resetRaidoButton();
		switch (checkedId) {
		case R.id.fragment_header_showone:
			one.setTextColor(getResources().getColor(R.color.main_bottom_text));
			viewpager.setCurrentItem(0);
			break;
		case R.id.fragment_header_showtwo:
			two.setTextColor(getResources().getColor(R.color.main_bottom_text));
			viewpager.setCurrentItem(1);
			break;
		case R.id.fragment_header_showthree:
			thr.setTextColor(getResources().getColor(R.color.main_bottom_text));
			viewpager.setCurrentItem(2);
			break;
		case R.id.fragment_header_showfour:
			fou.setTextColor(getResources().getColor(R.color.main_bottom_text));
			viewpager.setCurrentItem(3);
			break;
		case R.id.fragment_header_showfive:
			fiv.setTextColor(getResources().getColor(R.color.main_bottom_text));
			viewpager.setCurrentItem(4);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 下午3:36:10
	 * 
	 * @author Zhangyuhui MainFragment.java TODO
	 */
	private void resetRaidoButton() {
		// TODO Auto-generated method stub
		one.setTextColor(getResources().getColor(R.color.set_list_line));
		two.setTextColor(getResources().getColor(R.color.set_list_line));
		thr.setTextColor(getResources().getColor(R.color.set_list_line));
		fou.setTextColor(getResources().getColor(R.color.set_list_line));
		fiv.setTextColor(getResources().getColor(R.color.set_list_line));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		if (state == 1) {
			isScroll = true;
		} else {
			isScroll = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int position, float offset, int offsetPixels) {
		// TODO Auto-generated method stub
		if (isScroll) {
			if (lastValue > offsetPixels) {
				// 递减，向右侧滑动
				left = false;
			} else if (lastValue < offsetPixels) {
				// 递减，向右侧滑动
				left = true;
			} else if (lastValue == offsetPixels) {
				left = false;
			}
		}
		lastValue = offsetPixels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		resetRaidoButton();
		switch (position) {
		case 0:
			radio.getChildAt(R.id.fragment_header_showone);
			one.setTextColor(getResources().getColor(R.color.main_bottom_text));
			break;
		case 1:
			radio.getChildAt(R.id.fragment_header_showtwo);
			two.setTextColor(getResources().getColor(R.color.main_bottom_text));
			break;
		case 2:
			radio.getChildAt(R.id.fragment_header_showthree);
			thr.setTextColor(getResources().getColor(R.color.main_bottom_text));
			break;
		case 3:
			radio.getChildAt(R.id.fragment_header_showfour);
			fou.setTextColor(getResources().getColor(R.color.main_bottom_text));
			break;
		case 4:
			radio.getChildAt(R.id.fragment_header_showfive);
			fiv.setTextColor(getResources().getColor(R.color.main_bottom_text));
			break;

		default:
			break;
		}
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (left) {
					scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				} else {
					scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);
				}
			}
		});
	}

}
