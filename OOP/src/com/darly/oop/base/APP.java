/**
 * 下午2:27:28
 * @author zhangyh2
 * $
 * APP.java
 * TODO
 */
package com.darly.oop.base;

import android.app.Application;

import com.darly.oop.db.DBMongo;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author zhangyh2 APP $ 下午2:27:28 TODO 系统入口程序。开始初始化的东西。
 */
public class APP extends Application {

	private static APP instance;

	public static APP getInstance() {
		if (instance == null) {
			LogUtils.i("系统初始化错误...");
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		//初始化数据库。
		DBMongo.getInstance();
	}

}
