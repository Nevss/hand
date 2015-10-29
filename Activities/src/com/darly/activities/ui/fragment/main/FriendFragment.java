/**
 * 下午3:16:34
 * @author Zhangyuhui
 * FriendFragment.java
 * TODO
 */
package com.darly.activities.ui.fragment.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.HTTPServ;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.Goddesses;
import com.darly.activities.poll.HTTPSevTasker;
import com.darly.activities.ui.R;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;

/**
 * @author Zhangyuhui FriendFragment 下午3:16:34 TODO 单个美女信息展示。
 */
public class FriendFragment extends BaseFragment {
	private static final String TAG = "FriendFragment";
	private View rootView;

	private int tuid;

		/**
	 * 上午11:27:30 TODO美女名称
	 */
	private TextView name;
	/**
	 * 上午11:27:46 TODO 美女图片
	 */
	private ImageView image;
	/**
	 * 上午11:27:55 TODO 美女详细资料
	 */
	private TextView descrip;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.main_fragment_friend, container,
				false);// 关联布局文件
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
		name = (TextView) rootView.findViewById(R.id.main_fragment_frid_name);
		image = (ImageView) rootView
				.findViewById(R.id.main_fragment_frid_image);
		descrip = (TextView) rootView
				.findViewById(R.id.main_fragment_frid_descrip);
		image.setLayoutParams(new LayoutParams(Literal.width, Literal.width));
		tuid = new Random().nextInt(10);
		LogFileHelper.getInstance().i(TAG, "FriendFragment is run");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("tuid", tuid + ""));
		List<BasicNameValuePair> propety = new ArrayList<BasicNameValuePair>();
		propety.add(new BasicNameValuePair("apikey", HTTPServ.APPIDKEY));
		manager.start();
		manager.addAsyncTask(new HTTPSevTasker(getActivity(), params,
				HTTPServ.GODDESSES, handler, true, Literal.GET_HANDLER, propety));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
		Goddesses model = new Gson().fromJson((String) object, Goddesses.class);
		if (model != null) {
			name.setText(model.getTu_name());
			descrip.setText(model.getTu_value());
			imageLoader.displayImage(model.getTu_dizhi(), image, options);
		} else {
			ToastApp.showToast(getActivity(), "网络连接异常，请检查网络");
		}
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

}
