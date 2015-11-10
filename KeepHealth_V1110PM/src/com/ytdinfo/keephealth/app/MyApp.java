package com.ytdinfo.keephealth.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.hisun.phone.core.voice.model.im.InstanceMsg;
import com.hisun.phone.core.voice.util.Log4Util;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPUtil;
import com.voice.demo.tools.CrashHandler;
import com.voice.demo.tools.preference.CCPPreferenceSettings;
import com.voice.demo.tools.preference.CcpPreferences;
import com.voice.demo.ui.CCPHelper;
import com.voice.demo.ui.model.DemoAccounts;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.model.ChatInfoBean;
import com.ytdinfo.keephealth.utils.DBUtilsHelper;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;

public class MyApp extends Application {
	public static final String TAG = MyApp.class.getName();
	public static ArrayList<String> interphoneIds = null;

	public static ArrayList<String> chatRoomIds;

	public final static String VALUE_DIAL_MODE_FREE = "voip_talk";
	public final static String VALUE_DIAL_MODE_BACK = "back_talk";
	public final static String VALUE_DIAL_MODE_DIRECT = "direct_talk";
	public final static String VALUE_DIAL_MODE = "mode";
	public final static String VALUE_DIAL_SOURCE_PHONE = "srcPhone";
	public final static String VALUE_DIAL_VOIP_INPUT = "VoIPInput";
	public final static String VALUE_DIAL_MODE_VEDIO = "vedio_talk";

	private File vStore;

	private DemoAccounts accounts;
	boolean isRegistBroadCast = false;

	public boolean isRegistBroadCast() {
		return isRegistBroadCast;
	}

	public void setRegistBroadCast(boolean isRegistBroadCast) {
		this.isRegistBroadCast = isRegistBroadCast;
	}

	boolean isDeveloperMode = false;

	public boolean isDeveloperMode() {
		return isDeveloperMode;
	}

	public void setDeveloperMode(boolean isDeveloperMode) {
		this.isDeveloperMode = isDeveloperMode;
	}

	boolean isChecknet = false;

	public boolean isChecknet() {

		return isChecknet;
	}

	public void setChecknet(boolean isChecknet) {

		this.isChecknet = isChecknet;
	}

	private static MyApp instance;

	/**
	 * 单例，返回一个实例
	 * 
	 * @return
	 */
	public static MyApp getInstance() {
		if (instance == null) {
			LogUtil.w("[MyApp] instance is null.");
		}
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		
		LogUtil.i("wpc==", "MyApp---oncreate()");
		
		SharedPrefsUtil.putValue(Constants.ISLOADED, false);
		
		SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT, 0);  
		
		SharedPrefsUtil.putValue(Constants.CHECKISUPDATE, true);
		LogUtil.i("wpc2", "MyApp===true");


		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

		// 友盟统计启动
		MobclickAgent.updateOnlineConfig(this);
		// 禁止默认的页面统计方式
		MobclickAgent.openActivityDurationTrack(false);

		// initSQLiteManager();
		initFileStore();
		initCrashHandler();

		// Sets the default preferences if no value is set yet
		CcpPreferences.loadDefaults();

		boolean firstUse = CcpPreferences.getSharedPreferences().getBoolean(
				CCPPreferenceSettings.SETTINGS_FIRST_USE.getId(),
				((Boolean) CCPPreferenceSettings.SETTINGS_FIRST_USE
						.getDefaultValue()).booleanValue());

		// Display the welcome message?
		if (firstUse) {
			if (getVoiceStore() != null) {
				CCPUtil.delAllFile(getVoiceStore().getAbsolutePath());
			}

			// Don't display again this dialog
			try {
				CcpPreferences.savePreference(
						CCPPreferenceSettings.SETTINGS_FIRST_USE,
						Boolean.FALSE, true);
			} catch (Exception e) {
				/** NON BLOCK **/
			}
		}

		if (interphoneIds == null) {
			interphoneIds = new ArrayList<String>();
		}
		if (chatRoomIds == null) {
			chatRoomIds = new ArrayList<String>();
		}

		// Intent startService = new Intent(this, T9Service.class);
		// startService(startService);

		// MyAppManager.setContext(instance);
		// JPushInterface.init(this); // 初始化 JPush
		// initYuntxIM();
		// initCCPSDK();
		initImageLoader();
		initCCP();
		getSubjectStatus();

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


