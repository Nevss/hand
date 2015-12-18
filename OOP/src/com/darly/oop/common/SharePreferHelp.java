/**
 * 下午3:13:26
 * @author zhangyh2
 * $
 * SharePreferHelp.java
 * TODO
 */
package com.darly.oop.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.darly.oop.base.APP;

/**
 * @author zhangyh2 SharePreferHelp $ 下午3:13:26 TODO
 */
/**
 * @author zhangyh2
 * SharePreferHelp
 * $
 * 下午3:31:38
 * TODO 缓存文件类
 */
public class SharePreferHelp {

	private static final Context context = APP.getInstance()
			.getApplicationContext();

	public static final String SETTING = "oop";

	/**
	 * @param key
	 * @param value
	 *            下午3:18:23
	 * @author zhangyh2 SharePreferHelp.java TODO 保存int类型的数据
	 */
	public static void putValue(String key, int value) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putInt(key, value);
		sp.commit();
	}

	/**
	 * @param key
	 * @param value
	 *            下午3:18:38
	 * @author zhangyh2 SharePreferHelp.java TODO保存Long类型的数据
	 */
	public static void putValue(String key, Long value) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putLong(key, value);

		sp.commit();
	}

	/**
	 * @param key
	 * @param value
	 *            下午3:18:53
	 * @author zhangyh2 SharePreferHelp.java TODO保存boolean类型的数据
	 */
	public static void putValue(String key, boolean value) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	/**
	 * @param key
	 * @param value
	 *            下午3:19:06
	 * @author zhangyh2 SharePreferHelp.java TODO保存String类型的数据
	 */
	public static void putValue(String key, String value) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putString(key, value);
		sp.commit();
	}

	/**
	 * @param key
	 * @param defValue
	 * @return 下午3:19:14
	 * @author zhangyh2 SharePreferHelp.java TODO获取int类型的数据
	 */
	public static int getValue(String key, int defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		int value = sp.getInt(key, defValue);
		return value;
	}

	/**
	 * @param key
	 * @param defValue
	 * @return 下午3:19:24
	 * @author zhangyh2 SharePreferHelp.java TODO获取long类型的数据
	 */
	public static long getValue(String key, long defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		long value = sp.getLong(key, defValue);
		return value;
	}

	/**
	 * @param key
	 * @param defValue
	 * @return 下午3:19:32
	 * @author zhangyh2 SharePreferHelp.java TODO获取boolean类型的数据
	 */
	public static boolean getValue(String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}

	/**
	 * @param key
	 * @param defValue
	 * @return 下午3:19:41
	 * @author zhangyh2 SharePreferHelp.java TODO获取String类型的数据
	 */
	public static String getValue(String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		String value = sp.getString(key, defValue);
		return value;
	}

	/**
	 * @param key
	 *            下午3:19:50
	 * @author zhangyh2 SharePreferHelp.java TODO移除某项
	 */
	public static void remove(String key) {
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

}
