package com.darly.activities.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.darly.activities.common.LogApp;

/**
 * @ClassName: AppStack
 * @Description: TODO(App堆栈操作)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2015年1月5日 上午10:04:28
 *
 */
public class AppStack extends Application {
	private List<Activity> mList = new LinkedList<Activity>();
	private static AppStack instance;

	private AppStack() {
	}

	public synchronized static AppStack getInstance() {
		if (null == instance) {
			instance = new AppStack();
		}
		return instance;
	}

	// add Activity
	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:04:49 Project_Name:DFram
	 * Description:TODO(对集合中的数据添加一个Activity) Throws Return:void
	 */
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:05:19 Project_Name:DFram
	 * Description:TODO(返回Activity集合) Throws Return:List<Activity>
	 */
	public List<Activity> getList() {
		return mList;
	}


	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:05:36 Project_Name:DFram
	 * Description:TODO(移除某个Activity) Throws Return:void
	 */
	public void remove(Activity act) {
		for (Activity activity : mList) {
			if (activity == act) {
				activity.finish();
			}
			LogApp.i("activity.finish()");
		}
	}

	public Activity deleteLast() {
		if (getSize() == 1) {
			return null;
		}
		return mList.remove(getSize() - 1);
	}

	public void moveToIndex() {
		while (getSize() != 1) {
			LogApp.i(getSize() + "");
			remove(deleteLast());
		}
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:05:53 Project_Name:DFram
	 * Description:TODO(得到集合的长度) Throws Return:int
	 */
	public int getSize() {
		return mList.size();
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:06:09 Project_Name:DFram
	 * Description:TODO(退出时清空集合) Throws Return:void
	 */
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}