package com.darly.activities.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.darly.activities.R;
import com.darly.activities.app.Constract;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.PreferenceUserInfor;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.UserInformation;
import com.darly.activities.ui.qinjia.ChatMessageAdapter;
import com.darly.activities.ui.qinjia.GotyeVoicePlayClickPlayListener;
import com.darly.activities.ui.qinjia.RTPullListView;
import com.darly.activities.ui.qinjia.RTPullListView.OnRefreshListener;
import com.darly.activities.ui.qinjia.util.CommonUtils;
import com.darly.activities.ui.qinjia.util.SendImageMessageTask;
import com.darly.activities.ui.qinjia.util.URIUtil;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.google.gson.Gson;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeCustomerService;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMedia;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.gotye.api.WhineMode;

/**
 * @author Zhangyuhui ChatPage $ 上午9:54:58 TODO 用户聊天窗口。
 */
public class ChatPage extends Activity implements OnClickListener {
	private static final String TAG = "ChatPage";
	public static final int REALTIMEFROM_OTHER = 2;
	public static final int REALTIMEFROM_SELF = 1;
	public static final int REALTIMEFROM_NO = 0;
	private static final int REQUEST_PIC = 1;
	private static final int REQUEST_CAMERA = 2;

	public static final int VOICE_MAX_TIME = 60 * 1000;
	/**
	 * 上午9:55:23 TODO 消息列表
	 */
	private RTPullListView pullListView;
	/**
	 * 上午9:55:32 TODO 消息列表适配器
	 */
	public ChatMessageAdapter adapter;
	/**
	 * 上午9:55:44 TODO 私聊
	 */
	private GotyeUser o_user, user;
	/**
	 * 上午9:55:58 TODO 群聊
	 */
	private GotyeRoom o_room, room;
	/**
	 * 上午9:56:07 TODO 组群
	 */
	private GotyeGroup o_group, group;
	/**
	 * 上午9:56:15 TODO 服务
	 */
	private GotyeCustomerService o_cserver, cserver;
	private GotyeUser currentLoginUser;
	private ImageView voice_text_chage;
	private Button pressToVoice;
	private EditText textMessage;
	private ImageView showMoreType;
	private LinearLayout moreTypeLayout;

	// private PopupWindow menuWindow;
	// private AnimationDrawable anim;
	public int chatType = 0;

	private View realTalkView;
	private TextView realTalkName, stopRealTalk;
	private AnimationDrawable realTimeAnim;
	private boolean moreTypeForSend = true;

	public int onRealTimeTalkFrom = -1; // -1默认状态 ,0表示我在说话,1表示别人在实时语音

	private File cameraFile;
	public static final int IMAGE_MAX_SIZE_LIMIT = 1024 * 1024;
	public static final int Voice_MAX_TIME_LIMIT = 60 * 1000;
	private long playingId;
	private TextView title;
	// private VoiceRecognitionClient mASREngine;
	// private PopupWindow mPopupWindow;
	public boolean makingVoiceMessage = false;
	public GotyeAPI api = GotyeAPI.getInstance();
	boolean isClick = false;

