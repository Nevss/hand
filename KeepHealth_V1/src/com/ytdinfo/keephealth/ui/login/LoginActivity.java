package com.ytdinfo.keephealth.ui.login;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.voice.demo.group.GroupBaseActivity;
import com.voice.demo.group.model.IMChatMessageDetail;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPIntentUtils;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.model.UserGroupBean;
import com.ytdinfo.keephealth.model.UserModel;
import com.ytdinfo.keephealth.ui.BaseActivity;
import com.ytdinfo.keephealth.ui.MainActivity;
import com.ytdinfo.keephealth.ui.forgetpass.FindPassActivity;
import com.ytdinfo.keephealth.ui.personaldata.PersonalDataActivity;
import com.ytdinfo.keephealth.ui.register.RegisterActivity;
import com.ytdinfo.keephealth.ui.view.CommonButton;
import com.ytdinfo.keephealth.ui.view.MyProgressDialog;
import com.ytdinfo.keephealth.utils.DBUtilsHelper;
import com.ytdinfo.keephealth.utils.HandlerUtils;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;
import com.ytdinfo.keephealth.utils.ToastUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "LoginActivity";

	private ImageButton ib_close1;
	private ImageButton ib_close2;
	private ImageView iv11;
	private ImageView back;
	private EditText user, password;
	private CommonButton login;
	private RelativeLayout register;
	private RelativeLayout rl_forget_pass;
	/** 用户Token标示 */
	String token;
	/** 用户登录状态==Fail */
	String loginStatus;
	/** 用户登录信息 */
	String message;

	String userString;
	String passwordString;
	private DbUtils dbUtil;

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initView();
		initListener();

		handler = HandlerUtils.useHandler(new EditText[] { user, password },
				login.bt_common);
		HandlerUtils.startTimer(handler);

		MobclickAgent.setDebugMode(true);
	}

	private void initView() {
		ib_close1 = (ImageButton) findViewById(R.id.id_ib_close1);
		ib_close2 = (ImageButton) findViewById(R.id.id_ib_close2);
		iv11 = (ImageView) findViewById(R.id.iv11);
		back = (ImageView) findViewById(R.id.login_back);
		user = (EditText) findViewById(R.id.et_user);
		password = (EditText) findViewById(R.id.et_password);
		login = (CommonButton) findViewById(R.id.bt_login);
		login.bt_common.setText("登 录");
		register = (RelativeLayout) findViewById(R.id.id_rl_register);
		rl_forget_pass = (RelativeLayout) findViewById(R.id.id_rl_forget_pass);
	}

	private void initListener() {
		login.bt_common.setOnClickListener(this);
		register.setOnClickListener(this);
		ib_close1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user.setText("");
			}
		});
		ib_close2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				password.setText("");
			}
		});

		iv11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 隐藏输入法
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(LoginActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		rl_forget_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, FindPassActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_bt_common:
			// 登录
			userString = user.getText().toString().trim();
			passwordString = password.getText().toString().trim();
			if (userString.equals("")) {
				ToastUtil.showMessage("请输入手机号");
			} else if (userString.length() != 11) {
				ToastUtil.showMessage("请输入正确的手机号");
			} else if (passwordString.equals("")) {
				ToastUtil.showMessage("请输入密码");
			} else {
				requestLogin(userString, passwordString);
			}

			break;
		case R.id.id_rl_register:
			// 注册
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}
	}

	private MyProgressDialog myProgressDialog;

	private void requestLogin(String userString, String passwordString) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("UserAccount", userString);
			jsonObject.put("Password", passwordString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		HttpClient.post(Constants.LOGIN_URl, jsonObject.toString(),
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
						myProgressDialog = new MyProgressDialog(
								LoginActivity.this);
						myProgressDialog.setMessage("正在登录...");
						myProgressDialog.show();
					}

					@Override
					public void onSuccess(final ResponseInfo<String> arg0) {
						myProgressDialog.dismiss();
						LogUtil.i(TAG, "onsuccess-----" + arg0.result);

						// //启动新线程
						// new Thread(new Runnable() {
						//
						// @Override
						// public void run() {
						// analyzeJson(arg0.result);
						//
						// }
						// }).start();
						analyzeJson(arg0.result);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						myProgressDialog.dismiss();
						LogUtil.e(TAG, arg1.toString());
						ToastUtil.showMessage("网络请求失败");
					}
				});
	}

	/**
	 * 解析json
	 * 
	 * @param json
	 */
	private void analyzeJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			loginStatus = jsonObject.getString("Loginstatus");
			message = jsonObject.getString("Message");
			if (!loginStatus.equals("Fail")) {
				String token = jsonObject.getString("Token");
				String usermodel = jsonObject.getString("UserModel");
				String userId = jsonObject.getJSONObject("UserModel")
						.getString("ID");
				SharedPrefsUtil.putValue(Constants.TOKEN, token);
				SharedPrefsUtil.putValue(Constants.USERID, userId);
				SharedPrefsUtil.putValue(Constants.USERMODEL, usermodel);
				String s = SharedPrefsUtil.getValue(Constants.TOKEN, null);
				// System.out.println(s);
				initgroup();
				// 跳到首页
				UserModel userModel = new Gson().fromJson(usermodel,
						UserModel.class);
				final String headUrl = userModel.getHeadPicture();
				// TODO:Download face file to local disk

				dbUtil = DBUtilsHelper.getInstance().getDb();

				Intent i;
				if (null == userModel.getIDcard()
						|| "".equals(userModel.getIDcard())) {
					i = new Intent(this, PersonalDataActivity.class);
				} else {
					i = new Intent(this, MainActivity.class);
				}
				// Intent i = new Intent(this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				// initCCPSDK();

			} else {
				ToastUtil.showMessage(message);
			}

			// /LogUtil.i(TAG, SharedPrefsUtil.getValue(Constants.TOKEN, null));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initgroup() {
		// TODO Auto-generated method stub
		// if (CCPHelper.getInstance().getDevice() == null) {
		CCPHelper.getInstance().registerCCP(new CCPHelper.RegistCallBack() {
			@Override
			public void onRegistResult(int reason, String msg) {
				// Log.i("XXX", String.format("%d, %s",
				// reason, msg));
				if (reason == 8192) {
					LogUtil.i(TAG, "通讯云登录成功");
					requestGetUserGroup();
				} else {
					LogUtil.i(TAG, "通讯云登录失败");
					CCPHelper.getInstance().reConnect();
				}
			}
		});
		// } else {
		// requestGetUserGroup();
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void requestGetUserGroup() {
		// TODO Auto-generated method stub
		HttpClient.get(LoginActivity.this, Constants.GETUSERGROUPS_URL,
				new RequestParams(), new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, arg0.result);
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							JSONObject data = jsonObject.getJSONObject("Data");
							String statusCode = data.getString("statusCode");
							if (statusCode.equals("000000")) {
								SharedPrefsUtil
										.putValue("loadingSuccess", true);
								if (data.getString("groups") == null
										|| data.getString("groups").equals(
												"null")) {
									Intent intent = new Intent(
											CCPIntentUtils.INTENT_IM_RECIVE);
									intent.putExtra(
											GroupBaseActivity.KEY_GROUP_ID, "");
									// intent.putExtra("groupName",
									// userGroupBean.getName());
									sendBroadcast(intent);
									return;
								}
								List<UserGroupBean> list = new Gson().fromJson(
										data.getString("groups"),
										new TypeToken<List<UserGroupBean>>() {
										}.getType());
								for (int i = 0; i < list.size(); i++) {
									UserGroupBean userGroupBean = list.get(i);
									try {
										UserGroupBean userGroupBean2 = dbUtil
												.findFirst(Selector
														.from(UserGroupBean.class)
														.where("groupId",
																"=",
																userGroupBean
																		.getGroupId()));
										if (null != userGroupBean2) {
											return;
										}
									} catch (DbException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									saveGroupBean(userGroupBean);
									IMChatMessageDetail chatMessageDetail = IMChatMessageDetail.getGroupItemMessageReceived(
											userGroupBean.getGroupId(),
											IMChatMessageDetail.TYPE_MSG_TEXT,
											userGroupBean.getGroupId(),
											userGroupBean.getGroupId());
									// chatMessageDetail.setMessageContent(message);
									chatMessageDetail.setDateCreated(System
											.currentTimeMillis() + "");
									chatMessageDetail
											.setIsRead(IMChatMessageDetail.STATE_READED);
									chatMessageDetail
											.setGroupName(userGroupBean
													.getName());
									// chatMessageDetail.setUserData(aMsg.getUserData());
									try {
										CCPSqliteManager.getInstance()
												.insertIMMessage(
														chatMessageDetail);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Intent intent = new Intent(
											CCPIntentUtils.INTENT_IM_RECIVE);
									intent.putExtra(
											GroupBaseActivity.KEY_GROUP_ID,
											userGroupBean.getGroupId());
									// intent.putExtra("groupName",
									// userGroupBean.getName());
									sendBroadcast(intent);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void saveGroupBean(UserGroupBean userGroupBean) {
		// TODO Auto-generated method stub

		try {
			dbUtil.createTableIfNotExist(UserGroupBean.class);
			dbUtil.save(userGroupBean);
			// List<DocInfoBean> docInfoBeans = db.findAll(DocInfoBean.class);
			// DocInfoBean docInfoBean2 =
			// db.findFirst(Selector.from(DocInfoBean.class).where("voipAccount","=","89077100000003"));
			// DocInfoBean docInfoBean2 = db.findById(DocInfoBean.class,
			// docInfoBean.getVoipAccount());
			// System.out.println("doc..........."+docInfoBean2.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showLoginHint() {
		/*
		 * else { //登录成功
		 * 
		 * //将token，帐号，密码保存到本地 SharedPrefsUtil.putValue(Constants.TOKEN, token);
		 * SharedPrefsUtil.putValue(Constants.UserAccount, userString);
		 * SharedPrefsUtil.putValue(Constants.Password, passwordString); }
		 */
	}

	public void onResume() {
		super.onResume();

		MobclickAgent.onPageStart("LoginActivity");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();

		MobclickAgent.onPageEnd("LoginActivity");
		MobclickAgent.onPause(this);
	}
}
