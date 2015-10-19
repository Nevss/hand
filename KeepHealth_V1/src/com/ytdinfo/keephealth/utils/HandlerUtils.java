package com.ytdinfo.keephealth.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ytdinfo.keephealth.R;

import android.graphics.Color;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

public class HandlerUtils {

	public static Handler useHandler(final EditText ets[], final Button bt) {

		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				
				listenET(ets, bt);

			};
		};
		return handler;
	}

	public static void startTimer(final Handler handler) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0x321);
			}
		};
		timer.schedule(task, 0, 100);
	}
	
	public static void listenET(final EditText ets[], final Button bt){
		for (int i = 0; i < ets.length; i++) {
			if (ets[i].getText().toString().equals("")) {
				bt.setClickable(false);
				bt.setTextColor(Color.parseColor("#66ffffff"));
				break;
			} else {
				bt.setClickable(true);
				bt.setTextColor(Color.parseColor("#ffffffff"));
			}
		}
	}

}
