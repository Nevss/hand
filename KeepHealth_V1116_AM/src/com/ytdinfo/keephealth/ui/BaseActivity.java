package com.ytdinfo.keephealth.ui;

import java.sql.SQLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.hisun.phone.core.voice.util.Log4Util;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPIntentUtils;
import com.voice.demo.tools.CCPUtil;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;

public class BaseActivity extends Activity {
	InternalReceiver internalReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerReceiver(new String[] { CCPIntentUtils.INTENT_KICKEDOFF,
				Intent.ACTION_MEDIA_MOUNTED, Intent.ACTION_MEDIA_REMOVED });
		
		SharedPrefsUtil.putValue(Constants.CHECKISUPDATE, false);
		LogUtil.i("wpc2", "BaseActivity===false");
	}

	protected final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter(
				INTETN_ACTION_EXIT_CCP_DEMO);
		intentfilter.addAction(CCPIntentUtils.INTENT_CONNECT_CCP);
		intentfilter.addAction(CCPIntentUtils.INTENT_DISCONNECT_CCP);
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}

		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}

	/**
	 * 
	 */
	public void launchCCP() {
		
		Intent intent = new Intent();
//		intent.putExtra("radioBt_tag", 0);
		intent.setClass(BaseActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		startActivity(intent);
	}

	public static final String INTETN_ACTION_EXIT_CCP_DEMO = "exit_demo";

	class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent == null) {
				return;
			}

			if (CCPIntentUtils.INTENT_KICKEDOFF.equals(intent.getAction())
			/* || CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent.getAction()) */) {

				String message = "您的账号在其他地方已经登录";
				/*
				 * if(CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent.getAction(
				 * ))) { message = "无效的代理,与云通讯服务器断开"; }
				 */
				cancelUser();
				Dialog dialog = new AlertDialog.Builder(BaseActivity.this)
						.setTitle(R.string.account_offline_notify)
						.setIcon(R.drawable.navigation_bar_help_icon)
						.setMessage(message)
						.setPositiveButton(R.string.dialog_btn,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int whichButton) {
										
										dialog.dismiss();

										// CCPUtil.exitCCPDemo();
										launchCCP();
									}

								}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

			} else if (intent != null
					&& INTETN_ACTION_EXIT_CCP_DEMO.equals(intent.getAction())) {
				Log4Util.d(CCPHelper.DEMO_TAG, "Launcher destory.");
				CCPUtil.exitCCPDemo();
				finish();
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

	}

	private void cancelUser() {
		
		SharedPrefsUtil.remove(Constants.TOKEN);
		SharedPrefsUtil.remove(Constants.USERID);
		SharedPrefsUtil.remove(Constants.USERMODEL);
		SharedPrefsUtil.remove(Constants.ONLINE_QUES_USERMODEL);
		// ToastUtil.showMessage("已退出");
		// 跳到首页
		try {
			// update sending message status ...
			CCPSqliteManager.getInstance().updateAllIMMessageSendFailed();
			// CCPSqliteManager.getInstance().
		} catch (SQLException e) {
			e.printStackTrace();
		}

		CCPUtil.exitCCPDemo();
		try {
			// CCPSqliteManager.getInstance().deleteAllIMMessage();
			// CCPSqliteManager.getInstance().
			// DbUtils.create(SettingActivity.this).deleteAll(UserGroupBean.class);
			// DbUtils.create(SettingActivity.this).deleteAll(ChatInfoBean.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.i("wpc", "BaseActivity===onDestroy");
		unregisterReceiver(internalReceiver);
	}

}
