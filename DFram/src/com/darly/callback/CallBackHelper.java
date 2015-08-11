package com.darly.callback;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.reflect.TypeToken;

/**
* @ClassName: CallBackHelper
* @Description: TODO(回掉方法)
* @author 张宇辉 zhangyuhui@octmami.com
* @date 2014年12月15日 上午9:16:28
*
* @param <T>
*/ 
public class CallBackHelper<T> {
	private List<BasicNameValuePair> params;
	private String url;
	private TypeToken<T> token;
	private boolean isGet;

	public CallBackHelper(List<BasicNameValuePair> params, String url,
			TypeToken<T> token, boolean isGet) {
		super();
		this.params = params;
		this.url = url;
		this.token = token;
		this.isGet = isGet;
	}

	public void back(final CallBack<T> callBack) {
		new Thread() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (isGet) {
					callBack.callBack((T) GetData.doGetForJson(url, params,
							token));
				} else {
					callBack.callBack((T) GetData.doPostForJson(url, params,
							token));
				}
			}

		}.start();
	}

	public interface CallBack<T> {
		public void callBack(T t);
	}
}
