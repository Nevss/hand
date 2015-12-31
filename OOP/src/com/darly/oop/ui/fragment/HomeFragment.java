/**
 * 上午11:34:10
 * @author Zhangyuhui
 * $
 * HomeFragment.java
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
 * @author Zhangyuhui HomeFragment $ 上午11:34:10 TODO
 */
public class HomeFragment extends BaseFragment {

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
		tv.setText("HomeFragment");
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
