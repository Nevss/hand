/**
 * 上午9:42:44
 * @author Zhangyuhui
 * BaseHandler.java
 * TODO
 */
package com.darly.activities.base;

import com.darly.activities.common.Literal;

import android.os.Handler;
import android.os.Message;

/**
 * @author Zhangyuhui BaseHandler 上午9:42:44 TODO 进行一个Handler的整理。方便以后使用。
 */
public class BaseHandler extends Handler {

	private BaseActivity act;

	public BaseHandler(BaseActivity act) {
		super();
		this.act = act;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch (msg.what) {
		case Literal.GET_HANDLER:
			act.refreshGet(msg.obj);
			break;
		case Literal.POST_HANDLER:
			act.refreshPost(msg.obj);
			break;
		default:
			break;
		}

	}

}
