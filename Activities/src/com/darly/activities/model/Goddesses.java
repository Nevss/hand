/**
 * 上午11:19:45
 * @author Zhangyuhui
 * $
 * Goddesses.java
 * TODO
 */
package com.darly.activities.model;

/**
 * @author Zhangyuhui Goddesses $ 上午11:19:45 TODO 美女信息资料MODEL
 */
public class Goddesses {
	private int id;
	private String tu_name;
	private String tu_value;
	private String tu_dizhi;

	public Goddesses(int id, String tu_name, String tu_value, String tu_dizhi) {
		super();
		this.id = id;
		this.tu_name = tu_name;
		this.tu_value = tu_value;
		this.tu_dizhi = tu_dizhi;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTu_name() {
		return tu_name;
	}

	public void setTu_name(String tu_name) {
		this.tu_name = tu_name;
	}

	public String getTu_value() {
		return tu_value;
	}

	public void setTu_value(String tu_value) {
		this.tu_value = tu_value;
	}

	public String getTu_dizhi() {
		return tu_dizhi;
	}

	public void setTu_dizhi(String tu_dizhi) {
		this.tu_dizhi = tu_dizhi;
	}

}
