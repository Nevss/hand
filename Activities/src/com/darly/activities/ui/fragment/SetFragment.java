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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.adapter.SetFragmentAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.PreferenceUserInfor;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.SetFragmentModel;
import com.darly.activities.model.UserInformation;
import com.darly.activities.ui.R;
import com.darly.activities.ui.login.LoginAcitvity;
import com.darly.activities.ui.qrcode.MipcaActivityCapture;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 SetFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午5:00:02 SetFragment TODO用户设置页面
 */
public class SetFragment extends BaseFragment implements OnItemClickListener {
	private static final String TAG = "SetFragment";
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

	private String lebal[] = { "扫一扫", "个人信息", "修改密码", "我的收藏", "我的评论" };

	private int drawableId[] = { R.drawable.set_scan, R.drawable.set_info,
			R.drawable.set_pass, R.drawable.set_see, R.drawable.set_word };

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
			// 退出程序。
			new AlertDialog.Builder(getActivity()).setMessage("退出程序")
					.setPositiveButton("确认", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							PreferenceUserInfor.cleanUserInfor(getActivity());
							System.exit(0);
						}
					}).setNegativeButton("取消", null).show();
			break;
		case R.id.set_login:
			// 登录事件
			startActivityForResult(new Intent(getActivity(),
					LoginAcitvity.class), Activity.RESULT_FIRST_USER);
			break;
		case R.id.set_regest:
			// 注册事件
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

		header.findViewById(R.id.set_login).setOnClickListener(this);
		header.findViewById(R.id.set_regest).setOnClickListener(this);
		loginUser();
		list.addHeaderView(header);
	}

	/**
	 * 
	 * 下午4:21:49
	 * 
	 * @author Zhangyuhui SetFragment.java TODO 用户登录信息展示
	 */
	private void loginUser() {
		// 进入页面先判断用户是否登录。
		if (PreferenceUserInfor.isUserLogin(Literal.USERINFO, getActivity())) {
			// 用户登录状态。界面进行变化。否则界面不变
			header.findViewById(R.id.set_unlogin).setVisibility(View.GONE);
			header.findViewById(R.id.set_islogin).setVisibility(View.VISIBLE);
			UserInformation user = new Gson().fromJson(PreferenceUserInfor
					.getUserInfor(Literal.USERINFO, getActivity()),
					UserInformation.class);
			TextView name = (TextView) header.findViewById(R.id.set_name);
			name.setText(user.getUserTrueName());
			RoundedImageView image = (RoundedImageView) header
					.findViewById(R.id.set_userimage);
			imageLoader.displayImage(user.getUserPhoto(), image, options);
		} else {
			header.findViewById(R.id.set_unlogin).setVisibility(View.VISIBLE);
			header.findViewById(R.id.set_islogin).setVisibility(View.GONE);
		}
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
			startActivity(new Intent(getActivity(), MipcaActivityCapture.class));
			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:

			break;
		default:
			break;
		}
		ToastApp.showToast(getActivity(), "position" + position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		LogFileHelper.getInstance().i(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		loginUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loginUser();
	}

}
