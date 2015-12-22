/**
 * 下午2:27:28
 * @author zhangyh2
 * $
 * APP.java
 * TODO
 */
package com.darly.oop.base;

import org.apkplug.app.FrameworkFactory;
import org.apkplug.app.FrameworkInstance;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.darly.oop.common.ToastOOP;
import com.darly.oop.db.DBMongo;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author zhangyh2 APP $ 下午2:27:28 TODO 系统入口程序。开始初始化的东西。
 */
public class APP extends Application {

	private static APP instance;

	private FrameworkInstance frame;

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
		LogUtils.allowI = true; // 关闭 LogUtils.i(...) 的 adb log 输出
		// 初始化数据库。
		DBMongo.getInstance();
		startApkplug(this);
		if (APPEnum.WIDTH.getLen() == 0 || APPEnum.HEIGHT.getLen() == 0) {
			getParamsWithWH();
		}
	}

	/**
	 * 
	 * 下午1:53:28
	 * 
	 * @author zhangyh2 APP.java TODO
	 */
	@SuppressWarnings("deprecation")
	private void getParamsWithWH() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) instance
				.getSystemService(Context.WINDOW_SERVICE);
		APPEnum.WIDTH.setLen(wm.getDefaultDisplay().getWidth());
		APPEnum.HEIGHT.setLen(wm.getDefaultDisplay().getHeight());
	}

	public FrameworkInstance getFrame() {
		return frame;
	}

	private void startApkplug(Context context) {
		try {
			frame = FrameworkFactory.getInstance().start(null, context);
			LogUtils.i("插件启动成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ToastOOP.showToast(context, "插件管理初始化异常，请尝试重新打开应用");
			e.printStackTrace();
		}
	}
}
