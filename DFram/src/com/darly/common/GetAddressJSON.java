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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * ClassName: GetMsgByJson Description:
 * TODO(获取JSON并封装的一个线程公共类，通过GET传入参数列表，进行封装。将值传回Handler然后在Ui线程中使用。) author 张宇辉
 * zhangyuhui@octmami.com date 2014年11月15日 上午10:41:31
 *
 */
public class GetAddressJSON extends Thread {
	private List<BasicNameValuePair> params;
	private String url;
	private Handler handler;
	private boolean isGet;

	private int handlerCode;

	public static boolean isLog;


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
	public GetAddressJSON(List<BasicNameValuePair> params, String url, Handler handler, boolean isGet, int handlerCode) {
		super();
		this.params = params;
		this.url = url;
		this.handler = handler;
		this.isGet = isGet;
		this.handlerCode = handlerCode;
	}

	
	
	@Override
	public void run() {
		Message message = new Message();
		if (isGet) {
			message.obj = getStringFromUrl(url, params);
		} else {
			message.obj = postStringFromUrl(url, params);
		}
		message.what = handlerCode;
		handler.sendMessage(message);
	}

	private static final int TIMEOUT_IN_MILLIONS = 5000;

	/**
	* @Title: getAddressFromString
	* @Description: 获取地区的数据，从存储的数据中
	* @param @param json
	* @param @param token
	* @param @return
	* @return T
	* @throws
	*/ 
	public static <T> T getAddressFromString(String json, TypeToken<?> token) {
		return new Gson().fromJson(json, token.getType());
	}

	/**
	* @Title: getStringFromUrl
	* @Description: doGet方法从网络获取JSON字符串
	* @param @param url
	* @param @param params
	* @param @return
	* @return String
	* @throws
	*/ 
	private static String getStringFromUrl(String url,
			List<BasicNameValuePair> params) {
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
			LogApp.i("doGet", geturl.toString());
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
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int lenth = 0;
			while ((lenth = is.read(buffer)) != -1) {
				bos.write(buffer, 0, lenth);
				bos.flush();
			}
			bos.close();
			return bos.toString();
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

	/**
	* @Title: postStringFromUrl
	* @Description: doPOST方法从网络获取JSON字符串
	* @param @param url
	* @param @param params
	* @param @return
	* @return String
	* @throws
	*/ 
	public static String postStringFromUrl(String url,
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
