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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.darly.activities.adapter.FragmentAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.ui.R;
import com.darly.activities.ui.fragment.main.CaiyicaiFragment;
import com.darly.activities.ui.fragment.main.ChatFragment;
import com.darly.activities.ui.fragment.main.FragListener;
import com.darly.activities.ui.fragment.main.FriendFragment;
import com.darly.activities.ui.fragment.main.IndexFragment;
import com.darly.activities.ui.fragment.main.TuringFragment;
import com.darly.activities.widget.horilistview.HorizontalListView;
import com.darly.activities.widget.horilistview.HorizontalListViewAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 IndexFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午4:59:37 IndexFragment TODO娱乐首页下层框架
 *         当其进行滑动，或者点击的情况下，滑动问题解决，但点击后并没有出现和滑动对应的效果。进行调整；
 */
public class MainFragment extends BaseFragment implements OnPageChangeListener,
		FragListener, OnItemClickListener {
	private static final String TAG = "MainFragment";
	/**
	 * TODO根View
	 */
	private View rootView;
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	@ViewInject(R.id.fragment_main_virepager)
	private ViewPager viewpager;

	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	@ViewInject(R.id.fragment_main_list)
	private HorizontalListView hlist;

	private HorizontalListViewAdapter adapter;

	private int[] titles = new int[] { R.string.travel_one,
			R.string.travel_two, R.string.travel_free, R.string.travel_fou,
			R.string.travel_fiv };

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
		// radio.setOnCheckedChangeListener(this);
		title.setText(R.string.index);
		IndexFragment index = new IndexFragment();
		index.setTok(this);
		fragmentList.add(index);
		CaiyicaiFragment cai = new CaiyicaiFragment();
		cai.setTok(this);
		fragmentList.add(cai);
		FriendFragment fri = new FriendFragment();
		fri.setTok(this);
		fragmentList.add(fri);
		ChatFragment chat = new ChatFragment();
		chat.setTok(this);
		fragmentList.add(chat);
		TuringFragment tur = new TuringFragment();
		tur.setTok(this);
		fragmentList.add(tur);
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
		adapter = new HorizontalListViewAdapter(getActivity(), titles);
		hlist.setAdapter(adapter);
		hlist.setOnItemClickListener(this);
		adapter.setSelectIndex(0);
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
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		viewpager.setCurrentItem(position);
		adapter.setSelectIndex(position);
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(final int position) {
		// TODO Auto-generated method stub
		adapter.setSelectIndex(position);
		hlist.setSelection(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.fragment.main.FragListener#caiListener(java.lang
	 * .String)
	 */
	@Override
	public void caiListener(String msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.fragment.main.FragListener#chatListener(java.
	 * lang.String)
	 */
	@Override
	public void chatListener(String msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.fragment.main.FragListener#friListener(java.lang
	 * .String)
	 */
	@Override
	public void friListener(String msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.fragment.main.FragListener#indexListener(java
	 * .lang.String)
	 */
	@Override
	public void indexListener(String msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.fragment.main.FragListener#turListener(java.lang
	 * .String)
	 */
	@Override
	public void turListener(String msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, msg);
	}

}
