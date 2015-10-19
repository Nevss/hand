/**
 * 上午11:56:22
 * @author Zhangyuhui
 * $
 * TuringModel.java
 * TODO
 */
package com.darly.activities.model;

/**
 * @author Zhangyuhui TuringModel $ 上午11:56:22 TODO 图灵测试MODEL
 */
public class TuringModel {
	private int code;

	private String text;

	public TuringModel(int code, String text) {
		super();
		this.code = code;
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
