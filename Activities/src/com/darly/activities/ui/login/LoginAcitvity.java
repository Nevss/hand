/**
 * 下午2:15:37
 * @author Zhangyuhui
 * LoginAcitvity.java
 * TODO
 */
package com.darly.activities.ui.login;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.common.PreferenceUserInfor;
import com.darly.activities.common.ToastApp;
import com.darly.activities.ui.R;
import com.darly.activities.widget.clearedit.LoginClearEdit;
import com.darly.activities.widget.intel.RectRestoreSurfaceView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Zhangyuhui LoginAcitvity 下午2:15:37 TODO 登陆页面
 */
@ContentView(R.layout.activity_login)
public class LoginAcitvity extends BaseActivity {
	/**
	 * 下午3:01:22 TODO 顶部标签
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	/**
	 * 下午3:01:35 TODO 顶部返回
	 */
	@ViewInject(R.id.main_header_back)
	private ImageView back;
	/**
	 * 上午9:13:32 TODO 回行矩形框
	 */
	@ViewInject(R.id.act_login_relative)
	private RelativeLayout relative;

	/**
	 * 上午9:55:56 TODO 绘图类
	 */
	@ViewInject(R.id.act_login_restore)
	private RectRestoreSurfaceView restore;
	/**
	 * 下午3:01:44 TODO 用户名
	 */
	@ViewInject(R.id.act_login_name)
	private LoginClearEdit name;
	/**
	 * 下午3:01:51 TODO 用户密码
	 */
	@ViewInject(R.id.act_login_pass)
	private LoginClearEdit pass;

	/**
	 * 下午3:08:38 TODO 登陆
	 */
	@ViewInject(R.id.act_login_login)
	private Button login;

	private boolean isUserName;

	private boolean isPassWord;

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
		case R.id.act_login_login:
			// 双用户登录。可以进行双通讯。
			if ("18321127312".equals(name.getText().getText().toString())) {
				if ("111111".equals(pass.getText().getText().toString())) {
					JSONObject object = new JSONObject();
					try {
						// 测试数据
						object.put("username", "18321127312");
						object.put("password", "111111");
						object.put("userID", "18321127312");
						object.put("userTrueName", "darly");
					} catch (Exception e) {
						// TODO: handle exception
					}
					LogApp.i(TAG, object.toString());
					PreferenceUserInfor.saveUserInfor(Literal.USERINFO,
							object.toString(), this);
					// 登录成功后进行初始化融云通讯。
					// AppStack.initConnRongIM(this);
					ToastApp.showToast(this, "登录成功");
					finish();
				} else {
					ToastApp.showToast(this, "用户名密码错误");
				}
			} else if ("13891431454"
					.equals(name.getText().getText().toString())) {
				if ("111111".equals(pass.getText().getText().toString())) {
					JSONObject object = new JSONObject();
					try {
						// 测试数据
						object.put("username", "13891431454");
						object.put("password", "111111");
						object.put("userID", "13891431454");
						object.put("userTrueName", "hellen");
					} catch (Exception e) {
						// TODO: handle exception
					}
					LogApp.i(TAG, object.toString());
					PreferenceUserInfor.saveUserInfor(Literal.USERINFO,
							object.toString(), this);
					// 登录成功后进行初始化融云通讯。
					// AppStack.initConnRongIM(this);
					ToastApp.showToast(this, "登录成功");
					finish();
				} else {
					ToastApp.showToast(this, "用户名密码错误");
				}
			} else {
				ToastApp.showToast(this, "用户名密码错误");
			}

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
		title.setText("登录");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		login.setText("登录");
		login.setBackgroundResource(R.drawable.app_btn_unpress);
		login.setTextColor(getResources().getColor(R.color.pop_back));
		login.setClickable(false);

		restore.setLayoutParams(new LayoutParams(Literal.width,
				Literal.width / 2));
		// 设置用户名
		name.setTarget("用户名称", "手机号/邮箱/QQ");
		// 设置密码
		pass.setTarget("用户密码", "请输入您的密码");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		name.getText().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s != null && !"".equals(s.toString())) {
					isUserName = true;
					if (isUserName && isPassWord) {
						login.setBackgroundResource(R.drawable.app_btn_select);
						login.setTextColor(getResources().getColor(
								R.color.white));
						login.setClickable(true);
						login.setOnClickListener(LoginAcitvity.this);
					}
				} else {
					isUserName = false;
					login.setBackgroundResource(R.drawable.app_btn_unpress);
					login.setTextColor(getResources()
							.getColor(R.color.pop_back));
					login.setClickable(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		pass.getText().setInputType(
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pass.getText().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s != null && !"".equals(s.toString())) {
					isPassWord = true;
					if (isUserName && isPassWord) {
						login.setBackgroundResource(R.drawable.app_btn_select);
						login.setTextColor(getResources().getColor(
								R.color.white));
						login.setClickable(true);
						login.setOnClickListener(LoginAcitvity.this);
					}
				} else {
					isPassWord = false;
					login.setBackgroundResource(R.drawable.app_btn_unpress);
					login.setTextColor(getResources()
							.getColor(R.color.pop_back));
					login.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

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
	 * @see com.darly.activities.base.BaseActivity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if (restore != null) {
			restore.setFlag(false);
		}

		super.finish();
	}

}
