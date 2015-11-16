package com.ytdinfo.keephealth.ui.register;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.ui.BaseActivity;
import com.ytdinfo.keephealth.ui.login.LoginActivity;
import com.ytdinfo.keephealth.ui.view.CommonActivityTopView;
import com.ytdinfo.keephealth.ui.view.CommonButton;
import com.ytdinfo.keephealth.utils.HandlerUtils;
import com.ytdinfo.keephealth.utils.ToastUtil;

public class SettingPassActivity extends BaseActivity implements OnClickListener {
	private ImageButton back;
	private ImageButton clearPass;
	private EditText et_pass1;
	private EditText et_pass2;
	private ImageButton ibt_clearPass1;
	private ImageButton ibt_clearPass2;
	private CommonActivityTopView commonActivityTopView;
	private CommonButton ok;

	private String telephone;
	
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_pass);

		Intent intent = getIntent();
		telephone = intent.getStringExtra("telephone");

		initView();
		initListener();
		
		handler = HandlerUtils.useHandler(new EditText[]{et_pass1, et_pass2}, ok.bt_common);
		HandlerUtils.startTimer(handler);
	}

	private void initView() {
		commonActivityTopView = (CommonActivityTopView) findViewById(R.id.id_CommonActivityTopView);
		commonActivityTopView.setTitle("设置密码");
		back = (ImageButton) commonActivityTopView
				.findViewById(R.id.id_ibt_back);
		et_pass1 = (EditText) findViewById(R.id.id_et_pass1);
		et_pass2 = (EditText) findViewById(R.id.id_et_pass2);
		ibt_clearPass1 = (ImageButton) findViewById(R.id.id_ib_clearPass1);
		ibt_clearPass2 = (ImageButton) findViewById(R.id.id_ib_clearPass2);
		ok = (CommonButton) findViewById(R.id.id_bt_ok);
		ok.bt_common.setText("确 认");
	}

	private void initListener() {
		// 确认
		ok.bt_common.setOnClickListener(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ibt_clearPass1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_pass1.setText("");
			}
		});
		ibt_clearPass2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_pass2.setText("");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_bt_common:
			// 确认
			String pass1 = et_pass1.getText().toString().trim();
			String pass2 = et_pass2.getText().toString().trim();
			if (pass1.length() < 6 || pass1.length() > 20) {
				ToastUtil.showMessage("请输入6-20位密码");
			} else {
				if (pass2.equals("")) {
					ToastUtil.showMessage("请输入确认密码");
				} else if (!pass1.equals(pass2)) {
					ToastUtil.showMessage("两次密码不一致");
				} else {
					requestPassSetting();

				}
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 请求服务器进行密码设置
	 */
	private void requestPassSetting() {

		try {
			// 向服务器发送请求
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("UserAccount", telephone);
			jsonParam.put("Password", et_pass1.getText().toString().trim());
		HttpClient.post(Constants.SETPASS_URl, jsonParam.toString(), new RequestCallBack<String>() {
						@Override
						public void onStart() {
							Log.i("HttpUtil", "onStart");
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							Log.i("HttpUtil", "onLoading");
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Log.i("HttpUtil", "onSuccess");

							Log.i("HttpUtil", "onSuccess==="
									+ responseInfo.result.toString());
							parseJson_setPass(responseInfo.result.toString());
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.i("HttpUtil", "onFailure===" + msg);
							ToastUtil.showMessage("网络获取失败");
							// testTextView.setText(error.getExceptionCode() +
							// ":" +
							// msg);
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置密码返回的json
	 * 
	 * @param json
	 */
	private void parseJson_setPass(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			boolean isSuccess = jsonObject.getBoolean("Success");
			if (isSuccess) {
				ToastUtil.showMessage("密码设置成功");
				// 跳到登录界面
				Intent i = new Intent(this, LoginActivity.class);
				startActivity(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		MobclickAgent.onPageStart("SettingPassActivity");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		MobclickAgent.onPageEnd("SettingPassActivity");
		MobclickAgent.onPause(this);
	}
}
