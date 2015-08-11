package com.darly.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.darly.model.Login;

/**
 * @ClassName: FilePerferenceHelp
 * @Description: TODO(密码存入数据类（包括操作）)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月26日 上午9:02:01
 *
 */
public class FilePerferenceHelp {

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:54:00 Project_Name:DFram
	 * Description:此方法判断用户是否登录成功 Throws boolean
	 */
	public static boolean isLogining(String filename, Context context, long det) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		int time = preferences.getInt("login_time", 0);
		int timeout = preferences.getInt("timeout", 0);
		boolean isLog = preferences.getBoolean("isLog", false);
		boolean a = (det - time) >= timeout;
		LogApp.i("isLogining", "" + ((a && isLog) ? true : false));
		return (a && isLog) ? true : false;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:54:07 Project_Name:DFram
	 * Description:判断用户数据是否成功存储 Throws boolean
	 */
	public static boolean isSave(String filename, Context context, Login login) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Log.i("perference", login.member_name + login.member_id);
		Editor editor = preferences.edit();
		editor.putInt("member_id", login.member_id);
		editor.putString("member_name", login.member_name);
		editor.putInt("login_time", login.login_time);
		editor.putInt("timeout", login.timeout);
		editor.putBoolean("isLog", true);
		editor.putString("login", login.member_info);
		editor.commit();
		return true;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:54:50 Project_Name:DFram
	 * Description:判断用户信息是否删除成功 Throws boolean
	 */
	public static boolean delete(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		return true;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:55:11 Project_Name:DFram
	 * Description:从文件中获取用户的Member_id Throws int
	 */
	public static int getMember_id(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);

		return preferences.getInt("member_id", 0);
	}

	public static String getLogin(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		return preferences.getString("login", "");
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:55:11 Project_Name:DFram
	 * Description:从文件中获取用户的名称 Throws int
	 */
	public static String getUser(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);

		return preferences.getString("member_name", "");
	}

	/**
	 * Title: firstInstall Description: 用户是否是首次安装。 param @return return boolean
	 * throws
	 */
	public static boolean firstInstall(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		return preferences.getBoolean("firstInstall", false);
	}

	public static void firstInstallSave(String filename, Context context,
			String time, Boolean firstInstall) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("time", time);
		editor.putBoolean("firstInstall", firstInstall);
		editor.commit();
	}

	/**
	 * @Title: saveUserInfo
	 * @Description: 记录用户的登录信息（用户名和密码，点击记住我的时候启用。）
	 * @param @param name
	 * @param @param pass
	 * @return void
	 * @throws
	 */
	public static void saveUserInfo(String filename, Context context,
			String name, String pass) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putString("pass", pass);
		editor.commit();
	}

	/**
	 * @Title: getUserName
	 * @Description: 获取用户名
	 * @param @param filename
	 * @param @param context
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getUserName(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		return preferences.getString("name", "");
	}

	/**
	 * @Title: getUserPass
	 * @Description: 获取密码
	 * @param @param filename
	 * @param @param context
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getUserPass(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		return preferences.getString("pass", "");
	}

	/**
	 * @Title: cleanUserInfo
	 * @Description: 删除用户名密码信息
	 * @param @param filename
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean cleanUserInfo(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		return true;
	}

	/**
	 * @Title: saveAddress
	 * @Description: 保存地区JSON
	 * @param @param filename
	 * @param @param context
	 * @param @param address
	 * @return void
	 * @throws
	 */
	public static void saveAddress(String filename, Context context,
			String address) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("address", address);
		editor.commit();
	}

	/**
	 * @Title: getAddress
	 * @Description: TODO
	 * @param @param 获取保存的地区JSON
	 * @param @param context
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getAddress(String filename, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		return preferences.getString("address", "");
	}

}
