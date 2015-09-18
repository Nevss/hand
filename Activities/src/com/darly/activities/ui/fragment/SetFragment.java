/**
 * 2015年9月16日
 * SetFragment.java
 * com.darly.activities.ui.fragment
 * @auther Darly Fronch
 * 下午5:00:02
 * SetFragment
 * TODO
 */
package com.darly.activities.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.darly.activities.adapter.SetFragmentAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.Literal;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.SetFragmentModel;
import com.darly.activities.ui.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 SetFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午5:00:02 SetFragment TODO用户设置页面
 */
public class SetFragment extends BaseFragment implements OnItemClickListener {
	/**
	 * TODO根View
	 */
	private View rootView;
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	/**
	 * TODO设置中的ListView控件。
	 */
	@ViewInject(R.id.set_list)
	private ListView list;

	private String lebal[] = { "个人信息", "修改密码", "我的收藏", "我的评论" };

	private int drawableId[] = { R.drawable.set_info, R.drawable.set_pass,
			R.drawable.set_see, R.drawable.set_word };

	/**
	 * TODO用户信息，ListView的头部
	 */
	private View header;

	/**
	 * TODO退出按钮
	 */
	@ViewInject(R.id.item_footer_btn)
	private Button consel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_set, container, false);// 关联布局文件
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
		switch (v.getId()) {
		case R.id.item_footer_btn:
			ToastApp.showToast(getActivity(), "item_footer_btn");
			break;

		default:
			break;
		}
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
		consel.setOnClickListener(this);
		list.setOnItemClickListener(this);
		header = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_set_item_header, null);
		ImageView headerIv = (ImageView) header
				.findViewById(R.id.item_header_image);
		headerIv.setLayoutParams(new LayoutParams(Literal.width,
				414 * Literal.width / 1242));
		headerIv.setImageResource(R.drawable.login_table_bg);
		list.addHeaderView(header);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		List<SetFragmentModel> data = new ArrayList<SetFragmentModel>();
		for (int i = 0, len = lebal.length; i < len; i++) {
			data.add(new SetFragmentModel(lebal[i], drawableId[i]));
		}

		list.setAdapter(new SetFragmentAdapter(data,
				R.layout.fragment_set_item, getActivity()));
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
		switch (position) {
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		default:
			break;
		}
		ToastApp.showToast(getActivity(), "position" + position);

	}

}
