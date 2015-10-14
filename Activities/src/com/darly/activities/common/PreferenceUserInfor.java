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

	public static void saveUserInfor(String key, String value, Context context) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putString(key, value);
		sp.commit();
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

}
