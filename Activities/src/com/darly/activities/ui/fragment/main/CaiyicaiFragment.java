/**
 * 下午3:16:43
 * @author Zhangyuhui
 * ContactsFragment.java
 * TODO
 */
package com.darly.activities.ui.fragment.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.HTTPServ;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.CaiModel;
import com.darly.activities.poll.HTTPSevTasker;
import com.darly.activities.ui.R;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;

/**
 * @author Zhangyuhui ContactsFragment 下午3:16:43 TODO测试类
 */
public class CaiyicaiFragment extends BaseFragment {
	private static final String TAG = "CaiyicaiFragment";
	private View rootView;

	private TextView title;

	private TextView ques;

	private TextView ans;

	private Button start;
	private Button stop;

	private Timer timer;

	private int delay = 5000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.main_fragment_caiyicai, container,
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
		case R.id.main_fragment_cai_start:
			startTimer();
			break;
		case R.id.main_fragment_cai_stop:
			stopTimer();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * 下午5:16:58
	 * 
	 * @author Zhangyuhui CaiyicaiFragment.java TODO
	 */
	private void stopTimer() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
			timer = null;
			System.gc();
		}
	}

	/**
	 * 
	 * 下午5:16:46
	 * 
	 * @author Zhangyuhui CaiyicaiFragment.java TODO 开启计时
	 */
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<BasicNameValuePair> propety = new ArrayList<BasicNameValuePair>();
				propety.add(new BasicNameValuePair("apikey", HTTPServ.APPIDKEY));
				manager.start();
				manager.addAsyncTask(new HTTPSevTasker(getActivity(), null,
						HTTPServ.CAIYICAI, handler, true, Literal.GET_HANDLER,
						propety));
			}
		}, 0, delay);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		title = (TextView) rootView.findViewById(R.id.main_fragment_cai_name);
		ques = (TextView) rootView.findViewById(R.id.main_fragment_cai_ques);
		ans = (TextView) rootView.findViewById(R.id.main_fragment_cai_ans);
		start = (Button) rootView.findViewById(R.id.main_fragment_cai_start);
		stop = (Button) rootView.findViewById(R.id.main_fragment_cai_stop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		LogFileHelper.getInstance().i(TAG, "CaiyicaiFragment is run");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
		CaiModel model = new Gson().fromJson((String) object, CaiModel.class);
		if (model != null) {
			title.setText("猜一猜");
			ques.setText("题目" + model.getTitle());
			ans.setText("答案" + model.getAnswer());
		} else {
			ToastApp.showToast(getActivity(), "网络连接异常，请检查网络");
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

}
