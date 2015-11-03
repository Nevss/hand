/**
 * 上午9:51:55
 * @author Zhangyuhui
 * $
 * DepInfo.java
 * TODO
 */
package com.darly.activities.widget.intel;

import java.util.ArrayList;

/**
 * @author Zhangyuhui DepInfo $ 上午9:51:55 TODO 机构平面图信息，包含机构所有信息。
 */
public class DepInfo {
	private int code;
	private String msg;
	private ArrayList<DepInfoFloor> floor;

	public DepInfo(int code, String msg, ArrayList<DepInfoFloor> floor) {
		this.code = code;
		this.msg = msg;
		this.floor = floor;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<DepInfoFloor> getFloor() {
		return floor;
	}

	public void setFloor(ArrayList<DepInfoFloor> floor) {
		this.floor = floor;
	}

}
