/**
 * 下午5:45:11
 * @author Zhangyuhui
 * $
 * MeSecActivity.java
 * TODO
 */
package com.darly.activities.ui.fragment.set;

import android.os.Bundle;
import android.view.View;

import com.darly.activities.R;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.LogFileHelper;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * @author Zhangyuhui
 * MeSecActivity
 * $
 * 下午5:45:11
 * TODO
 */
@ContentView(R.layout.activity_main)
public class MeSecActivity extends BaseActivity {
	private static final String TAG = "MeSecActivity";

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogFileHelper.getInstance().i(TAG, "MeSecActivity is run");
	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

}
