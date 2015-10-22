package com.darly.activities.app;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;

import java.io.File;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.darly.activities.common.Literal;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @ClassName: AppStack
 * @Description: TODO(App堆栈操作)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2015年1月5日 上午10:04:28
 *
 */
public class AppStack extends Application {
	private static AppStack instance;

	/**
	 * @return 上午10:43:20
	 * @author Zhangyuhui AppStack.java TODO获取APP应用信息启动单例，获取唯一的APP资源
	 */
	public static AppStack getInstance() {
		if (null == instance) {
			return null;
		}
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		initImageLoader();

		/**
		 * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
		 * io.rong.push 为融云 push 进程名称，不可修改。
		 */
		if (getApplicationInfo().packageName
				.equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);
		}
		initConnRongIM();
	}

	/**
	 * 
	 * 下午6:12:34
	 * 
	 * @author Zhangyuhui AppStack.java TODO
	 */
	private void initConnRongIM() {
		// TODO Auto-generated method stub
		if (Literal.token != null && Literal.token.length() != 0) {
			RongIM.connect(Literal.token, new ConnectCallback() {

				@Override
				public void onSuccess(String userid) {
					// TODO Auto-generated method stub
					Log.d("LoginActivity", "--onSuccess" + userid);
				}

				@Override
				public void onError(ErrorCode errorCode) {
					// TODO Auto-generated method stub
					Log.d("LoginActivity", "--onError" + errorCode);
				}

				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					Log.d("LoginActivity", "--onTokenIncorrect");
				}
			});
		}
	}

	/**
	 * 获得当前进程的名字
	 *
	 * @param context
	 * @return 进程号
	 */
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	/**
	 * 
	 * 上午10:44:40
	 * 
	 * @author Zhangyuhui AppStack.java TODO初始化单例模式下的ImageLoader
	 */
	private void initImageLoader() {
		// TODO Auto-generated method stub

		File cacheDir = StorageUtils.getOwnCacheDirectory(this, "Act/Cache");
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(480, 800)
				// maxwidth, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				.writeDebugLogs() // Remove for releaseapp
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	/**
	 * @param context
	 * @return 上午9:23:50
	 * @author Zhangyuhui AppStack.java TODO 判断网络连接状态
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * @param context
	 * @return 下午1:53:21
	 * @author Zhangyuhui AppStack.java TODO 友盟获取设备信息的方法体。
	 */
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = tm.getDeviceId();
			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}