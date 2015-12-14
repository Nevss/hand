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

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.darly.activities.R;
import com.darly.activities.adapter.GridViewAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.BaseData;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.model.GridViewData;
import com.darly.activities.ui.fragment.me.MeDetailsAcitvity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 MeFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午5:00:18 MeFragment TODO个人娱乐页面
 */
public class MeFragment extends BaseFragment implements OnItemClickListener {
	private static final String TAG = "MeFragment";
	/**
	 * TODO根View
	 */
	private View rootView;

	/**
	 * 下午4:05:17
	 * 
	 * @author Zhangyuhui MeFragment.java TODO 第二个展示页面。
	 */
	@ViewInject(R.id.me_fragment_grid)
	private GridView grid;

	private ArrayList<GridViewData> gridData;
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;

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
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		title.setText(getClass().getSimpleName());
		getData();
	}

	/**
	 * 
	 * 下午4:09:35
	 * 
	 * @author Zhangyuhui MeFragment.java TODO 获取GridView数据
	 */
	private void getData() {
		// TODO Auto-generated method stub
		gridData = new ArrayList<GridViewData>();
		for (int i = 0; i < BaseData.IMAGES.length; i++) {
			gridData.add(new GridViewData(i, BaseData.IMAGES[i]));
		}
		grid.setAdapter(new GridViewAdapter(gridData,
				R.layout.item_fragment_gridview, getActivity()));
		grid.setOnItemClickListener(this);
		LogFileHelper.getInstance().i(TAG, "MeFragment is running");
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub点击跳入详情。并附带留言效果。
		Intent intent = new Intent(getActivity(), MeDetailsAcitvity.class);
		intent.putParcelableArrayListExtra("GridViewData", gridData);
		startActivity(intent);

	}

}
