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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.darly.activities.R;
import com.darly.activities.adapter.SetFragmentAdapter;
import com.darly.activities.app.Constract;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.HTTPServ;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.PreferenceUserInfor;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.SetFragmentModel;
import com.darly.activities.model.UserInformation;
import com.darly.activities.poll.HttpClient;
import com.darly.activities.poll.HttpTaskerForString;
import com.darly.activities.ui.InterlActivityVtoo;
import com.darly.activities.ui.MainActivity;
import com.darly.activities.ui.fragment.set.FieldsBackActivity;
import com.darly.activities.ui.fragment.set.FieldsBackListener;
import com.darly.activities.ui.login.LoginAcitvity;
import com.darly.activities.ui.qrcode.MipcaActivityCapture;
import com.darly.activities.widget.roundedimage.RoundedImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 SetFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午5:00:02 SetFragment TODO用户设置页面
 */
public class SetFragment extends BaseFragment implements OnItemClickListener,
		FieldsBackListener {
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

	private int lebal[] = { R.string.set_0, R.string.set_1, R.string.set_2,
			R.string.set_3, R.string.set_4, R.string.set_5 };

	private int drawableId[] = { R.drawable.set_scan, R.drawable.set_info,
			R.drawable.set_info, R.drawable.set_pass, R.drawable.set_see,
			R.drawable.set_word };

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
			new AlertDialog.Builder(getActivity())
					.setMessage(R.string.set_out_app)
					.setPositiveButton(R.string.yes, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							PreferenceUserInfor.cleanUserInfor(getActivity());
							System.exit(0);
						}
					}).setNegativeButton(R.string.no, null).show();
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
		headerIv.setLayoutParams(new LayoutParams(Constract.width,
				414 * Constract.width / 1242));
		headerIv.setImageResource(R.drawable.login_table_bg);

		header.findViewById(R.id.set_login).setOnClickListener(this);
		header.findViewById(R.id.set_regest).setOnClickListener(this);
		loginUser();
		list.addHeaderView(header);
		FieldsBackActivity.setBackListener(this);
	}

	/**
	 * 
	 * 下午4:21:49
	 * 
	 * @author Zhangyuhui SetFragment.java TODO 用户登录信息展示
	 */
	private void loginUser() {
		// 进入页面先判断用户是否登录。
		if (PreferenceUserInfor.isUserLogin(Constract.USERINFO, getActivity())) {
			// 用户登录状态。界面进行变化。否则界面不变
			header.findViewById(R.id.set_unlogin).setVisibility(View.GONE);
			header.findViewById(R.id.set_islogin).setVisibility(View.VISIBLE);
			UserInformation user = new Gson().fromJson(PreferenceUserInfor
					.getUserInfor(Constract.USERINFO, getActivity()),
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
		getResult();
	}

	/**
	 * @return 下午2:57:41
	 * @author Zhangyuhui SetFragment.java TODO 反向HTTP请求完成后对后续页面进行修改。
	 */
	private void getResult() {
		// TODO Auto-generated method stub
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("tuid", 1 + ""));
		List<BasicNameValuePair> propety = new ArrayList<BasicNameValuePair>();
		propety.add(new BasicNameValuePair("apikey", HTTPServ.APPIDKEY));
		manager.start();
		manager.addAsyncTask(new HttpTaskerForString(getActivity(), params,
				HTTPServ.GODDESSES, handler, true, Constract.GET_HANDLER, propety));
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
			startActivity(new Intent(getActivity(), FieldsBackActivity.class));
			break;
		case 3:
			startActivity(new Intent(getActivity(), InterlActivityVtoo.class));
			break;
		case 4:
			new AlertDialog.Builder(getActivity())
					.setMessage(R.string.set_iscache)
					.setPositiveButton(R.string.yes, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							cleanCach();
						}
					}).setNegativeButton(R.string.no, null).show();
			break;
		case 5:
			// 切换语言
			new AlertDialog.Builder(getActivity())
					.setMessage(R.string.set_islagu)
					.setPositiveButton(R.string.yes, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String lagu = PreferenceUserInfor
									.getLagu(getActivity());
							Log.i(TAG, lagu);
							if ("zh".equals(lagu)) {
								changeLague("en");
								PreferenceUserInfor.saveLagu("en",
										getActivity());
							} else if ("en".equals(lagu)) {
								changeLague("zh");
								PreferenceUserInfor.saveLagu("zh",
										getActivity());
							}
						}
					}).setNegativeButton(R.string.no, null).show();
			break;
		case 6:

			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * 上午11:49:08
	 * 
	 * @author Zhangyuhui SetFragment.java TODO 变更语言
	 */
	private void changeLague(String sta) {
		// TODO Auto-generated method stub
		// 本地语言设置
		Locale myLocale = new Locale(sta);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * 
	 * 下午5:44:12
	 * 
	 * @author Zhangyuhui SetFragment.java TODO 清理缓存。
	 */
	private void cleanCach() {
		// TODO Auto-generated method stub
		File file = new File(Constract.SROOT);
		File log = new File(Constract.LOG);
		boolean isSuccess = false;
		if (file.exists()) {
			isSuccess = deleteDir(file);
		}
		if (log.exists()) {
			isSuccess = deleteDir(log);
		}
		if (isSuccess) {
			ToastApp.showToast(getActivity(), R.string.set_cache_succ);
		} else {
			ToastApp.showToast(getActivity(), R.string.set_cache_fail);
		}
	}

	/**
	 * @param dir
	 * @return 下午5:48:03
	 * @author Zhangyuhui SetFragment.java TODO 递归删除文件
	 */
	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return true;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.darly.activities.ui.fragment.set.FieldsBackListener#backListener(
	 * java.lang.String)
	 */
	@Override
	public void backListener(View view, final TextView titl, String name) {
		// TODO Auto-generated method stub

		final TextView tv = (TextView) view
				.findViewById(R.id.item_fields_back_text);
		RequestParams requestParams = new RequestParams();
		HttpClient.get(getActivity(), "http://test.rayelink.com/api/banner",
				requestParams, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// updateViewList();
						titl.setText(arg0.result);
						tv.setText(arg0.result);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
				});
	}

}
