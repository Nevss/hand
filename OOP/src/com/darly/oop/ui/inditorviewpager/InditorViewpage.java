/**
 * 上午11:00:04
 * @author zhangyh2
 * $
 * InditorViewpage.java
 * TODO
 */
package com.darly.oop.ui.inditorviewpager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import com.darly.oop.R;
import com.darly.oop.adapter.InitorAdapter;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.widget.viewpager.indicator.IndicatorViewPager;
import com.darly.oop.widget.viewpager.indicator.ScrollIndicatorView;
import com.darly.oop.widget.viewpager.indicator.slidebar.ColorBar;
import com.darly.oop.widget.viewpager.indicator.transation.OnTransitionTextListener;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zhangyh2 InditorViewpage $ 上午11:00:04 TODO
 */
@ContentView(R.layout.activity_inditor)
public class InditorViewpage extends BaseActivity {

	@ViewInject(R.id.inditor_indicator)
	protected ScrollIndicatorView indicator;
	@ViewInject(R.id.inditor_viewPager)
	protected ViewPager viewPager;

	private IndicatorViewPager indicatorViewPager;

	private LayoutInflater inflate;

	private String[] names = { "CUPCAKE", "DONUT", "FROYO", "GINGERBREAD",
			"HONEYCOMB", "ICE CREAM SANDWICH", "JELLY BEAN", "KITKAT" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		indicator.setScrollBar(new ColorBar(this, Color.RED, 5));

		// 设置滚动监听
		indicator.setOnTransitionListener(new OnTransitionTextListener()
				.setColorId(this, R.color.umeng_socialize_text_share_content,
						R.color.back_color));

		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		inflate = LayoutInflater.from(getApplicationContext());

		List<String> datas = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			datas.add(names[i]);
		}

		indicatorViewPager.setAdapter(new InitorAdapter(
				getSupportFragmentManager(), datas, inflate));
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

}
