package com.darly.common;

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

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * ClassName: GetMsgByJson Description:
 * TODO(获取JSON并封装的一个线程公共类，通过GET传入参数列表，进行封装。将值传回Handler然后在Ui线程中使用。) author 张宇辉
 * zhangyuhui@octmami.com date 2014年11月15日 上午10:41:31
 *
 */
public class GetMsgByJson extends Thread {
	private List<BasicNameValuePair> params;
	private String url;
	private String tag;
	private TypeToken<?> token;
	private Handler handler;
	private boolean isGet;

	private int handlerCode;

	public static boolean isLog = false;

	/**
	 * Title: Description: 这个是带输出Tag的正常实例对象的构造方法
	 * 
	 * @param params
	 * @param tag
	 * @param url
	 * @param token
	 * @param handler
	 * @param isGet
	 * @param handlerCode
	 */
	public GetMsgByJson(List<BasicNameValuePair> params, String tag,
			String url, TypeToken<?> token, Handler handler, boolean isGet,
			int handlerCode) {
		super();
		this.params = params;
		this.tag = tag;
		this.url = url;
		this.token = token;
		this.handler = handler;
		this.isGet = isGet;
		this.handlerCode = handlerCode;
	}

	/**
	 * Title: Description: 这个是不带输出Tag的正常实例对象的构造方法
	 * 
	 * @param params
	 * @param url
	 * @param token
	 * @param handler
	 * @param isGet
	 * @param handlerCode
	 */
	public GetMsgByJson(List<BasicNameValuePair> params, String url,
			TypeToken<?> token, Handler handler, boolean isGet, int handlerCode) {
		super();
		this.params = params;
		this.url = url;
		this.token = token;
		this.handler = handler;
		this.isGet = isGet;
		this.handlerCode = handlerCode;
	}

	@Override
	public void run() {
		Log.i("Threads", Thread.currentThread().getName());
		Message message = new Message();
		if (isGet) {
			message.obj = doGetForJson(url, params, token);
		} else {
			message.obj = doPostForJson(url, params, token);
		}
		message.what = handlerCode;
		handler.sendMessage(message);
	}

	private static final int TIMEOUT_IN_MILLIONS = 5000;

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
				Log.i(tag, url);
				for (BasicNameValuePair basicNameValuePair : params) {
					Log.i(tag, basicNameValuePair.getName() + "="
							+ basicNameValuePair.getValue());
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int lenth = 0;
				while ((lenth = is.read(buffer)) != -1) {
					bos.write(buffer, 0, lenth);
					bos.flush();
				}
				bos.close();
				String json = bos.toString();
				Log.i(tag, json);
				return new Gson().fromJson(json, token.getType());
			} else {
				return new Gson().fromJson(in, token.getType());
			}

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Title: doGetForJson Description: 发送请求的 URL,直接返回JSON数据解析。返回集合列表 return T
	 * throws
	 */
	private <T> T doGetForJson(String url, List<BasicNameValuePair> params,
			TypeToken<?> token) {
		InputStream is = null;
		InputStreamReader in = null;
		StringBuffer geturl = new StringBuffer();
		geturl.append(url);
		if (params != null) {
			for (BasicNameValuePair item : params) {
				geturl.append("&" + item.getName() + "=" + item.getValue());
			}
		}
		try {
			URL realUrl = new URL(new String(geturl.toString().getBytes(),
					"ISO-8859-1"));
			System.out.println(realUrl);
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
				Log.i(tag, geturl.toString());
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int lenth = 0;
				while ((lenth = is.read(buffer)) != -1) {
					bos.write(buffer, 0, lenth);
					bos.flush();
				}
				bos.close();
				String json = bos.toString();
				Log.i(tag, json);
				return new Gson().fromJson(json, token.getType());
			} else {
				return new Gson().fromJson(in, token.getType());
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static String doPostForJson(String url,
			List<BasicNameValuePair> params) {
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
			byte[] buffer = new byte[1024];

			return buffer.toString();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
}
