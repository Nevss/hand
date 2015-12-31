/**
 * 上午11:19:14
 * @author zhangyh2
 * $
 * MainFragment.java
 * TODO
 */
package com.darly.oop.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darly.oop.R;
import com.shizhefei.fragment.LazyFragment;

/**
 * @author zhangyh2
 * MainFragment
 * $
 * 上午11:19:14
 * TODO
 */
public class MainFragment extends LazyFragment {
	
	private ProgressBar progressBar;
	private TextView textView;
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabmain_item);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
		textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
		textView.setText("界面" + " " + tabIndex + " 加载完毕");
		handler.sendEmptyMessageDelayed(1, 2000);
	}

	@Override
	public void onDestroyViewLazy() {
		super.onDestroyViewLazy();
		handler.removeMessages(1);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
		}
	};

}
