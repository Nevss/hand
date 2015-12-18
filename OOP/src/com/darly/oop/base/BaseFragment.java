/**
 * 下午2:33:01
 * @author zhangyh2
 * $
 * BaseActivity.java
 * TODO
 */
package com.darly.oop.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author zhangyh2 BaseActivity $ 下午2:33:01 TODO
 */
public abstract class BaseFragment extends Fragment {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initGlobalVariable();

		initView(savedInstanceState);

		loadData();

		initListener();

	}

	/**
	 * 
	 * 下午2:36:27
	 * 
	 * @author zhangyh2 BaseActivity.java TODO
	 *         初始化全局的一些变量。而且做好的静态变量。每个Activity里面的变量由自己来进行定义。
	 */
	private void initGlobalVariable() {
		// TODO Auto-generated method stub
		LogUtils.customTagPrefix = "oop"; // 方便调试时过滤 adb logcat 输出
		LogUtils.allowI = true; // 关闭 LogUtils.i(...) 的 adb log 输出
		ViewUtils.inject(getActivity());// 注入view和事件
	}

	/**
	 * @param savedInstanceState
	 *            下午2:34:08
	 * @author zhangyh2 BaseActivity.java TODO 初始化控件
	 */
	protected abstract void initView(Bundle savedInstanceState);

	/**
	 * 
	 * 下午2:34:10
	 * 
	 * @author zhangyh2 BaseActivity.java TODO 加载数据
	 */
	protected abstract void loadData();

	/**
	 * 
	 * 下午2:42:02
	 * 
	 * @author zhangyh2 BaseFragment.java TODO 初始化坚挺事件
	 */
	protected abstract void initListener();

		
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
