/**
 * 下午5:45:11
 * @author Zhangyuhui
 * $
 * MeSecActivity.java
 * TODO
 */
package com.darly.activities.ui.fragment.set;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.darly.activities.R;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.LogFileHelper;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Zhangyuhui FieldsBackActivity $ 上午11:27:40 TODO 尝试接口传递参数，。
 */
@ContentView(R.layout.activity_fields_back)
public class FieldsBackActivity extends BaseActivity implements
		OnItemClickListener {
	private static final String TAG = "FieldsBackActivity";
	@ViewInject(R.id.fields_list)
	private ListView ls;
	private List<String> data;
	private ArrayAdapter<String> adapter;
	@ViewInject(R.id.main_header_text)
	private TextView title;
	@ViewInject(R.id.main_header_back)
	private ImageView back;

	private static FieldsBackListener backListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_header_back:
			finish();
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogFileHelper.getInstance().i(TAG, "FieldsBackActivity is run");
		title.setText(R.string.fields_back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		data = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			data.add(i + "");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		adapter = new ArrayAdapter<String>(this, R.layout.item_fields_back,
				R.id.item_fields_back_text, data);
		ls.setAdapter(adapter);
		ls.setOnItemClickListener(this);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		backListener.backListener(view,title,parent.toString());
	}

	/**
	 * @param backListener
	 *            the backListener to set
	 */
	public static void setBackListener(FieldsBackListener backListener) {
		FieldsBackActivity.backListener = backListener;
	}
}
