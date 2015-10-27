/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.voice.demo.group;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hisun.phone.core.voice.Device;
import com.hisun.phone.core.voice.Device.State;
import com.hisun.phone.core.voice.model.im.IMAttachedMsg;
import com.hisun.phone.core.voice.model.im.IMTextMsg;
import com.hisun.phone.core.voice.model.im.InstanceMsg;
import com.hisun.phone.core.voice.util.Log4Util;
import com.hisun.phone.core.voice.util.VoiceUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.voice.demo.chatroom.XQuickActionBar;
import com.voice.demo.group.baseui.CCPChatFooter;
import com.voice.demo.group.baseui.CCPTextView;
import com.voice.demo.group.model.IMChatMessageDetail;
import com.voice.demo.group.utils.EmoticonUtil;
import com.voice.demo.group.utils.FileUtils;
import com.voice.demo.group.utils.MimeTypesTools;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPAudioManager;
import com.voice.demo.tools.CCPIntentUtils;
import com.voice.demo.tools.CCPUtil;
import com.voice.demo.tools.net.ITask;
import com.voice.demo.tools.net.TaskKey;
import com.voice.demo.tools.preference.CCPPreferenceSettings;
import com.voice.demo.tools.preference.CcpPreferences;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.app.MyApp;
import com.ytdinfo.keephealth.model.ChatInfoBean;
import com.ytdinfo.keephealth.model.DocInfoBean;
import com.ytdinfo.keephealth.model.GroupUserInfoBean;
import com.ytdinfo.keephealth.model.UserGroupBean;
import com.ytdinfo.keephealth.model.UserModel;
import com.ytdinfo.keephealth.service.TimerService;
import com.ytdinfo.keephealth.ui.ChatPhotoViewerActivity;
import com.ytdinfo.keephealth.ui.MainActivity;
import com.ytdinfo.keephealth.ui.report.ChatCommentActivity;
import com.ytdinfo.keephealth.ui.view.MyProgressDialog;
import com.ytdinfo.keephealth.utils.BitmapCompressorUtil;
import com.ytdinfo.keephealth.utils.DBUtilsHelper;
import com.ytdinfo.keephealth.utils.ImageLoaderUtils;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.NetworkReachabilityUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;
import com.ytdinfo.keephealth.utils.ToastUtil;
import com.ytdinfo.keephealth.utils.UserDataUtils;

/**
 * The details of interface group chat, voice chat and send files or text.
 * 
 * @version Time: 2013-7-21
 */
