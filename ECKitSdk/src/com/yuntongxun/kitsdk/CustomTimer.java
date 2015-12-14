/**
 * Project Name:ECKitSdk
 * File Name:CustomTimer.java
 * Package Name:com.yuntongxun.kitsdk
 * Date:2015-11-3上午10:04:57
 * Copyright (c) 2015, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.yuntongxun.kitsdk;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.util.Log;

/**
 * ClassName:CustomTimer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015-11-3 上午10:04:57 <br/>
 * @author   Think
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class CustomTimer {
	
	private Timer  timer;
	
	private int count;
	
	private TimerTask timerTask;
	
	public  static  InfoChangeObserver observer;
	
	public int controlFlag=2;//2:初始化 0:开始  1：停止  3是暂停 
	
	private static int delay = 1000; // 1s
	private static int period = 1000; // 1s
	
	private String contactId;
	
	private int MAX_COUNT=15*60;
	
	
	public CustomTimer(String mContactId)
	{
		contactId=mContactId;
		timer=new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
			   if(controlFlag==0){
				   if(count<=MAX_COUNT){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
						count++;
						Log.e("CustomTimer"+contactId,count+"");
						if(observer!=null) 
							 observer.onDataChanged(contactId,count);
				   }else {
					   //发送停止广播
					   Log.e("CustomTimer-stop","stop");
					   Intent intent = new Intent("com.rayelink.stoptime");
					   intent.putExtra("ContactId", contactId);
					   ECDeviceKit.getmContext().sendBroadcast(intent);
					   controlFlag=1;
					   this.cancel();
					   timer.cancel();
				   }
			   }else if(controlFlag==3)
			   {
					if(observer!=null) 
						 observer.onDataChanged(contactId,count);
			   }
			   
			}
		};
	}
	
	public void start()
	{
		if(controlFlag==2){
			controlFlag=0;
			timer.schedule(timerTask, delay, period);
		}else  if(controlFlag==3){
			controlFlag=0;
		}
	}
	
	public void stop()
	{
		controlFlag=1;
		if(timerTask!=null)
			timerTask.cancel();
		if(timer!=null)
			timer.cancel();
	}
	
	public void pause()
	{
		controlFlag=3;	
	}
 
}

