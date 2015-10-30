package com.darly.activities.common;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

public class PreferencesJsonCach {
	public final static String SETTING = "ytdinfo_preference";

	public static void putValue(String key, String value, Context context) {
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putString(key, value);
		sp.commit();
	}

	public static String getInfo(String key, Context context) {
		return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.getString(key, null);
	}

	public static String getValue(int key, Context context) {
		return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.getString(key + "", null);
	}

	/**
	 * @param url
	 * @param bitmap
	 *            上午10:53:39
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO 将Bitmap保存到文件。
	 */
	public static void saveBitmap(String url, Bitmap bitmap, String TAG) {
		LogFileHelper.getInstance().i(TAG, "保存图片");
		File f = new File(url);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			LogFileHelper.getInstance().i(TAG, "已经保存");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance()
					.e("PreferencesJsonCach", e.getMessage());
		}
	}
}
