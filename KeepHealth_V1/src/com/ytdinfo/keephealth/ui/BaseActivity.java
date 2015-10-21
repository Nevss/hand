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
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;

/**
 * @author Zhangyuhui
 * BaseActivity
 * $
 * 上午9:24:59
 * TODO 第一个基础类，注册广播。
 */
public class BaseActivity extends Activity{
	InternalReceiver internalReceiver = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerReceiver(new String[]{CCPIntentUtils.INTENT_KICKEDOFF 
				, Intent.ACTION_MEDIA_MOUNTED 
				,Intent.ACTION_MEDIA_REMOVED}); 
	}
	protected final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter(INTETN_ACTION_EXIT_CCP_DEMO);
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
	 * 上午9:30:05
	 * @author Zhangyuhui
	 * BaseActivity.java
	 * TODO 用戶退出后跳入首頁。必須清除栈目录中的所有Activity页面，新建一个主类。
	 */
	public void launchCCP() {
		Intent intent = new Intent();   
		intent.setClass(BaseActivity.this, MainActivity.class);  
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意本行的FLAG设置  
		startActivity(intent);
	}	
	public static final String INTETN_ACTION_EXIT_CCP_DEMO = "exit_demo";
	/**
	 * @author Zhangyuhui
	 * InternalReceiver
	 * $
	 * 上午9:26:59
	 * TODO 广播类，主要功能，注册即时通讯，接收信息。
	 */
	class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent == null ) {
				return;
			}
			
			if (CCPIntentUtils.INTENT_KICKEDOFF.equals(intent.getAction())
					/*|| CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent.getAction())*/) {
				//用户账户他方登录情况下。在线用户接收到此信息，从而退出登录状态。
				String message = "您的账号在其他地方已经登录";
				/*if(CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent.getAction())) {
					message = "无效的代理,与云通讯服务器断开";
				}*/
				cancelUser();
				Dialog dialog = new AlertDialog.Builder(BaseActivity.this)
						.setTitle(R.string.account_offline_notify)
						.setIcon(R.drawable.navigation_bar_help_icon)
						.setMessage(message)
						.setPositiveButton(R.string.dialog_btn,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
										
										//CCPUtil.exitCCPDemo();
										launchCCP();  
									}

								}).create();
				dialog.setCanceledOnTouchOutside(false);
					dialog.show();
					
					
			} else if (intent != null && INTETN_ACTION_EXIT_CCP_DEMO.equals(intent.getAction())) {
				//实时通讯工作完成。资源释放。
				Log4Util.d(CCPHelper.DEMO_TAG, "Launcher destory.");
				CCPUtil.exitCCPDemo();
				finish();
			} else {
				if(intent == null || TextUtils.isEmpty(intent.getAction())) {
					return;
				}
				
				/**
				 * version 3.5 for listener SDcard status
				 */
				if(Intent.ACTION_MEDIA_REMOVED.equalsIgnoreCase(intent.getAction()) 
						|| Intent.ACTION_MEDIA_MOUNTED.equalsIgnoreCase(intent.getAction())) {
					
				//	updateExternalStorageState();
					return ;
				}
				
				
				onReceiveBroadcast(intent);
			}
		}
	}
protected void onReceiveBroadcast(Intent intent) {
		
	}
/**
 * 
 * 上午9:29:13
 * @author Zhangyuhui
 * BaseActivity.java
 * TODO用户退出，清除用户资料。
 */
private void cancelUser() {
	SharedPrefsUtil.remove(Constants.TOKEN);
	SharedPrefsUtil.remove(Constants.USERID);
	SharedPrefsUtil.remove(Constants.USERMODEL);
	SharedPrefsUtil.remove(Constants.ONLINE_QUES_USERMODEL);
	//ToastUtil.showMessage("已退出");
	// 跳到首页
	try {
		// update sending message status ...
		CCPSqliteManager.getInstance().updateAllIMMessageSendFailed();
		//CCPSqliteManager.getInstance().
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	CCPUtil.exitCCPDemo();
	try {
		//CCPSqliteManager.getInstance().deleteAllIMMessage();
		//CCPSqliteManager.getInstance().
		//DbUtils.create(SettingActivity.this).deleteAll(UserGroupBean.class);
		//DbUtils.create(SettingActivity.this).deleteAll(ChatInfoBean.class);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}
