/**
 * 下午1:34:11
 * @author zhangyh2
 * $
 * DBdoView.java
 * TODO
 */
package com.darly.oop.widget.dbview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.darly.oop.R;

/**
 * @author zhangyh2 DBdoView $ 下午1:34:11 TODO
 */
public class DBdoView extends TableLayout {

	private EditText name;
	private EditText age;
	private EditText sex;
	private EditText time;

	private Button btn;

	/**
	 * @param context
	 *            下午1:34:22
	 * @author zhangyh2 DBdoView.java TODO
	 */
	public DBdoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * @param context
	 *            下午1:34:47
	 * @author zhangyh2 DBdoView.java TODO
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.activity_db, this);
		name = (EditText) findViewById(R.id.db_name);
		age = (EditText) findViewById(R.id.db_age);
		sex = (EditText) findViewById(R.id.db_sex);
		time = (EditText) findViewById(R.id.db_birth);
		btn = (Button) findViewById(R.id.db_submit);
	}

	public TextView getName() {
		return name;
	}

	public TextView getAge() {
		return age;
	}

	public TextView getSex() {
		return sex;
	}

	public TextView getTime() {
		return time;
	}

	public Button getBtn() {
		return btn;
	}

}