@TargetApi(9)
public class GroupChatActivity extends GroupBaseActivity implements
		View.OnClickListener
		/* ,onVoiceMediaPlayListener */
		, CCPChatFooter.OnChattingLinstener {
	private static final String TAG = GroupChatActivity.class.getName();
	private static String USER_DATA = null;
	public static final int CHAT_MODE_IM_POINT = 0x1;
	public static final int CHAT_MODE_IM_GROUP = 0x2;

	public static final int REQUEST_CODE_TAKE_PICTURE = 11;
	public static final int REQUEST_CODE_SELECT_FILE = 12;

	// recording of three states
	public static final int RECORD_NO = 0;
	public static final int RECORD_ING = 1;
	public static final int RECORD_ED = 2;

	public int mRecordState = 0;
	// the most short recording time, in milliseconds seconds,
	// 0 for no time limit is set to 1000, suggestion
	// recording time
	// microphone gain volume value
	private static final int MIX_TIME = 1000;
	protected static final int CAMERA_RECORD_ACTIVITY = 13;

	public static HashMap<String, Boolean> voiceMessage = new HashMap<String, Boolean>();

	private IMGroupChatItemAdapter mIMGroupApapter;
	private String currentRecName;
	private String mGroupId;
	private String mGroupName;
	private ListView mIMGroupListView;
	private TextView mNoticeTips;
	private CCPChatFooter mChatFooter = null;
	private XQuickActionBar xQuickActionBar;
	private boolean isRecordAndSend = false;
	private int chatModel = CHAT_MODE_IM_POINT;
	private long recodeTime = 0;
	public int mPosition = -1;
	private Button more;
	private ImageView back;
	private RelativeLayout gc_t_re;
	private TextView gc_title2;
	private TextView title;
	// private ReportBean reportBean;
	private UserModel userModel;
	// private String SubjectID,DocID,DocAccount,DocVoip,DocHeadPic;
	// private String StudyID;
	private String subjectID;
	private DocInfoBean docInfoBean;
	private ProgressBar mProgressBar;

	private RelativeLayout reConsult_re, myProgressbar_re;
	private Button reConsult_bt;

	private ChatInfoBean chatInfoBean;
	private ChatInfoBean chatInfoBean2;
	DbUtils dbUtils;
	
	private PopupWindow pop;
	public Button bt1;
	public Button bt2;
	public Button bt3;
	
	View activityRootView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userModel = new Gson().fromJson(
				SharedPrefsUtil.getValue(Constants.USERMODEL, null),
				UserModel.class);
		dbUtils = DBUtilsHelper.getInstance().getDb();
		// init emoji .
		EmoticonUtil.initEmoji();
		// ysj....
		initView();
		initResourceRefs();
		initialize(savedInstanceState);
		registerReceiver(new String[] { CCPIntentUtils.INTENT_IM_RECIVE,
				CCPIntentUtils.INTENT_REMOVE_FROM_GROUP,
				CCPIntentUtils.INTENT_DELETE_GROUP_MESSAGE });
		SharedPreferences ccpDemoSP = CcpPreferences.getSharedPreferences();
		isRecordAndSend = ccpDemoSP.getBoolean(
				CCPPreferenceSettings.SETTING_VOICE_ISCHUNKED.getId(),
				((Boolean) CCPPreferenceSettings.SETTING_VOICE_ISCHUNKED
						.getDefaultValue()).booleanValue());

		CCPHelper.getInstance().setHandler(mIMChatHandler);
		if (checkeDeviceHelper()) {
			/*
			 * String titleString = null; if(chatModel == CHAT_MODE_IM_GROUP) {
			 * titleString = mGroupName; //rightButton =
			 * getString(R.string.str_title_right_group_info); } else {
			 * titleString = mGroupName; }
			 */
			// title.setText(titleString);
			/*
			 * handleTitleDisplay(getString(R.string.btn_title_back) , title ,
			 * rightButton);
			 */
			// / inintProgressbar();
		}
		try {
			//如果超时窗口已弹出，则不显示评价窗口
			if(alertDialog != null && alertDialog.isShowing()){
				return;
			}
			chatInfoBean = dbUtils.findFirst(Selector.from(
					ChatInfoBean.class).where("docInfoBeanId", "=", mGroupId));
			
			if(chatInfoBean != null && SharedPrefsUtil.getValue("RM_"+chatInfoBean.getSubjectID(), false) == false){
				gc_title2.setVisibility(View.VISIBLE);
			}else{
				gc_title2.setVisibility(View.GONE);
			}
			if (null != chatInfoBean && !chatInfoBean.isComment()
					&& !chatInfoBean.isStatus() && !chatInfoBean.isTimeout()) {
				gc_title2.setVisibility(View.GONE);
				Intent i = new Intent();
				i.putExtra("subjectId", subjectID);
				//i.putExtra("voipAccount", chatInfoBean.getDocInfoBeanId());
				i.setClass(GroupChatActivity.this, ChatCommentActivity.class);
				startActivity(i);
			}
			
			 

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initProgressbar() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putInt("op", 1);
		serciceIntent.putExtras(bundle);
		startService(serciceIntent);
	}

	private Intent serciceIntent;

	private void initView() {
		// TODO Auto-generated method stub
		 activityRootView = findViewById(R.id.im_root);
		gc_t_re = (RelativeLayout) findViewById(R.id.gc_t_re);
		back = (ImageView) findViewById(R.id.gc_back);
		more = (Button) findViewById(R.id.gc_more);
		title = (TextView) findViewById(R.id.gc_title);
		gc_title2 = (TextView) findViewById(R.id.gc_title2);
		
		mProgressBar = (ProgressBar) findViewById(R.id.gc_progressbar);
		reConsult_re = (RelativeLayout) findViewById(R.id.reconsult_re);
		myProgressbar_re = (RelativeLayout) findViewById(R.id.mp_re);
		reConsult_bt = (Button) findViewById(R.id.reconsult_bt);
		title.setText("体检报告在线解读");
		reConsult_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isComment) {
					if (DBUtilsHelper.getInstance().isOnline()) {
						ToastUtil.showMessage("您当前正在进行在线咨询，结束后才能进行报告解读哦");
					} else {
						requestChatAgain();
					}
				} else {
					Intent i = new Intent();
					i.putExtra("subjectId", subjectID);
					//i.putExtra("voipAccount", chatInfoBean.getDocInfoBeanId());
					i.setClass(GroupChatActivity.this,
							ChatCommentActivity.class);
					startActivity(i);
				}

			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GroupChatActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("news", "news");
				startActivity(intent);
				finish();
			}
		});
		more.setOnClickListener(new OnClickListener() {
			// 结束咨询
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (chatInfoBean2 != null) {
					if (chatInfoBean2.getSubjectType().equals("3")) {
						// 在线问诊
						MobclickAgent.onEvent(GroupChatActivity.this,
								Constants.UMENG_EVENT_11);
					}
				}

				/** 隐藏软键盘 **/
				View view = getWindow().peekDecorView();
				if (view != null) {
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}
				showExitChat();
			}
		});

		mProgressBar = (ProgressBar) findViewById(R.id.gc_progressbar);
		mProgressBar.setMax(Constants.PROGRESSMAX);
		serciceIntent = new Intent(GroupChatActivity.this, TimerService.class);
		TimerService.setmHandler(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				LogUtil.i(TAG,msg.arg1 + "");
				if (NetworkReachabilityUtil.isNetworkConnected(MyApp
						.getInstance())) {
					if (CCPHelper.getInstance().getDevice() == null
							|| CCPHelper.getInstance().getDevice().isOnline() != State.ONLINE) {
						CCPHelper.getInstance().reConnect();
					}
				}
				super.handleMessage(msg);
				if (msg.arg1 >= Constants.PROGRESSMAX) {
					gc_title2.setVisibility(View.GONE);
					SharedPrefsUtil.putValue("RM_"+chatInfoBean2.getSubjectID(), true);
					try {
						chatInfoBean2 = dbUtils.findFirst(Selector.from(
								ChatInfoBean.class).where("docInfoBeanId", "=",
										docInfoBean.getVoipAccount()));
					
					chatInfoBean2.setTimeout(true);
					chatInfoBean2.setStatus(false);
					subjectId = chatInfoBean2.getSubjectID();
					DBUtilsHelper.getInstance().saveChatinfo(chatInfoBean2);
					Intent intent = new Intent(CCPIntentUtils.INTENT_IM_RECIVE);
					intent.putExtra(GroupBaseActivity.KEY_GROUP_ID, Constants.CLOSESUBJECT);
					MyApp.getInstance().sendBroadcast(intent);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//move to timerservice					
//					String uniqueID = getDeviceHelper()
//							.sendInstanceMessage(mGroupId,
//									Constants.CLOSESUBJECT, null,
//									USER_DATA);
//					RequestParams params = new RequestParams();
//					params.addQueryStringParameter("subjectId", subjectId);
//					HttpClient.get(GroupChatActivity.this, Constants.CLOSESUBJECT_URL,
//							params, new RequestCallBack<String>() {
//								@Override
//								public void onStart() {
//									// TODO Auto-generated method stub
//									super.onStart();
//								}
//
//								@Override
//								public void onSuccess(ResponseInfo<String> arg0) {
//									// TODO Auto-generated method stub
//									LogUtil.i(TAG, arg0.result);
//									// exitComment();
//								}
//
//								@Override
//								public void onFailure(HttpException arg0, String arg1) {
//									// TODO Auto-generated method stub
//									LogUtil.i(TAG, arg0.getMessage());
//								}
//							});
				} else {
					mProgressBar.setProgress(msg.arg1);
				}
				// title.setText(msg.arg1+"");
			}
		});
	}

	private void exitComment() {
		// TODO Auto-generated method stub
		mProgressBar.setProgress(0);
		mChatFooter.setVisibility(View.GONE);
		reConsult_re.setVisibility(View.VISIBLE);
		/*
		 * Bundle bundle = new Bundle(); bundle.putInt("op", 3);
		 * serciceIntent.putExtras(bundle); startService(serciceIntent);
		 * TimerService.isPause = false;
		 */
		// SharedPrefsUtil.remove(Constants.SUBJECTID);
		// SharedPrefsUtil.remove(Constants.CHATINFO);
	}

	private void initResourceRefs() {

//		final View activityRootView = findViewById(R.id.im_root);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					boolean showInput = false;

					@Override
					public void onGlobalLayout() {

						Rect r = new Rect();
						activityRootView.getWindowVisibleDisplayFrame(r);
						int heightDiff = activityRootView.getRootView()
								.getHeight() - (r.bottom - r.top);

						// int heightDiff =
						// activityRootView.getRootView().getHeight() -
						// activityRootView.getHeight();
						if (heightDiff > 100) {
							showInput = true;
							if (heightDiff >= 100) {

								// do nothing ..
								return;
							}
							// If the difference is more than 100 pixels, is
							// likely to be a soft keyboard...
							// do something here

							if (mIMGroupListView != null
									&& mIMGroupApapter != null) {
								mIMGroupListView.setSelection(mIMGroupApapter
										.getCount() - 1);

							}
							// The judge of this input method is the pop-up
							// state,
							// then set the record button is not available
							mChatFooter.setMode(1);
						} else {

							// int heightDensity = Math.round(38 *
							// getResources().getDisplayMetrics().densityDpi /
							// 160.0F);
							if (!showInput) {
								return;
							}
							showInput = false;
							if (mChatFooter.getMode() == 1) {
								mChatFooter.setMode(2, false);
							}
						}
					}
				});

		mNoticeTips = (TextView) findViewById(R.id.notice_tips);
		mNoticeTips.setVisibility(View.GONE);
		mIMGroupListView = (ListView) findViewById(R.id.im_chat_list);

		//
		mIMGroupListView
				.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mIMGroupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				HideSoftKeyboard();

				// After the input method you can use the record button.
				// mGroudChatRecdBtn.setEnabled(true);
				mChatFooter.setMode(2, false);
				return false;
			}
		});

		mChatFooter = (CCPChatFooter) findViewById(R.id.nav_footer);
		mChatFooter.setOnChattingLinstener(this);
		mChatFooter.mImEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
					mIMGroupListView.setStackFromBottom(hasFocus);
			}
		});

	}

	private UserGroupBean userGroupBean;

	private void initialize(Bundle savedInstanceState) {
		// Read parameters or previously saved state of this activity.
		Intent intent = getIntent();
		if (intent.hasExtra("groupId")) {
			mGroupId = intent.getExtras().getString("groupId");
			chatModel = CHAT_MODE_IM_GROUP;
			more.setVisibility(View.INVISIBLE);
			gc_title2.setVisibility(View.GONE);
			new IMListyncTask().execute(mGroupId);
			try {
				userGroupBean = dbUtils.findFirst(Selector.from(
						UserGroupBean.class).where("groupId", "=", mGroupId));
				if (null != userGroupBean) {
					title.setText(userGroupBean.getName());
				} else {
					title.setText(mGroupId);
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (intent.hasExtra("chatInfoBean")) {
			chatInfoBean = (ChatInfoBean) intent
					.getSerializableExtra("chatInfoBean");
			subjectID = chatInfoBean.getSubjectID();
			USER_DATA = UserDataUtils.getUserData(subjectID);
			// initProgressbar();
			myProgressbar_re.setVisibility(View.VISIBLE);
			gc_t_re.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
			try {
				docInfoBean = dbUtils.findFirst(Selector
						.from(DocInfoBean.class).where("voipAccount", "=",
								chatInfoBean.getDocInfoBeanId()));
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mGroupId = chatInfoBean.getDocInfoBeanId();
			String docName = docInfoBean.getUserName();
			if (null != docName && !docName.equals("")) {
				title.setText(docName);
			} else {
				title.setText(mGroupId);
			}
			if (!chatInfoBean.getSubjectType().equals("3")) {
				// send real message
				onSendTextMesage("您好，医生，麻烦您帮我解读一下这次的体检报告，我有点看不明白，我需要复查一次吗？");
			}
			if (!chatInfoBean.getSubjectType().equals("1")) {
				// onSendTextMesage("您好，医生，以下情况想跟您咨询一下。");

				if (intent.hasExtra("content")) {
					String content = intent.getStringExtra("content");
					onSendTextMesage(content);
					SharedPrefsUtil.putValue("msg_content", content);
				}
				if (intent.hasExtra("imageList")) {
					// onSendTextMesage(intent.getStringExtra("content"));
					List<String> list = intent
							.getStringArrayListExtra("imageList");
					if (null != list) {
						for (int i = 0; i < list.size(); i++) {
							// send fake message
							onSendTextMesageFile(list.get(i));
						}
					}
				}

			}
			new Handler().postDelayed(new Runnable() {
				public void run() {
					// execute the task
					onSendTextMesage4(getResources().getString(
							R.string.bmyxzs_messge));
				}
			}, 1000);
			new IMListyncTask().execute(mGroupId);

		} else if (intent.hasExtra("docInfoBean")) {
			docInfoBean = (DocInfoBean) intent
					.getSerializableExtra("docInfoBean");
			mGroupId = docInfoBean.getVoipAccount();
			String docName = docInfoBean.getUserName();
			ChatInfoBean chatInfoBean2 = null;
			try {
				chatInfoBean2 = dbUtils.findFirst(Selector.from(
						ChatInfoBean.class).where("docInfoBeanId", "=",
						docInfoBean.getVoipAccount()));
				subjectID = chatInfoBean2.getSubjectID();
				USER_DATA = UserDataUtils.getUserData(subjectID);
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				chatInfoBean = dbUtils.findFirst(Selector.from(
						ChatInfoBean.class).where("status", "=", true));
				if (null != chatInfoBean) {
					String docinfoId = chatInfoBean.getDocInfoBeanId();
					// chatInfoBean.setStatus(false);
					if (docInfoBean.getVoipAccount().equals(docinfoId)) {

						mChatFooter.setVisibility(View.VISIBLE);
						reConsult_re.setVisibility(View.GONE);
						more.setVisibility(View.VISIBLE);
						myProgressbar_re.setVisibility(View.VISIBLE);
						gc_t_re.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());

					} else {
						mChatFooter.setVisibility(View.GONE);
						reConsult_re.setVisibility(View.VISIBLE);
						more.setVisibility(View.GONE);
						myProgressbar_re.setVisibility(View.GONE);
						gc_t_re.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());

					}
				} else {
					mChatFooter.setVisibility(View.GONE);
					reConsult_re.setVisibility(View.VISIBLE);
					more.setVisibility(View.GONE);
					myProgressbar_re.setVisibility(View.GONE);
					gc_t_re.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
				}

				if (null != docName && !docName.equals("")) {
					title.setText(docName);
				} else {
					title.setText(mGroupId);
				}

			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new IMListyncTask().execute(mGroupId);
			mProgressBar.setProgress(TimerService.count);
			if (chatInfoBean2.isTimeout()) {
				if (null != chatInfoBean2 && !chatInfoBean2.isComment()
						&& !chatInfoBean2.isStatus()
						&& !chatInfoBean2.isTimeout()) {
					Intent i = new Intent();
					i.putExtra("subjectId", subjectID);
					i.putExtra("voipAccount", chatInfoBean2.getDocInfoBeanId());
					i.setClass(GroupChatActivity.this,
							ChatCommentActivity.class);
					startActivity(i);
				}
				showTimeOut();
				chatInfoBean2.setTimeout(false);
				DBUtilsHelper.getInstance().saveChatinfo(chatInfoBean2);
			}
		}
		/*
		 * { if (intent.hasExtra("subjectID")) {
		 * subjectID=intent.getExtras().getString("subjectID"); USER_DATA =
		 * UserDataUtils.getUserData(subjectID); initProgressbar();
		 * myProgressbar_re.setVisibility(View.VISIBLE); }else {
		 * mChatFooter.setVisibility(View.GONE);
		 * reConsult_re.setVisibility(View.VISIBLE);
		 * more.setVisibility(View.GONE);
		 * myProgressbar_re.setVisibility(View.GONE); } if
		 * (intent.hasExtra("docInfoBean")) { docInfoBean=(DocInfoBean)
		 * intent.getSerializableExtra("docInfoBean"); if
		 * (null!=docInfoBean&&null!=chatInfoBean) { if (chatInfoBean==null) {
		 * mChatFooter.setVisibility(View.GONE);
		 * reConsult_re.setVisibility(View.VISIBLE); } if
		 * (!docInfoBean.getVoipAccount
		 * ().equals(chatInfoBean.getDocInfoBean().getVoipAccount())) {
		 * mChatFooter.setVisibility(View.GONE);
		 * reConsult_re.setVisibility(View.VISIBLE); }
		 * 
		 * } } //LogUtil.i(TAG, reportBean.toString()); if
		 * (subjectID!=null&&docInfoBean ==null) {
		 * 
		 * ArrayList<IMChatMessageDetail> list =
		 * CCPSqliteManager.getInstance().queryIMMessagesBySessionId(subjectID);
		 * if (null==list||list.size() ==0) {
		 * onSendTextMesage2("您好，医生，麻烦您帮我解读一下这次的体检报告，我有点看不明白，我需要复查一次吗？"); new
		 * Handler().postDelayed(new Runnable(){ public void run() { //execute
		 * the task
		 * onSendTextMesage3("您好，亲！我是帮忙医的机器人--小医。我们已经收到您的提问，马上会有医生来为您解答，请稍后..."
		 * ); } }, 1000); }else { new IMListyncTask().execute(subjectID); }
		 * requestDoctor(); } if (docInfoBean !=null) { mGroupId
		 * =docInfoBean.getVoipAccount(); String docName =
		 * docInfoBean.getUserName(); if (null!=docName&&!docName.equals("")) {
		 * title.setText(docName); }else { title.setText(mGroupId); }
		 * 
		 * if (intent.getBooleanExtra("firstStart", false)) {
		 * 
		 * onSendTextMesage("您好，医生，麻烦您帮我解读一下这次的体检报告，我有点看不明白，我需要复查一次吗？"); new
		 * Handler().postDelayed(new Runnable(){ public void run() { //execute
		 * the task
		 * onSendTextMesage4("您好，亲！我是帮忙医的机器人--小医。我们已经收到您的提问，马上会有医生来为您解答，请稍后..."
		 * ); } }, 1000);
		 * 
		 * } new IMListyncTask().execute(mGroupId); }
		 */
		// }

		/*
		 * if (intent.hasExtra(KEY_GROUP_ID)) { Bundle extras =
		 * intent.getExtras(); if (extras != null) { mGroupId = (String)
		 * extras.get(KEY_GROUP_ID);
		 * 
		 * } }
		 */

		/*
		 * if (intent.hasExtra("groupName")) { Bundle extras =
		 * intent.getExtras(); if (extras != null) { mGroupName = (String)
		 * extras.get("groupName");
		 * 
		 * } }
		 */

		/*
		 * if(TextUtils.isEmpty(mGroupId)) {
		 * Toast.makeText(getApplicationContext(),
		 * R.string.toast_group_id_error, Toast.LENGTH_SHORT).show(); finish();
		 * return; mGroupName = "体检报告解读";
		 * 
		 * }else { if(mGroupId.startsWith("g")) { chatModel =
		 * CHAT_MODE_IM_GROUP; } else { chatModel = CHAT_MODE_IM_POINT; }
		 * 
		 * new IMListyncTask().execute(mGroupId); }
		 * 
		 * if(TextUtils.isEmpty(mGroupName)) { mGroupName = mGroupId; }
		 */
		// }
	}

	private void requestDoctor() {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("SubjectType", "1");
			/*
			 * jsonObject.put("UserID", userModel.getID());
			 * jsonObject.put("UserName", userModel.getUserName());
			 * jsonObject.put("UserSex", userModel.getUserSex());
			 * jsonObject.put("Age", null); jsonObject.put("HeadPicture",
			 * userModel.getHeadPicture());
			 */
			jsonObject.put("RelationShip", "1");
			jsonObject.put("StudyID", subjectID);
			jsonObject.put("AttachPics", null);
			jsonObject.put("BodyContent", null);
			HttpClient.post(Constants.STARTCHAT_URl, jsonObject.toString(),
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							LogUtil.i(TAG, arg0.result);
							analyzeJson(arg0.result);

						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

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
			/*
			 * SubjectID = data.getString("SubjectID"); JSONObject responser =
			 * data.getJSONObject("responser"); DocID = responser
			 * .getInt("UserID")+""; DocAccount = responser
			 * .getString("subAccountSid"); DocVoip = responser
			 * .getString("voipAccount"); DocHeadPic = responser
			 * .getString("HeadPicture"); mGroupId = DocVoip; new
			 * IMListyncTask().execute(mGroupId);
			 */
			title.setText(mGroupId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void handleTaskBackGround(ITask iTask) {
		super.handleTaskBackGround(iTask);
		int key = iTask.getKey();
		if (key == TaskKey.TASK_KEY_DEL_MESSAGE) {
			try {
				CCPSqliteManager.getInstance().deleteIMMessageBySessionId(
						mGroupId);
				if (mIMGroupApapter != null) {
					for (int i = 0; i < mIMGroupApapter.getCount(); i++) {
						IMChatMessageDetail item = mIMGroupApapter.getItem(i);
						if (item == null
								|| item.getMessageType() == IMChatMessageDetail.TYPE_MSG_TEXT) {
							continue;
						}

						CCPUtil.delFile(item.getFilePath());

					}
				}
				closeConnectionProgress();
				sendbroadcast();// after dismiss progress
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * @Override protected void handleTitleAction(int direction) {
	 * 
	 * if(direction == TITLE_RIGHT_ACTION) { if(chatModel == CHAT_MODE_IM_GROUP)
	 * { if(xQuickActionBar==null){ xQuickActionBar = new
	 * XQuickActionBar(findViewById(R.id.voice_right_btn));
	 * xQuickActionBar.setOnPopClickListener(popListener); }
	 * 
	 * int switchId = isEarpiece ? R.string.pull_mode_speaker :
	 * R.string.pull_mode_earpiece;
	 * 
	 * int[] arrays = new int[]{switchId , R.string.str_title_right_group_info};
	 * 
	 * xQuickActionBar.setArrays(arrays); xQuickActionBar.show();
	 * 
	 * return ; }
	 * 
	 * showConnectionProgress(getString(R.string.str_dialog_message_default));
	 * ITask iTask = new ITask(TaskKey.TASK_KEY_DEL_MESSAGE); addTask(iTask);
	 * 
	 * } else { super.handleTitleAction(direction); } }
	 */

	String uniqueId = null;

	@Override
	public void onRecordInit() {

		if (getRecordState() != RECORD_ING) {
			setRecordState(RECORD_ING);

			// release all playing voice file
			releaseVoiceAnim(-1);
			readyOperation();

			mChatFooter.showVoiceDialog(findViewById(R.id.im_root).getHeight()
					- mChatFooter.getHeight());

			// True audio data recorded immediately transmitted to the server
			// False just recording audio data, then send audio file after the
			// completion of recording..
			// isRecordAndSend = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					currentRecName = +System.currentTimeMillis() + ".amr";
					File directory = getCurrentVoicePath();
					if (checkeDeviceHelper()) {
						// If it is sent non't in chunked mode, only second
						// parameters
						try {
							uniqueId = getDeviceHelper().startVoiceRecording(
									mGroupId, directory.getAbsolutePath(),
									isRecordAndSend, USER_DATA);
							voiceMessage.put(uniqueId, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		}
	}

	@Override
	public void onRecordStart() {

		// If you are in when being loaded, then send to start recording
		mIMChatHandler.removeMessages(WHAT_ON_COMPUTATION_TIME);
		mIMChatHandler.sendEmptyMessageDelayed(WHAT_ON_COMPUTATION_TIME,
				GroupChatActivity.TONE_LENGTH_MS);
	}

	@Override
	public void onRecordCancle() {
		handleMotionEventActionUp(true);
	}

	@Override
	public void onRecordOver() {
		handleMotionEventActionUp(false);
	}
	
	@Override
	public void onSendTextMesage(String text){
		onSendTextMesage(text,null);
	}
	
	public void onSendTextMesage(String text,String userData) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		/*
		 * if (mGroupId==null) { onSendTextMesage2(text); return; }
		 */
		IMChatMessageDetail chatMessageDetail = IMChatMessageDetail
				.getGroupItemMessage(IMChatMessageDetail.TYPE_MSG_TEXT,
						IMChatMessageDetail.STATE_IM_SENDING, mGroupId);
		chatMessageDetail.setMessageContent(text);
		// IMChatMessageDetail chatMessageDetail2 =
		// IMChatMessageDetail.getGroupItemMessage(IMChatMessageDetail.TYPE_MSG_TEXT,
		// imState, sessionId)
		// IMChatMessageDetail cImChatMessageDetail
		if (!checkeDeviceHelper()) {
			return;
		}
		String userData2 = USER_DATA;
		if(userData != null){
			userData2 = userData;
		}
		try {
			String uniqueID = getDeviceHelper().sendInstanceMessage(mGroupId,
					text.toString(), null, userData2);
			if (TextUtils.isEmpty(uniqueID)) {
				MyApp.getInstance().showToast(
						R.string.toast_send_group_message_failed);
				chatMessageDetail
						.setImState(IMChatMessageDetail.STATE_IM_SEND_FAILED);
				return;
			}
			chatMessageDetail.setMessageId(uniqueID);
			chatMessageDetail.setUserData(userData2);
			if(!mGroupId.startsWith("g")){
				timingPause();
			}
			CCPSqliteManager.getInstance().insertIMMessage(chatMessageDetail);
			sendbroadcast();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		text = null;
	}
	
	private void timingPause() {
		// TODO Auto-generated method stub
		if (!TimerService.isPause && TimerService.isStart) {
			/*
			 * Bundle bundle = new Bundle(); bundle.putInt("op", 2);
			 * serciceIntent.putExtras(bundle);
			 * MyApp.getInstance().startService(serciceIntent);
			 */
			TimerService.isPause = !TimerService.isPause;
		}
	}

	List<IMChatMessageDetail> result = new ArrayList<IMChatMessageDetail>();

	public void onSendTextMesageFile(String text) {
		IMChatMessageDetail chatMessageDetail = IMChatMessageDetail
				.getGroupItemMessage(IMChatMessageDetail.TYPE_MSG_FILE,
						IMChatMessageDetail.STATE_IM_SEND_SUCCESS, mGroupId);
		chatMessageDetail.setMessageContent(text);
		chatMessageDetail.setFileUrl(text);
		// chatMessageDetail.setGroupSender("321");
		chatMessageDetail.setImState(IMChatMessageDetail.STATE_IM_SEND_SUCCESS);
		chatMessageDetail.setMessageId(System.currentTimeMillis() + "");
		chatMessageDetail.setUserData(USER_DATA);
		try {
			CCPSqliteManager.getInstance().insertIMMessage(chatMessageDetail);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!mGroupId.startsWith("g")){
			timingPause();
		}
		sendbroadcast();
		// text = null;
		// result.add(chatMessageDetail);
		// mIMGroupApapter = new IMGroupChatItemAdapter(result);
		// mIMGroupListView.setAdapter(mIMGroupApapter);

	}

	public void onSendTextMesage3(String text) {
		IMChatMessageDetail chatMessageDetail = IMChatMessageDetail
				.getGroupItemMessageReceived("321",
						IMChatMessageDetail.TYPE_MSG_TEXT, subjectID, "321");
		chatMessageDetail.setMessageContent(text);
		chatMessageDetail.setGroupSender("321");
		chatMessageDetail.setImState(IMChatMessageDetail.STATE_IM_RECEIVEED);
		// chatMessageDetail.setImState(IMChatMessageDetail.STATE_IM_SEND_SUCCESS);
		try {
			CCPSqliteManager.getInstance().insertIMMessage(chatMessageDetail);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendbroadcastbyId(subjectID);
		text = null;

	}

	public void onSendTextMesage4(String text) {
		IMChatMessageDetail chatMessageDetail = IMChatMessageDetail
				.getGroupItemMessageReceived(
						System.currentTimeMillis() + "xzs",
						IMChatMessageDetail.TYPE_MSG_TEXT, mGroupId, mGroupId);
		chatMessageDetail.setMessageContent(text);
		chatMessageDetail.setGroupSender(mGroupId);
		chatMessageDetail.setImState(IMChatMessageDetail.STATE_IM_RECEIVEED);
		// chatMessageDetail.setImState(IMChatMessageDetail.STATE_IM_SEND_SUCCESS);
		try {
			CCPSqliteManager.getInstance().insertIMMessage(chatMessageDetail);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendbroadcastbyId(mGroupId,chatMessageDetail.getMessageId());
		text = null;

	}

	@Override
	public void onSelectVideo() {
		new AlertDialog.Builder(this)
				.setItems(R.array.chatvideo_select_item,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (!Environment.getExternalStorageState()
										.equals(Environment.MEDIA_MOUNTED)) {
									Toast.makeText(
											getApplicationContext(),
											R.string.sdcard_not_file_trans_disable,
											Toast.LENGTH_SHORT).show();
									return;
								}

								if (which == 0) {// take videoRecored
									Intent intent = new Intent(
											GroupChatActivity.this,
											VideoRecordActivity.class);
									startActivityForResult(intent,
											REQUEST_CODE_SELECT_FILE);
									// Intent mIntent = new
									// Intent(MediaStore.ACTION_VIDEO_CAPTURE);
									// mIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,
									// 30);//这个值以秒为单位，显示视频采集的时长
									// startActivityForResult(mIntent,
									// CAMERA_RECORD_ACTIVITY);//CAMERA_ACTIVITY
									// = 1

								} else if (which == 1) {// go to photo album
									Intent intent = new Intent(
											GroupChatActivity.this,
											FileBrowserActivity.class);
									// intent.putExtra("to", recipient);
									startActivityForResult(intent,
											REQUEST_CODE_SELECT_FILE);
								}
							}
						}).setTitle(R.string.dialog_list_item_title).create()
				.show();
	}
	public void initPop() {

		pop = new PopupWindow(this);

		View view = (this).getLayoutInflater().inflate(
				R.layout.item_popupwindows, null);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LogUtil.i(TAG, "照相");
				pop.dismiss();
				//照相
				takePicture();
				}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				// 相册
				takepicfile = CCPUtil.TackPicFilePath();
				openGallery();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 取消
				pop.dismiss();
			}
		});

	}
	@Override
	public void onSelectFile() {
		if (!CCPUtil.isExistExternalStore()) {
			Toast.makeText(getApplicationContext(), "SD card is un_mounted ",
					Toast.LENGTH_SHORT).show();
			return;
		}
		initPop();
		pop.showAtLocation(activityRootView, Gravity.BOTTOM, 0, 0);
//		new AlertDialog.Builder(this)
//				.setItems(R.array.chat_select_item,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// The 'which' argument contains the index
//								// position
//								// of the selected item
//								if (which == 0) {// take pic
//									takePicture();
//								} else if (which == 1) {
//									takepicfile = CCPUtil.TackPicFilePath();
//									openGallery();
//
//									/*
//									 * //save
//									 * if(!Environment.getExternalStorageState
//									 * ().equals(Environment.MEDIA_MOUNTED)) {
//									 * Toast.makeText(getApplicationContext(),
//									 * R.string.sdcard_not_file_trans_disable,
//									 * Toast.LENGTH_SHORT).show(); return; }
//									 * Intent intent = new
//									 * Intent(GroupChatActivity.this,
//									 * FileBrowserActivity.class) ;
//									 * //intent.putExtra("to", recipient);
//									 * startActivityForResult(intent ,
//									 * REQUEST_CODE_SELECT_FILE);
//									 */}
//							}
//						}).setTitle(R.string.dialog_list_item_title).create()
//				.show();
	}

	public void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		this.startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
		/*
		 * takepicfile = CCPUtil.TackPicFilePath(); Intent intent = new
		 * Intent(Intent.ACTION_PICK);// 打开相册
		 * intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
		 * "image/*"); intent.putExtra("output", Uri.fromFile(takepicfile));
		 * startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
		 */
	}

	/**
	 * @version 3.5
	 *          <p>
	 *          Title: handleMotionEventActionUp
	 *          </p>
	 *          <p>
	 *          Description: The current activity is not visible, if you are
	 *          recording, stop recording and performing transmission or cancel
	 *          the operation {@link GroupChatActivity#mRecordState   }
	 *          {@link GroupChatActivity#RECORD_ED}
	 *          {@link GroupChatActivity#RECORD_ING}
	 *          {@link GroupChatActivity#RECORD_NO}
	 *          </p>
	 * 
	 * @see Device#startVoiceRecording(String, String, boolean, String);
	 * @see Device#cancelVoiceRecording();
	 * @see Device#stopVoiceRecording();
	 */
	private void handleMotionEventActionUp(boolean doCancle) {

		if (getRecordState() == RECORD_ING) {

			if (checkeDeviceHelper()) {
				if (doCancle) {
					getDeviceHelper().cancelVoiceRecording();
				} else {
					getDeviceHelper().stopVoiceRecording();
				}
			}
			doProcesOperationRecordOver(doCancle);
			Log4Util.d(CCPHelper.DEMO_TAG, "handleMotionEventActionUp");
		}
	}

	private static final int REQUEST_KEY_RESEND_IMMESSAGE = 0X1;
	private static final int WHAT_ON_COMPUTATION_TIME = 10000;
	private long computationTime = -1L;
	private Toast mRecordTipsToast;

	private void readyOperation() {
		computationTime = -1L;
		mRecordTipsToast = null;
		playTone(ToneGenerator.TONE_PROP_BEEP, TONE_LENGTH_MS);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				stopTone();
			}
		}, TONE_LENGTH_MS);
		vibrate(50L);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// version 3.5
		// re send message where failed message
		case R.id.error_Icon:

			try {
				Integer position = (Integer) v.getTag();
				if (mIMGroupApapter != null
						&& mIMGroupApapter.getItem(position) != null) {
					mPosition = position;
					showAlertTipsDialog(REQUEST_KEY_RESEND_IMMESSAGE,
							getString(R.string.str_chatting_resend_title),
							getString(R.string.str_chatting_resend_content),
							getString(R.string.dialog_btn),
							getString(R.string.dialog_cancle_btn));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void handleDialogOkEvent(int requestKey) {

		if (requestKey == REQUEST_KEY_RESEND_IMMESSAGE) {
			if (mPosition == -1) {
				return;
			}
			reSendImMessage(mPosition);
		} else {

			super.handleDialogOkEvent(requestKey);
		}

	}

	@Override
	protected void handleDialogCancelEvent(int requestKey) {
		if (requestKey == REQUEST_KEY_RESEND_IMMESSAGE) {
			mPosition = -1;
		} else {
			super.handleDialogCancelEvent(requestKey);
		}
	}

	/**
	 * 
	 * <p>
	 * Title: reSendImMessage
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param postion
	 */
	public void reSendImMessage(int position) {

		if (mIMGroupApapter != null
				&& mIMGroupApapter.getItem(position) != null) {
			IMChatMessageDetail item = mIMGroupApapter.getItem(position);

			try {
				String uniqueID = null;
				if (checkeDeviceHelper()) {
					if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_TEXT) {
						uniqueID = getDeviceHelper().sendInstanceMessage(
								mGroupId, item.getMessageContent(), null,
								USER_DATA);
					} else {
						uniqueID = getDeviceHelper().sendInstanceMessage(
								mGroupId, null, item.getFilePath(), USER_DATA);
					}
				}
				if (TextUtils.isEmpty(uniqueID)) {
					MyApp.getInstance().showToast(
							R.string.toast_send_group_message_failed);
					item.setImState(IMChatMessageDetail.STATE_IM_SEND_FAILED);
					return;
				}
				CCPSqliteManager.getInstance().deleteIMMessageByMessageId(
						item.getMessageId());
				item.setMessageId(uniqueID);
				item.setImState(IMChatMessageDetail.STATE_IM_SENDING);
				CCPSqliteManager.getInstance().insertIMMessage(item);

				mIMGroupApapter.notifyDataSetChanged();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	private boolean isEarpiece = true;
	private boolean isComment = false;

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("GroupChatActivity");
		MobclickAgent.onResume(this);

		// updateReadStatus();
		// default speaker
		isEarpiece = CcpPreferences.getSharedPreferences().getBoolean(
				CCPPreferenceSettings.SETTING_HANDSET.getId(),
				((Boolean) CCPPreferenceSettings.SETTING_HANDSET
						.getDefaultValue()).booleanValue());
		try {
			ChatInfoBean chatInfoBean = dbUtils.findFirst(Selector.from(
					ChatInfoBean.class).where("docInfoBeanId", "=", mGroupId));
			if (null != chatInfoBean) {
				chatInfoBean2 = chatInfoBean;
				if (chatInfoBean.isComment()) {
					isComment = true;
					reConsult_bt.setText("再次咨询");
				} else {
					isComment = false;
					reConsult_bt.setText("评价医生");
					/*
					 * Intent i = new Intent(); i.putExtra("subjectId",
					 * subjectID); i.setClass(GroupChatActivity.this,
					 * ChatCommentActivity.class); startActivity(i);
					 */
				}
			}

		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @author Jorstin Chan
	 * @version 3.4.1.1
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log4Util.d(CCPHelper.DEMO_TAG, "isEarpiece :" + isEarpiece);
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int streamType;
		if (!isEarpiece) {
			streamType = AudioManager.STREAM_MUSIC;
		} else {
			streamType = AudioManager.STREAM_VOICE_CALL;
		}

		int maxVolumeVoiceCall = mAudioManager.getStreamMaxVolume(streamType);
		int index = maxVolumeVoiceCall / 7;
		if (index == 0) {
			index = 1;
		}
		int currentVolume = mAudioManager.getStreamVolume(streamType);
		;
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			mAudioManager.setStreamVolume(streamType, currentVolume - index,
					AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			mAudioManager.setStreamVolume(streamType, currentVolume + index,
					AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
			return true;
		} else {

			// If a recording tool button to display, the display area of the
			// recording instrument
			// this BUG for 3.4.1.1 before
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				Intent intent = new Intent(GroupChatActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("news", "news");
				startActivity(intent);
				finish();

				Log4Util.d(
						CCPHelper.DEMO_TAG,
						"keycode back , chatfooter mode: "
								+ mChatFooter.getMode());
				if (mChatFooter.getMode() != 2) {
					mChatFooter.setMode(2, false);
					return true;
				}
			}

			return super.onKeyDown(keyCode, event);
		}

	}

	private void updateReadStatus() {
		try {
			CCPSqliteManager.getInstance()
					.updateIMMessageUnreadStatusToReadBySessionId(mGroupId,
							IMChatMessageDetail.STATE_READED);
			// NotificationUtils.send(MyApp.getInstance()).cancelAll();
			// NotificationUtils.getNm().cancelAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @version 3.5
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			// release voice record resource.
			if (mChatFooter != null) {
				handleMotionEventActionUp(mChatFooter.isVoiceRecordCancle());
			}

			// release voice play resources
			releaseVoiceAnim(-1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mChatFooter != null) {
			mChatFooter.onDistory();
			mChatFooter = null;
		}
		mIMGroupApapter = null;

		if (mIMChatHandler != null) {
			mIMChatHandler = null;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("GroupChatActivity");
		MobclickAgent.onPause(this);

		HideSoftKeyboard();
		// release voice record resource.
		if (mChatFooter != null) {
			handleMotionEventActionUp(mChatFooter.isVoiceRecordCancle());
		}

		// release voice play resources
		releaseVoiceAnim(-1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log4Util.d(CCPHelper.DEMO_TAG,
				"[IMChatActivity] onActivityResult: requestCode=" + requestCode
						+ ", resultCode=" + resultCode + ", data=" + data);

		// If there's no data (because the user didn't select a file or take pic
		// and
		// just hit BACK, for example), there's nothing to do.
		if (requestCode != REQUEST_CODE_TAKE_PICTURE
				|| requestCode == REQUEST_CODE_SELECT_FILE) {
			if (data == null) {
				return;
			}
		} else if (resultCode != RESULT_OK) {
			Log4Util.d(CCPHelper.DEMO_TAG,
					"[GroupChatActivity] onActivityResult: bail due to resultCode="
							+ resultCode);
			return;
		}

		String fileName = null;
		String filePath = null;
		switch (requestCode) {
		case CAMERA_RECORD_ACTIVITY: {
			if (resultCode != RESULT_OK)
				return;

			try {
				AssetFileDescriptor videoAsset = getContentResolver()
						.openAssetFileDescriptor(data.getData(), "r");
				FileInputStream fis = videoAsset.createInputStream();
				File tmpFile = CCPUtil.TackVideoFilePath();
				FileOutputStream fos = new FileOutputStream(tmpFile);
				byte[] buf = new byte[1024];
				int len;
				while ((len = fis.read(buf)) > 0) {
					fos.write(buf, 0, len);
				}
				fis.close();
				fos.close();
				fileName = tmpFile.getName();
				filePath = tmpFile.getAbsolutePath();

			} catch (IOException io_e) {
			}
			break;
		}
		case REQUEST_CODE_TAKE_PICTURE: {
			new MyTask().execute("0");
			/*
			 * File file = takepicfile; if (file == null || !file.exists()) {
			 * return; }
			 * 
			 * Bitmap bitmap =
			 * BitmapFactory.decodeFile(takepicfile.getAbsolutePath());
			 * NativeUtil.compressBitmap(bitmap,
			 * 50,takepicfile.getAbsolutePath(), true); bitmap.recycle();
			 * filePath = file.getAbsolutePath();
			 */

			// do send pic ...
			break;
		}

		case REQUEST_CODE_SELECT_FILE: {

			Uri uri = data.getData();// 可以得到图片在Content：//。。。中的地址，把它转化成绝对地址如下
			String path = getRealPathFromURI(uri);
			takepicfile = new File(path);
			new MyTask().execute("1");
			/*
			 * Bitmap bitmap = BitmapFactory.decodeFile(path);
			 * NativeUtil.compressBitmap(bitmap,
			 * 50,takepicfile.getAbsolutePath(), true); bitmap.recycle();
			 * filePath = takepicfile.getAbsolutePath();
			 */
			// System.out.println("sb.");
			// Uri uri = Uri.fromFile(file);
			// addImage(uri, false);

			// do send pic ...
			/*
			 * if(data.hasExtra("file_name")) { Bundle extras =
			 * data.getExtras(); if (extras != null) { fileName =
			 * extras.getString("file_name"); } }
			 * 
			 * if(data.hasExtra("file_url")) { Bundle extras = data.getExtras();
			 * if (extras != null) { filePath = extras.getString("file_url"); }
			 * }
			 */

			break;
		}
		}
		/*
		 * if (TextUtils.isEmpty(filePath)) { // Select the local file does not
		 * exist or has been deleted. Toast.makeText(GroupChatActivity.this,
		 * R.string.toast_file_exist, Toast.LENGTH_SHORT).show(); return; }
		 * 
		 * if (TextUtils.isEmpty(fileName)) { fileName = new
		 * File(filePath).getName(); // fileName =
		 * filePath.substring(filePath.indexOf("/"), // filePath.length()); }
		 * 
		 * IMChatMessageDetail chatMessageDetail = IMChatMessageDetail
		 * .getGroupItemMessage(IMChatMessageDetail.TYPE_MSG_FILE,
		 * IMChatMessageDetail.STATE_IM_SENDING, mGroupId);
		 * chatMessageDetail.setMessageContent(fileName);
		 * chatMessageDetail.setFilePath(filePath); String extensionName =
		 * VoiceUtil.getExtensionName(fileName); if
		 * ("amr".equals(extensionName)) { chatMessageDetail
		 * .setMessageType(IMChatMessageDetail.TYPE_MSG_VOICE); }
		 * chatMessageDetail.setFileExt(extensionName);
		 * 
		 * if (!checkeDeviceHelper()) { return; } try { String uniqueID =
		 * getDeviceHelper().sendInstanceMessage(mGroupId, null, filePath,
		 * USER_DATA); chatMessageDetail.setMessageId(uniqueID);
		 * CCPSqliteManager.getInstance().insertIMMessage(chatMessageDetail);
		 * chatMessageDetail.setUserData( USER_DATA);
		 * notifyGroupDateChange(chatMessageDetail); } catch (SQLException e) {
		 * e.printStackTrace(); }
		 */
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			// Do not call Cursor.close() on a cursor obtained using this
			// method,
			// because the activity will do that for you at the appropriate time
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	private class MyTask extends AsyncTask<String, Integer, String> {

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			myProgressDialog = new MyProgressDialog(GroupChatActivity.this);
			myProgressDialog.setMessage("正在处理图片...");
			myProgressDialog.show();
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			File file = takepicfile;
			if (file == null || !file.exists()) {
				return null;
			}
			//int degree = ImageUtil.readPictureDegree(file.getAbsolutePath());  
		//Bitmap	bitmap = ImageUtil.rotaingImageView(degree, bitmap);
			String filePath = null;
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(
						takepicfile.getAbsolutePath());
				if (fileInputStream.available() / 1024 > 300) {
					String smallFilePath = CCPUtil.TackPicFilePath2()
							.getAbsolutePath();
//					Bitmap bitmap = BitmapFactory.decodeFile(takepicfile
//							.getAbsolutePath());
					Bitmap bitmap = BitmapCompressorUtil.decodeSampledBitmapFromFile(takepicfile.getAbsolutePath(),
							480, 640);
					smallFilePath = BitmapCompressorUtil.compressBitmap2(
							bitmap, 100, smallFilePath);
					// NativeUtil.compressBitmap(bitmap,
					// 50,CCPUtil.TackPicFilePath2().getAbsolutePath(), true);
					// bitmap.recycle();
					filePath = smallFilePath;
					;
				} else {
					filePath = takepicfile.getAbsolutePath();
				}
				fileInputStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (params[0].equals("0")) {

				file.delete();
			}
			return filePath;
		}

		// onProgressUpdate方法用于更新进度信息
		@Override
		protected void onProgressUpdate(Integer... progresses) {

		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			myProgressDialog.dismiss();
			if (TextUtils.isEmpty(result)) {
				// Select the local file does not exist or has been deleted.
				Toast.makeText(GroupChatActivity.this,
						R.string.toast_file_exist, Toast.LENGTH_SHORT).show();
				return;
			}

			/* if (TextUtils.isEmpty(fileName)) { */
			String fileName = new File(result).getName();
			// fileName = filePath.substring(filePath.indexOf("/"),
			// filePath.length());
			// }

			IMChatMessageDetail chatMessageDetail = IMChatMessageDetail
					.getGroupItemMessage(IMChatMessageDetail.TYPE_MSG_FILE,
							IMChatMessageDetail.STATE_IM_SENDING, mGroupId);
			chatMessageDetail.setMessageContent(fileName);
			chatMessageDetail.setFilePath(result);
			String extensionName = VoiceUtil.getExtensionName(fileName);
			if ("amr".equals(extensionName)) {
				chatMessageDetail
						.setMessageType(IMChatMessageDetail.TYPE_MSG_VOICE);
			}
			chatMessageDetail.setFileExt(extensionName);

			if (!checkeDeviceHelper()) {
				return;
			}
			try {
				String uniqueID = getDeviceHelper().sendInstanceMessage(
						mGroupId, null, result, USER_DATA);
				chatMessageDetail.setMessageId(uniqueID);
				CCPSqliteManager.getInstance().insertIMMessage(
						chatMessageDetail);
				chatMessageDetail.setUserData(USER_DATA);
				
				notifyGroupDateChange(chatMessageDetail);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
		}
	}

	private void notifyGroupDateChange(IMChatMessageDetail chatMessageDetail) {
		if (mIMGroupApapter == null) {
			ArrayList<IMChatMessageDetail> iChatMsg = new ArrayList<IMChatMessageDetail>();
			iChatMsg.add(chatMessageDetail);
			mIMGroupApapter = new IMGroupChatItemAdapter(iChatMsg);
			mIMGroupListView.setAdapter(mIMGroupApapter);
		} else {
			mIMGroupApapter.insert(chatMessageDetail,
					mIMGroupApapter.getCount());
		}

		mIMGroupListView.setSelection(mIMGroupListView.getCount());
	}

	private void sendbroadcast() {
		Intent intent = new Intent(CCPIntentUtils.INTENT_IM_RECIVE);
		intent.putExtra(KEY_GROUP_ID, mGroupId);
		intent.putExtra(KEY_IS_LOCAL, true);
		sendBroadcast(intent);
	}

	private void sendbroadcastbyId(String mGroupId) {
		Intent intent = new Intent(CCPIntentUtils.INTENT_IM_RECIVE);
		intent.putExtra(KEY_GROUP_ID, mGroupId);
		sendBroadcast(intent);
	}
	
	private void sendbroadcastbyId(String mGroupId,String messageID) {
		Intent intent = new Intent(CCPIntentUtils.INTENT_IM_RECIVE);
		intent.putExtra(KEY_GROUP_ID, mGroupId);
		intent.putExtra(KEY_MESSAGE_ID, messageID);
		sendBroadcast(intent);
	}

	class IMGroupChatItemAdapter extends ArrayAdapter<IMChatMessageDetail> {

		LayoutInflater mInflater;

		public IMGroupChatItemAdapter(List<IMChatMessageDetail> iChatMsg) {
			super(GroupChatActivity.this, 0, iChatMsg);

			mInflater = getLayoutInflater();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int po = position;
			final GroupMsgHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = mInflater.inflate(
						R.layout.list_item_voice_mseeage, null);
				holder = new GroupMsgHolder();
				convertView.setTag(holder);

				holder.lavatar = (ImageView) convertView
						.findViewById(R.id.voice_chat_avatar_l);
				holder.ravatar = (ImageView) convertView
						.findViewById(R.id.voice_chat_avatar_r);
				holder.gLayoutLeft = (LinearLayout) convertView
						.findViewById(R.id.voice_item_left);
				holder.gLayoutRight = (LinearLayout) convertView
						.findViewById(R.id.voice_item_right);
				// holder.gTimeRight = (TextView) convertView
				// .findViewById(R.id.im_chat_time_right);
				// holder.gTimeLeft = (TextView) convertView
				// .findViewById(R.id.im_chat_time_left);

				holder.gNameleft = (TextView) convertView
						.findViewById(R.id.name_l);
				holder.gNameRight = (TextView) convertView
						.findViewById(R.id.name_r);
				holder.docName = (TextView) convertView
						.findViewById(R.id.ic_doc_name);
				holder.gVoiceChatLyLeft = (LinearLayout) convertView
						.findViewById(R.id.voice_chat_ly_l);
				holder.gIMChatLyLeft = (LinearLayout) convertView
						.findViewById(R.id.im_chat_ly);
				holder.gIMChatLyLeft_videopreview_fl = convertView
						.findViewById(R.id.fl_imageview);
				holder.gIMChatLyLeft_videopreview = (ImageView) convertView
						.findViewById(R.id.imageview);
				holder.gIMChatLyLeft_btn_play = (Button) convertView
						.findViewById(R.id.btn_play);

				holder.gVoiceChatLyRight = (LinearLayout) convertView
						.findViewById(R.id.voice_chat_ly_r);

				holder.gIMChatLyRight = (LinearLayout) convertView
						.findViewById(R.id.im_chat_ly_r);
				holder.gIMChatLyRight_videopreview_fl = convertView
						.findViewById(R.id.fl_imageview_right);
				holder.gIMChatLyRight_videopreview = (ImageView) convertView
						.findViewById(R.id.imageview_right);
				holder.gIMChatLyRight_btn_play = (Button) convertView
						.findViewById(R.id.btn_play_right);

				holder.imFileIconL = (ImageView) convertView
						.findViewById(R.id.im_chatting_file_icon_l);
				holder.imFileIconR = (ImageView) convertView
						.findViewById(R.id.im_chatting_file_icon);

				holder.imFileNameLeft = (CCPTextView) convertView
						.findViewById(R.id.file_name_left);
				holder.imFileNameRight = (CCPTextView) convertView
						.findViewById(R.id.file_name_right);

				holder.imTimeLeft = (TextView) convertView
						.findViewById(R.id.im_chat_time_left);
				holder.imTimeRight = (TextView) convertView
						.findViewById(R.id.im_chat_time_right);

				holder.imVoiceTimeLeft = (TextView) convertView
						.findViewById(R.id.im_voice_time_left);
				holder.imVoiceTimeRight = (TextView) convertView
						.findViewById(R.id.im_voice_time_right);

				holder.rProBar = (ProgressBar) convertView
						.findViewById(R.id.voice_sending_r);

				// voice item time
				holder.lDuration = (TextView) convertView
						.findViewById(R.id.voice_content_len_l);
				holder.rDuration = (TextView) convertView
						.findViewById(R.id.voice_content_len_r);

				// vioce chat animation
				holder.vChatContentFrom = (ImageView) convertView
						.findViewById(R.id.voice_chat_recd_tv_l);
				holder.vChatContentTo = (ImageView) convertView
						.findViewById(R.id.voice_chat_recd_tv_r);

				holder.vErrorIcon = (ImageView) convertView
						.findViewById(R.id.error_Icon);
				holder.vErrorIcon.setOnClickListener(GroupChatActivity.this);
			} else {
				holder = (GroupMsgHolder) convertView.getTag();
			}
			final IMChatMessageDetail item = getItem(position);
			if (userModel.getUserSex().equals("Man")) {
				// 显示默认男头像
				ImageLoader.getInstance().displayImage(
						userModel.getHeadPicture(), holder.ravatar,
						ImageLoaderUtils.getOptions3());
			} else if (userModel.getUserSex().equals("Woman")) {
				// 显示默认女头像
				ImageLoader.getInstance().displayImage(
						userModel.getHeadPicture(), holder.ravatar,
						ImageLoaderUtils.getOptions());
			} else {
				// 显示默认头像
				ImageLoader.getInstance().displayImage(
						userModel.getHeadPicture(), holder.ravatar,
						ImageLoaderUtils.getOptions2());
			}
			// ImageLoader.getInstance().displayImage(userModel.getHeadPicture(),
			// holder.ravatar, ImageLoaderUtils.getOptions2());
			if (item != null) {
				if (item.getMessageId().equals(item.getSessionId())) {
					holder.gLayoutLeft.setVisibility(View.GONE);
					holder.gLayoutRight.setVisibility(View.GONE);
					return convertView;
				}
				if (item.getMessageId().toString().contains("xzs")) {
					/*
					 * holder.gLayoutLeft.setVisibility(View.GONE);
					 * holder.gLayoutRight.setVisibility(View.GONE);
					 */
					// ImageLoader.getInstance().displayImage(docInfoBean.getHeadPicture(),
					// holder.lavatar, ImageLoaderUtils.getOptions2());
					holder.lavatar.setImageResource(R.drawable.icon_bang);
					// return convertView;
				} else {
					if (docInfoBean != null) {
						ImageLoader.getInstance().displayImage(
								docInfoBean.getHeadPicture(), holder.lavatar,
								ImageLoaderUtils.getOptions2());
					}
				}
				/*
				 * View.OnClickListener onClickListener = new OnClickListener()
				 * {
				 * 
				 * @Override public void onClick(View v) { Intent intent = new
				 * Intent(GroupChatActivity.this,
				 * GroupMemberCardActivity.class); intent.putExtra(KEY_GROUP_ID,
				 * mGroupId); intent.putExtra("voipAccount",
				 * item.getGroupSender()); intent.putExtra("modify", false);
				 * startActivity(intent); } };
				 */
				if (item.getImState() == IMChatMessageDetail.STATE_IM_RECEIVEED) {
					holder.gLayoutLeft.setVisibility(View.VISIBLE);
					holder.gLayoutRight.setVisibility(View.GONE);
					String groupSender = item.getGroupSender();
					String groupSender2 = item.getGroupSender();
					if (!TextUtils.isEmpty(groupSender)
							&& groupSender.length() > 4) {
						groupSender = groupSender.substring(
								groupSender.length() - 4, groupSender.length());
					}
					if (chatModel == CHAT_MODE_IM_GROUP) {
						// holder.lavatar.setOnClickListener(onClickListener);
						// requestGetUserface(groupSender2);
						GroupUserInfoBean groupUserInfoBean = new GroupUserInfoBean();
						try {
							groupUserInfoBean = dbUtils.findFirst(Selector
									.from(GroupUserInfoBean.class).where(
											"voipAccount", "=", groupSender2));
							if (null != groupUserInfoBean) {
								// holder.gNameleft.setText(groupUserInfoBean.getUserName());
								if (groupUserInfoBean.isIsDoctor()) {
									holder.docName.setVisibility(View.VISIBLE);
									holder.docName.setText(groupUserInfoBean
											.getUserName() + "医生");
								}
								ImageLoader.getInstance().displayImage(
										groupUserInfoBean.getFace(),
										holder.lavatar,
										ImageLoaderUtils.getOptions2());

							} else {

								HttpClient.put(Constants.GETUSERFACE_URL, "[\""
										+ groupSender2 + "\"]",
										new RequestCallBack<String>() {
											@Override
											public void onSuccess(
													ResponseInfo<String> arg0) {
												// TODO Auto-generated method
												// stub
												LogUtil.i(TAG, arg0.result);
												try {
													JSONObject jsonObject = new JSONObject(
															arg0.result);
													String data = jsonObject
															.getString("Data");
													if (null != data) {

														List<GroupUserInfoBean> l = new Gson()
																.fromJson(
																		data,
																		new TypeToken<List<GroupUserInfoBean>>() {
																		}.getType());

														GroupUserInfoBean userInfoBean = l
																.get(0);
														String headPic = userInfoBean
																.getFace();
														// String userName =
														// userInfoBean.getUserName();
														if (userInfoBean
																.isIsDoctor()) {
															holder.docName
																	.setVisibility(View.VISIBLE);
															holder.docName.setText(userInfoBean
																	.getUserName()
																	+ "医生");
														}
														DBUtilsHelper
																.getInstance()
																.saveGroupUserInfo(
																		userInfoBean);
														// holder.gNameleft.setText(userName);
														ImageLoader
																.getInstance()
																.displayImage(
																		headPic,
																		holder.lavatar,
																		ImageLoaderUtils
																				.getOptions2());
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

											}

											@Override
											public void onFailure(
													HttpException arg0,
													String arg1) {
												// TODO Auto-generated method
												// stub
												LogUtil.i(TAG, arg1.toString());
											}
										});

							}
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// requesrGetUserFace();
						// holder.gNameleft.setText(groupSender);
						holder.gNameleft.setVisibility(View.GONE);
					} else {
						holder.gNameleft.setText("");
					}

					// ysj holder.gNameleft.setText(groupSender);
					// holder.gNameleft.setText("");
					if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_VOICE) { // voice
																						// chat
																						// ...itme

						// If the speech information, you need to display the
						// voice information
						// distribution, and voice information unified time
						// display in the middle
						// position above the voice information
						// And hidden files IM layout
						holder.gVoiceChatLyLeft.setVisibility(View.VISIBLE);
						holder.gIMChatLyLeft.setVisibility(View.GONE);
						holder.imVoiceTimeLeft.setVisibility(View.VISIBLE);
						holder.imVoiceTimeLeft.setText(item.getCurDate());
						int duration = 0;
						if (checkeDeviceHelper()) {
							duration = (int) Math
									.ceil(getDeviceHelper().getVoiceDuration(
											item.getFilePath()) / 1000);
						}
						duration = duration == 0 ? 1 : duration;
						holder.lDuration.setText(duration + "''");

						holder.gVoiceChatLyLeft
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// It shows only in the presence of the
										// voice files
										if (!TextUtils.isEmpty(item
												.getFilePath())
												&& new File(item.getFilePath())
														.exists()) {
											ViewPlayAnim(
													holder.vChatContentFrom,
													item.getFilePath(), po);
											// CCPVoiceMediaPlayManager.getInstance(GroupChatActivity.this).putVoicePlayQueue(position,
											// item.getFilePath());
										} else {
											Toast.makeText(
													getApplicationContext(),
													R.string.media_ejected,
													Toast.LENGTH_LONG).show();
										}
									}

								});

					} else {
						// TEXT FILE
						// If not the voice information, you need to display the
						// IM file layout,
						// and the layout of item audio information hiding, also
						// need the voice
						// information time display view hide this time, only
						// need to display
						// the time view IM style
						holder.gVoiceChatLyLeft.setVisibility(View.GONE);
						holder.gIMChatLyLeft.setVisibility(View.VISIBLE);
						if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_TEXT) {
							holder.imFileNameLeft.setVisibility(View.VISIBLE);
							holder.imFileNameLeft.setEmojiText(item
									.getMessageContent());
							holder.imFileIconL.setVisibility(View.GONE);

						} else if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_FILE) {
							holder.imFileIconL.setVisibility(View.VISIBLE);
							// final IMChatMessageDetail item2= item;
							// item.getFilePath();
							ImageLoader
									.getInstance()
									.displayImage(
											item.getFileUrl(),
											holder.imFileIconL,
											ImageLoaderUtils
													.getOptions(R.drawable.icon_phtot_default));
							OnClickListener onClickListener2 = new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											GroupChatActivity.this,
											ChatPhotoViewerActivity.class);
									intent.putExtra("fileUrl", getItem(po)
											.getFileUrl());
									startActivity(intent);
									// snedFilePrevieIntent(ImageLoader.getInstance().getDiscCache().get(getItem(po).getFileUrl()).getPath());
								}
							};

							holder.gIMChatLyLeft
									.setOnClickListener(onClickListener2);
							holder.gIMChatLyLeft_videopreview
									.setOnClickListener(onClickListener2);
							holder.gIMChatLyLeft_btn_play
									.setOnClickListener(onClickListener2);

							// file name
							holder.imFileNameLeft.setVisibility(View.GONE);

						}

						holder.imTimeLeft.setText(item.getCurDate());

						// 是视频的话 加 预览图片

					}
				} else {
					if (chatModel == CHAT_MODE_IM_GROUP) {
						// holder.ravatar.setOnClickListener(onClickListener);
					}
					holder.gLayoutLeft.setVisibility(View.GONE);
					holder.gLayoutRight.setVisibility(View.VISIBLE);
					// ysj
					// holder.gNameRight.setText(CCPConfig.VoIP_ID.substring(CCPConfig.VoIP_ID.length()
					// - 4, CCPConfig.VoIP_ID.length()));
					holder.gNameRight.setText("");
					if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_VOICE) {

						// voice chat ...itme
						holder.gVoiceChatLyRight.setVisibility(View.VISIBLE);
						holder.gIMChatLyRight.setVisibility(View.GONE);
						holder.imVoiceTimeRight.setVisibility(View.VISIBLE);
						holder.imVoiceTimeRight.setText(item.getCurDate());

						int duration = 0;
						if (checkeDeviceHelper()) {
							duration = (int) Math
									.ceil(getDeviceHelper().getVoiceDuration(
											item.getFilePath()) / 1000);

						}
						duration = duration == 0 ? 1 : duration;
						holder.rDuration.setText(duration + "''");
						holder.gVoiceChatLyRight
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// It shows only in the presence of the
										// voice files
										if (!TextUtils.isEmpty(item
												.getFilePath())
												&& new File(item.getFilePath())
														.exists()) {
											ViewPlayAnim(holder.vChatContentTo,
													item.getFilePath(), po);
											// CCPVoiceMediaPlayManager.getInstance(GroupChatActivity.this).putVoicePlayQueue(position,
											// item.getFilePath());
										} else {
											Toast.makeText(
													getApplicationContext(),
													R.string.media_ejected,
													Toast.LENGTH_LONG).show();
										}

									}
								});

					} else {
						// TEXT FILE
						holder.gVoiceChatLyRight.setVisibility(View.GONE);
						holder.gIMChatLyRight.setVisibility(View.VISIBLE);
						holder.imFileNameRight.setVisibility(View.VISIBLE);
						if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_TEXT) {
							holder.imFileNameRight.setEmojiText(item
									.getMessageContent());
							holder.imFileIconR.setVisibility(View.GONE);

							// If it is sent the text is not realistic loading
							// ...
							holder.rProBar.setVisibility(View.GONE);

						} else if (item.getMessageType() == IMChatMessageDetail.TYPE_MSG_FILE) {
							holder.imFileIconR.setVisibility(View.VISIBLE);
							// setImageView(holder.imFileIconR,
							// item.getFilePath());
							if (null != item.getFileUrl()) {
								ImageLoader
										.getInstance()
										.displayImage(
												item.getFileUrl(),
												holder.imFileIconR,
												ImageLoaderUtils
														.getOptions(R.drawable.icon_phtot_default));
							} else {

								ImageLoader
										.getInstance()
										.displayImage(
												"file://" + item.getFilePath(),
												holder.imFileIconR,
												ImageLoaderUtils
														.getOptions(R.drawable.icon_phtot_default));
							}
							// file name
							holder.imFileNameRight.setVisibility(View.GONE);
							holder.imFileNameRight.setEmojiText(item
									.getMessageContent());
							OnClickListener onClickListener2 = new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									IMChatMessageDetail imChatMessageDetail = getItem(po);
									if (null != item.getFileUrl()) {
										Intent intent = new Intent(
												GroupChatActivity.this,
												ChatPhotoViewerActivity.class);
										intent.putExtra("fileUrl", getItem(po)
												.getFileUrl());
										startActivity(intent);
									} else {

										Intent intent = new Intent(
												GroupChatActivity.this,
												ChatPhotoViewerActivity.class);
										intent.putExtra(
												"fileUrl",
												"file://"
														+ imChatMessageDetail
																.getFilePath());
										startActivity(intent);
										// System.out.println("......"+imChatMessageDetail.toString());
										// snedFilePrevieIntent(imChatMessageDetail.getFilePath());
									}
								}
							};
							holder.gIMChatLyRight
									.setOnClickListener(onClickListener2);
							holder.gIMChatLyRight_btn_play
									.setOnClickListener(onClickListener2);
							holder.gIMChatLyRight_videopreview
									.setOnClickListener(onClickListener2);

						}

						holder.imTimeRight.setText(item.getCurDate());
					}

					// is sending ?
					if (item.getImState() == IMChatMessageDetail.STATE_IM_SENDING) {
						holder.rProBar.setVisibility(View.VISIBLE);
						holder.vErrorIcon.setVisibility(View.GONE);
					} else if (item.getImState() == IMChatMessageDetail.STATE_IM_SEND_SUCCESS) {
						holder.rProBar.setVisibility(View.GONE);
						holder.vErrorIcon.setVisibility(View.GONE);
					} else if (item.getImState() == IMChatMessageDetail.STATE_IM_SEND_FAILED) {
						holder.vErrorIcon.setVisibility(View.VISIBLE);
						holder.rProBar.setVisibility(View.GONE);

						// version 3.5
						holder.vErrorIcon.setTag(position);
					}

					// 是视频的话 加 预览图片
					if ("mp4".equals(item.getFileExt())) {
						Bitmap createVideoThumbnail = FileUtils
								.createVideoThumbnail(item.getFilePath());
						if (createVideoThumbnail != null) {
							holder.gIMChatLyRight_videopreview_fl
									.setVisibility(View.VISIBLE);
							holder.gIMChatLyRight_videopreview
									.setImageBitmap(createVideoThumbnail);
						}
					} else {
						holder.gIMChatLyRight_videopreview_fl
								.setVisibility(View.GONE);
					}
				}
			}
			return convertView;
		}

		private void timingPause() {
			// TODO Auto-generated method stub
			if (!TimerService.isPause && TimerService.isStart) {
				Bundle bundle = new Bundle();
				bundle.putInt("op", 2);
				serciceIntent.putExtras(bundle);
				MyApp.getInstance().startService(serciceIntent);
				TimerService.isPause = !TimerService.isPause;
			}
		}

		class GroupMsgHolder {
			ImageView lavatar;
			ImageView ravatar;
			// root layout
			LinearLayout gLayoutLeft;
			LinearLayout gLayoutRight;

			// TextView gTimeLeft;
			// TextView gTimeRight;

			TextView gNameleft;
			TextView gNameRight;
			TextView docName;

			LinearLayout gVoiceChatLyLeft;
			LinearLayout gIMChatLyLeft;
			View gIMChatLyLeft_videopreview_fl;
			ImageView gIMChatLyLeft_videopreview;
			Button gIMChatLyLeft_btn_play;

			LinearLayout gVoiceChatLyRight;
			LinearLayout gIMChatLyRight;
			View gIMChatLyRight_videopreview_fl;
			ImageView gIMChatLyRight_videopreview;
			Button gIMChatLyRight_btn_play;

			ImageView imFileIconL; // IM FILE
			ImageView imFileIconR;
			CCPTextView imFileNameLeft;
			CCPTextView imFileNameRight;
			TextView imTimeLeft;
			TextView imTimeRight;

			TextView imVoiceTimeLeft;
			TextView imVoiceTimeRight;

			ProgressBar rProBar;

			// voice time
			TextView lDuration;
			TextView rDuration;

			ImageView vChatContentFrom;
			ImageView vChatContentTo;

			ImageView vErrorIcon;
		}

		private void setImageView(ImageView imageView, String realPath) {
			Bitmap bmp = BitmapFactory.decodeFile(realPath);
			int degree = readPictureDegree(realPath);
			if (degree <= 0) {
				imageView.setImageBitmap(bmp);
			} else {
				// Log.e(tag, "rotate:"+degree);
				// 创建操作图片是用的matrix对象
				Matrix matrix = new Matrix();
				// 旋转图片动作
				matrix.postRotate(degree);
				// 创建新图片
				Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
						bmp.getWidth(), bmp.getHeight(), matrix, true);
				imageView.setImageBitmap(resizedBitmap);
			}
		}

		private int readPictureDegree(String path) {
			int degree = 0;
			try {
				ExifInterface exifInterface = new ExifInterface(path);
				int orientation = exifInterface.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return degree;
		}

	}

	class IMListyncTask extends
			AsyncTask<String, Void, ArrayList<IMChatMessageDetail>> {

		boolean isReceiveNewMessage = false;

		@Override
		protected ArrayList<IMChatMessageDetail> doInBackground(
				String... params) {
			if (params != null && params.length > 0) {
				ArrayList<IMChatMessageDetail> arrayList = new ArrayList<IMChatMessageDetail>();
				try {
					if (params.length > 1) {
						// new Message .
						ArrayList<IMChatMessageDetail> newImMessages = CCPSqliteManager
								.getInstance().queryNewIMMessagesBySessionId(
										params[0]);
						CCPSqliteManager.getInstance()
								.updateIMMessageUnreadStatusToRead(
										newImMessages,
										IMChatMessageDetail.STATE_READED);
						isReceiveNewMessage = true;
						return newImMessages;
					}
					updateReadStatus();
					// x
					// CCPSqliteManager.getInstance().deleteIMMessageByMessageId("ysj");
					// arrayList.addAll(CCPSqliteManager.getInstance().queryIMMessagesBySessionId(subjectID));
					// arrayList.addAll(CCPSqliteManager.getInstance().queryIMMessagesBySessionId(params[0]));
					return CCPSqliteManager.getInstance()
							.queryIMMessagesBySessionId(params[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<IMChatMessageDetail> result) {
			super.onPostExecute(result);

			if (result != null && !result.isEmpty()) {

				/*
				 * ArrayList<IMChatMessageDetail> result2 = new
				 * ArrayList<IMChatMessageDetail>(); for (int i = 0; i <
				 * result.size(); i++) { IMChatMessageDetail imChatMessageDetail
				 * = result.get(i); if
				 * (imChatMessageDetail.getMessageType()==IMChatMessageDetail
				 * .TYPE_MSG_TEXT
				 * &&null!=imChatMessageDetail.getMessageContent()&&
				 * !imChatMessageDetail.getMessageContent().equals("")) {
				 * result2.add(imChatMessageDetail); } }
				 */

				// add for versoin 3.5 when receive new message
				if (isReceiveNewMessage && mIMGroupApapter != null) {
					for (IMChatMessageDetail imCMessageDetail : result) {
						mIMGroupApapter.insert(imCMessageDetail,
								mIMGroupApapter.getCount());
					}
					return;
				}

				mIMGroupApapter = new IMGroupChatItemAdapter(result);
				mIMGroupListView.setAdapter(mIMGroupApapter);
			} else {
				if (result == null)

					mIMGroupListView.setAdapter(null);
			}
		}
	}

	private XQuickActionBar.OnPopClickListener popListener = new XQuickActionBar.OnPopClickListener() {

		@Override
		public void onPopClick(int index) {
			switch (index) {
			case R.string.str_title_right_group_info:

				if (!TextUtils.isEmpty(mGroupId)) {

					Intent intent = new Intent(GroupChatActivity.this,
							GroupDetailActivity.class);
					intent.putExtra(KEY_GROUP_ID, mGroupId);
					intent.putExtra("isJoin", true);
					startActivity(intent);

				} else {

					// failed ..
					Toast.makeText(getApplicationContext(),
							R.string.toast_click_into_group_error,
							Toast.LENGTH_SHORT).show();
				}

				break;
			case R.string.pull_mode_earpiece:
			case R.string.pull_mode_speaker:
				try {

					CcpPreferences.savePreference(
							CCPPreferenceSettings.SETTING_HANDSET, !isEarpiece,
							true);
					isEarpiece = CcpPreferences
							.getSharedPreferences()
							.getBoolean(
									CCPPreferenceSettings.SETTING_HANDSET
											.getId(),
									((Boolean) CCPPreferenceSettings.SETTING_HANDSET
											.getDefaultValue()).booleanValue());
					int text = isEarpiece ? R.string.fmt_route_phone
							: R.string.fmt_route_speaker;
					addNotificatoinToView(getString(text), Gravity.TOP);
				} catch (InvalidClassException e) {
					e.printStackTrace();
				}

				break;

			default:

				break;
			}
			xQuickActionBar.dismissBar();
		}
	};

	/**
	 * @param fileName
	 */
	void snedFilePrevieIntent(String fileName) {
		String type = "";
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			type = MimeTypesTools
					.getMimeType(getApplicationContext(), fileName);
			File file = new File(fileName);
			intent.setDataAndType(Uri.fromFile(file), type);
			startActivity(intent);
		} catch (Exception e) {
			System.out
					.println("android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.VIEW dat=file:///mnt/sdcard/xxx typ="
							+ type + " flg=0x10000000");
		}
	}

	@Override
	protected void onReceiveBroadcast(Intent intent) {
		super.onReceiveBroadcast(intent);
		if (intent == null) {
			return;
		}
		
		if (CCPIntentUtils.INTENT_IM_RECIVE.equals(intent.getAction())
				|| CCPIntentUtils.INTENT_DELETE_GROUP_MESSAGE.equals(intent
						.getAction())) {
			// update UI...
			// new IMListyncTask().execute();
			if (intent.hasExtra(KEY_GROUP_ID)) {
				if(intent.hasExtra(KEY_TRANSFORM)){
					TimerService.count = 0;
					mProgressBar.setProgress(0);
				}
				if (intent.getStringExtra(KEY_GROUP_ID).equals(
						Constants.CLOSESUBJECT)) {
					showTimeOut();
					gc_title2.setVisibility(View.GONE);
					SharedPrefsUtil.putValue("RM_"+chatInfoBean2.getSubjectID(), true);
					chatInfoBean2.setTimeout(false);
					chatInfoBean2.setStatus(false);
					DBUtilsHelper.getInstance().saveChatinfo(chatInfoBean2);
					exitComment();
					/*
					 * try { chatInfoBean = dbUtils.findFirst(Selector.from(
					 * ChatInfoBean.class).where("docInfoBeanId", "=",
					 * mGroupId)); } catch (DbException e1) { // TODO
					 * Auto-generated catch block e1.printStackTrace(); }
					 */
					/*
					 * chatInfoBean.setStatus(false); try {
					 * dbUtils.saveOrUpdate(chatInfoBean); chatInfoBean =
					 * dbUtils .findFirst(Selector.from(ChatInfoBean.class)
					 * .where("status", "=", false)); //
					 * chatInfoBean.isStatus(); } catch (DbException e) { //
					 * TODO Auto-generated catch block e.printStackTrace(); }
					 */
					return;
				}
				String sender = intent.getStringExtra(KEY_GROUP_ID);
				// receive new message ,then load this message set adapter of
				// listView.
				String newMessageId = null;
				if (intent.hasExtra(KEY_MESSAGE_ID)) {
					newMessageId = intent.getStringExtra(KEY_MESSAGE_ID);
				}
				if(sender.equals(mGroupId) && !intent.hasExtra(KEY_MESSAGE_ID)&&chatModel!=CHAT_MODE_IM_GROUP && !intent.hasExtra(KEY_IS_LOCAL)){
					gc_title2.setVisibility(View.GONE);
					SharedPrefsUtil.putValue("RM_"+chatInfoBean2.getSubjectID(), true);
				}

				if (!TextUtils.isEmpty(sender) && !sender.equals(mGroupId)) {
					try {

						//获取上个咨询的医生
						String doctorOld = "";
						if(docInfoBean != null){
							doctorOld = docInfoBean.getUserName();
						}
						
//						//转诊用户无需评价
//						chatInfoBean.setComment(true);
//						chatInfoBean.setStatus(false);
//						dbUtils.saveOrUpdate(chatInfoBean);
						
						docInfoBean = dbUtils.findFirst(Selector.from(
								DocInfoBean.class).where("voipAccount", "=",
								sender));
						// System.out.println("d........"+docInfoBean==null);
						if (null == docInfoBean) {
							// requestDocInfo(docVoip,subjectId);
						} else {
							mGroupId = sender;
							title.setText(docInfoBean.getUserName());
							new IMListyncTask().execute(mGroupId);
							
							//onSendTextMesage("您已成功转诊至" + docInfoBean.getUserName() + "医生",UserDataUtils.getUserDataForTrans(subjectID,doctorOld));
//							if(chatInfoBean2.getSubjectType().equals("3")){
//								String content = SharedPrefsUtil.getValue("msg_content", "");
//								onSendTextMesage("主诉:"+content,UserDataUtils.getUserDataForTrans(subjectID,doctorOld));
//							}else{
//								onSendTextMesage("您好医生，请帮忙解读一下我的体检报告。",UserDataUtils.getUserDataForTrans(subjectID,doctorOld));
//							}
						}
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}

				if (!TextUtils.isEmpty(sender) && sender.equals(mGroupId)) {
					if (TextUtils.isEmpty(newMessageId)) {
						new IMListyncTask().execute(mGroupId);
					} else {
						new IMListyncTask().execute(mGroupId, newMessageId);
					}
				}
			}

		} else if (CCPIntentUtils.INTENT_REMOVE_FROM_GROUP.equals(intent
				.getAction())) {
			// remove from group ...
			this.finish();
		}
	}

	private android.os.Handler mIMChatHandler = new android.os.Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = null;
			int reason = -1;
			if (msg.obj instanceof Bundle) {
				b = (Bundle) msg.obj;
			}

			switch (msg.what) {
			case CCPHelper.WHAT_ON_SEND_MEDIAMSG_RES:
				if (b == null) {
					return;
				}
				// receive a new IM message
				// then shown in the list.
				try {
					reason = b.getInt(Device.REASON);
					InstanceMsg instancemsg = (InstanceMsg) b
							.getSerializable(Device.MEDIA_MESSAGE);
					if (instancemsg == null) {
						return;
					}

					// IMChatMessageDetail chatMessageDetail = null;
					int sendType = IMChatMessageDetail.STATE_IM_SEND_FAILED;
					String messageId = null;
					if (instancemsg instanceof IMAttachedMsg) {
						IMAttachedMsg rMediaInfo = (IMAttachedMsg) instancemsg;
						messageId = rMediaInfo.getMsgId();
						if (reason == 0) {

							sendType = IMChatMessageDetail.STATE_IM_SEND_SUCCESS;

							try {
								CCPUtil.playNotifycationMusic(
										getApplicationContext(),
										"voice_message_sent.mp3");
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							if (reason == 230007 && mChatFooter != null
									&& mChatFooter.isVoiceRecordCancle()) {
								mChatFooter.setCancle(false);
								// Here need to determine whether is it right?
								// You cancel this recording,
								// and callback upload failed in real-time
								// recording uploaded case,
								// so we need to do that here, when cancel the
								// upload is not prompt the user interface
								// 230007 is the server did not receive a normal
								// AMR recording end for chunked...
								return;
							}

							if (GroupChatActivity.voiceMessage
									.containsKey(rMediaInfo.getMsgId())) {
								Log4Util.e(CCPHelper.DEMO_TAG,
										"isRecordAndSend");
								isRecordAndSend = false;
								return;
							}

							// This is a representative chunked patterns are
							// transmitted speech file
							// If the execution returns to the false, is not
							// chunked or send files
							// VoiceSQLManager.getInstance().updateIMChatMessage(rMediaInfo.getMsgId(),
							// IMChatMsgDetail.TYPE_MSG_SEND_FAILED);
							sendType = IMChatMessageDetail.STATE_IM_SEND_FAILED;

							// failed
							// If the recording mode of data collection is the
							// recording side upload (chunked),
							// then in the recording process may be done to
							// interrupt transfer for various reasons,
							// so, This failed reason can callback method ,But
							// can't immediately begin to upload voice file ,
							// because there may not completed recording ,
							// You can set a Identification here on behalf of
							// the recording process, transmission failure,
							// wait for real recording completed then sending
							// voice recording file

							// If it is after recording then uploading
							// files,When the transmission
							// failure can be Send out in second times in this
							// callback methods
							Toast.makeText(getApplicationContext(),
									R.string.toast_voice_send_failed,
									Toast.LENGTH_SHORT).show();
							if (mIMGroupApapter != null) {
								// mIMGroupApapter.remove(msgDetail);
								// mIMGroupApapter = null ;
							}
						}

					} else if (instancemsg instanceof IMTextMsg) {
						IMTextMsg imTextMsg = (IMTextMsg) instancemsg;
						messageId = imTextMsg.getMsgId();
						if (reason == 0) {
							sendType = IMChatMessageDetail.STATE_IM_SEND_SUCCESS;
						} else {
							// do send text message failed ..
							sendType = IMChatMessageDetail.STATE_IM_SEND_FAILED;
						}
					}
					CCPSqliteManager.getInstance()
							.updateIMMessageSendStatusByMessageId(messageId,
									sendType);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sendbroadcast();
				break;
			case CCPHelper.WHAT_ON_AMPLITUDE:

				double amplitude = b.getDouble(Device.VOICE_AMPLITUDE);

				if (mChatFooter != null) {
					mChatFooter.displayAmplitude(amplitude);
				}

				break;

			case CCPHelper.WHAT_ON_RECODE_TIMEOUT:

				doProcesOperationRecordOver(false);
				break;

			case CCPHelper.WHAT_ON_PLAY_VOICE_FINSHING:
				mVoicePlayState = TYPE_VOICE_STOP;
				releaseVoiceAnim(-1);
				// VoiceApplication.getInstance().setSpeakerEnable(false);
				CCPAudioManager.getInstance().resetSpeakerState(
						GroupChatActivity.this);
				break;
			case WHAT_ON_COMPUTATION_TIME:
				if (promptRecordTime() && getRecordState() == RECORD_ING) {
					sendEmptyMessageDelayed(WHAT_ON_COMPUTATION_TIME,
							TONE_LENGTH_MS);
				}

				break;

			// This call may be redundant, but to ensure compatibility system
			// event more,
			// not only is the system call
			case CCPHelper.WHAT_ON_RECEIVE_SYSTEM_EVENTS:

				onPause();
				break;
			default:
				break;
			}
		}

	};

	// voice local save path ..
	private File getCurrentVoicePath() {
		File directory = new File(MyApp.getInstance().getVoiceStore(),
				currentRecName);
		return directory;
	}

	/**
	 * 
	 * <p>
	 * Title: doProcesOperationRecordOver
	 * </p>
	 * <p>
	 * Description: update version 3.5 .
	 * </p>
	 * 
	 * @param isCancleSend
	 */
	private void doProcesOperationRecordOver(boolean isCancleSend) {
		if (getRecordState() == RECORD_ING) {
			// version 3.5
			// Here sometimes bug. for MODE chunked..
			// Because the record data of CCP SDK Record in the underlying
			// collection according to the AMR code,
			// if the first packet of data up to 650 bytes, so will open a
			// thread transmission of the data,
			// if you have not at the 650 bytes, so will not open the thread for
			// the transmission of
			// data and does not generate local audio files, So here if you are
			// choosing a chunked transmission.
			// You or there is a better way to determine whether to send, at
			// least judging there will be
			// problems at a time.

			// e.g (When you use Chunke to transmission, so you can according to
			// local file exists to decision
			// whether the voice is too short) .In other . you can according to
			// the speech voice duration to
			// decision whether the voice is too short

			boolean isVoiceToShort = false;

			if (getCurrentVoicePath() != null
					&& new File(getCurrentVoicePath().getAbsolutePath())
							.exists() && checkeDeviceHelper()) {
				recodeTime = getDeviceHelper().getVoiceDuration(
						getCurrentVoicePath().getAbsolutePath());

				// if not chunked ,then the voice file duration is greater than
				// 1000ms.
				// If it is chunked. the voice file exists that has been send
				// out
				if (!isRecordAndSend) {
					if (recodeTime < MIX_TIME) {
						isVoiceToShort = true;
					}
				}

			} else {

				isVoiceToShort = true;
			}

			setRecordState(RECORD_NO);

			if (mChatFooter != null) {

				if (isVoiceToShort && !isCancleSend) {
					mChatFooter.tooShortPopuWindow();
					return;
				}

				mChatFooter.removePopuWindow();
			}

			if (!isCancleSend) {

				IMChatMessageDetail mVoicechatMessageDetail = IMChatMessageDetail
						.getGroupItemMessage(
								IMChatMessageDetail.TYPE_MSG_VOICE,
								IMChatMessageDetail.STATE_IM_SENDING, mGroupId);

				mVoicechatMessageDetail.setFilePath(getCurrentVoicePath()
						.getAbsolutePath());

				if (!isRecordAndSend && checkeDeviceHelper()) {
					// send
					uniqueId = getDeviceHelper().sendInstanceMessage(mGroupId,
							null, getCurrentVoicePath().getAbsolutePath(),
							USER_DATA);

				} else {
					voiceMessage.remove(uniqueId);
				}

				try {
					mVoicechatMessageDetail.setMessageId(uniqueId);
					mVoicechatMessageDetail.setUserData(USER_DATA);

					mVoicechatMessageDetail.setFileExt("amr");
					CCPSqliteManager.getInstance().insertIMMessage(
							mVoicechatMessageDetail);
					if(!mGroupId.startsWith("g")){
						timingPause();
					}
					notifyGroupDateChange(mVoicechatMessageDetail);

				} catch (SQLException e) {
					e.printStackTrace();

				}

			}

		}
		recodeTime = 0;
	}

	AnimationDrawable mVoiceAnimation = null;
	ImageView mVoiceAnimImage;

	private static final int TYPE_VOICE_PLAYING = 3;
	private static final int TYPE_VOICE_STOP = 4;
	private int mVoicePlayState = TYPE_VOICE_STOP;;
	private int mPlayPosition = -1;

	void ViewPlayAnim(final ImageView iView, String path, int position) {
		int releasePosition = releaseVoiceAnim(position);

		if (releasePosition == position) {
			return;
		}
		mPlayPosition = position;
		try {
			// local downloaded file
			if (!TextUtils.isEmpty(path) && isLocalAmr(path)) {

				if (mVoicePlayState == TYPE_VOICE_STOP) {
					if (!checkeDeviceHelper()) {
						return;
					}
					if (iView.getId() == R.id.voice_chat_recd_tv_r) {
						mVoiceAnimation = (AnimationDrawable) getResources()
								.getDrawable(R.anim.voice_play_to);
					} else if (iView.getId() == R.id.voice_chat_recd_tv_l) {
						mVoiceAnimation = (AnimationDrawable) getResources()
								.getDrawable(R.anim.voice_play_from);
					}

					iView.setImageDrawable(mVoiceAnimation);
					mVoiceAnimImage = iView;
					mVoiceAnimation.start();
					// ---
					CCPAudioManager.getInstance().switchSpeakerEarpiece(
							GroupChatActivity.this, isEarpiece);

					// Here we suggest to try not to use SDK voice play
					// interface
					// and you can achieve Voice file playback interface
					// for example
					// CCPVoiceMediaPlayManager.getInstance(GroupChatActivity.this).putVoicePlayQueue(position,
					// path);

					// Interface of new speakerOn parameters,(Earpiece or
					// Speaker)
					getDeviceHelper().playVoiceMsg(path, !isEarpiece);

					// 3.4.1.2
					updateVoicePlayModelView(isEarpiece);

					mVoicePlayState = TYPE_VOICE_PLAYING;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <p>
	 * Title: updateVoicePlayModelView
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param isEarpiece
	 * @version 3.4.1.2
	 */
	private void updateVoicePlayModelView(boolean isEarpiece) {
		String speakerEarpiece = null;
		if (isEarpiece) {
			speakerEarpiece = getString(R.string.voice_listen_earpiece);
		} else {
			speakerEarpiece = getString(R.string.voice_listen_speaker);
		}

		addNotificatoinToView(speakerEarpiece, Gravity.TOP);
	}

	/**
	 * @version 3.5
	 *          <p>
	 *          Title: releaseAnim
	 *          </p>
	 *          <p>
	 *          Description: The release is in a voice playback process
	 *          resources picture, animation resources, and stop playing the
	 *          voice {@link GroupChatActivity#TYPE_VOICE_PLAYING}
	 *          {@link GroupChatActivity#TYPE_VOICE_STOP}
	 *          </p>
	 * 
	 * @see Device#stopVoiceMsg()
	 * @see CCPAudioManager#resetSpeakerState(Context);
	 */
	private int releaseVoiceAnim(int position) {

		if (mVoiceAnimation != null) {
			mVoiceAnimation.stop();
			int id = 0;
			if (mVoiceAnimImage.getId() == R.id.voice_chat_recd_tv_l) {
				id = R.drawable.voice_from_playing;
			} else if (mVoiceAnimImage.getId() == R.id.voice_chat_recd_tv_r) {
				id = R.drawable.voice_to_playing;
			}
			// mVoiceAnimImage.setImageResource(0);
			mVoiceAnimImage.setImageResource(id);

			mVoiceAnimation = null;
			mVoiceAnimImage = null;

		}

		// if position is -1 ,then release Animatoin and can't start new Play.
		if (position == -1) {
			mPlayPosition = position;
		}

		// if amr voice file is playing ,then stop play
		if (mVoicePlayState == TYPE_VOICE_PLAYING) {
			if (!checkeDeviceHelper()) {
				return -1;
			}
			getDeviceHelper().stopVoiceMsg();
			// reset speaker
			CCPAudioManager.getInstance().resetSpeakerState(
					GroupChatActivity.this);
			mVoicePlayState = TYPE_VOICE_STOP;

			return mPlayPosition;
		}

		return -1;
	}

	boolean isLocalAmr(String url) {
		if (new File(url).exists()) {
			return true;
		}
		Toast.makeText(this, R.string.toast_local_voice_file_does_not_exist,
				Toast.LENGTH_SHORT).show();
		return false;
	}

	private File takepicfile;

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takepicfile = CCPUtil.TackPicFilePath();
		if (takepicfile != null) {
			Uri uri = Uri.fromFile(takepicfile);
			if (uri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
		}
		startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

	}

	private boolean promptRecordTime() {
		if (computationTime == -1L) {
			computationTime = SystemClock.elapsedRealtime();
		}
		long period = SystemClock.elapsedRealtime() - computationTime;
		int duration;
		if (period >= 50000L && period <= 60000L) {
			if (mRecordTipsToast == null) {
				vibrate(50L);
				duration = (int) ((60000L - period) / 1000L);
				Log4Util.i(CCPHelper.DEMO_TAG, "The remaining recording time :"
						+ duration);
				mRecordTipsToast = Toast.makeText(getApplicationContext(),
						getString(R.string.chatting_rcd_time_limit, duration),
						Toast.LENGTH_SHORT);
			}
		} else {
			if (period < 60000L) {
				// sendEmptyMessageDelayed(WHAT_ON_COMPUTATION_TIME,
				// TONE_LENGTH_MS);
				return true;
			}

			return false;

		}

		if (mRecordTipsToast != null) {
			duration = (int) ((60000L - period) / 1000L);
			Log4Util.i(CCPHelper.DEMO_TAG, "The remaining recording time :"
					+ duration);
			mRecordTipsToast.setText(getString(
					R.string.chatting_rcd_time_limit, duration));
			mRecordTipsToast.show();
		}
		return true;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.layout_group_chat_activity;
	}

	public int getRecordState() {
		return mRecordState;
	}

	public void setRecordState(int state) {
		this.mRecordState = state;
	}

	// 3.4.1.1
	int getFirstMsgListItemBodyTop() {

		return -1;
	}

	AlertDialog alertDialog;

	private void showTimeOut() {
		// TODO Auto-generated method stub
		if (alertDialog != null && alertDialog.isShowing()) {
			return;
		}
		more.setVisibility(View.GONE);
		alertDialog = new AlertDialog.Builder(GroupChatActivity.this).create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.chat_timeout);
		ImageView close = (ImageView) window.findViewById(R.id.cto_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
				Intent i = new Intent();
				i.putExtra("subjectId", subjectID);
				//i.putExtra("voipAccount", chatInfoBean.getDocInfoBeanId());
				i.setClass(GroupChatActivity.this, ChatCommentActivity.class);
				startActivity(i);
			}
		});
		Button cancel = (Button) window.findViewById(R.id.cto_cancle);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
				Intent i = new Intent();
				i.putExtra("subjectId", subjectID);
				//i.putExtra("voipAccount", chatInfoBean.getDocInfoBeanId());
				i.setClass(GroupChatActivity.this, ChatCommentActivity.class);
				startActivity(i);

			}
		});
		Button sure = (Button) window.findViewById(R.id.cto_sure);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestChatAgain();
				alertDialog.dismiss();
			}
		});

		// TextView tv_title = (TextView)
		// window.findViewById(R.id.tv_dialog_title);
		// tv_title.setText("详细信息");
		// TextView tv_message = (TextView)
		// window.findViewById(R.id.tv_dialog_message);
		// tv_message.setText(info);

	}

	private void requestGetUserface(String s) {
		// TODO Auto-generated method stub

		HttpClient.put(Constants.GETUSERFACE_URL, "[\"" + s + "\"]",
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, arg0.result);
						try {
							JSONObject jsonObject = new JSONObject(arg0.result);
							String data = jsonObject.getString("Data");
							List<GroupUserInfoBean> l = new Gson().fromJson(
									data,
									new TypeToken<List<GroupUserInfoBean>>() {
									}.getType());
							for (int i = 0; i < l.size(); i++) {
								GroupUserInfoBean userInfoBean = l.get(i);
								String headPic = userInfoBean.getFace();
								String userName = userInfoBean.getUserName();
								DBUtilsHelper.getInstance().saveGroupUserInfo(
										userInfoBean);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, arg1.toString());
					}
				});
	}

	MyProgressDialog myProgressDialog2;

	private void requestChatAgain() {
		// TODO Auto-generated method stub
		RequestParams requestParams = new RequestParams();
		requestParams.addQueryStringParameter("subjectID", subjectID);
		requestParams.addQueryStringParameter("voipAccount",
				docInfoBean.getVoipAccount());
		HttpClient.delete(GroupChatActivity.this, Constants.CHATAGAIN_URL,
				requestParams, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						myProgressDialog2 = new MyProgressDialog(
								GroupChatActivity.this);
						myProgressDialog2.setMessage("正在请求...");
						myProgressDialog2.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, arg0.result);
						if (chatInfoBean2 != null) {
							String subjecType = chatInfoBean2.getSubjectType();
							if (subjecType.equals("3")) {
								// 在线问诊-再次咨询
								MobclickAgent.onEvent(GroupChatActivity.this,
										Constants.UMENG_EVENT_14);
							} else {
								// 报告解读-再次咨询
								MobclickAgent.onEvent(GroupChatActivity.this,
										Constants.UMENG_EVENT_10);
							}
						}

						myProgressDialog2.dismiss();
						analyzeJson1(arg0.result);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						myProgressDialog2.dismiss();
					}
				});

	}

	private void analyzeJson1(String json) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject data = jsonObject.getJSONObject("Data");
			subjectID = data.getString("SubjectID");
			if (!subjectID.equals("-1")) {
				String docInfoBeanStr = data.getString("responser");
				if (null == docInfoBeanStr && docInfoBeanStr.equals("")
						&& docInfoBeanStr.equals("null")) {
					ToastUtil.showMessage("当前没有医生在线...");
					return;
				}
				
				docInfoBean = new Gson().fromJson(docInfoBeanStr,
						DocInfoBean.class);
				//ChatInfoBean chatInfoBean;
				try {
					chatInfoBean = dbUtils.findFirst(Selector.from(
							ChatInfoBean.class).where("docInfoBeanId", "=",
							docInfoBean.getVoipAccount()));
					if (null == chatInfoBean) {
						chatInfoBean = new ChatInfoBean();
						chatInfoBean.setDocInfoBeanId(docInfoBean
								.getVoipAccount());
						chatInfoBean.setComment(false);
					} else {
						chatInfoBean.setComment(false);
					}
					chatInfoBean.setTimeout(false);
					chatInfoBean.setSubjectID(subjectID);
					chatInfoBean.setStatus(true);
					DBUtilsHelper.getInstance().saveChatinfo(chatInfoBean);
					chatInfoBean2 = chatInfoBean;
					SharedPrefsUtil.putValue("RM_"+chatInfoBean2.getSubjectID(), false);
					gc_title2.setVisibility(View.VISIBLE);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// chatInfoBean.setDocInfoBean(docInfoBean);
				// SharedPrefsUtil.putValue(Constants.CHATINFO,
				// chatInfoBean.toString());
				// SharedPrefsUtil.putValue(Constants.SUBJECTID, subjectId);
				/*
				 * Bundle bundle = new Bundle(); bundle.putInt("op", 1);
				 * serciceIntent.putExtras(bundle); startService(serciceIntent);
				 */
				TimerService.count = 0;
				mProgressBar.setProgress(0);
				USER_DATA = UserDataUtils.getUserData(subjectID);
				onSendTextMesage("用户发起了再次咨询");
				mChatFooter.setVisibility(View.VISIBLE);
				reConsult_re.setVisibility(View.GONE);
				myProgressbar_re.setVisibility(View.VISIBLE);
				gc_t_re.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
				more.setVisibility(View.VISIBLE);
				isComment = false;
				reConsult_bt.setText("评价医生");
			} else {
				ToastUtil.showMessage(data.getString("Message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private PopupWindow mpopupWindow;

	private void showExitChat() {
		View view = View.inflate(getApplicationContext(), R.layout.exit_chat,
				null);
		Button sure = (Button) view.findViewById(R.id.ec_sure);
		Button cancel = (Button) view.findViewById(R.id.ec_cancel);
		sure.setOnClickListener(this);
		cancel.setOnClickListener(this);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mpopupWindow.dismiss();
				// showComment();
				/*
				 * Intent i = new Intent(); i.setClass(GroupChatActivity.this,
				 * ChatCommentActivity.class); startActivity(i);
				 */
				requestExitChat();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mpopupWindow.dismiss();

			}
		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mpopupWindow.dismiss();
			}
		});

		view.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.fade_in));
		// RelativeLayout ll_popup = (RelativeLayout)
		// view.findViewById(R.id.ll_popup);
		// ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		// R.anim.push_bottom_in));

		if (mpopupWindow == null) {
			mpopupWindow = new PopupWindow(this);
			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(true);
		}

		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(more, Gravity.BOTTOM, 0, 0);
		mpopupWindow.update();
	}

	String subjectId = null;
	private MyProgressDialog myProgressDialog;

	private void requestExitChat() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		try {
			chatInfoBean = dbUtils.findFirst(Selector.from(ChatInfoBean.class)
					.where("status", "=", true));
			subjectId = chatInfoBean.getSubjectID();
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params.addQueryStringParameter("subjectId", subjectId);
		HttpClient.get(GroupChatActivity.this, Constants.CLOSESUBJECT_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						myProgressDialog = new MyProgressDialog(
								GroupChatActivity.this);
						myProgressDialog.setMessage("正在请求...");
						myProgressDialog.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, arg0.result);
						myProgressDialog.dismiss();
						try {
							gc_title2.setVisibility(View.GONE);
							SharedPrefsUtil.putValue("RM_"+chatInfoBean2.getSubjectID(), true);
							TimerService.count = 0;
							timingStop();
							JSONObject jsonObject = new JSONObject(arg0.result);
							String data = jsonObject.getString("Data");
							if (data.equals("true")) {
								String uniqueID = getDeviceHelper()
										.sendInstanceMessage(mGroupId,
												Constants.CLOSESUBJECT, null,
												USER_DATA);
								exitComment();
								Intent i = new Intent();
								i.setClass(GroupChatActivity.this,
										ChatCommentActivity.class);
								i.putExtra("subjectId", subjectId);
								//i.putExtra("voipAccount", chatInfoBean.getDocInfoBeanId());
								startActivity(i);
								chatInfoBean.setStatus(false);
								try {
									dbUtils.saveOrUpdate(chatInfoBean);
									chatInfoBean = dbUtils.findFirst(Selector
											.from(ChatInfoBean.class).where(
													"status", "=", false));
									// chatInfoBean.isStatus();
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								more.setVisibility(View.GONE);
								// dbUtils.update(ChatInfoBean.class,
								// updateColumnNames);
								// dbUtils.update(ChatInfoBean.class,
								// whereBuilder, "status");

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						// System.out.println("arg......"+arg0.getMessage());
						myProgressDialog.dismiss();
					}
				});
	}

	private void requestExitChat2() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		try {
			chatInfoBean = dbUtils.findFirst(Selector.from(ChatInfoBean.class)
					.where("status", "=", true));
			subjectId = chatInfoBean.getSubjectID();
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params.addQueryStringParameter("subjectId", subjectId);
		HttpClient.get(GroupChatActivity.this, Constants.CLOSESUBJECT_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtil.i(TAG, arg0.result);
						// exitComment();
						showTimeOut();
						chatInfoBean.setStatus(false);
						try {
							dbUtils.saveOrUpdate(chatInfoBean);
							chatInfoBean = dbUtils.findFirst(Selector.from(
									ChatInfoBean.class).where("status", "=",
									false));
							// chatInfoBean.isStatus();
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						// System.out.println("arg......"+arg0.getMessage());
					}
				});
	}

	/*
	 * private void analyzeJson(String json) { // TODO Auto-generated method
	 * stub try { JSONObject jsonObject = new JSONObject(json); JSONObject data
	 * = jsonObject.getJSONObject("Data"); SubjectID =
	 * data.getString("SubjectID");
	 * SharedPrefsUtil.putValue(Constants.SUBJECTID, SubjectID); JSONObject
	 * responser = data.getJSONObject("responser"); docInfoBean = new
	 * Gson().fromJson(responser.toString(), DocInfoBean.class); Intent i = new
	 * Intent(); i.setClass(context, GroupChatActivity.class); //
	 * i.putExtra(GroupBaseActivity.KEY_GROUP_ID, "88249600000048"); Bundle
	 * bundle = new Bundle(); bundle.putString("subjectID", SubjectID);
	 * bundle.putSerializable("docInfoBean", docInfoBean);
	 * bundle.putBoolean("firstStart", true); i.putExtras(bundle);
	 * context.startActivity(i); } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */
	private void timingStop() {
		// TODO Auto-generated method stub

		Bundle bundle = new Bundle();
		bundle.putInt("op", 3);
		serciceIntent.putExtras(bundle);
		MyApp.getInstance().startService(serciceIntent);
		TimerService.isPause = false;

	}

	private void showComment() {/*
								 * View view =
								 * View.inflate(getApplicationContext(),
								 * R.layout.chat_comment, null); Button sure =
								 * (Button) view.findViewById(R.id.ec_sure);
								 * Button cancel = (Button)
								 * view.findViewById(R.id.ec_cancel);
								 * sure.setOnClickListener(this);
								 * cancel.setOnClickListener(this);
								 * sure.setOnClickListener(new OnClickListener()
								 * {
								 * 
								 * @Override public void onClick(View v) { //
								 * TODO Auto-generated method stub
								 * mpopupWindow2.dismiss(); } });
								 * cancel.setOnClickListener(new
								 * OnClickListener() {
								 * 
								 * @Override public void onClick(View v) { //
								 * TODO Auto-generated method stub
								 * mpopupWindow.dismiss(); } });
								 * view.setOnClickListener(new OnClickListener()
								 * {
								 * 
								 * @Override public void onClick(View v) {
								 * mpopupWindow2.dismiss(); } });
								 * 
								 * view.startAnimation(AnimationUtils.loadAnimation
								 * (getApplicationContext(), R.anim.fade_in));
								 * //RelativeLayout ll_popup = (RelativeLayout)
								 * view.findViewById(R.id.ll_popup);
								 * //ll_popup.startAnimation
								 * (AnimationUtils.loadAnimation
								 * (getApplicationContext(),
								 * R.anim.push_bottom_in));
								 * 
								 * if(mpopupWindow2==null){ mpopupWindow2 = new
								 * PopupWindow(this);
								 * mpopupWindow2.setWidth(LayoutParams
								 * .MATCH_PARENT);
								 * mpopupWindow2.setHeight(LayoutParams
								 * .MATCH_PARENT);
								 * mpopupWindow2.setBackgroundDrawable(new
								 * BitmapDrawable());
								 * mpopupWindow2.setFocusable(true);
								 * mpopupWindow2.setOutsideTouchable(true); }
								 * 
								 * mpopupWindow2.setContentView(view);
								 * mpopupWindow2.showAtLocation(more,
								 * Gravity.BOTTOM, 0, 0);
								 * mpopupWindow2.update();
								 */
	}

}
