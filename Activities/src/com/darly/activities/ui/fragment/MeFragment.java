/**
 * 2015年9月16日
 * MeFragment.java
 * com.darly.activities.ui.fragment
 * @auther Darly Fronch
 * 下午5:00:18
 * MeFragment
 * TODO
 */
package com.darly.activities.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.ToastApp;
import com.darly.activities.ui.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 MeFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午5:00:18 MeFragment TODO个人娱乐页面
 */
public class MeFragment extends BaseFragment implements OnItemSelectedListener {
	/**
	 * TODO根View
	 */
	private View rootView;
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	@ViewInject(R.id.ia_guide_city)
	private Spinner city;
	@ViewInject(R.id.ia_image_city)
	private ImageView iv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_me, container, false);// 关联布局文件
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
	@SuppressLint("NewApi")
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		title.setText(getClass().getSimpleName());
		String[] arra = getResources().getStringArray(R.array.degrees);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arra);
		city.setAdapter(adapter);
		city.setOnItemSelectedListener(this);
				
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub

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
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		iv.setAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.spinner_off));
		ToastApp.showToast(getActivity(), "onItemSelected");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		iv.setAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.spinner_off));
		ToastApp.showToast(getActivity(), "onNothingSelected");
	}

}
