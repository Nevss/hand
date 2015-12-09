/**
 * 上午11:34:10
 * @author Zhangyuhui
 * $
 * HomeFragment.java
 * TODO
 */
package com.darly.activities.ui.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.darly.activities.R;
import com.darly.activities.app.Constract;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.ToastApp;
import com.darly.activities.widget.carousel.Carousel;
import com.darly.activities.widget.carousel.ImageHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Zhangyuhui HomeFragment $ 上午11:34:10 TODO
 */
public class HomeFragment extends BaseFragment {
	private View rootView;
	@ViewInject(R.id.main_header_text)
	private TextView title;
	@ViewInject(R.id.main_header_relative)
	private RelativeLayout header;
	@ViewInject(R.id.home_top_view_click)
	private LinearLayout top_click;
	@ViewInject(R.id.home_fragment_top_mark)
	private Button tips;
	@ViewInject(R.id.home_top_below_click)
	private LinearLayout top_bolew_click;
	@ViewInject(R.id.home_fragment_tjyy)
	private TableRow tjyy;
	@ViewInject(R.id.home_fragment_zndj)
	private TableRow zndj;
	@ViewInject(R.id.home_fragment_bgcx)
	private TableRow bgcx;
	@ViewInject(R.id.home_fragment_bgjd)
	private TableRow bgjd;
	@ViewInject(R.id.home_fragment_carousel)
	private LinearLayout cousel;

	/**
	 * TODO轮播开始循环使用的Handler
	 */
	WeakReference<HomeFragment> weak = new WeakReference<HomeFragment>(this);
	public ImageHandler<HomeFragment> imagehandler = new ImageHandler<HomeFragment>(
			weak);

	private static final String[] IMAGES = new String[] {
			"http://pic2.ooopic.com/01/01/17/53bOOOPIC4e.jpg",
			"http://pica.nipic.com/2007-11-14/20071114114452315_2.jpg" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.home_fragment, container, false);
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
		switch (v.getId()) {
		case R.id.home_top_view_click:
			// 顶部按钮点击
			break;
		case R.id.home_top_below_click:
			// 顶部下方按钮点击
			break;
		case R.id.home_fragment_tjyy:
			// 体检预约
			break;
		case R.id.home_fragment_zndj:
			// 智能导检
			break;
		case R.id.home_fragment_bgcx:
			// 报告查询
			break;
		case R.id.home_fragment_bgjd:
			// 报告解读
			break;

		default:
			break;
		}
		ToastApp.showToast(getActivity(), "点击按钮" + v.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(Constract.width / 2,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		tjyy.setLayoutParams(lp);
		zndj.setLayoutParams(lp);
		bgcx.setLayoutParams(lp);
		bgjd.setLayoutParams(lp);

		LayoutParams cou = new LayoutParams(Constract.width, Constract.height / 5);
		cousel.setLayoutParams(cou);

		title.setText(R.string.home_top_askd);
		header.setBackgroundResource(R.drawable.home_bg);

		tips.setText(R.string.home_fragment_free);
		tips.setBackgroundResource(R.drawable.bt_bg_green);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		top_click.setOnClickListener(this);
		top_bolew_click.setOnClickListener(this);
		tjyy.setOnClickListener(this);
		zndj.setOnClickListener(this);
		bgcx.setOnClickListener(this);
		bgjd.setOnClickListener(this);
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 0; i < IMAGES.length; i++) {
			data.add(IMAGES[i]);
		}
		Carousel<HomeFragment> carous = new Carousel<HomeFragment>(
				getActivity(), data, imageLoader, options, imagehandler);
		cousel.addView(carous.view);
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

}
