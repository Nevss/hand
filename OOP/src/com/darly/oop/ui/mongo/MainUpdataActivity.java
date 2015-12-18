/**
 * 上午11:50:24
 * @author zhangyh2
 * $
 * MainInsertActivity.java
 * TODO
 */
package com.darly.oop.ui.mongo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.darly.oop.base.BaseActivity;
import com.darly.oop.model.DarlyTableModel;
import com.darly.oop.widget.dbview.DBdoView;

/**
 * @author zhangyh2 MainInsertActivity $ 上午11:50:24 TODO
 */
public class MainUpdataActivity extends BaseActivity implements OnClickListener {
	private DarlyTableModel data;
	private DBdoView view;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = new DBdoView(this);
		setContentView(view);
		data = getIntent().getParcelableExtra("data");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		view.getBtn().setText("更新数据");
		view.getBtn().setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#loadData()
	 */
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		view.getName().setText(data.name);
		view.getAge().setText(data.age + "");
		view.getSex().setText(data.sex);
		view.getTime().setText(data.time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DarlyTableModel model = new DarlyTableModel();
		model.name = view.getName().getText().toString().trim();
		model.age = Integer.parseInt(view.getAge().getText().toString().trim());
		model.sex = view.getSex().getText().toString().trim();
		model.time = view.getTime().getText().toString().trim();
		Intent intent = new Intent();
		intent.putExtra("model", model);
		setResult(RESULT_OK, intent);
		finish();
	}

}
