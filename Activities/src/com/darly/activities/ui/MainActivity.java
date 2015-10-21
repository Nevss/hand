package com.darly.activities.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.ui.fragment.MainFragment;
import com.darly.activities.ui.fragment.MeFragment;
import com.darly.activities.ui.fragment.SecFragment;
import com.darly.activities.ui.fragment.SetFragment;
import com.darly.activities.widget.pop.BottomPop;
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
	 * 下午4:33:48 TODO 旋转图标。
	 */
	@ViewInject(R.id.main_bottom_me)
	private LinearLayout layout;

	@ViewInject(R.id.main_bottom_me_iv)
	private ImageView bottomIV;
	/**
	 * TODO第一个标签。
	 */
	@ViewInject(R.id.main_bottom_index)
	private RadioButton buttom;
	/**
	 * TODO第一个标签。
	 */
	@ViewInject(R.id.main_bottom_local)
	private RadioButton local;
	/**
	 * TODO第一个标签。
	 */
	@ViewInject(R.id.main_bottom_search)
	private RadioButton search;
	/**
	 * TODO第一个标签。
	 */
	@ViewInject(R.id.main_bottom_set)
	private RadioButton setEnd;
	/**
	 * TODO首页展示效果哦Fragment
	 */
	private MainFragment index;
	/**
	 * TODO用户自己页面展示Fragment
	 */
	private MeFragment me;

	/**
	 * 下午6:03:37 TODO美女列表Fragment
	 */
	private SecFragment sec;
	/**
	 * TODO用户信息设置Fragment
	 */
	private SetFragment set;

	private BottomPop pop;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_bottom_me:
			// 添加一个POP窗口。
			if (pop == null) {
				pop = new BottomPop(this, v);
			}
			if (pop.isShowing()) {
				bottomIV.startAnimation(AnimationUtils.loadAnimation(this,
						R.anim.anim_bottom_iv_pls));
				pop.dismiss();
			} else {
				bottomIV.startAnimation(AnimationUtils.loadAnimation(this,
						R.anim.anim_bottom_iv_add));
				pop.showAtLocation(v, Gravity.BOTTOM, 0, v.getHeight());
			}

			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initView()
	 */
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		layout.setOnClickListener(this);
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
		buttom.setTextColor(getResources().getColor(R.color.set_list_line));
		local.setTextColor(getResources().getColor(R.color.set_list_line));
		search.setTextColor(getResources().getColor(R.color.set_list_line));
		setEnd.setTextColor(getResources().getColor(R.color.set_list_line));
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		hideFragments(ft);
		switch (checkedId) {
		case R.id.main_bottom_index:
			buttom.setTextColor(getResources().getColor(
					R.color.main_bottom_text));
			if (index != null) {
				if (index.isVisible())
					return;
				ft.show(index);
			} else {
				index = new MainFragment();
				ft.add(R.id.main_frame, index);
			}
			break;
		case R.id.main_bottom_local:
			local.setTextColor(getResources()
					.getColor(R.color.main_bottom_text));
			if (me != null) {
				if (me.isVisible())
					return;
				ft.show(me);
			} else {
				me = new MeFragment();
				ft.add(R.id.main_frame, me);
			}
			break;

		case R.id.main_bottom_search:
			search.setTextColor(getResources().getColor(
					R.color.main_bottom_text));
			if (sec != null) {
				if (sec.isVisible())
					return;
				ft.show(sec);
			} else {
				sec = new SecFragment();
				ft.add(R.id.main_frame, sec);
			}
			break;
		case R.id.main_bottom_set:
			setEnd.setTextColor(getResources().getColor(
					R.color.main_bottom_text));
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
		if (sec != null) {
			transaction.hide(sec);
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
