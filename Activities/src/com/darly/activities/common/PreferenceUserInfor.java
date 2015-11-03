/**
 * 下午2:20:18
 * @author Zhangyuhui
 * PreferenceUserInfor.java
 * TODO
 */
package com.darly.activities.common;

import com.darly.activities.ui.login.LoginAcitvity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Zhangyuhui PreferenceUserInfor 下午2:20:18 TODO
 */
public class PreferenceUserInfor {
	public final static String SETTING = "userinfo_preference";

	private static final String TOKEN = "token";

	public static void saveUserInfor(String key, String value, Context context) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putString(key, value);
		sp.commit();
	}

	public static void saveToken(String value, Context context) {
		Editor sp = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
				.edit();
		sp.putString(TOKEN, value);
		sp.commit();
	}

	public static String getToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(TOKEN,
				Context.MODE_PRIVATE);
		return preferences.getString(TOKEN, null);
	}

	public static String getUserInfor(String key, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		return preferences.getString(key, null);
	}

	public static boolean isUserLogin(String key, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		if (preferences.getString(key, null) == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void intenTO(Context context) {
		context.startActivity(new Intent(context, LoginAcitvity.class));
	}

	public static void cleanUserInfor(Context context) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.clear();
		sp.commit();
	}

	private static String Lagu = "lagu";

	/**
	 * @param value
	 * @param context
	 *            上午11:56:19
	 * @author Zhangyuhui PreferenceUserInfor.java TODO 保存当前使用语言
	 */
	public static void saveLagu(String value, Context context) {
		Editor sp = context.getSharedPreferences(Lagu, Context.MODE_PRIVATE)
				.edit();
		sp.putString(Lagu, value);
		sp.commit();
	}

	/**
	 * @param context
	 * @return 上午11:56:22
	 * @author Zhangyuhui PreferenceUserInfor.java TODO 获取当前使用语言。
	 */
	public static String getLagu(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(Lagu,
				Context.MODE_PRIVATE);
		return preferences.getString(Lagu, null);
	}

}
