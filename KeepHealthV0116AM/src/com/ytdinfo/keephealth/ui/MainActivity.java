package com.ytdinfo.keephealth.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hisun.phone.core.voice.util.Log4Util;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.voice.demo.group.model.IMConversation;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPIntentUtils;
import com.voice.demo.tools.CCPUtil;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.jpush.ExampleUtil;
import com.ytdinfo.keephealth.ui.login.LoginActivity;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;

public class MainActivity extends Base2Activity {
	private String TAG = "MainActivity";
	private AlertDialog alertDialog;
	private RadioGroup radioGroup;
	private FragmentManager fragmentManager;
	private HomeFragment homeFragment;
	private NewsFragment newsFragment;
	private UserInfoFragment userInfoFragment;
	private RadioButton radioButton0, radioButton1, radioButton2;

	private int oldBtn = 0;
	private boolean flag = true;

	public static boolean isForeground = false;

	public RadioButton getRadioButton0() {
		return radioButton0;
	}

	public void setRadioButton0(RadioButton radioButton0) {
		this.radioButton0 = radioButton0;
	}

	private String fromPersonData;
	private ImageView newsPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LogUtil.i("wpc", "Main---onCreate()");
		LogUtil.i("wpc",
				SharedPrefsUtil.getValue(Constants.CHECKISUPDATE, false) + "");
		// 检查更新
		if (SharedPrefsUtil.getValue(Constants.CHECKISUPDATE, false)) {

			checkISupdate();

			// showUpdateDialog();
		}

		// 统计活跃用户
		statisticActiveUser();

		// ysj
		initView();

		int checkedid_radiobt = SharedPrefsUtil.getValue(
				Constants.CHECKEDID_RADIOBT, 0);
		((RadioButton) radioGroup.getChildAt(checkedid_radiobt))
				.setTextColor(getResources().getColor(R.color.w_RadioButton));

		if (getIntent().hasExtra("flag")) {
			initHome2();
			radioButton2.setChecked(true);
		} else if (getIntent().hasExtra("news")) {
			initHome1();
			radioButton1.setChecked(true);
		} else {
			initHome();
			radioButton0.setChecked(true);
		}
		initListener();

