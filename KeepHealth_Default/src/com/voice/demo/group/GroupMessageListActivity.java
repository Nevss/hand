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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hisun.phone.core.voice.util.Log4Util;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.voice.demo.group.baseui.CCPTextView;
import com.voice.demo.group.model.IMConversation;
import com.voice.demo.group.utils.EmoticonUtil;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPIntentUtils;
import com.voice.demo.tools.CCPUtil;
import com.voice.demo.tools.net.ITask;
import com.voice.demo.tools.net.TaskKey;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.app.MyApp;
import com.ytdinfo.keephealth.jpush.LittleHelperActivity;
import com.ytdinfo.keephealth.model.DocInfoBean;
import com.ytdinfo.keephealth.ui.view.RoundImageView;
import com.ytdinfo.keephealth.utils.ImageLoaderUtils;

/**
 * IM, group chat entrance interface
 * Display all the IM, group chat sessions and the system notification list
 * 
 * @version Time: 2013-6-15
 */
public class GroupMessageListActivity extends Activity  implements View.OnClickListener ,OnItemClickListener{
	
	private TextView mContactNum;
	private TextView mGroupNum;
	
	private LinearLayout mGroupTopContentLy;
	private ListView mGroupListLv;
	private LinearLayout mGroupListEmpty;
	
