package com.ytdinfo.keephealth.service;

import java.util.Timer;
import java.util.TimerTask;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.voice.demo.group.GroupChatActivity;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.app.MyApp;
import com.ytdinfo.keephealth.model.ChatInfoBean;
import com.ytdinfo.keephealth.utils.DBUtilsHelper;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.UserDataUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class TimerService extends Service {
	private static final String TAG = TimerService.class.getName();

	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private static Handler mHandler = null;
	public static int count = 0;

	public static boolean isStart = false;
	public static boolean isPause = false;
	private boolean isStop = true;
	private static int delay = 1000; // 1s
	private static int period = 1000; // 1s

	public static void setmHandler(Handler mHandler2) {
		mHandler = mHandler2;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "onStart");
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int op = bundle.getInt("op");
				switch (op) {
				case 1:
					if (!isStart) {
						startTimer();
					}
					break;
				case 2:
					/*
					 * if (!isStart) { startTimer(); }
					 */
					isPause = !isPause;
					break;
				case 3:
					stopTimer();
					break;
				}

			}
		}

	}

	private void startTimer() {
		Log.v(TAG, "startTimer");
		isStart = true;
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					// Log.i(TAG, "count: "+String.valueOf(count));
					// sendMessage(UPDATE_TEXTVIEW);
					Message message = new Message();
					message.arg1 = count;
					if (count >= Constants.PROGRESSMAX) {
						stopTimer();
						isPause = false;
						sendCloseSubjectRequest();
					}
					if (mHandler != null) {
						mHandler.sendMessage(message);
						LogUtil.i(TAG, count + "");
					}
					do {
						try {
							// Log.i(TAG, "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					} while (isPause);

					count++;
				}
			};
		}

		if (mTimer != null && mTimerTask != null)
			mTimer.schedule(mTimerTask, delay, period);

	}
	
	private void sendCloseSubjectRequest(){
		DbUtils dbUtils = DBUtilsHelper.getInstance()
				.getDb();
		ChatInfoBean chatInfoBean = null;
		try {
			chatInfoBean = dbUtils.findFirst(Selector.from(
					ChatInfoBean.class).where("status",
					"=", true));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (chatInfoBean != null) {
			String subjectID = chatInfoBean.getSubjectID();
			CCPHelper
					.getInstance()
					.getDevice()
					.sendInstanceMessage(
							chatInfoBean.getDocInfoBeanId(),
							Constants.CLOSESUBJECT,
							null,
							UserDataUtils
									.getUserData(subjectID));
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("subjectId",
					subjectID);
			HttpClient.get(MyApp.getInstance(),
					Constants.CLOSESUBJECT_URL, params,
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							super.onStart();
						}

						@Override
						public void onSuccess(
								ResponseInfo<String> arg0) {
							LogUtil.i(TAG, arg0.result);
						}

						@Override
						public void onFailure(
								HttpException arg0,
								String arg1) {
							LogUtil.i(TAG,
									arg0.getMessage());
						}
					});
		}
	}

	private void stopTimer() {
		LogUtil.i(TAG, "stopTimer");
		isStart = false;
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		count = 0;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopTimer();
	}
}
