package com.darly.activities.poll;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.darly.activities.common.LogFileHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author zhangyuhui 以线程池模式进行的网络数据请求。请求回来的数据，使用Handler，返回到对应目标位置。
 */
public class HttpTasker extends ThreadPoolTask {

	private String TAG = getClass().getName();

	/**
	 * 上午9:50:29
	 * TODO 用户需要传递的GetPost请求辅助参数
	 */
	private List<BasicNameValuePair> params;
	/**
	 * 上午9:50:52
	 * TODO 请求链接
	 */
	private String url;
	/**
	 * 上午9:51:03
	 * TODO 用户需要返回的Model类
	 */
	private TypeToken<?> token;
	/**
	 * 上午9:51:23
	 * TODO 传入Handler
	 */
	private Handler handler;
	/**
	 * 上午9:51:37
	 * TODO 是否为Get请求
	 */
	private boolean isGet;
	/**
	 * 上午9:51:49
	 * TODO 请求返回码，返回Handler对应方法。
	 */
	private int handlerCode;

	public boolean isLog = true;

	/**
	 * @param context 上下文关系。
	 * @param params 用户需要传递的GetPost请求辅助参数
	 * @param url 请求链接
	 * @param token 用户需要返回的Model类
	 * @param handler 传入Handler
	 * @param isGet 是否为Get请求
	 * @param handlerCode 请求返回码，返回Handler对应方法。
	 * 上午9:52:33
	 * @author Zhangyuhui
	 * HttpTasker.java
	 * TODO 常规化的GETPOST请求。
	 */
	public HttpTasker(Context context, List<BasicNameValuePair> params,
			String url, TypeToken<?> token, Handler handler, boolean isGet,
			int handlerCode) {
		super(context);
		this.params = params;
		this.url = url;
		this.token = token;
		this.handler = handler;
		this.isGet = isGet;
		this.handlerCode = handlerCode;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
		Message message = new Message();
		if (isGet) {
			// 通过Get请求返回Model
			message.obj = doGetForJson(url, params, token);
		} else {
			// 通过Post请求返回Model
			message.obj = doPostForJson(url, params, token);
		}
		message.what = handlerCode;
		handler.sendMessage(message);
	}

	private static final int TIMEOUT_IN_MILLIONS = 4000;

	/**
	 * Title: doPostForJson Description: 发送请求的 URL,直接返回JSON数据解析。返回集合列表 return T
	 * throws
	 */
	private <T> T doPostForJson(String url, List<BasicNameValuePair> params,
			TypeToken<?> token) {
		InputStream is = null;
		InputStreamReader in = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();// 使用DefaultHttpClient创建HttpClient实例
			HttpPost post = new HttpPost(url);// 构建HttpPost
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);// 使用编码构建Post实体
			post.setEntity(entity);// 执行Post方法
			HttpResponse response = httpClient.execute(post);// 获取返回实体
			is = response.getEntity().getContent();
			in = new InputStreamReader(is);
			if (isLog) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int lenth = 0;
				while ((lenth = is.read(buffer)) != -1) {
					bos.write(buffer, 0, lenth);
					bos.flush();
				}
				bos.close();
				String json = bos.toString();
				LogFileHelper.getInstance().i(TAG, post.getURI().toString());
				LogFileHelper.getInstance().i(TAG, json);
				return new Gson().fromJson(json, token.getType());
			} else {
				return new Gson().fromJson(in, token.getType());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e(getClass().getSimpleName(),
					e.getMessage());
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (is != null) {
					is.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				LogFileHelper.getInstance().e(getClass().getSimpleName(),
						e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Title: doGetForJson Description: 发送请求的 URL,直接返回JSON数据解析。返回集合列表 return T
	 * throws
	 * 
	 * @param isString
	 */
	private <T> T doGetForJson(String url, List<BasicNameValuePair> params,
			TypeToken<?> token) {
		InputStream is = null;
		InputStreamReader in = null;
		StringBuffer geturl = new StringBuffer();
		geturl.append(url);

		if (params != null) {
			geturl.append("?");
			for (BasicNameValuePair item : params) {
				geturl.append(item.getName() + "=" + item.getValue() + "&");
			}
			geturl.deleteCharAt(geturl.length() - 1);
		}
		try {
			URL realUrl = new URL(new String(geturl.toString().getBytes(),
					"ISO-8859-1"));
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性

			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");

			// 定义BufferedReader输入流来读取URL的响应
			is = conn.getInputStream();
			in = new InputStreamReader(is);
			if (isLog) {
				Log.i(TAG, realUrl.toString());
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int lenth = 0;
				while ((lenth = is.read(buffer)) != -1) {
					bos.write(buffer, 0, lenth);
					bos.flush();
				}
				bos.close();
				String json = bos.toString();
				LogFileHelper.getInstance().i(TAG, realUrl.toString());
				LogFileHelper.getInstance().i(TAG, json);
				return new Gson().fromJson(json, token.getType());
			} else {
				return new Gson().fromJson(in, token.getType());
			}
		} catch (Exception e) {
			LogFileHelper.getInstance().e(getClass().getSimpleName(),
					e.getMessage());
		}

		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				LogFileHelper.getInstance().e(getClass().getSimpleName(),
						e.getMessage());
			}
		}
		return null;
	}
}