	ChatInfoBean chatInfoBean;
private void getSubjectStatus() {
	// TODO Auto-generated method stub
	DbUtils dbUtils =DBUtilsHelper.getInstance().getDb();
	if (null ==dbUtils) {
		return;
	}
		try {
			chatInfoBean = dbUtils.findFirst(Selector.from(ChatInfoBean.class)
					.where("status", "=", true));
			if (chatInfoBean == null)
				return;
			RequestParams requestParams = new RequestParams();
			requestParams.addQueryStringParameter("SubjectID",
					chatInfoBean.getSubjectID());
			HttpClient.get(this, Constants.SUBJECTSTATUS, requestParams,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							LogUtil.i(TAG, arg0.result);
							try {
								JSONObject jsonObject = new JSONObject(
										arg0.result);
								JSONObject data = jsonObject
										.getJSONObject("Data");
								String isActive = data.getString("IsActive");
								if (isActive.equals("false")) {
									chatInfoBean.setStatus(false);
									DBUtilsHelper.getInstance().saveChatinfo(
											chatInfoBean);

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initCCP() {
		// TODO Auto-generated method stub
		if (CCPHelper.getInstance().getDevice() == null) {
			
			CCPHelper.getInstance().registerCCP(new CCPHelper.RegistCallBack() {
				@Override
				public void onRegistResult(int reason, String msg) {
					// Log.i("XXX", String.format("%d, %s",
					// reason, msg));
					if (reason == 8192) {
						LogUtil.i(TAG, "通讯云登录成功");
						// requestGetUserGroup();
					} else {
						LogUtil.i(TAG, "通讯云登录失败");
					}
				}
			});
		}
	}

	

	


	/**
	 * 返回当前程序版本名
	 */
	public String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	private void initImageLoader() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "KeepHealth/image");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				// .memoryCacheExtraOptions(480, 800)
				// max width, max height，即保存的每个缓存文件的最大长宽
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
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout
																			// (5
																			// s),
																			// readTimeout
																			// (30
																			// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	private void initCCPSDK() {
		// TODO Auto-generated method stub
		CCPHelper.getInstance().registerCCP(new CCPHelper.RegistCallBack() {

			@Override
			public void onRegistResult(int reason, String msg) {
				// Log.i("XXX", String.format("%d, %s",
				// reason, msg));
				if (reason == 8192) {
					LogUtil.i(TAG, "SDK初始化成功");
				} else {
					LogUtil.i(TAG, "SDK初始化失败");
				}
			}
		});
	}

	private void initCrashHandler() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	public void initSQLiteManager() {
		CCPSqliteManager.getInstance();
		Log4Util.d(CCPHelper.DEMO_TAG, "CCPApplication.initSQLiteManager");
	}

	private void initFileStore() {
		if (!CCPUtil.isExistExternalStore()) {
			Toast.makeText(getApplicationContext(), R.string.media_ejected,
					Toast.LENGTH_LONG).show();
			return;
		}
		File directory = new File(Environment.getExternalStorageDirectory(),
				CCPUtil.DEMO_ROOT_STORE);
		if (!directory.exists() && !directory.mkdirs()) {
			Toast.makeText(getApplicationContext(),
					"Path to file could not be created", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		vStore = directory;
	}

	public File getVoiceStore() {
		if (vStore == null || vStore.exists()) {
			initFileStore();
		}
		return vStore;
	}

	public void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	public void showToast(int resId) {
		Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * User-Agent
	 * 
	 * @return user-agent
	 */
	public String getUser_Agent() {
		String ua = "Android;" + getOSVersion() + ";"
				+ com.hisun.phone.core.voice.Build.SDK_VERSION + ";"
				+ com.hisun.phone.core.voice.Build.LIBVERSION.FULL_VERSION
				+ ";" + getVendor() + "-" + getDevice() + ";";

		ua = ua + getDevicNO() + ";" + System.currentTimeMillis() + ";";

		Log4Util.d(CCPHelper.DEMO_TAG, "User_Agent : " + ua);
		return ua;
	}

	public String getDevicNO() {
		if (!TextUtils.isEmpty(getDeviceId())) {
			return getDeviceId();
		}

		if (!TextUtils.isEmpty(getMacAddress())) {
			return getMacAddress();
		}
		return " ";
	}

	public String getDeviceId() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			return telephonyManager.getDeviceId();
		}

		return null;

	}

	public String getMacAddress() {
		// start get mac address
		WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifiMan != null) {
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			if (wifiInf != null && wifiInf.getMacAddress() != null) {
				// 48位，如FA:34:7C:6D:E4:D7
				return wifiInf.getMacAddress();
			}
		}
		return null;
	}

	/**
	 * device model name, e.g: GT-I9100
	 * 
	 * @return the user_Agent
	 */
	public String getDevice() {
		return Build.MODEL;
	}

	/**
	 * device factory name, e.g: Samsung
	 * 
	 * @return the vENDOR
	 */
	public String getVendor() {
		return Build.BRAND;
	}

	/**
	 * @return the SDK version
	 */
	public int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * @return the OS version
	 */
	public String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * Retrieves application's version number from the manifest
	 * 
	 * @return versionName
	 */
	public String getVersion() {
		String version = "0.0.0";
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}


	/**
	 * 
	 * @param mode
	 */
	public void setAudioMode(int mode) {
		AudioManager audioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager != null) {
			audioManager.setMode(mode);
		}
	}

	/**
	 * 
	 * @param phoneNum
	 */
	public void startCalling(String phoneNum) {
		try {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"
					+ phoneNum));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			// showToast(R.string.toast_call_phone_error);
		}
	}

