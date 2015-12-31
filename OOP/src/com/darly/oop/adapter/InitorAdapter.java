/**
 * 上午11:11:49
 * @author zhangyh2
 * $
 * InitorAdapter.java
 * TODO
 */
package com.darly.oop.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.ui.fragment.MainFragment;
import com.darly.oop.widget.viewpager.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

/**
 * @author zhangyh2 InitorAdapter $ 上午11:11:49 TODO
 */
public class InitorAdapter extends IndicatorFragmentPagerAdapter {

	private List<String> names;
	private LayoutInflater inflate;

	/**
	 * @param fragmentManager
	 *            上午11:11:54
	 * @author zhangyh2 InitorAdapter.java TODO
	 */
	public InitorAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		// TODO Auto-generated constructor stub
	}

	public InitorAdapter(FragmentManager fragmentManager, List<String> names,
			LayoutInflater inflate) {
		super(fragmentManager);
		this.names = names;
		this.inflate = inflate;
	}

	@Override
	public int getCount() {
		return names != null ? names.size() : 0;
	}

	@Override
	public View getViewForTab(int position, View convertView,
			ViewGroup container) {
		if (convertView == null) {
			convertView = inflate.inflate(R.layout.tab_top, container, false);
		}
		TextView textView = (TextView) convertView;
		textView.setText(names.get(position));
		textView.setPadding(20, 0, 20, 0);
		return convertView;
	}

	@Override
	public Fragment getFragmentForPage(int position) {
		MainFragment fragment = new MainFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(MainFragment.INTENT_INT_INDEX, position);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

}
