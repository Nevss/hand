package com.darly.common;

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

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName: Json
 * @Description: TODO(单独获取JSON类，通过传入JSON字符串，获取数据)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2015年1月5日 上午9:56:23
 *
 */
public class Json {
	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:57:27 Project_Name:DFram
	 * Description:TODO(获取解析数据) Throws T
	 */
	public synchronized static <T> T getJsonResult(final String json,
			final Class<T> c) {
		T result = null;
		if (json == null) {
			return null;
		}
		try {
			result = c.newInstance();
			LogApp.i("run this result = c.newInstance();");
			result = new Gson().fromJson(json, c);
			LogApp.i("run this gson.fromJson(json, result.getClass());");
			return result;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:58:09 Project_Name:DFram
	 * Description:TODO(发送请求的 URL,直接返回JSON数据解析。返回集合列表) Throws T
	 */
	public static <T> T doGetForJson(String url,
			List<BasicNameValuePair> params, TypeToken<?> token) {
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
			Log.i("doGet", geturl.toString());
			URL realUrl = new URL(new String(geturl.toString().getBytes(),
					"ISO-8859-1"));
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性

			conn.setRequestMethod("GET");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");

			// 定义BufferedReader输入流来读取URL的响应
			is = conn.getInputStream();
			in = new InputStreamReader(is);

			return new Gson().fromJson(in, token.getType());
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

	public static <T> T doPostForJson(String url,
			List<BasicNameValuePair> params, TypeToken<?> token) {
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
			return new Gson().fromJson(in, token.getType());

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
