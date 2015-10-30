package com.darly.activities.common;

import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ClassName: SPUtils
 * @Description: TODO(保存在手机里面的文件,可以作为JSON缓存。。)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年10月24日 下午3:07:50
 *
 */
public class SPUtils {
	public SPUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 保存在手机里面的文件名
	 */
	public static final String FILE_NAME = "share_data";

	/**
	 * Auther:张宇辉
	 * User:zhangyuhui
	 * 2015年1月5日
	 * 上午10:01:05
	 * Project_Name:DFram
	 * Description:TODO(保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法)
	 * Throws
	 * Return:void
	 */ 
	public static void put(Context context, String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * Auther:张宇辉
	 * User:zhangyuhui
	 * 2015年1月5日
	 * 上午10:01:20
	 * Project_Name:DFram
	 * Description:TODO(得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值)
	 * Throws
	 * Return:Object
	 */ 
	public static Object get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);

		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * Auther:张宇辉
	 * User:zhangyuhui
	 * 2015年1月5日
	 * 上午10:01:35
	 * Project_Name:DFram
	 * Description:TODO(移除某个key值已经对应的值)
	 * Throws
	 * Return:void
	 */ 
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * Auther:张宇辉
	 * User:zhangyuhui
	 * 2015年1月5日
	 * 上午10:01:46
	 * Project_Name:DFram
	 * Description:TODO(清除所有数据)
	 * Throws
	 * Return:void
	 */ 
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * Auther:张宇辉
	 * User:zhangyuhui
	 * 2015年1月5日
	 * 上午10:01:55
	 * Project_Name:DFram
	 * Description:TODO(查询某个key是否已经存在)
	 * Throws
	 * Return:boolean
	 */ 
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * Auther:张宇辉
	 * User:zhangyuhui
	 * 2015年1月5日
	 * 上午10:02:07
	 * Project_Name:DFram
	 * Description:TODO(返回所有的键值对)
	 * Throws
	 * Return:Map<String,?>
	 */ 
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	* @ClassName: SharedPreferencesCompat
	* @Description: TODO(创建一个解决SharedPreferencesCompat.apply方法的一个兼容类)
	* @author 张宇辉 zhangyuhui@octmami.com
	* @date 2015年1月5日 上午10:02:20
	*
	*/ 
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * Auther:张宇辉
		 * User:zhangyuhui
		 * 2015年1月5日
		 * 上午10:02:37
		 * Project_Name:DFram
		 * Description:TODO(反射查找apply的方法)
		 * Throws
		 * Return:Method
		 */ 
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				LogFileHelper.getInstance().e("SPUtils", e.getMessage());
			}

			return null;
		}

		/**
		 * Auther:张宇辉
		 * User:zhangyuhui
		 * 2015年1月5日
		 * 上午10:02:48
		 * Project_Name:DFram
		 * Description:TODO(如果找到则使用apply执行，否则使用commit)
		 * Throws
		 * Return:void
		 */ 
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (Exception e) {
				LogFileHelper.getInstance().e("SPUtils", e.getMessage());
			}
			editor.commit();
		}
	}

}