		registerMessageReceiver(); // used for receive msg
		init();
		registerReceiver(new String[] { CCPIntentUtils.INTENT_IM_RECIVE,
				CCPIntentUtils.INTENT_REMOVE_FROM_GROUP,
				CCPIntentUtils.INTENT_DELETE_GROUP_MESSAGE });
	}

	public void showUpdateDialog(final String lastestUrl) {
		if (alertDialog != null && alertDialog.isShowing()) {
			return;
		}
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.update);// 设置对话框的布局

		Button sure = (Button) window.findViewById(R.id.sure);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				// checkISupdate();
				update(lastestUrl);
				finish();
			}
		});
	}

	/**
	 * 检查更新
	 */
	private void checkISupdate() {
		int versionCode = getVersionCode();

		String channel = "";
		try {
			ApplicationInfo appInfo = this.getPackageManager()
					.getApplicationInfo(getPackageName(),
							PackageManager.GET_META_DATA);
			channel = appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = Constants.ROOT_URl + "/api/SoftwareUpdate/List?version="
				+ versionCode + "&type=0&channel=" + channel;
		LogUtil.i("paul", url);

		RequestParams params = new RequestParams();

		HttpClient.get(this, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LogUtil.i("paul", arg0.result.toString());

				parseJson(arg0.result.toString());
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
		});

	}

	/**
	 * Retrieves application's version code from the manifest
	 * 
	 * @return versionCode
	 */
	public int getVersionCode() {
		int code = 1;
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			code = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return code;
	}

	private void parseJson(String jsonStr) {
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject jsonData = jsonObject.getJSONObject("Data");
			String lastestUrl = jsonData.getString("LastestUrl");

			LogUtil.i("paul", lastestUrl);

			if (!lastestUrl.equals("null") && lastestUrl != null
					&& !lastestUrl.equals("")) {
				int lastIndex_Backslash = lastestUrl.lastIndexOf("/");
				int index_apk = lastestUrl.lastIndexOf(".apk");
				String lastesVersion = lastestUrl.substring(
						lastIndex_Backslash + 1, index_apk);

				// String versionName = getAppVersionName(this);
				int versionCode = getVersionCode();

				LogUtil.i("paul", "服务器上版本： " + lastesVersion + "====="
						+ "本地版本： " + versionCode);
				if (lastesVersion.compareTo(versionCode + "") > 0) {
					// 需要更新
					showUpdateDialog(lastestUrl);
					// update(lastestUrl);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void update(String lastestUrl) {
		LogUtil.i("paul", lastestUrl);

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(lastestUrl));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

	}

	private void statisticActiveUser() {
		List<String> listDate;
		Calendar calendar = Calendar.getInstance();
		// 日期输出格式（MM代表月，E代表星期，HH代表24小时制，hh代表12小时制，mm代表分）
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String currentDate = simpleDateFormat.format(calendar.getTime());
		LogUtil.i("wpc", "currentDate== " + currentDate); // 20150925

		String dateJson = SharedPrefsUtil.getValue(Constants.DATE, "");
		LogUtil.i("wpc", "dateJson== " + dateJson);

		if (dateJson.equals("")) {
			listDate = new ArrayList<String>();
			listDate.add(currentDate);
		} else {
			listDate = new Gson().fromJson(dateJson,
					new TypeToken<List<String>>() {
					}.getType());
			if (!listDate.contains(currentDate)) {

				listDate.add(currentDate);
			}
			// 判断统计
			if (listDate.size() >= 3) {
				if ((Integer.parseInt(listDate.get(listDate.size() - 1)) - Integer
						.parseInt(listDate.get(listDate.size() - 3))) <= 30) {
					// 统计
					LogUtil.i("wpc", "统计");
					MobclickAgent.onEvent(this, Constants.UMENG_EVENT_24);
				}
			}
		}
		SharedPrefsUtil.putValue(Constants.DATE, new Gson().toJson(listDate));

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
	}

	private void initView() {
		// TODO Auto-generated method stub
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioButton0 = (RadioButton) findViewById(R.id.tab_rb_1);
		radioButton1 = (RadioButton) findViewById(R.id.tab_rb_2);
		radioButton2 = (RadioButton) findViewById(R.id.tab_rb_3);
		newsPoint = (ImageView) findViewById(R.id.news_point);
	}

	private void initHome() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		homeFragment = new HomeFragment();
		transaction.add(R.id.framelayout, homeFragment);
		transaction.show(homeFragment);
		transaction.commit();
	}

	private void initHome1() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		newsFragment = new NewsFragment();
		transaction.add(R.id.framelayout, newsFragment);
		transaction.show(newsFragment);
		transaction.commit();
	}

	private void initHome2() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		userInfoFragment = new UserInfoFragment();
		transaction.add(R.id.framelayout, userInfoFragment);
		transaction.show(userInfoFragment);
		transaction.commit();
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				// int position = radioButtonPosition(checkedId);
				// flag = position > oldBtn ? true : false;//
				// 根据点击顺序判断fragment的进入方�?
				// oldBtn = position;
				// SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT,
				// position);
				clickRadioButton(checkedId);
			}
		});
	}

	@SuppressLint("ResourceAsColor")
	public void clickRadioButton(int checkedId) {
		// TODO Auto-generated method stub
		int position = radioButtonPosition(checkedId);
		flag = position > oldBtn ? true : false;// 根据点击顺序判断fragment的进入方�?
		oldBtn = position;
		SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT, position);

		changeRadioButtonTextColor();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (flag) {
			transaction.setCustomAnimations(R.anim.anim_push_from_right,
					R.anim.anim_pop_from_right);
		} else {
			transaction.setCustomAnimations(R.anim.anim_push_from_left,
					R.anim.anim_pop_from_left);
		}

		hideFragments(transaction);
		switch (checkedId) {
		case R.id.tab_rb_1:
			radioButton0.setTextColor(getResources().getColor(
					R.color.w_RadioButton));
			if (homeFragment == null) {
				homeFragment = new HomeFragment();
				transaction.add(R.id.framelayout, homeFragment);
			} else {
				if (homeFragment.isVisible())
					return;
				transaction.show(homeFragment);
			}
			transaction.commitAllowingStateLoss();
			break;
		case R.id.tab_rb_2:
			radioButton1.setTextColor(getResources().getColor(
					R.color.w_RadioButton));
			if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
				if (newsFragment == null) {
					newsFragment = new NewsFragment();
					transaction.add(R.id.framelayout, newsFragment);
				} else {
					if (newsFragment.isVisible())
						return;
					transaction.show(newsFragment);
				}
				transaction.commitAllowingStateLoss();

				/*
				 * System.out.println("device///"+CCPHelper.getInstance().getDevice
				 * ()); if(null==CCPHelper.getInstance().getDevice()||CCPHelper.
				 * getInstance().getDevice().isOnline()!=State.ONLINE){
				 * CCPHelper.getInstance().registerCCP( new
				 * CCPHelper.RegistCallBack() {
				 * 
				 * @Override public void onRegistResult(int reason, String msg)
				 * { // Log.i("XXX", String.format("%d, %s", // reason, msg));
				 * if (reason==8192) { Intent intent = new Intent();
				 * intent.setClass(MainActivity.this,
				 * GroupMessageListActivity.class); if (flag) {
				 * startActivityForResult(intent,1001); }else {
				 * startActivityForResult(intent,1002); } } else {
				 * 
				 * } } });}else { Intent intent = new Intent();
				 * intent.setClass(MainActivity.this,
				 * GroupMessageListActivity.class); if (flag) {
				 * startActivityForResult(intent,1001); }else {
				 * startActivityForResult(intent,1002); } }
				 */} else {
				Intent i = new Intent(MainActivity.this, LoginActivity.class);
				startActivityForResult(i, 1003);
			}

			/*
			 * if (newsFragment == null) { newsFragment = new NewsFragment();
			 * transaction.add(R.id.framelayout, newsFragment); } else { if
			 * (newsFragment.isVisible()) return;
			 * transaction.show(newsFragment); }
			 */
			break;
		case R.id.tab_rb_3:
			radioButton2.setTextColor(getResources().getColor(
					R.color.w_RadioButton));

			if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
				if (userInfoFragment == null) {
					userInfoFragment = new UserInfoFragment();
					transaction.add(R.id.framelayout, userInfoFragment);
				} else {
					if (userInfoFragment.isVisible())
						return;
					transaction.show(userInfoFragment);
				}
				transaction.commitAllowingStateLoss();
			} else {
				Intent i = new Intent(MainActivity.this, LoginActivity.class);
				startActivityForResult(i, 1003);
			}
			break;
		}

	}

	@SuppressLint("ResourceAsColor")
	public void clickRadioButton2(int checkedId) {
		// TODO Auto-generated method stub
		int position = radioButtonPosition(checkedId);
		flag = position > oldBtn ? true : false;// 根据点击顺序判断fragment的进入方�?
		oldBtn = position;
		SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT, position);

		radioButton1.setChecked(true);

		changeRadioButtonTextColor();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
