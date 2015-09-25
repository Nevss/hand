package com.darly.activities.ui;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.Literal;
import com.darly.activities.ui.fragment.IndexFragment;
import com.darly.activities.ui.fragment.MeFragment;
import com.darly.activities.ui.fragment.SetFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements
		OnCheckedChangeListener {
	/**
	 * TODO底部标签栏，主要功能负责切换三个Fragment，切换页面的功能。当然默认的是第一项选中状态。
	 */
	@ViewInject(R.id.main_bottom_group)
	private RadioGroup group;
	/**
	 * TODO第一个标签。
	 */
	@ViewInject(R.id.main_bottom_index)
	private RadioButton buttom;
	/**
	 * TODO首页展示效果哦Fragment
	 */
	private IndexFragment index;
	/**
	 * TODO用户自己页面展示Fragment
	 */
	private MeFragment me;
	/**
	 * TODO用户信息设置Fragment
	 */
	private SetFragment set;

	/**
	 * TODOActivity中使用网络请求，对应的数据返回区。
	 */
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Literal.GET_HANDLER:
				refreshGet(msg.obj);
				break;
			case Literal.POST_HANDLER:
				refreshPost(msg.obj);
				break;
			default:
				break;
			}
		}

	};

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
	 * @see com.darly.activities.base.BaseActivity#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		LogUtils.customTagPrefix = "xUtilsSample"; // 方便调试时过滤 adb logcat 输出
		LogUtils.allowI = false; // 关闭 LogUtils.i(...) 的 adb log 输出
		ViewUtils.inject(this);// 注入view和事件
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group.setOnCheckedChangeListener(this);
		buttom.setChecked(true);
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
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		hideFragments(ft);
		switch (checkedId) {
		case R.id.main_bottom_index:
			if (index != null) {
				if (index.isVisible())
					return;
				ft.show(index);
			} else {
				index = new IndexFragment();
				ft.add(R.id.main_frame, index);
			}
			break;
		case R.id.main_bottom_me:
			if (me != null) {
				if (me.isVisible())
					return;
				ft.show(me);
			} else {
				me = new MeFragment();
				ft.add(R.id.main_frame, me);
			}
			break;
		case R.id.main_bottom_set:
			if (set != null) {
				if (set.isVisible())
					return;
				ft.show(set);
			} else {
				set = new SetFragment();
				ft.add(R.id.main_frame, set);
			}
			break;

		default:
			break;
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (index != null) {
			transaction.hide(index);
		}
		if (me != null) {
			transaction.hide(me);
		}
		if (set != null) {
			transaction.hide(set);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

}
