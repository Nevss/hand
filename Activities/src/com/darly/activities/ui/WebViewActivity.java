package com.darly.activities.ui;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.base.BaseWebView;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.common.PreferencesJsonCach;
import com.darly.activities.common.ToastApp;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_webview)
public class WebViewActivity extends BaseActivity {
	/**
	 * 上午11:51:49 TODO WebView 基础类
	 */
	private BaseWebView webview;
	@ViewInject(R.id.id_rl)
	private RelativeLayout rl;
	@ViewInject(R.id.id_bt_update)
	private Button bt_update;

	private String current_url;

	private boolean isPageLoaded = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_bt_update:
			LogApp.i(TAG, "重新加载");
			LogApp.i(TAG, current_url);
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		webview = new BaseWebView(this);
		webViewListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	private void webViewListener() {
		webview.setWebViewClient(new WebViewClient() {
			// 加载失败
			@SuppressWarnings("deprecation")
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				LogApp.i(TAG, "加载失败===" + errorCode + "---" + description
						+ "---" + failingUrl + "---");

				current_url = failingUrl;
				webview.clearView();
				if (failingUrl.contains("#")) {
					String[] temp;
					temp = failingUrl.split("#");
					webview.loadUrl(temp[0]);
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					webview.loadUrl(failingUrl);
				} else {
					webview.loadUrl("file:///android_asset/error.html#"
							+ failingUrl);
				}

				ToastApp.showToast(getApplicationContext(), "页面加载失败，请点击重新加载");

				// super.onReceivedError(view, errorCode, description,
				// failingUrl);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * "WebViewClient.shouldOverrideUrlLoading",
				 * Toast.LENGTH_SHORT);
				 */
				LogApp.i(TAG, "拦截url---shouldOverrideUrlLoading-->" + url);
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse(url));
					startActivity(intent);
				} else {
					HashMap<String, String> hashmap = new HashMap<String, String>();
					if (null != PreferencesJsonCach.getValue(Literal.TOKEN,
							null)) {
						hashmap.put("token", PreferencesJsonCach.getValue(
								Literal.TOKEN, null));
					}
					webview.loadUrl(url, hashmap);
				}

				return true;
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				isPageLoaded = false;
				LogApp.i(TAG, "拦截url---onPageStarted-->" + url);
				HashMap<String, String> hashmap = new HashMap<String, String>();
				if (null != PreferencesJsonCach.getValue(Literal.TOKEN, null)) {
					hashmap.put("token",
							PreferencesJsonCach.getValue(Literal.TOKEN, null));
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				isPageLoaded = true;
				LogApp.i(TAG, "页面加载完后==onPageFinished==" + url);

			}
		});
	}

	/**
	 * 同步一下cookie
	 */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		String token = PreferencesJsonCach.getValue(Literal.TOKEN, null);
		cookieManager.setCookie(url, "token=" + token + ";path=/");// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogApp.i(TAG, "==onKeyDown==");
			if (isPageLoaded) {
				webview.loadUrl("javascript:CommonRedirect.goBack()");
			} else {
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
			}
		}
		return false;
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToIndex() {
		Log.i(TAG, "goToIndex()");
		finish();
	}

	/**
	 * JS调用的方法
	 */
	@SuppressWarnings("deprecation")
	@JavascriptInterface
	public void reloadUrl(String url) {
		Log.i(TAG, "reloadUrl()");
		HashMap<String, String> hashmap = new HashMap<String, String>();
		if (null != PreferencesJsonCach.getValue(Literal.TOKEN, null)) {
			hashmap.put("token",
					PreferencesJsonCach.getValue(Literal.TOKEN, null));
		}
		webview.clearView();
		webview.loadUrl(url, hashmap);
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToUpdateUrl() {
		Log.i(TAG, "goToUpdateUrl()");
		webview.loadUrl("file:///android_asset/a.html？'" + current_url + "'");
		// finish();
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToPhone(int number) {
		Log.i(TAG, "goToPhone()");
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ number));
		startActivity(intent);

		// finish();
	}

}