	private ProgressDialogUtil loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gotye_activity_chat);

		loading = new ProgressDialogUtil(this);
		loading.setMessage("正在进入房间...");
		// mASREngine = VoiceRecognitionClient.getInstance(this);
		// mASREngine.setTokenApis(Constants.API_KEY, Constants.SECRET_KEY);
		currentLoginUser = api.getLoginUser();
		UserInformation information = new Gson().fromJson(
				PreferenceUserInfor.getUserInfor(Constract.USERINFO, this),
				UserInformation.class);
		if (currentLoginUser != null
				&& !information.getUserTrueName().equals(
						currentLoginUser.getName())) {
			api.login(information.getUserTrueName(), null);
		}
		// api.addListener(this);
		api.addListener(mDelegate);
		try {
			o_user = user = (GotyeUser) getIntent()
					.getSerializableExtra("user");
			o_room = room = (GotyeRoom) getIntent()
					.getSerializableExtra("room");
			o_group = group = (GotyeGroup) getIntent().getSerializableExtra(
					"group");
			o_cserver = cserver = (GotyeCustomerService) getIntent()
					.getSerializableExtra("cserver");
		} catch (Exception e) {
			// TODO: handle exception
			LogFileHelper.getInstance().e(TAG, e.getMessage());
		}

		initView();
		if (chatType == 0) {
			api.activeSession(user);
			loadData();
		} else if (chatType == 1) {
			int code = api.enterRoom(room);
			if (code == GotyeStatusCode.CodeOK) {
				api.activeSession(room);
				loadData();
				api.getMessageList(room, true);
			} else {
				loading.show();
			}
		} else if (chatType == 2) {
			api.activeSession(group);
			loadData();
		} else if (chatType == 3) {
			api.activeSession(cserver);
			loadData();
		}
		SharedPreferences spf = getSharedPreferences("fifter_cfg",
				Context.MODE_PRIVATE);
		boolean fifter = spf.getBoolean("fifter", false);
		api.enableTextFilter(GotyeChatTargetType.values()[chatType], fifter);
		int state = api.isOnline();
		if (state != 1) {
			setErrorTip(0);
		} else {
			setErrorTip(1);
		}
	}

	private void initView() {
		pullListView = (RTPullListView) findViewById(R.id.gotye_msg_listview);
		findViewById(R.id.back).setOnClickListener(this);
		title = ((TextView) findViewById(R.id.title));
		realTalkView = findViewById(R.id.real_time_talk_layout);
		realTalkName = (TextView) realTalkView
				.findViewById(R.id.real_talk_name);
		Drawable[] anim = realTalkName.getCompoundDrawables();
		realTimeAnim = (AnimationDrawable) anim[2];
		stopRealTalk = (TextView) realTalkView
				.findViewById(R.id.stop_real_talk);
		stopRealTalk.setOnClickListener(this);

		if (user != null) {
			chatType = 0;
			title.setText("和 " + user.getName() + " 聊天");
		} else if (room != null) {
			chatType = 1;
			title.setText("聊天室：" + room.getRoomID());
		} else if (group != null) {
			chatType = 2;
			String titleText = null;
			if (!TextUtils.isEmpty(group.getGroupName())) {
				titleText = group.getGroupName();
			} else {
				GotyeGroup temp = api.getGroupDetail(group, true);
				if (temp != null && !TextUtils.isEmpty(temp.getGroupName())) {
					titleText = temp.getGroupName();
				} else {
					titleText = String.valueOf(group.getGroupID());
				}
			}
			title.setText("群：" + titleText);
		} else if (cserver != null) {
			chatType = 3;
			title.setText("和客服" + String.valueOf(cserver.getGroupId()) + "聊天");
		}

		voice_text_chage = (ImageView) findViewById(R.id.send_voice);
		pressToVoice = (Button) findViewById(R.id.press_to_voice_chat);
		textMessage = (EditText) findViewById(R.id.text_msg_input);
		showMoreType = (ImageView) findViewById(R.id.more_type);
		moreTypeLayout = (LinearLayout) findViewById(R.id.more_type_layout);

		moreTypeLayout.findViewById(R.id.to_gallery).setOnClickListener(this);
		moreTypeLayout.findViewById(R.id.to_camera).setOnClickListener(this);
		moreTypeLayout.findViewById(R.id.voice_to_text)
				.setOnClickListener(this);
		moreTypeLayout.findViewById(R.id.real_time_voice_chat)
				.setOnClickListener(this);

		voice_text_chage.setOnClickListener(this);
		showMoreType.setOnClickListener(this);
		textMessage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				String text = arg0.getText().toString();
				// GotyeMessage message =new GotyeMessage();
				// GotyeChatManager.getInstance().sendMessage(message);
				sendTextMessage(text);
				textMessage.setText("");
				return true;
			}
		});
		pressToVoice.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// showPopupWindow2(pressToVoice);-------------------------------------------
					if (onRealTimeTalkFrom == 0) {

						ToastApp.showToast(ChatPage.this, R.string.gotevoice);
						return false;
					}

					if (GotyeVoicePlayClickPlayListener.isPlaying) {
						GotyeVoicePlayClickPlayListener.currentPlayListener
								.stopPlayVoice(true);
					}
					int code = 0;
					if (chatType == 0) {
						code = api.startTalk(user, WhineMode.DEFAULT, false,
								60 * 1000);
					} else if (chatType == 1) {
						code = api.startTalk(room, WhineMode.DEFAULT, false,
								60 * 1000);
					} else if (chatType == 2) {
						code = api.startTalk(group, WhineMode.DEFAULT, false,
								60 * 1000);
					} else if (chatType == 3) {
						code = api.startTalk(cserver, WhineMode.DEFAULT, false,
								60 * 1000);
					}
					int c = code;
					LogFileHelper.getInstance().i(c + "");
					pressToVoice.setText("松开 发送");
					break;
				case MotionEvent.ACTION_UP:
					if (onRealTimeTalkFrom == 0) {
						return false;
					}
					Log.d("chat_page",
							"onTouch action=ACTION_UP" + event.getAction());
					// if (onRealTimeTalkFrom > 0) {
					// return false;
					// }
					api.stopTalk();
					Log.d("chat_page",
							"after stopTalk action=" + event.getAction());
					pressToVoice.setText("按住 说话");
					break;
				case MotionEvent.ACTION_CANCEL:
					if (onRealTimeTalkFrom == 0) {
						return false;
					}
					Log.d("chat_page",
							"onTouch action=ACTION_CANCEL" + event.getAction());
					// if (onRealTimeTalkFrom > 0) {
					// return false;
					// }
					api.stopTalk();
					pressToVoice.setText("按住 说话");
					break;
				default:
					Log.d("chat_page",
							"onTouch action=default" + event.getAction());
					break;
				}
				return false;
			}
		});
		adapter = new ChatMessageAdapter(this, new ArrayList<GotyeMessage>());
		pullListView.setAdapter(adapter);
		pullListView.setSelection(adapter.getCount());
		setListViewInfo();
		refreshToTail();
	}

	private void sendTextMessage(String text) {
		String extraStr = null;
		if (!TextUtils.isEmpty(text)) {
			GotyeMessage toSend = null;
			if (chatType == 0) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser,
						o_user, text);
			} else if (chatType == 1) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser,
						o_room, text);
			} else if (chatType == 2) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser,
						o_group, text);
			} else if (chatType == 3) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser,
						o_cserver, text);
				extraStr = "http://kefu-c.gotye.com.cn/product";
			}
			// String extraStr = null;
			// String extraStr = currentLoginUser.getName()+text;
			if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}

			} else if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}
			}
			if (extraStr != null) {
				toSend.putExtraData(extraStr.getBytes());
			}

			// putExtre(toSend);
			int code = api.sendMessage(toSend);
			Log.d("", String.valueOf(code));
			adapter.addMsgToBottom(toSend);
			refreshToTail();
			refresh();
		}
	}

	// 从assets中读取文件写入到额外数据中
	public void putExtre(GotyeMessage msg) {
		try {
			InputStream in = getAssets().open("json.txt");
			InputStreamReader ir = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(ir);
			// String s=br.readLine();
			// msg.putExtraData(s.getBytes());
			// msg.setText(s);
			String s = "";
			for (int i = 0; i < 1000; i++) {
				s += i;
			}
			msg.setText(s);
			br.close();
			ir.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e(TAG, e.getMessage());
		}
	}

	public void sendUserDataMessage(byte[] userData, String text) {
		if (userData != null) {
			GotyeMessage toSend = null;
			if (chatType == 0) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						user, userData, userData.length);
			} else if (chatType == 1) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						room, userData, userData.length);
			} else if (chatType == 2) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						group, userData, userData.length);
			} else if (chatType == 3) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						cserver, userData, userData.length);
			}
			String extraStr = null;
			if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}

			} else if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}
			}
			if (extraStr != null) {
				toSend.putExtraData(extraStr.getBytes());
			}

			// int code =
			api.sendMessage(toSend);
			adapter.addMsgToBottom(toSend);
			refreshToTail();
			refresh();
		}
	}

	public void callBackSendImageMessage(GotyeMessage msg) {
		adapter.addMsgToBottom(msg);
		refreshToTail();
	}

	public void info(View v) {
		// 实时语音判断
		if (onRealTimeTalkFrom >= 0) {
			ToastApp.showToast(this, R.string.chat_vo_time);
			return;
		}
		// 语音消息判断
		if (makingVoiceMessage) {
			ToastApp.showToast(this, R.string.chat_vo_luyin);
			return;
		}

		LogFileHelper.getInstance().i(TAG, chatType + "");
		// if (chatType == 0) {
		// Intent intent = getIntent();
		// intent.setClass(getApplication(), UserInfoPage.class);
		// intent.putExtra("user", user);
		// startActivity(intent);
		// } else if (chatType == 1) {
		// Intent info = new Intent(getApplication(), RoomInfoPage.class);
		// info.putExtra("room", room);
		// startActivity(info);
		// } else if (chatType == 2) {
		// Intent info = new Intent(getApplication(), GroupInfoPage.class);
		// info.putExtra("group", group);
		// startActivity(info);
		// } else if (chatType == 3) {
		// return;
		// }
	}

	private void loadData() {
		List<GotyeMessage> messages = null;
		if (user != null) {
			messages = api.getMessageList(user, true);
		} else if (room != null) {
			messages = api.getMessageList(room, true);
		} else if (group != null) {
			messages = api.getMessageList(group, true);
		} else if (cserver != null) {
			messages = api.getMessageList(cserver, false);
		}
		if (messages == null) {
			messages = new ArrayList<GotyeMessage>();
		}
		adapter.refreshData(messages);
	}

	private void setListViewInfo() {
		// 下拉刷新监听器
		pullListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (chatType == 1) {
					api.getMessageList(room, true);
				} else {
					List<GotyeMessage> list = null;

					if (chatType == 0) {
						list = api.getMessageList(user, true);
					} else if (chatType == 2) {
						list = api.getMessageList(group, true);
					} else if (chatType == 3) {
						list = api.getMessageList(cserver, true);
					}
					if (list != null) {
						adapter.refreshData(list);
					} else {
						ToastApp.showToast(ChatPage.this,
								R.string.chat_no_hismes_more);
					}
				}
				adapter.notifyDataSetChanged();
				pullListView.onRefreshComplete();
			}
		});
		pullListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				final GotyeMessage message = adapter.getItem(position - 1);
				pullListView.setTag(message);
				pullListView.showContextMenu();
				return true;
			}
		});
		pullListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu conMenu,
							View arg1, ContextMenuInfo arg2) {
						final GotyeMessage message = (GotyeMessage) pullListView
								.getTag();
						if (message == null) {
							return;
						}
						MenuItem m = null;
						if (chatType == 1
								&& !message.getSender().getName()
										.equals(currentLoginUser.getName())) {
							m = conMenu.add(0, 0, 0, "举报");
						}
						// if(message.getType()==GotyeMessageType.GotyeMessageTypeAudio){
						// m= conMenu.add(0, 1, 0, "转为文字(仅限普通话)");
						// }
						if (m == null) {
							return;
						}
						m.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								switch (item.getItemId()) {
								case 0:
									api.report(0, "举报的说明", message);
									break;

								case 1:
									// api.decodeAudioMessage(message);
									break;
								}
								return true;
							}
						});
					}
				});

	}

	@Override
	protected void onDestroy() {
		api.removeListener(mDelegate);
		if (chatType == 0) {
			api.deactiveSession(o_user);
		} else if (chatType == 1) {
			api.deactiveSession(o_room);
			api.leaveRoom(o_room);
		} else if (chatType == 2) {
			api.deactiveSession(o_group);
		} else if (chatType == 3) {
			api.deactiveSession(o_cserver);
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (GotyeVoicePlayClickPlayListener.isPlaying
				&& GotyeVoicePlayClickPlayListener.currentPlayListener != null) {
			// 停止语音播放
			GotyeVoicePlayClickPlayListener.currentPlayListener
					.stopPlayVoice(false);
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		api.stopTalk();
		api.stopPlay();
		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			if (onRealTimeTalkFrom == 0) {
				ToastApp.showToast(this, R.string.chat_vo_time_notdo);
				return;
			}
			// if(makingVoiceMessage){
			// ToastApp.showToast(this, R.string.chat_vo_time_notdo);
			// return;
			// }
			onBackPressed();
			break;
		case R.id.send_voice:
			if (pressToVoice.getVisibility() == View.VISIBLE) {
				pressToVoice.setVisibility(View.GONE);
				textMessage.setVisibility(View.VISIBLE);
				voice_text_chage
						.setImageResource(R.drawable.voice_btn_selector);
				showMoreType.setImageResource(R.drawable.send_selector);
				moreTypeForSend = true;
				moreTypeLayout.setVisibility(View.GONE);
			} else {
				pressToVoice.setVisibility(View.VISIBLE);
				textMessage.setVisibility(View.GONE);

				voice_text_chage
						.setImageResource(R.drawable.change_to_text_press);

				showMoreType.setImageResource(R.drawable.more_type_selector);
				moreTypeForSend = false;
				hideKeyboard();
			}

			break;
		case R.id.more_type:
			if (moreTypeForSend) {
				hideKeyboard();
				String str = textMessage.getText().toString();
				sendTextMessage(str);
				textMessage.setText("");
			} else {
				if (moreTypeLayout.getVisibility() == View.VISIBLE) {
					moreTypeLayout.setVisibility(View.GONE);
				} else {
					moreTypeLayout.setVisibility(View.VISIBLE);
					if (chatType == 1 && api.supportRealtime(room) == true) {
						moreTypeLayout.findViewById(R.id.real_time_voice_chat)
								.setVisibility(View.VISIBLE);
					}

					// else if(chatType == 0){
					// moreTypeLayout.findViewById(R.id.voice_to_text)
					// .setVisibility(View.VISIBLE);
					// }

				}
			}
			break;
		case R.id.to_gallery:
			takePic();
			break;
		case R.id.to_camera:
			takePhoto();
			break;
		case R.id.voice_to_text:
			// showTalkView();

			// if(isClick == true){
			// pullListView.setBackgroundResource(getResources().getColor(R.color.transparent));
			// isClick = false;
			// }else{
			// pullListView.setBackgroundColor(Color.BLACK);
			// isClick = true;
			// }

			break;
		case R.id.real_time_voice_chat:
			realTimeTalk();
			break;
		case R.id.stop_real_talk:
			api.stopTalk();
			break;
		default:
			break;
		}
	}

	public void showImagePrev(GotyeMessage message) {
		hideKeyboard();
	}

	public void realTimeTalk() {
		if (onRealTimeTalkFrom > 0) {
			Toast.makeText(this, "请稍后...", Toast.LENGTH_SHORT).show();
			return;
		}
		api.startTalk(room, WhineMode.DEFAULT, true, Voice_MAX_TIME_LIMIT);
		moreTypeLayout.setVisibility(View.GONE);
	}

	public void hideKeyboard() {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法
		imm.hideSoftInputFromWindow(textMessage.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void takePic() {
		Intent intent;
		intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/jpeg");
		startActivityForResult(intent, REQUEST_PIC);

		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		// intent.setType("image/*");
		// startActivityForResult(intent, REQUEST_PIC);
	}

	private void takePhoto() {
		selectPicFromCamera();
	}

	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照",
					Toast.LENGTH_SHORT).show();
			return;
		}

		cameraFile = new File(URIUtil.getAppFIlePath()
				+ +System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选取图片的返回值
		if (requestCode == REQUEST_PIC) {
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					String path = URIUtil.uriToPath(this, selectedImage);
					sendPicture(path);
				}
			}

		} else if (requestCode == REQUEST_CAMERA) {
			if (resultCode == RESULT_OK) {

				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			}
		}
		// TODO 获取图片失败
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void sendPicture(String path) {
		SendImageMessageTask task = null;
		if (chatType == 0) {
			task = new SendImageMessageTask(this, user);
		} else if (chatType == 1) {
			task = new SendImageMessageTask(this, room);
		} else if (chatType == 2) {
			task = new SendImageMessageTask(this, group);
		} else if (chatType == 3) {
			task = new SendImageMessageTask(this, cserver);
		}
		task.execute(path);
	}

	public void setPlayingId(long playingId) {
		this.playingId = playingId;
		adapter.notifyDataSetChanged();
	}

	public long getPlayingId() {
		return playingId;
	}

	private boolean isMyMessage(GotyeMessage message) {
		if (message.getSender() != null
				&& user.getName().equals(message.getSender().getName())
				&& currentLoginUser.getName().equals(
						message.getReceiver().getName())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isMySerVerMessage(GotyeMessage message) {
		if (message.getSender() != null
				&& cserver.getId() == message.getSender().getId()
				&& currentLoginUser.getName().equals(
						message.getReceiver().getName())) {
			return true;
		} else {
			return false;
		}
	}

	boolean realTalk, realPlay;

	private void setErrorTip(int code) {
		if (code == 1) {
			findViewById(R.id.error_tip).setVisibility(View.GONE);
		} else {
			findViewById(R.id.error_tip).setVisibility(View.VISIBLE);
			if (code == -1) {
				findViewById(R.id.loading).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.showText)).setText("连接中...");
				findViewById(R.id.error_tip_icon).setVisibility(View.GONE);
			} else {
				findViewById(R.id.loading).setVisibility(View.GONE);
				((TextView) findViewById(R.id.showText)).setText("未连接");
				findViewById(R.id.error_tip_icon).setVisibility(View.VISIBLE);
			}
		}
	}

	public void refreshToTail() {
		if (adapter != null) {
			if (pullListView.getLastVisiblePosition()
					- pullListView.getFirstVisiblePosition() <= pullListView
						.getCount())
				pullListView.setStackFromBottom(false);
			else
				pullListView.setStackFromBottom(true);

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {

					pullListView.setSelection(pullListView.getAdapter()
							.getCount() - 1);

					// This seems to work
					handler.post(new Runnable() {
						@Override
						public void run() {
							pullListView.clearFocus();
							pullListView.setSelection(pullListView.getAdapter()
									.getCount() - 1);
						}
					});
				}
			}, 300);
			pullListView.setSelection(adapter.getCount()
					+ pullListView.getHeaderViewsCount() - 1);
		}
	}

	private Handler handler = new Handler();

	private GotyeDelegate mDelegate = new GotyeDelegate() {

		@Override
		public void onSendMessage(int code, GotyeMessage message) {
			Log.d("OnSend", "code= " + code + "message = " + message);
			// GotyeChatManager.getInstance().insertChatMessage(message);
			adapter.updateMessage(message);
			if (message.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
				// api.decodeAudioMessage(message);
			}
			// message.senderUser =
			// DBManager.getInstance().getUser(currentLoginName);
			pullListView.setSelection(adapter.getCount());
		}

		@Override
		public void onReceiveMessage(GotyeMessage message) {
			// GotyeChatManager.getInstance().insertChatMessage(message);
			if (chatType == 0) {
				if (isMyMessage(message)) {
					adapter.addMsgToBottom(message);
					pullListView.setSelection(adapter.getCount());
					api.downloadMediaInMessage(message);
				}
			} else if (chatType == 1) {
				if (message.getReceiver().getId() == room.getRoomID()) {
					adapter.addMsgToBottom(message);
					pullListView.setSelection(adapter.getCount());
					api.downloadMediaInMessage(message);

				}
			} else if (chatType == 2) {
				if (message.getReceiver().getId() == group.getGroupID()) {
					adapter.addMsgToBottom(message);
					pullListView.setSelection(adapter.getCount());
					api.downloadMediaInMessage(message);
				}
			} else if (chatType == 3) {
				if (isMySerVerMessage(message)) {
					adapter.addMsgToBottom(message);
					pullListView.setSelection(adapter.getCount());
					api.downloadMediaInMessage(message);

				}
			}

			// scrollToBottom();
		}

		@Override
		public void onDownloadMediaInMessage(int code, GotyeMessage message) {
			adapter.updateChatMessage(message);
		}

		@Override
		public void onEnterRoom(int code, GotyeRoom room) {
			loading.dismiss();
			if (code == 0) {
				api.activeSession(room);
				loadData();
				if (room != null && !TextUtils.isEmpty(room.getRoomName())) {
					title.setText("聊天室：" + room.getRoomName());
				}
			} else {
				ToastApp.showToast(ChatPage.this, R.string.chat_roomnotexsist);
				finish();
			}
		}

		@Override
		public void onGetMessageList(int code, List<GotyeMessage> list) {
			if (chatType == 0) {
				List<GotyeMessage> listmessages = api.getMessageList(o_user,
						false);
				if (listmessages != null) {
					adapter.refreshData(listmessages);
				} else {
					ToastApp.showToast(ChatPage.this, R.string.chat_no_hismes);
				}
			} else if (chatType == 1) {
				List<GotyeMessage> listmessages = api.getMessageList(o_room,
						false);
				if (listmessages != null) {
					adapter.refreshData(listmessages);
				} else {
					ToastApp.showToast(ChatPage.this, R.string.chat_no_hismes);
				}
			} else if (chatType == 2) {
				List<GotyeMessage> listmessages = api.getMessageList(o_group,
						false);
				if (listmessages != null) {
					adapter.refreshData(listmessages);
				} else {
					ToastApp.showToast(ChatPage.this, R.string.chat_no_hismes);
				}
			}
			adapter.notifyDataSetInvalidated();
			pullListView.onRefreshComplete();
		}

		@Override
		public void onStartTalk(int code, boolean isRealTime, int targetType,
				GotyeChatTarget target) {

			Log.e("player", "onStartTalk====================================:"
					+ isRealTime + code);
			if (isRealTime) {
				ChatPage.this.realTalk = true;
				if (code != 0) {
					ToastApp.showToast(ChatPage.this, R.string.chat_qiangmai);
					return;
				}
				if (GotyeVoicePlayClickPlayListener.isPlaying) {
					GotyeVoicePlayClickPlayListener.currentPlayListener
							.stopPlayVoice(true);
				}
				onRealTimeTalkFrom = 0;
				realTimeAnim.start();
				realTalkView.setVisibility(View.VISIBLE);
				realTalkName.setText(R.string.chat_you_talking);
				stopRealTalk.setVisibility(View.VISIBLE);
			} else {
				makingVoiceMessage = true;
			}
		}

		@Override
		public void onStopTalk(int code, GotyeMessage message,
				boolean isVoiceReal) {
			Log.e("player", "onStopTalk=====================" + code);

			if (isVoiceReal) {
				onRealTimeTalkFrom = -1;
				realTimeAnim.stop();
				realTalkView.setVisibility(View.GONE);
			} else {
				if (code != 0) {
					ToastApp.showToast(ChatPage.this, R.string.chat_timeless);
					return;
				} else if (message == null) {
					ToastApp.showToast(ChatPage.this, R.string.chat_timeless);
					return;
				}
				// api.decodeAudioMessage(message);
				if (!isVoiceReal && message.getText().length() > 0) {
					message.putExtraData(message.getText().getBytes());
				}
				if (chatType == 3) {
					String extraStr = "http://kefu-c.gotye.com.cn/product";
					message.putExtraData(extraStr.getBytes());
				}
				api.sendMessage(message);
				adapter.addMsgToBottom(message);
				refreshToTail();
				makingVoiceMessage = false;
			}

		}

		@Override
		public void onPlayStart(int code, GotyeMessage message) {
		}

		@Override
		public void onPlaying(int code, int position) {
		}

		@Override
		public void onPlayStop(int code) {
			setPlayingId(-1);

			if (ChatPage.this.realPlay) {
				onRealTimeTalkFrom = -1;
				realTimeAnim.stop();
				realTalkView.setVisibility(View.GONE);
			}
			if (ChatPage.this.realPlay) {
				ChatPage.this.realPlay = false;
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onRealPlayStart(int code, GotyeRoom room, GotyeUser who) {
			if (code == 0 && room.getRoomID() == ChatPage.this.room.getRoomID()) {
				ChatPage.this.realPlay = true;
				onRealTimeTalkFrom = 1;
				realTalkView.setVisibility(View.VISIBLE);
				realTalkName.setText(who.getName() + R.string.chat_talking);
				realTimeAnim.start();
				stopRealTalk.setVisibility(View.GONE);
				if (GotyeVoicePlayClickPlayListener.isPlaying) {
					GotyeVoicePlayClickPlayListener.currentPlayListener
							.stopPlayVoice(false);
				}
			}
		}

		@Override
		public void onGetUserDetail(int code, GotyeUser user) {
			if (chatType == 0) {
				if (user.getName().equals(ChatPage.this.user.getName())) {
					ChatPage.this.user = user;
				}
			}
		}

		@Override
		public void onDownloadMedia(int code, GotyeMedia media) {
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onUserDismissGroup(GotyeGroup group, GotyeUser user) {
			// TODO Auto-generated method stub
			if (ChatPage.this.group != null
					&& group.getGroupID() == ChatPage.this.group.getGroupID()) {
				Intent i = new Intent(ChatPage.this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if (!(user.getName()).equals(currentLoginUser.getName())) {
					Toast.makeText(getBaseContext(), R.string.chat_display,
							Toast.LENGTH_SHORT).show();
				}
				finish();
				startActivity(i);
			}
		}

		@Override
		public void onUserKickedFromGroup(GotyeGroup group, GotyeUser kicked,
				GotyeUser actor) {
			// TODO Auto-generated method stub
			if (ChatPage.this.group != null
					&& group.getGroupID() == ChatPage.this.group.getGroupID()) {
				if (kicked.getName().equals(currentLoginUser.getName())) {
					Intent i = new Intent(ChatPage.this, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Toast.makeText(getBaseContext(), R.string.chat_you_reless,
							Toast.LENGTH_SHORT).show();
					finish();
					startActivity(i);
				}

			}
		}

		@Override
		public void onReport(int code, GotyeMessage message) {
			// TODO Auto-generated method stub
			if (code == GotyeStatusCode.CodeOK) {
				ToastApp.showToast(ChatPage.this, R.string.chat_succ_jubao);
			} else {
				ToastApp.showToast(ChatPage.this, R.string.chat_fail_jubao);
			}
			super.onReport(code, message);
		}

		@Override
		public void onGetGroupDetail(int code, GotyeGroup group) {
			if (ChatPage.this.group != null
					&& ChatPage.this.group.getGroupID() == group.getGroupID()) {
				title.setText("群：" + group.getGroupName());
			}
		}

		@Override
		public void onDecodeMessage(int code, GotyeMessage message) {
			if (code == GotyeStatusCode.CodeOK) {
				// VoiceToTextUtil util = new VoiceToTextUtil(ChatPage.this,
				// mASREngine);
				// util.toPress(message);
			}
			super.onDecodeMessage(code, message);
		}

		@Override
		public void onLogin(int code, GotyeUser currentLoginUser) {
			// TODO Auto-generated method stub
			setErrorTip(1);
		}

		@Override
		public void onLogout(int code) {
			// TODO Auto-generated method stub
			setErrorTip(0);
			if (mDelegate != null) {
				api.removeListener(mDelegate);
				finish();
			}
		}

		@Override
		public void onReconnecting(int code, GotyeUser currentLoginUser) {
			// TODO Auto-generated method stub
			setErrorTip(-1);
		}
	};

	/**
	 * 
	 * 上午11:19:02
	 * 
	 * @author Zhangyuhui ChatPage.java TODO 每次发送完毕后。手动刷新界面。出现对方会话内容。
	 */
	private void refresh() {
		// TODO Auto-generated method stub
		// if (chatType == 1) {
		// api.getMessageList(room, true);
		// } else {
		// List<GotyeMessage> list = null;
		//
		// if (chatType == 0) {
		// list = api.getMessageList(user, true);
		// } else if (chatType == 2) {
		// list = api.getMessageList(group, true);
		// } else if (chatType == 3) {
		// list = api.getMessageList(cserver, true);
		// }
		// if (list != null) {
		// adapter.refreshData(list);
		// } else {
		// ToastApp.showToast(ChatPage.this, "没有更多历史消息");
		// }
		// }
		// adapter.notifyDataSetChanged();
		// pullListView.onRefreshComplete();
	}
}
