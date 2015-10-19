/**
 * 上午11:26:46
 * @author Zhangyuhui
 * BaseWebView.java
 * TODO
 */
package com.darly.activities.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author Zhangyuhui BaseWebView 上午11:26:46 TODO 基础的WebView
 */
public class BaseWebView extends WebView {

	private long last_time = 0;

	/**
	 * @param context
	 *            上午11:27:23
	 * @author Zhangyuhui BaseWebView.java TODO
	 */
	public BaseWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initWebView(context);
	}

	/**
	 * @param context
	 * 上午11:52:56
	 * @author Zhangyuhui
	 * BaseWebView.java
	 * TODO
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(Context context) {
		// TODO Auto-generated method stub
			// 实例化WebView对象
			WebSettings webSettings = getSettings();
			// 设置WebView属性，能够执行Javascript脚本
			webSettings.setJavaScriptEnabled(true);
			webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
			//
			webSettings.setUseWideViewPort(true);// 关键点
			webSettings.setLoadWithOverviewMode(true);
			webSettings.setAppCacheEnabled(true);
			webSettings.setAppCacheMaxSize(8 * 1024 * 1024); // 8MB
			String appCacheDir = getContext()
					.getDir("cache", Context.MODE_PRIVATE).getPath();
			webSettings.setAppCachePath(appCacheDir);
			webSettings.setAllowFileAccess(true);
			webSettings.setDomStorageEnabled(true);
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
			// js调用安卓方法
			addJavascriptInterface(this, "RedirectListner");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.webkit.WebView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			long current_time = System.currentTimeMillis();
			long d_time = current_time - last_time;
			System.out.println(d_time);
			;
			if (d_time < 300) {
				last_time = current_time;
				return true;
			} else {
				last_time = current_time;
			}
			break;
		}
		return super.onTouchEvent(event);
	}
}
