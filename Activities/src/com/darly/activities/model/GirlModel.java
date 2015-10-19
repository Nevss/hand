/**
 * 下午3:11:48
 * @author Zhangyuhui
 * GirlModel.java
 * TODO
 */
package com.darly.activities.model;

import java.util.List;

/**
 * @author Zhangyuhui GirlModel 下午3:11:48 TODO
 */
public class GirlModel {

	private int code;
	private String msg;

	private List<GirlBase> data;

	public GirlModel(int code, String msg, List<GirlBase> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
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

	public List<GirlBase> getData() {
		return data;
	}

	public void setData(List<GirlBase> data) {
		this.data = data;
	}

}
