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
 */package com.voice.demo.group.utils;

import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
/**
 * 
 * @author Jorstin Chan
 * @version Time: 2013-9-13
 */
public final class RecordPopupWindow extends PopupWindow {

	public RecordPopupWindow(View contentView) {
		super(contentView);
	}

	public RecordPopupWindow(View contentView, int width, int height) {
		super(contentView, width, height, false);
	}

	public RecordPopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
	}
	
	@Override
	public void dismiss() {

		try {
			super.dismiss();
		} catch (Exception e) {
			Log.d("MicroMsg.MMPopupWindow", "dismiss exception, e = " + e.getMessage());
			e.printStackTrace();
		}
    }
}
