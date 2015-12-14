/**
 * 上午9:40:51
 * @author Zhangyuhui
 * RotateAcitvity.java
 * TODO
 */
package com.darly.activities.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.darly.activities.R;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.LogFileHelper;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Zhangyuhui RotateAcitvity 上午9:40:51 TODO 测试canvas.save();
 *         canvas.restore(); canvas.rotate(90, Literal.width/2,
 *         Literal.width/2);功能是否强悍
 */
@ContentView(R.layout.activity_rotate)
public class RotateAcitvity extends BaseActivity {
	private static final String TAG = "RotateAcitvity";
	@ViewInject(R.id.main_header_text)
	private TextView title;
	@ViewInject(R.id.main_header_back)
	private ImageView back;

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
		title.setText("组件旋转");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		LogFileHelper.getInstance().i(TAG, "RotateAcitvity is run");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub

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
}
