package com.darly.oop.common;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @ClassName: ToastApp
 * @Description: TODO(Toast统一管理类)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年10月24日 下午3:08:55
 *
 */
public class ToastOOP {

	private static final int TOASTTIME = 100;

	public static boolean isShow = true;

	private static Toast toast = null;

	/**
	 * @auther Darly Fronch 2015 下午2:25:56 TODO显示Toast
	 */
	public static void showToast(Context context, String msg) {
		if (!isShow)
			return;

		if (toast == null) {
			toast = Toast.makeText(context, msg, TOASTTIME);
		} else {
			toast.setText(msg);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToast(Context context, int msg) {
		if (!isShow)
			return;

		if (toast == null) {
			toast = Toast.makeText(context, msg, TOASTTIME);
		} else {
			toast.setText(msg);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}