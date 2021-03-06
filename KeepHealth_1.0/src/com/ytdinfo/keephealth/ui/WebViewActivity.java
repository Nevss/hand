package com.ytdinfo.keephealth.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.rayelink.eckit.SDKCoreHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgentJSInterface;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.app.MyApp;
import com.ytdinfo.keephealth.model.DocInfoBean;
import com.ytdinfo.keephealth.model.UserModel;
import com.ytdinfo.keephealth.ui.login.LoginActivity;
import com.ytdinfo.keephealth.ui.view.MyProgressDialog;
import com.ytdinfo.keephealth.ui.view.MyWebView;
import com.ytdinfo.keephealth.utils.DBUtilsHelper;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;
import com.ytdinfo.keephealth.utils.ToastUtil;
import com.yuntongxun.kitsdk.ECDeviceKit;
import com.yuntongxun.kitsdk.beans.ChatInfoBean;

@SuppressLint("JavascriptInterface")
public class WebViewActivity extends BaseActivity {
	private final String TAG = "WebViewActivity";
	// private CommonActivityTopView commonActivityTopView;
	private MyWebView webview;
	private RelativeLayout rl;
	private Button bt_update;
	private Intent intent;
	private String loadUrl;

	private String current_url;

	private boolean isPageLoaded = false;

	private UserModel userModel;
	private MyProgressDialog myProgressDialog, myProgressDialog2;
	private Context context;
	private String SubjectID;
	private DocInfoBean docInfoBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		myProgressDialog2 = new MyProgressDialog(this);
		myProgressDialog2.setMessage("正在请求...");
		myProgressDialog2.show();

		context = this;
		intent = getIntent();
		loadUrl = intent.getStringExtra("loadUrl");

		bt_update = (Button) findViewById(R.id.id_bt_update);
		// commonActivityTopView = (CommonActivityTopView)
		// findViewById(R.id.id_CommonActivityTopView);
		// commonActivityTopView.tv_title.setText("webview");
		rl = (RelativeLayout) findViewById(R.id.id_rl);

