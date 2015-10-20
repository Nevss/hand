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

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.HTTPServ;
import com.darly.activities.common.Literal;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.TuringModel;
import com.darly.activities.poll.HTTPSevTasker;
import com.darly.activities.ui.R;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;

/**
 * @author Zhangyuhui ChatFragment 下午3:16:20 TODO美女图片展示。
 */
public class TuringFragment extends BaseFragment implements TextWatcher {
	private View rootView;

	private String key = "879a6cb3afb84dbf4fc84a1df2ab7319";

	private String userid = "eb2edb736";

	private TextView content;

	private StringBuffer buffer;

	private EditText msg;

	private Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.main_fragment_turing, container,
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
		switch (v.getId()) {
		case R.id.main_fragment_turing_sub:
			// 提交信息。
			// 请求美女数据
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("key", key + ""));
			params.add(new BasicNameValuePair("info", msg.getText().toString()
					.trim()));
			params.add(new BasicNameValuePair("userid", userid + ""));
			List<BasicNameValuePair> propety = new ArrayList<BasicNameValuePair>();
			propety.add(new BasicNameValuePair("apikey", HTTPServ.APPIDKEY));
			manager.start();
			manager.addAsyncTask(new HTTPSevTasker(getActivity(), params,
					HTTPServ.TURING, handler, true, Literal.GET_HANDLER,
					propety));
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub

		content = (TextView) rootView
				.findViewById(R.id.main_fragment_turing_content);
		msg = (EditText) rootView.findViewById(R.id.main_fragment_turing_msg);
		btn = (Button) rootView.findViewById(R.id.main_fragment_turing_sub);
		buffer = new StringBuffer();

		btn.setBackgroundResource(R.drawable.app_btn_unpress);
		btn.setTextColor(getResources().getColor(R.color.pop_back));
		btn.setClickable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		msg.addTextChangedListener(this);
		btn.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
		TuringModel model = new Gson().fromJson((String) object,
				TuringModel.class);
		if (model != null) {
			buffer.append(model.getText()).append("\r\n");
			content.setText(buffer);
		} else {
			ToastApp.showToast(getActivity(), "网络异常，请检查网络");
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
	 * int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
	 * int, int)
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		if (s != null && !"".equals(s.toString())) {
			btn.setBackgroundResource(R.drawable.app_btn_select);
			btn.setTextColor(getResources().getColor(R.color.white));
			btn.setClickable(true);

		} else {
			btn.setBackgroundResource(R.drawable.app_btn_unpress);
			btn.setTextColor(getResources().getColor(R.color.pop_back));
			btn.setClickable(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
