/**
 * 下午2:37:12
 * @author Zhangyuhui
 * HTTPSevTasker.java
 * TODO
 */
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

import com.darly.activities.common.CrashHandler;
import com.darly.activities.common.LogFileHelper;

/**
 * @author Zhangyuhui HTTPSevTasker 下午2:37:12 TODO 为了测试外置接口，专注一个请求类。
 */
public class HTTPSevTasker extends ThreadPoolTask {

	private String TAG = getClass().getName();
	private static final int TIMEOUT_IN_MILLIONS = 4000;

	private List<BasicNameValuePair> params;
	private String url;
	private Handler handler;
	private boolean isGet;
	private int handlerCode;
	private List<BasicNameValuePair> paramsProperty;

	public boolean isLog = true;

	public HTTPSevTasker(Context context, List<BasicNameValuePair> params,
			String url, Handler handler, boolean isGet, int handlerCode,
			List<BasicNameValuePair> paramsProperty) {
		super(context);
		this.params = params;
		this.url = url;
		this.handler = handler;
		this.isGet = isGet;
		this.handlerCode = handlerCode;
		this.paramsProperty = paramsProperty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
		Message message = new Message();
		if (isGet) {
			// 通过Get请求返回字符串
			message.obj = doGetForString(url, params);
		} else {
			// 通过Post请求返回字符串
			message.obj = doPostForString(url, params);
		}
		message.what = handlerCode;
		handler.sendMessage(message);

	}

	/**
	 * Title: doGetForJson Description: 发送请求的 URL,直接返回JSON数据解析。返回集合列表 return T
	 * throws
	 * 
	 * @param isString
	 */
	private String doGetForString(String url, List<BasicNameValuePair> params) {
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
			if (paramsProperty != null) {
				for (BasicNameValuePair basic : paramsProperty) {
					conn.setRequestProperty(basic.getName(), basic.getValue());
				}
			}

			// 定义BufferedReader输入流来读取URL的响应
			is = conn.getInputStream();
			in = new InputStreamReader(is);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int lenth = 0;
			while ((lenth = is.read(buffer)) != -1) {
				bos.write(buffer, 0, lenth);
				bos.flush();
			}
			bos.close();
			String json = bos.toString();
			if (isLog) {
				LogFileHelper.getInstance().i(TAG, realUrl.toString());
				LogFileHelper.getInstance().i(TAG, json);
			}
			return json;
		} catch (Exception e) {
			LogFileHelper.getInstance().e(getClass().getSimpleName(),
					e.getMessage());
			CrashHandler.getInstance().uncaughtException(
					Thread.currentThread(), e);
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
				CrashHandler.getInstance().uncaughtException(
						Thread.currentThread(), e);
			}
		}
		return null;
	}

	private String doPostForString(String url, List<BasicNameValuePair> params) {
		InputStream is = null;
		InputStreamReader in = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();// 使用DefaultHttpClient创建HttpClient实例
			HttpPost post = new HttpPost(url);// 构建HttpPost
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					HTTP.UTF_8);// 使用编码构建Post实体
			post.setEntity(entity);// 执行Post方法
			if (paramsProperty != null) {
				for (BasicNameValuePair basic : paramsProperty) {
					post.setHeader(basic.getName(), basic.getValue());
				}
			}
			HttpResponse response = httpClient.execute(post);// 获取返回实体
			is = response.getEntity().getContent();
			in = new InputStreamReader(is);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int lenth = 0;
			while ((lenth = is.read(buffer)) != -1) {
				bos.write(buffer, 0, lenth);
				bos.flush();
			}
			bos.close();
			String json = bos.toString();
			if (isLog) {
				LogFileHelper.getInstance().i(TAG, post.getURI().toString());
				LogFileHelper.getInstance().i(TAG, json);
			}
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e(getClass().getSimpleName(),
					e.getMessage());
			CrashHandler.getInstance().uncaughtException(
					Thread.currentThread(), e);
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
				CrashHandler.getInstance().uncaughtException(
						Thread.currentThread(), e);
			}
		}
		return null;
	}
}