//		if (flag) {
//			transaction.setCustomAnimations(R.anim.anim_push_from_right,
//					R.anim.anim_pop_from_right);
//		} else {
//			transaction.setCustomAnimations(R.anim.anim_push_from_left,
//					R.anim.anim_pop_from_left);
//		}

		hideFragments(transaction);

		// radioButton1.setChecked(true);
		radioButton1.setTextColor(getResources()
				.getColor(R.color.w_RadioButton));
		if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
			if (newsFragment == null) {
				newsFragment = new NewsFragment();
				transaction.add(R.id.framelayout, newsFragment);
			} else {
				if (newsFragment.isVisible())
					return;
				transaction.show(newsFragment);
			}
			transaction.commitAllowingStateLoss();

		} else {
			Intent i = new Intent(MainActivity.this, LoginActivity.class);
			startActivityForResult(i, 1003);
		}

	}

	private void hideFragments(FragmentTransaction transaction) {

		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (userInfoFragment != null) {
			transaction.hide(userInfoFragment);
		}

	}

	private void changeRadioButtonTextColor() {
		radioButton0.setTextColor(getResources().getColor(R.color.w_gray));
		radioButton1.setTextColor(getResources().getColor(R.color.w_gray));
		radioButton2.setTextColor(getResources().getColor(R.color.w_gray));
	}

	private int radioButtonPosition(int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.tab_rb_1:
			return 0;
		case R.id.tab_rb_2:
			return 1;
		case R.id.tab_rb_3:
			return 2;
		}

		return 0;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg2 == null)
			return;

		if (arg2.hasExtra("flag")) {
			radioButton2.setChecked(true);
		}
		switch (arg0) {
		case 1001:
			radioButton0.setChecked(true);
			break;
		case 1002:
			radioButton2.setChecked(true);
			break;
		case 1003:
			radioButton0.setChecked(true);
			break;

		default:
			break;
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 * 程序退出
	 * */
	private long firstime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - firstime > 3000) {
				Toast.makeText(MainActivity.this, "再按一次,退出程序",
						Toast.LENGTH_SHORT).show();
				firstime = System.currentTimeMillis();
				return true;
			} else {
				/*
				 * Intent intent = new Intent();
				 * intent.setClass(MainActivity.this, MainActivity.class);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * //注意本行的FLAG设置 startActivity(intent);
				 */
				SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT, 0);
				SharedPrefsUtil.putValue(Constants.CHECKISUPDATE, true);
				LogUtil.i("wpc2", "onKeyDown===true");
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init() {
		JPushInterface.init(getApplicationContext());
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();

		JPushInterface.onResume(this);

		MobclickAgent.onResume(this);
		new IMMsgAsyncTask().execute();

	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();

		JPushInterface.onPause(this);

		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mMessageReceiver);
		fragmentManager = getSupportFragmentManager();
		List<Fragment> frgList = fragmentManager.getFragments();
		frgList.clear();

	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}
	}

	private void setCostomMsg(String msg) {
		LogUtil.i("===", msg);
	}

	public MainActivity() {

	}

	public Context getContext() {
		return this;
	}

	private InternalReceiver internalReceiver;

	/*
	 * protected final void registerReceiver(String[] actionArray) { if
	 * (actionArray == null) { return; } IntentFilter intentfilter = new
	 * IntentFilter( INTETN_ACTION_EXIT_CCP_DEMO);
	 * intentfilter.addAction(CCPIntentUtils.INTENT_CONNECT_CCP);
	 * intentfilter.addAction(CCPIntentUtils.INTENT_DISCONNECT_CCP); for (String
	 * action : actionArray) { intentfilter.addAction(action); }
	 * 
	 * if (internalReceiver == null) { internalReceiver = new
	 * InternalReceiver(); } this.getApplicationContext().registerReceiver(
	 * internalReceiver, intentfilter); }
	 */
	public static final String INTETN_ACTION_EXIT_CCP_DEMO = "exit_demo";

	class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent == null) {
				return;
			}

			if (CCPIntentUtils.INTENT_KICKEDOFF.equals(intent.getAction())
					|| CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent
							.getAction())) {

				String message = "您的账号在其他地方已经登录";
				if (CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent
						.getAction())) {
					message = "无效的代理,与云通讯服务器断开";
				}
				Dialog dialog = new AlertDialog.Builder(MainActivity.this)
						.setTitle(R.string.account_offline_notify)
						.setIcon(R.drawable.navigation_bar_help_icon)
						.setMessage(message)
						.setPositiveButton(R.string.dialog_btn,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();

										CCPUtil.exitCCPDemo();
										// launchCCP();
									}

								}).create();
				dialog.show();

			} else if (intent != null
					&& INTETN_ACTION_EXIT_CCP_DEMO.equals(intent.getAction())) {
				Log4Util.d(CCPHelper.DEMO_TAG, "Launcher destory.");
				CCPUtil.exitCCPDemo();
				// finish();
			} else {
				if (intent == null || TextUtils.isEmpty(intent.getAction())) {
					return;
				}

				/**
				 * version 3.5 for listener SDcard status
				 */
				if (Intent.ACTION_MEDIA_REMOVED.equalsIgnoreCase(intent
						.getAction())
						|| Intent.ACTION_MEDIA_MOUNTED.equalsIgnoreCase(intent
								.getAction())) {

					// updateExternalStorageState();
					return;
				}

				onReceiveBroadcast(intent);
			}
		}

	}

	protected void onReceiveBroadcast(Intent intent) {
		if (intent != null
				&& (CCPIntentUtils.INTENT_IM_RECIVE.equals(intent.getAction()))) {
			// CCPSqliteManager.getInstance().queryIMMessagesBySessionId(sessionId);
			System.out.println("news.........point");
			// newsPoint.setVisibility(View.VISIBLE);
			new IMMsgAsyncTask().execute();
			// update UI...
			// initConversation();
		}

	}

	class IMMsgAsyncTask extends
			AsyncTask<Void, Void, ArrayList<IMConversation>> {

		@Override
		protected ArrayList<IMConversation> doInBackground(Void... params) {
			// System.out.println(CCPSqliteManager.getInstance());
			// if (null!=CCPSqliteManager.getInstance()) {

			try {
				return CCPSqliteManager.getInstance().queryIMConversation();
			} catch (Exception e) {
				e.printStackTrace();
				// }
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<IMConversation> result) {
			super.onPostExecute(result);
			if (result != null && !result.isEmpty()) {

				for (int i = 0; i < result.size(); i++) {
					if (!result.get(i).getUnReadNum().equals("0")) {
						// NotificationUtils.send(getActivity());
						newsPoint.setVisibility(View.VISIBLE);
						// NotificationUtils.send(MainActivity.this);
					}
				}

				/*
				 * if( getActivity() == null) return; //
				 * getActivity().getClass(); mIMAdapter = new
				 * IMConvAdapter((MainActivity) getActivity(), result);
				 * mGroupListLv.setAdapter(mIMAdapter);
				 * mGroupListEmpty.setVisibility(View.GONE);
				 */
			} else {
				newsPoint.setVisibility(View.GONE);
				//
				/*
				 * mGroupListLv.setAdapter(null);
				 * mGroupListEmpty.setVisibility(View.VISIBLE);
				 * data.setText("暂无数据");
				 */
			}
		}
	}
}
