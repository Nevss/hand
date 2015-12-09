package com.darly.activities.ui.qinjia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.darly.activities.R;
import com.darly.activities.app.App;
import com.darly.activities.ui.MainActivity;
import com.darly.activities.ui.qinjia.util.AppUtil;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeNotify;

public class GotyeService extends Service {
	public static final String ACTION_INIT = "gotyeim.init";
	public static final String ACTION_LOGIN = "gotyeim.login";
	private GotyeAPI api;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		api = GotyeAPI.getInstance();
		// //初始化
		// int code = api.init(getBaseContext(), MyApplication.APPKEY);
		// 语音识别初始化
		api.initIflySpeechRecognition();
		// api.enableLog(false, true, false);
		// 添加推送消息监听
		api.addListener(mDelegate);
		// 开始接收离线消息
		api.beginReceiveOfflineMessage();
		// api.beginReceiveCSOfflineMessage();

	}

	@Override
	public void onDestroy() {
		Log.d("gotye_service", "onDestroy");
		GotyeAPI.getInstance().removeListener(mDelegate);
		Intent localIntent = new Intent();
		localIntent.setClass(this, GotyeService.class); // 銷毀時重新啟動Service
		this.startService(localIntent);
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void notify(String msg) {
		String currentPackageName = AppUtil.getTopAppPackage(getBaseContext());
		if (currentPackageName.equals(getPackageName())) {
			return;
		}
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		Notification notification = new Notification(R.drawable.ic_launcher,
				msg, System.currentTimeMillis());
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("notify", 1);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, getString(R.string.app_name),
				msg, pendingIntent);
		notificationManager.notify(0, notification);
	}

	private GotyeDelegate mDelegate = new GotyeDelegate() {
		@Override
		public void onReceiveMessage(GotyeMessage message) {
			String msg = null;

			if (message.getType() == GotyeMessageType.GotyeMessageTypeText) {
				msg = message.getSender().getName() + ":" + message.getText();
			} else if (message.getType() == GotyeMessageType.GotyeMessageTypeImage) {
				msg = message.getSender().getName() + "发来了一条图片消息";
			} else if (message.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
				msg = message.getSender().getName() + "发来了一条语音消息";
			} else if (message.getType() == GotyeMessageType.GotyeMessageTypeUserData) {
				msg = message.getSender().getName() + "发来了一条自定义消息";
			} else {
				msg = message.getSender().getName() + "发来了一条群邀请信息";
			}
			if (message.getReceiver() instanceof GotyeGroup) {
				if (!(App
						.isGroupDontdisturb(message.getReceiver().getId()))) {
					GotyeService.this.notify(msg);
				}
				return;
			} else {
				GotyeService.this.notify(msg);
			}
		}

		@Override
		public void onReceiveNotify(GotyeNotify notify) {
			String msg = notify.getSender().getName() + "邀请您加入群[";
			if (!TextUtils.isEmpty(notify.getFrom().getName())) {
				msg += notify.getFrom().getName() + "]";
			} else {
				msg += notify.getFrom().getId() + "]";
			}
			GotyeService.this.notify(msg);
		}

	};
}
