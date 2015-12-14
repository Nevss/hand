package com.darly.activities.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.darly.activities.R;

/**
 * @ClassName: NetUtils
 * @Description: TODO(跟网络相关的工具类,1.判断网络是否连接,2.判断是否是wifi连接,3.打开网络设置界面)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年10月24日 下午3:05:04
 *
 */
public class NetUtils {
	private NetUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:58:37 Project_Name:DFram
	 * Description:TODO(判断网络是否连接) Throws boolean
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity) {

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:59:16 Project_Name:DFram
	 * Description:TODO(判断是否是wifi连接) Throws Return:boolean
	 */
	public static boolean isWifi(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return networkInfo.isConnectedOrConnecting();
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:59:26 Project_Name:DFram
	 * Description:TODO(打开网络设置界面) Throws Return:void
	 */
	public static void openSetting(final Activity activity) {
		// Intent intent = new Intent("/");
		// ComponentName cm = new ComponentName("com.android.settings",
		// "com.android.settings.WirelessSettings");
		// intent.setComponent(cm);
		// intent.setAction("android.intent.action.VIEW");
		// activity.startActivityForResult(intent, 0);

		new AlertDialog.Builder(activity)
				.setMessage(R.string.neterror)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Go to the activity of settings of wireless
						Intent intentToNetwork = new Intent("/");
						ComponentName componentName = new ComponentName(
								"com.android.settings",
								"com.android.settings.WirelessSettings");
						intentToNetwork.setComponent(componentName);
						intentToNetwork.setAction("android.intent.action.VIEW");

						// 3.2以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
						if (android.os.Build.VERSION.SDK_INT > 13) {
							activity.startActivity(new Intent(
									android.provider.Settings.ACTION_SETTINGS));
						} else {
							activity.startActivity(new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS));
						}
						// activity.startActivity(intentToNetwork);
						dialog.cancel();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

}
