package com.ytdinfo.keephealth.jpush;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.voice.demo.group.GroupBaseActivity;
import com.voice.demo.group.model.IMChatMessageDetail;
import com.voice.demo.sqlite.CCPSqliteManager;
import com.voice.demo.tools.CCPIntentUtils;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.MyApp;
import com.ytdinfo.keephealth.model.TBNews;
import com.ytdinfo.keephealth.model.UserModel;
import com.ytdinfo.keephealth.ui.MainActivity;
import com.ytdinfo.keephealth.utils.DBUtil;
import com.ytdinfo.keephealth.utils.JsonUtil;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private final String ACTION_NAME = "接受消息推送的广播";
	private static final String TAG = "JPush_MyReceiver";
	String regId;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		UserModel userModel = new Gson().fromJson(
				SharedPrefsUtil.getValue(Constants.USERMODEL, null),
				UserModel.class);
		
		if(userModel == null){
			return;
		}
		
        Bundle bundle = intent.getExtras();
		Log.i(TAG, "[MyReceiver]===" + intent.getAction() + "--- extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
             regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[MyReceiver]===接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.i(TAG, "[MyReceiver]===接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	
        	//当接收到推送的消息后，将消息内容保存到sqlite数据库中
        	String  desc = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        	String  extras_json = bundle.getString(JPushInterface.EXTRA_EXTRA);
        	String  msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        	TBNews tbNews = JsonUtil.jsonTOtbnews(extras_json);
        	tbNews.setDesc(desc);
        	DBUtil dbUtil = new DBUtil(MyApp.getInstance());
        	dbUtil.insert(tbNews);
        	Intent mIntent = new Intent(ACTION_NAME);  
            mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");  
            //发送广播  
           MyApp.getInstance().sendBroadcast(mIntent); 
        	
        	LogUtil.i("-----", "desc= "+desc+"\nextras= "+extras_json);
//        	processCustomMessage(context, bundle);
//        	dbUtil.insert(tbNews);
        	//LogUtil.i("-----", "desc= "+desc+"\nextras= "+extras_json);
        	//String  desc = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        	//String  extras_json = bundle.getString(JPushInterface.EXTRA_EXTRA);
        	IMChatMessageDetail chatMessageDetail = IMChatMessageDetail.getGroupItemMessageReceived(msg_id,IMChatMessageDetail.TYPE_MSG_TEXT
					, "帮忙医小助手", "帮忙医小助手");
			chatMessageDetail.setMessageContent(desc);
			chatMessageDetail.setDateCreated(System.currentTimeMillis()+"");
			//chatMessageDetail.setUserData(aMsg.getUserData());
			try {
				CCPSqliteManager.getInstance().insertIMMessage(chatMessageDetail);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent1 = new Intent(CCPIntentUtils.INTENT_IM_RECIVE);
			intent1.putExtra(GroupBaseActivity.KEY_GROUP_ID, "帮忙医小助手");
			context.sendBroadcast(intent1);
            // processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver]===接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[MyReceiver]===接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver]===用户点击打开了通知");
            
        	//打开自定义的Activity（帮忙医小助手）（该界面显示所有的推送消息，从sqlite中提取）
        	Intent i = new Intent(context, LittleHelperActivity.class);
//        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver]===用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.i(TAG, "[MyReceiver]===" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.i(TAG, "[MyReceiver]===Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey: " + key + ", value: " + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey: " + key + ", value: " + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey: " + key + ", value: " + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}
}
