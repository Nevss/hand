package com.darly.activities.poll;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.darly.activities.app.AppStack;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.NetUtils;
import com.darly.activities.common.PreferenceUserInfor;
import com.darly.activities.common.ToastApp;
import com.darly.activities.ui.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpClient {
	private static final String TAG = HttpClient.class.getName();
	private static final Context context = AppStack.getInstance();

	private HttpClient() {
	}

	private static HttpUtils httpUtils = new HttpUtils();

	public static void post(String url, String s,
			RequestCallBack<String> callBack) {
		if (!NetUtils.isConnected(context)) {
			ToastApp.showToast(context, R.string.neterror);
		} else {
			LogFileHelper.getInstance().i(TAG, "接口地址---->" + url);
			LogFileHelper.getInstance().i(TAG, "param---->" + s.toString());
			RequestParams params = new RequestParams();
			params.addHeader("Content-Type", "application/json;charset=UTF-8");
			params.addHeader("Accept", "application/json");
			params.addHeader("charset", "utf-8");
			try {
				StringEntity se = new StringEntity(s, "utf-8");
				params.setBodyEntity(se);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != PreferenceUserInfor.getToken(context)) {
				String token = PreferenceUserInfor.getToken(context);
				LogFileHelper.getInstance().i(TAG, "token--------->>>" + token);
				params.addHeader("token", token);
			}
			httpUtils.configTimeout(30 * 1000);
			httpUtils.configRequestRetryCount(1);
			httpUtils.configRequestThreadPoolSize(3);
			httpUtils.configResponseTextCharset("utf-8");
			httpUtils.send(HttpMethod.POST, url, params, callBack);

		}
	}

	public static void put(String url, String s,
			RequestCallBack<String> callBack) {
		if (!NetUtils.isConnected(context)) {
			ToastApp.showToast(context, R.string.neterror);
		} else {
			LogFileHelper.getInstance().i(TAG, "接口地址--------->>>" + url);
			LogFileHelper.getInstance().i(TAG, "param---->" + s.toString());
			// httpUtils.configCurrentHttpCacheExpiry(1000*10);
			RequestParams params = new RequestParams();
			params.addHeader("Content-Type", "application/json;charset=UTF-8");
			params.addHeader("Accept", "application/json");
			params.addHeader("charset", "utf-8");
			// params.addBodyParameter("param", s);
			try {
				StringEntity se = new StringEntity(s, "utf-8");
				params.setBodyEntity(se);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != PreferenceUserInfor.getToken(context)) {

				params.addHeader("token", PreferenceUserInfor.getToken(context));

			}
			httpUtils.configTimeout(30 * 1000);
			httpUtils.configRequestRetryCount(1);
			httpUtils.configRequestThreadPoolSize(3);
			httpUtils.configResponseTextCharset("utf-8");
			httpUtils.send(HttpMethod.PUT, url, params, callBack);

		}
	}

	public static void options(String url, String param,
			RequestCallBack<String> callBack) {
		if (!NetUtils.isConnected(context)) {
			ToastApp.showToast(context, R.string.neterror);
		} else {
			LogFileHelper.getInstance().i(TAG, "接口地址--------->>>" + url);
			LogFileHelper.getInstance().i(TAG, "param---->" + param);
			// httpUtils.configCurrentHttpCacheExpiry(1000*10);
			RequestParams params = new RequestParams();
			params.addBodyParameter("param", param);
			params.addHeader("token", PreferenceUserInfor.getToken(context));
			httpUtils.configTimeout(30 * 1000);
			httpUtils.configRequestRetryCount(1);
			httpUtils.configRequestThreadPoolSize(3);
			httpUtils.configResponseTextCharset("utf-8");
			httpUtils.send(HttpMethod.OPTIONS, url, params, callBack);

		}
	}

	public static void get(Context context, String url, RequestParams params,
			RequestCallBack<String> callBack) {
		if (!NetUtils.isConnected(context)) {
			ToastApp.showToast(context, R.string.neterror);
		} else {
			LogFileHelper.getInstance().i(TAG, "接口地址---->" + url);
			if (null != params.getQueryStringParams()) {
				LogFileHelper.getInstance().i(
						TAG,
						"params----->"
								+ params.getQueryStringParams().toString());
			}
			params.addHeader("token", PreferenceUserInfor.getToken(context));
			params.addHeader("Content-Type", "application/json");
			params.addHeader("Accept", "application/json");
			httpUtils.configResponseTextCharset("utf-8");
			httpUtils.configCurrentHttpCacheExpiry(500);
			httpUtils.send(HttpMethod.GET, url, params, callBack);
		}
	}

	public static void getString(Context context, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		httpUtils.send(HttpMethod.GET, url, params, callBack);
	}

	public static void delete(Context context, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		if (!NetUtils.isConnected(context)) {
			ToastApp.showToast(context, R.string.neterror);
		} else {
			LogFileHelper.getInstance().i(TAG, "接口地址---->" + url);
			if (null != params.getQueryStringParams()) {
				LogFileHelper.getInstance().i(
						TAG,
						"params----->"
								+ params.getQueryStringParams().toString());
			}
			params.addHeader("token", PreferenceUserInfor.getToken(context));
			params.addHeader("Content-Type", "application/json");
			params.addHeader("Accept", "application/json");
			httpUtils.configResponseTextCharset("utf-8");
			httpUtils.send(HttpMethod.DELETE, url, params, callBack);
		}
	}

	@SuppressWarnings("rawtypes")
	public static HttpHandler download(final Context context, final String url,
			final String fileneme, final RequestCallBack<File> callBack) {
		httpUtils.configCurrentHttpCacheExpiry(1000 * 10);
		return httpUtils.download(url, fileneme, true, true, callBack);
	}

}
