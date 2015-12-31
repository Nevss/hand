/**
 * 2015年9月16日
 * MeFragment.java
 * com.darly.activities.ui.fragment
 * @auther Darly Fronch
 * 下午5:00:18
 * MeFragment
 * TODO
 */
package com.darly.oop.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.base.BaseFragment;

/**
 * 2015年9月16日 MeFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午5:00:18 MeFragment TODO个人娱乐页面
 */

public class MeFragment extends BaseFragment {
	View rootView;
	TextView tv;

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
		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseFragment#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		tv = (TextView) rootView.findViewById(R.id.fragment_tv);
		tv.setText("MeFragment");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseFragment#loadData()
	 */
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseFragment#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub

	}

}
