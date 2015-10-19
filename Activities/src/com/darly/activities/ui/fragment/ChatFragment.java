/**
 * 下午3:16:20
 * @author Zhangyuhui
 * ChatFragment.java
 * TODO
 */
package com.darly.activities.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.darly.activities.adapter.ChatAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.HTTPServ;
import com.darly.activities.common.Literal;
import com.darly.activities.model.GirlBase;
import com.darly.activities.model.GirlModel;
import com.darly.activities.poll.HTTPSevTasker;
import com.darly.activities.ui.R;
import com.darly.activities.ui.WebViewActivity;
import com.darly.activities.widget.xlistview.XListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Zhangyuhui ChatFragment 下午3:16:20 TODO美女图片展示。
 */
public class ChatFragment extends BaseFragment implements OnItemClickListener {
	private View rootView;
	private int num = 10;
	@ViewInject(R.id.chat_fragment_xlist)
	private XListView list;

	private ChatAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.main_fragment_chat, container,
				false);// 关联布局文件
		ViewUtils.inject(this, rootView); // 注入view和事件
		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		// 请求美女数据
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("num", num + ""));
		List<BasicNameValuePair> propety = new ArrayList<BasicNameValuePair>();
		propety.add(new BasicNameValuePair("apikey", HTTPServ.APPIDKEY));
		manager.start();
		manager.addAsyncTask(new HTTPSevTasker(getActivity(), params,
				HTTPServ.PHOTO_GIRL, handler, true, Literal.GET_HANDLER,
				propety));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		list.setOnItemClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

		GirlModel model = getJSON((String) object);
		adapter = new ChatAdapter(model.getData(), R.layout.main_fragment_chat_item,
				getActivity(), imageLoader, options);
		list.setAdapter(adapter);
		
		
	}

	/**
	 * 
	 * 下午3:42:54
	 * 
	 * @author Zhangyuhui ChatFragment.java TODO
	 */
	private GirlModel getJSON(String json) {
		// TODO Auto-generated method stub
		List<GirlBase> data = new ArrayList<GirlBase>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			int code = jsonObject.getInt("code");
			String msg = jsonObject.getString("msg");
			for (int i = 0; i < 10; i++) {
				JSONObject array = jsonObject.getJSONObject(i + "");
				String title = array.getString("title");
				String description = array.getString("description");
				String picUrl = array.getString("picUrl");
				String url = array.getString("url");
				data.add(new GirlBase(title, description, picUrl, url));
			}
			return new GirlModel(code, msg, data);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		GirlBase base= (GirlBase) parent.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), WebViewActivity.class);
		intent.putExtra("URL", base.getUrl());
		startActivity(intent);
	}

}