		loadWebView();
		webViewListener();
		initListener();
		LogUtil.i(TAG, SharedPrefsUtil.getValue(Constants.TOKEN, null));
		HashMap<String, String> hashmap = new HashMap<String, String>();
		if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
			hashmap.put("token",
					SharedPrefsUtil.getValue(Constants.TOKEN, null));
		}
		webview.loadUrl(loadUrl, hashmap);
	}

	private void initListener() {
		bt_update.setOnClickListener(new OnClickListener() {
			// 重新加载
			@Override
			public void onClick(View v) {
				LogUtil.i(TAG, "重新加载");
				LogUtil.i("paul", current_url);
			}
		});
	}

	/**
	 * 同步一下cookie
	 */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		String token = SharedPrefsUtil.getValue(Constants.TOKEN, null);
		cookieManager.setCookie(url, "token=" + token + ";path=/");// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void loadWebView() {
		// 实例化WebView对象
		// webview = new MyWebView(WebViewActivity.this);
		LogUtil.i(TAG, "loadWebView===实例化WebView===");
		webview = (MyWebView) findViewById(R.id.id_webview);
		new MobclickAgentJSInterface(this, webview);

		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		//
		webSettings.setUseWideViewPort(true);// 关键点
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setAppCacheEnabled(true);

		webSettings.setAppCacheMaxSize(8 * 1024 * 1024); // 8MB
		// webSettings.setAppCachePath(Constants.WEBVIEW_CACHE_DIR );
		String appCacheDir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCachePath(appCacheDir);
		webSettings.setAllowFileAccess(true);
		webSettings.setDomStorageEnabled(true);

		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

		// js调用安卓方法
		webview.addJavascriptInterface(this, "RedirectListner");
	}

	private void webViewListener() {
		webview.setWebViewClient(new WebViewClient() {
			// 加载失败
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				LogUtil.i(TAG, "加载失败===" + errorCode + "---" + description
						+ "---" + failingUrl + "---");

				current_url = failingUrl;
				webview.clearView();
				if (failingUrl.contains("#")) {
					String[] temp;
					temp = failingUrl.split("#");
					webview.loadUrl(temp[0]);
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					webview.loadUrl(failingUrl);
				} else {
					webview.loadUrl("file:///android_asset/error.html#"
							+ failingUrl);
				}

				ToastUtil.showMessage("页面加载失败，请点击重新加载");

				// super.onReceivedError(view, errorCode, description,
				// failingUrl);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * "WebViewClient.shouldOverrideUrlLoading",
				 * Toast.LENGTH_SHORT);
				 */
				LogUtil.i(TAG, "拦截url---shouldOverrideUrlLoading-->" + url);
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse(url));
					startActivity(intent);
				} else {
					HashMap<String, String> hashmap = new HashMap<String, String>();
					if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
						hashmap.put("token",
								SharedPrefsUtil.getValue(Constants.TOKEN, null));
					}
					webview.loadUrl(url, hashmap);
				}

				return true;
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				isPageLoaded = false;
				LogUtil.i(TAG, "拦截url---onPageStarted-->" + url);
				HashMap<String, String> hashmap = new HashMap<String, String>();
				if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
					hashmap.put("token",
							SharedPrefsUtil.getValue(Constants.TOKEN, null));
				}
				if (url.toLowerCase().contains("/login")) {
					Intent i11 = new Intent();
					i11.setClass(WebViewActivity.this, LoginActivity.class);
					startActivity(i11);
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				isPageLoaded = true;
				LogUtil.i(TAG, "页面加载完后==onPageFinished==" + url);

				myProgressDialog2.dismiss();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.i(TAG, "==onKeyDown==");
			if (isPageLoaded) {
				webview.loadUrl("javascript:CommonRedirect.goBack()");
				LogUtil.i(TAG, "==onKeyDown==isPageLoaded==");
			} else {
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
			}
		}
		return false;
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToIndex() {
		Log.i(TAG, "goToIndex()");
		finish();
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void reloadUrl(String url) {
		Log.i(TAG, "reloadUrl()");
		HashMap<String, String> hashmap = new HashMap<String, String>();
		if (null != SharedPrefsUtil.getValue(Constants.TOKEN, null)) {
			hashmap.put("token",
					SharedPrefsUtil.getValue(Constants.TOKEN, null));
		}
		webview.clearView();
		webview.loadUrl(url, hashmap);
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToUpdateUrl() {
		Log.i(TAG, "goToUpdateUrl()");
		webview.loadUrl("file:///android_asset/a.html？'" + current_url + "'");
		// finish();
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToReport(String studyid) {
		userModel = new Gson().fromJson(
				SharedPrefsUtil.getValue(Constants.USERMODEL, null),
				UserModel.class);
		Log.i(TAG, "goToReport()");
		Intent intent = new Intent();
		if (!DBUtilsHelper.getInstance().isOnline()) {
			requestDoctor(studyid);
		} else {
			ToastUtil.showMessage("您当前正在进行在线咨询，结束后才能进行报告解读哦");
			SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT, 1);
			Intent intent2 = new Intent(WebViewActivity.this,
					MainActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent2.putExtra("news", "news");
			startActivity(intent2);
			finish();
			return;
		}
		startActivity(intent);
	}

	private void requestDoctor(String studyId) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("SubjectType", "1");
			jsonObject.put("UserID", userModel.getID());
			jsonObject.put("UserName", userModel.getUserName());
			jsonObject.put("UserSex", userModel.getUserSex());
			jsonObject.put("Age", userModel.getAge());
			jsonObject.put("HeadPicture", userModel.getHeadPicture());
			jsonObject.put("RelationShip", "1");
			jsonObject.put("StudyID", studyId);
			jsonObject.put("AttachPics", null);
			jsonObject.put("BodyContent", null);
			HttpClient.post(Constants.STARTCHAT_URl, jsonObject.toString(),
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
							myProgressDialog = new MyProgressDialog(context);
							myProgressDialog.setMessage("正在请求...");
							myProgressDialog.show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							LogUtil.i(TAG, arg0.result);
							myProgressDialog.dismiss();
							analyzeJson(arg0.result);

						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							myProgressDialog.dismiss();
						}
					});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void analyzeJson(String json) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject data = jsonObject.getJSONObject("Data");
			SubjectID = data.getString("SubjectID");
			// SharedPrefsUtil.putValue(Constants.SUBJECTID, SubjectID);
			String responser = data.getString("responser");
			if (null == responser || responser.equals("")
					|| responser.equals("null")) {
				ToastUtil.showMessage("当前没有医生在线...");
				return;
			}
			// 开始计时
			// TimerService.count = 0;
			docInfoBean = new Gson().fromJson(responser, DocInfoBean.class);
			saveDoc(docInfoBean);
			ChatInfoBean chatInfoBean = null;
			try {
				chatInfoBean = DBUtilsHelper
						.getInstance()
						.getDb()
						.findFirst(
								Selector.from(ChatInfoBean.class).where(
										"docInfoBeanId", "=",
										docInfoBean.getVoipAccount()));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null == chatInfoBean) {
				chatInfoBean = new ChatInfoBean();
				chatInfoBean.setSubjectID(SubjectID + "");
				chatInfoBean.setDocInfoBeanId(docInfoBean.getVoipAccount());
				chatInfoBean.setComment(false);
			} else {
				chatInfoBean.setSubjectID(SubjectID + "");
				chatInfoBean.setComment(false);
			}
			chatInfoBean.setTimeout(false);
			if (!SDKCoreHelper.isOnLine()) {
				myProgressDialog = new MyProgressDialog(WebViewActivity.this);
				myProgressDialog.setMessage("正在连接对话....");
				myProgressDialog.show();
				MyApp.ConnectYunTongXun();
			}
			goIntent(chatInfoBean);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void goIntent(ChatInfoBean chatInfoBean) {
		// TODO Auto-generated method stub
		chatInfoBean.setSubjectType("1");
		chatInfoBean.setStatus(false);
		DBUtilsHelper.getInstance().saveChatinfo(chatInfoBean);
		ECDeviceKit.getIMKitManager().startConversationActivity(chatInfoBean,
				null, null);
		// ECDeviceKit.getIMKitManager().startConversationActivity(chatInfoBean.getDocInfoBeanId());
	}

	private void saveDoc(DocInfoBean docInfoBean) {
		// TODO Auto-generated method stub
		// DbUtils dbUtils = new DbUtils(null);
		DbUtils db = DBUtilsHelper.getInstance().getDb();
		try {
			db.createTableIfNotExist(DocInfoBean.class);
			db.save(docInfoBean);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * JS调用的方法
	 */
	@JavascriptInterface
	public void goToPhone(int number) {
		Log.i(TAG, "goToPhone()");
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ number));
		startActivity(intent);

		// finish();
	}

	// 标题 图片Url,网址Url,网址简单描述
	@JavascriptInterface
	public void shareWebSiteToPlatForm(String titleName, String thumbUrl,
			String url, String siteDesc) {

	}

	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onPageStart("WebViewActivity");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		MobclickAgent.onPageEnd("WebViewActivity");
		MobclickAgent.onPause(this);
	}

}