	private IMConvAdapter mIMAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.layout_im_group_activity);
		/*handleTitleDisplay(getString(R.string.btn_title_back)
				, getString(R.string.app_title_group_msg_list)
				, getString(R.string.btn_clear_all_text));*/
		
		//initResourceRefs();
		initListView();
		// init emoji .
		EmoticonUtil.initEmoji();
		//initConversation();
		// regist .
		registerReceiver(new String[]{CCPIntentUtils.INTENT_IM_RECIVE
				,CCPIntentUtils.INTENT_DELETE_GROUP_MESSAGE
				,CCPIntentUtils.INTENT_REMOVE_FROM_GROUP
				,CCPIntentUtils.INTENT_RECEIVE_SYSTEM_MESSAGE
				,CCPIntentUtils.INTENT_JOIN_GROUP_SUCCESS});
		//initConversation();
	}

	private void initResourceRefs() {
		
		//mGroupTopContentLy = (LinearLayout) findViewById(R.id.group_info_base_content);
		
		// The contact item
		//View inflate = getLayoutInflater().inflate(R.layout.list_item_group_base, null);
	//	inflate.setId(R.drawable.im_contact_icon);
		//inflate.setOnClickListener(this);
	//	((ImageView) inflate.findViewById(R.id.icon)).setImageResource(R.drawable.im_contact_icon);
		//((TextView) inflate.findViewById(R.id.tv_name)).setText(R.string.str_contact);
		//mContactNum = ((TextView) inflate.findViewById(R.id.group_count_tv));
	//	mGroupTopContentLy.addView(inflate);
		// The group item
		//inflate = getLayoutInflater().inflate(R.layout.list_item_group_base, null);
		//inflate.setId(R.drawable.group_icon);
		//inflate.setOnClickListener(this);
		
		//((ImageView) inflate.findViewById(R.id.icon)).setImageResource(R.drawable.group_icon);
	//((TextView) inflate.findViewById(R.id.tv_name)).setText(R.string.str_group);
		//mGroupNum = ((TextView) inflate.findViewById(R.id.group_count_tv));
		
	//	mGroupTopContentLy.addView(inflate);
		
		/*if(CCPConfig.VoIP_ID_LIST != null) {
			String[] split = CCPConfig.VoIP_ID_LIST.split(",");
			if(split != null ) {
				int size = split.length > 0?split.length - 1:0;
				mContactNum.setText( "(" + size + ")");
			}
		}*/
		
		initListView();
	}

	private void initListView() {
		mGroupListLv = (ListView) findViewById(R.id.group_list_content);
		mGroupListLv.setOnItemClickListener(this);
		mGroupListEmpty = (LinearLayout) findViewById(R.id.group_list_empty);
		mGroupListLv.setEmptyView(mGroupListEmpty);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("GroupMessageListActivity");
		MobclickAgent.onResume(this);
		
	/*	SharedPreferences cPreferences = CcpPreferences.getSharedPreferences();
		int joinNum = cPreferences
				.getInt(CCPPreferenceSettings.SETTING_JOIN_GROUP_SIZE.getId(),
						(Integer)CCPPreferenceSettings.SETTING_JOIN_GROUP_SIZE
								.getDefaultValue());
		int pubNum = cPreferences.getInt(
				CCPPreferenceSettings.SETTING_PUB_GROUP_SIZE.getId(),
				(Integer) CCPPreferenceSettings.SETTING_PUB_GROUP_SIZE
						.getDefaultValue());
		if(joinNum != 0 || pubNum != 0) {
			mGroupNum.setText("(" + joinNum + "/" + pubNum + ")");
		}
		
		initConversation();*/
		initConversation();
	}
	
	protected void handleTaskBackGround(ITask iTask) {
		int key = iTask.getKey();
		if(key == TaskKey.TASK_KEY_DEL_MESSAGE) {
			// delete all IM message and del local file also.
			try {
				CCPSqliteManager.getInstance().deleteAllIMMessage();
				CCPUtil.delAllFile(MyApp.getInstance().getVoiceStore().getAbsolutePath());
				CCPSqliteManager.getInstance().deleteAllNoticeMessage();
				sendBroadcast(new Intent(CCPIntentUtils.INTENT_IM_RECIVE));
				//ysj closeConnectionProgress();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
/*	@Override
	protected void handleTitleAction(int direction) {
		
		if(direction == TITLE_RIGHT_ACTION) {
			showConnectionProgress(getString(R.string.str_dialog_message_default));
			ITask iTask = new ITask(TaskKey.TASK_KEY_DEL_MESSAGE);
			addTask(iTask);
		} else {
			super.handleTitleAction(direction);
		}
	}*/

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	/*ysj	case R.drawable.im_contact_icon:
			
			//startActivity(new Intent(GroupMessageListActivity.this, IMChatActivity.class));
			
			Intent intent = new Intent(GroupMessageListActivity.this, InviteInterPhoneActivity.class);
			intent.putExtra("create_to", InviteInterPhoneActivity.CREATE_TO_IM_TALK);
			startActivity(intent);
			
			break;*/
			
		case R.drawable.group_icon:
			
			startActivity(new Intent(GroupMessageListActivity.this, GroupListActivity.class));
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		EmoticonUtil.getInstace().release();
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		IMConversation vSession = mIMAdapter.getItem(position);
		//System.out.println("vseession........"+vSession.toString());
		if(vSession != null ) {
			Intent intent = null;
			if(vSession.getType() == IMConversation.CONVER_TYPE_SYSTEM) {
				intent = new Intent(GroupMessageListActivity.this, SystemMsgActivity.class) ;
				
			} else if (vSession.getId().equals("帮忙医小助手")) {
				Intent i = new Intent(GroupMessageListActivity.this, LittleHelperActivity.class);
//	        	i.putExtras(bundle);
	        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	        	startActivity(i);
			}
			else {
				intent = new Intent(GroupMessageListActivity.this, GroupChatActivity.class) ;
				Bundle bundle = new Bundle();
				bundle.putSerializable("docInfoBean", docInfoBean);
				intent.putExtras(bundle);
			}
			startActivity(intent);
		}
		
	}
	
	private DocInfoBean docInfoBean ;
	class IMConvAdapter extends ArrayAdapter<IMConversation> {
		private DbUtils dbUtils ;
		LayoutInflater mInflater;
		public IMConvAdapter(Context context,List<IMConversation> iMList) {
			super(context, 0, iMList);
			System.out.println("tds......."+iMList.toString());
			dbUtils = DbUtils.create(context);
			mInflater = getLayoutInflater();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			IMConvHolder holder;
			if (convertView == null|| convertView.getTag() == null) {
				convertView = mInflater.inflate(R.layout.im_chat_list_item, null);
				holder = new IMConvHolder();
				
				holder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.updateTime = (TextView) convertView.findViewById(R.id.update_time);
				holder.iLastMessage = (CCPTextView) convertView.findViewById(R.id.im_last_msg);
				holder.newCount = (TextView) convertView.findViewById(R.id.im_unread_count);
			//	holder.newCountLy = (LinearLayout) convertView.findViewById(R.id.unread_count_ly);
			holder.unReadBg = (RelativeLayout)convertView.findViewById(R.id.im_unread_bg);
			} else {
				holder = (IMConvHolder) convertView.getTag();
			}
			
			IMConversation iSession = getItem(position);
			if(iSession != null) {
				try {
					 docInfoBean =  dbUtils.findFirst(Selector.from(DocInfoBean.class).where("voipAccount","=",iSession.getContact()));
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(iSession.getType() == IMConversation.CONVER_TYPE_SYSTEM) {
					
				//	holder.avatar.setImageResource(R.drawable.system_messages_icon);
				} else if (iSession.getType() == IMConversation.CONVER_TYPE_MESSAGE) {
					if(iSession.getId().startsWith("g")) {
						holder.avatar.setImageResource(R.drawable.photo_default);
					} else if (iSession.getId().equals("帮忙医小助手")) {
						holder.avatar.setImageResource(R.drawable.ic_launcher);
					}
					else {
								ImageLoader.getInstance().displayImage(docInfoBean.getHeadPicture(), holder.avatar, ImageLoaderUtils.getOptions());
					}
				}
if (!docInfoBean.getUserName().equals("")&&null!=docInfoBean.getUserName()) {
	holder.name.setText(docInfoBean.getUserName());
}else {
	holder.name.setText(iSession.getContact());
}
				
				holder.updateTime.setText(iSession.getDateCreated().substring(5, 11));
				
				if(!TextUtils.isEmpty(iSession.getUnReadNum()) && !"0".equals(iSession.getUnReadNum())) {
					holder.newCount.setText(iSession.getUnReadNum());
					holder.newCount.setVisibility(View.VISIBLE);
					holder.unReadBg.setVisibility(View.VISIBLE);
				} else{
					holder.newCount.setVisibility(View.GONE);
					holder.unReadBg.setVisibility(View.GONE);
				}
				
				holder.iLastMessage.setEmojiText(iSession.getRecentMessage());
			}
			
			
			return convertView;
		}
		
		
		class IMConvHolder {
			
			RoundImageView avatar;
			TextView name;
			TextView updateTime;
			
			CCPTextView iLastMessage;
			TextView newCount;
			LinearLayout newCountLy;
			RelativeLayout unReadBg;
		}
	}
	
	
	class IMMsgAsyncTask extends AsyncTask<Void, Void, ArrayList<IMConversation>>{

		@Override
		protected ArrayList<IMConversation> doInBackground(Void... params) {
				try {
					return CCPSqliteManager.getInstance().queryIMConversation();
				} catch (Exception e) {
					e.printStackTrace();
				}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<IMConversation> result) {
			super.onPostExecute(result);
			if(result != null && !result.isEmpty()){
				//
				mIMAdapter = new IMConvAdapter(getApplicationContext(), result);
				mGroupListLv.setAdapter(mIMAdapter);
				mGroupListEmpty.setVisibility(View.GONE);
			} else {
				//
				mGroupListLv.setAdapter(null);
				mGroupListEmpty.setVisibility(View.VISIBLE);
			}
		}
	}
	
	
	protected void onReceiveBroadcast(Intent intent) {
		if (intent != null && (CCPIntentUtils.INTENT_IM_RECIVE.equals(intent.getAction())
				|| CCPIntentUtils.INTENT_DELETE_GROUP_MESSAGE.equals(intent.getAction())
				|| CCPIntentUtils.INTENT_REMOVE_FROM_GROUP.equals(intent.getAction()))
				|| CCPIntentUtils.INTENT_RECEIVE_SYSTEM_MESSAGE.equals(intent.getAction())
				|| CCPIntentUtils.INTENT_JOIN_GROUP_SUCCESS.equals(intent.getAction())) {
			//update UI...
			initConversation();
		}
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			setResult(Activity.RESULT_OK,intent);
			finish();
			}
		return super.onKeyDown(keyCode, event);
		}
	protected int getLayoutId() {
		return R.layout.layout_im_group_activity;
	}
	

	private void initConversation() {
		new IMMsgAsyncTask().execute();
	}
	private InternalReceiver internalReceiver;
	protected final void registerReceiver(String[] actionArray) {
		if (actionArray == null) {
			return;
		}
		IntentFilter intentfilter = new IntentFilter(INTETN_ACTION_EXIT_CCP_DEMO);
		intentfilter.addAction(CCPIntentUtils.INTENT_CONNECT_CCP);
		intentfilter.addAction(CCPIntentUtils.INTENT_DISCONNECT_CCP);
		for (String action : actionArray) {
			intentfilter.addAction(action);
		}

		if (internalReceiver == null) {
			internalReceiver = new InternalReceiver();
		}
		registerReceiver(internalReceiver, intentfilter);
	}
	public static final String INTETN_ACTION_EXIT_CCP_DEMO = "exit_demo";
	class InternalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent == null ) {
				return;
			}
			
			if (CCPIntentUtils.INTENT_KICKEDOFF.equals(intent.getAction())
					|| CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent.getAction())) {
				
				String message = "您的账号在其他地方已经登录";
				if(CCPIntentUtils.INTENT_INVALIDPROXY.equals(intent.getAction())) {
					message = "无效的代理,与云通讯服务器断开";
				}
				Dialog dialog = new AlertDialog.Builder(GroupMessageListActivity.this)
						.setTitle(R.string.account_offline_notify)
						.setIcon(R.drawable.navigation_bar_help_icon)
						.setMessage(message)
						.setPositiveButton(R.string.dialog_btn,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
										
										CCPUtil.exitCCPDemo();
									//	launchCCP();  
									}

								}).create();
					dialog.show();
					
					
			} else if (intent != null && INTETN_ACTION_EXIT_CCP_DEMO.equals(intent.getAction())) {
				Log4Util.d(CCPHelper.DEMO_TAG, "Launcher destory.");
				CCPUtil.exitCCPDemo();
				finish();
			} else {
				if(intent == null || TextUtils.isEmpty(intent.getAction())) {
					return;
				}
				
				/**
				 * version 3.5 for listener SDcard status
				 */
				if(Intent.ACTION_MEDIA_REMOVED.equalsIgnoreCase(intent.getAction()) 
						|| Intent.ACTION_MEDIA_MOUNTED.equalsIgnoreCase(intent.getAction())) {
					
					//updateExternalStorageState();
					return ;
				}
				
				
				onReceiveBroadcast(intent);
			}
		}
	}
	public void onPause() {
		super.onPause();
		
		MobclickAgent.onPageEnd("GroupMessageListActivity");
		MobclickAgent.onPause(this);
	}
}
