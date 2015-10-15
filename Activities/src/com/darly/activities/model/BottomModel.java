/**
 * 下午4:52:16
 * @author Zhangyuhui
 * BottomModel.java
 * TODO
 */
package com.darly.activities.model;

/**
 * @author Zhangyuhui BottomModel 下午4:52:16 TODO
 */
public class BottomModel {

	private int resid;

	private String name;

	private String t;

	/**
	 * 
	 * 上午11:09:22
	 * 
	 * @author Zhangyuhui BottomModel.java TODO
	 */
	public BottomModel() {
		// TODO Auto-generated constructor stub
	}

	public BottomModel(int resid, String name, String t) {
		super();
		this.resid = resid;
		this.name = name;
		this.t = t;
	}

	public int getResid() {
		return resid;
	}

	public void setResid(int resid) {
		this.resid = resid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

}