	public void quitApp() {
		System.exit(0);
	}

	public static HashMap<String, InstanceMsg> rMediaMsgList = new HashMap<String, InstanceMsg>();

	public InstanceMsg getMediaData(String key) {
		if (key != null) {
			return rMediaMsgList.get(key);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 * @param list
	 */
	public void putMediaData(String key, InstanceMsg obj) {
		if (key != null && obj != null) {
			rMediaMsgList.put(key, obj);
		}
	}

	public void removeMediaData(String key) {
		if (key != null) {
			rMediaMsgList.remove(key);
		}
	}

	public HashMap<String, InstanceMsg> getMediaMsgList() {
		return rMediaMsgList;
	}

	private HashMap<String, Object> dataMap = new HashMap<String, Object>();

	/**
	 * @param key
	 * @param list
	 */
	public void putData(String key, Object obj) {
		if (key != null && obj != null) {
			dataMap.put(key, obj);
		}
	}

	public void removeData(String key) {
		if (key != null) {
			dataMap.remove(key);
		}
	}

	public Object getData(String key) {
		if (key != null) {
			return dataMap.get(key);
		} else {
			return null;
		}
	}

	/**
	 * To obtain the system preferences to save the file to edit the object
	 * 
	 * @return
	 */
	public Editor getSharedPreferencesEditor() {
		SharedPreferences cCPreferences = getSharedPreferences(
				CcpPreferences.CCP_DEMO_PREFERENCE, MODE_PRIVATE);
		Editor edit = cCPreferences.edit();

		return edit;
	}

	/**
	 * To obtain the system preferences to save the file to edit the object
	 * 
	 * @return
	 */
	public SharedPreferences getSharedPreferences() {
		return getSharedPreferences(CcpPreferences.CCP_DEMO_PREFERENCE,
				MODE_PRIVATE);
	}

	/**
	 * 获取动态时间
	 * 
	 * @param key
	 * @return
	 */
	public String getSettingParams(String key) {
		SharedPreferences settings = getSharedPreferences();
		return settings.getString(key, "");
	}

	/**
	 * 保存动态时间
	 * 
	 * @param key
	 * @param value
	 */
	public void saveSettingParams(String key, String value) {
		SharedPreferences settings = getSharedPreferences();
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 清除动态时间
	 * 
	 * @param key
	 * @param value
	 */
	public void clearSettingParams() {
		SharedPreferences settings = getSharedPreferences();
		settings.edit().clear().commit();
	}

	/**
	 * 删除配置
	 * 
	 * @param key
	 */
	public void removeSettingParam(String key) {
		SharedPreferences settings = getSharedPreferences(
				"Dynamic_Time_Preferences", 0);
		settings.edit().remove(key).commit();
	}

	public void putDemoAccounts(DemoAccounts demoAccounts) {
		accounts = demoAccounts;
	}

	public DemoAccounts getDemoAccounts() {
		return accounts;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("bitherjni");

	}
}
