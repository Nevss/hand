/**
 * 下午2:32:27
 * @author Zhangyuhui
 * LoginClearEdit.java
 * TODO
 */
package com.darly.activities.widget.clearedit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darly.activities.R;

/**
 * @author Zhangyuhui LoginClearEdit 下午2:32:27 TODO
 */
public class LoginClearEdit extends LinearLayout {

	private TextView tv;

	private ClearEditText text;

	/**
	 * @param context
	 * @param attrs
	 *            下午2:32:36
	 * @author Zhangyuhui LoginClearEdit.java TODO
	 */
	public LoginClearEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 *            下午2:32:30
	 * @author Zhangyuhui LoginClearEdit.java TODO
	 */
	public LoginClearEdit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 *            下午2:33:34
	 * @author Zhangyuhui LoginClearEdit.java TODO
	 */
	private void init(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater.from(context).inflate(R.layout.login_clear_edit, this);
		tv = (TextView) findViewById(R.id.login_clear_text);
		text = (ClearEditText) findViewById(R.id.login_clear_edit);
		text.setShakeAnimation();
	}

	/**
	 * 
	 * 下午2:45:58
	 * 
	 * @author Zhangyuhui LoginClearEdit.java TODO
	 */
	public void setTarget(String title, String hint) {
		// TODO Auto-generated method stub
		if (title != null) {
			tv.setText(title);
		}
		if (hint != null) {
			text.setHint(hint);
		}
	}

	public TextView getTv() {
		return tv;
	}

	public ClearEditText getText() {
		return text;
	}

}
