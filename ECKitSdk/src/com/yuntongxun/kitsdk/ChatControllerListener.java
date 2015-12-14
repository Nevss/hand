package com.yuntongxun.kitsdk;

import android.content.Context;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.yuntongxun.kitsdk.adapter.ConversationAdapter;

public interface ChatControllerListener {
			// 初始化医生信息
			public void initDocInfo(String contactid, TextView tvUserName);

			// 开始会话
			public void chatStart(String contactid);

			// 结束咨询
			public void closeWindow(Context mContext, String contactid)
					throws DbException;

			// 再次咨询
			public void retryChat(Context mContext, String sujectId,
					String contactid);

			// 时间控制,时间停止
			public void timeStop(String contactid);

			// 时间控制,时间开始
			public void timeStart(String contactid);

			// 时间控制，时间暂停
			public void timePause(String contactid);

			// 打开评论页面
			public void openComment(Context mContext, String contactId);

			// 转诊
			public void transferTreat(String from, String contactId,String subjectId);

			// 消息处理
			public void handleMessage(String subjectId, String from);
			
			//切换到帮忙医小助手
			public void goToBMYHelpActivity(Context context);
			
			//关闭回话Activity
			public void backToMain(Context mContext);
			
			//同步群组 
			public void syncUserGroups(ConversationAdapter adapter);
			
			//消息提醒
			public void notifycationSend(Context mContext,String messsage);
			
			//获取chatInfoDB
			public DbUtils getChatInfoDb();

}
